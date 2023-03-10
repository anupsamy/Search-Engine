package cpen221.mp1.ngrams; 

import java.text.BreakIterator; 
import java.util.*; 
import java.util.List; 
import java.util.Map;

/**
 * @author Felix Ma
 *
 * Represents a piece of text to be analyzed.
 */
public class NGrams {

    /**
     * Array containing Strings from which 1Grams, 2Grams, etc. will be extracted.
     */
    String[] lines; 

    /**
     * Create an NGrams object.
     *
     * @param text all the text to analyze and create n-grams from; 
     *             is not null and is not empty.
     */
    public NGrams(String[] text) {
        this.lines = text; 
    }

    /**
     * Obtain the total number of unique 1-grams,
     * 2-grams, ..., n-grams.
     *
     * Specifically, if there are m_i i-grams,
     * obtain sum_{i=1}^{n} m_i.
     *
     * @return the total number of 1-grams,
     *         2-grams, ..., n-grams.
     */
    public long getTotalNGramCount(int n) throws IllegalArgumentException {

        int totalCount = 0; 

        List<Map<String, Long>> listOfGrams = this.getAllNGrams(); 

        if (n < 1 || n > listOfGrams.size()) {
            throw new IllegalArgumentException(); 
        }

        for (int i = 0; i < n; i++) {
            totalCount += listOfGrams.get(i).size(); 
        }

        return totalCount; 
    }

    /**
     * Get the n-grams, as a List, with the i-th entry being
     * all the (i+1)-grams and their counts.
     * @return a list of n-grams and their associated counts,
     * with the i-th entry being all the (i+1)-grams and their counts.
     */
    public List<Map<String, Long>> getAllNGrams() throws IllegalArgumentException {

        List<Map<String, Long>> listOfGrams = new ArrayList<>(); 

        for (int i = 0; i < lines.length; i++) {
            String[] currentLine = getWords(this.lines[i]); 

            if (currentLine.length == 0) {
                throw new IllegalArgumentException(); 
            }

            for (int gramLength = 1; gramLength <= currentLine.length; gramLength++) {

                listOfGrams.add(new HashMap<>()); 

                for (int j = 0; j <= currentLine.length - gramLength; j++) {

                    StringBuilder current = new StringBuilder(); 

                    for (int k = j; k < j + gramLength; k++) {
                        current.append(currentLine[k]); 

                        if (k != j + gramLength - 1) {
                            current.append(" "); 
                        }
                    }
                    String currentStr = current.toString(); 

                    if (listOfGrams.get(gramLength - 1).containsKey(currentStr)) {
                        Long count = listOfGrams.get(gramLength - 1).get(currentStr); 
                        listOfGrams.get(gramLength - 1).put(currentStr, ++count); 
                    } else {
                        listOfGrams.get(gramLength - 1).put(currentStr, 1L); 
                    }
                }
            }
        }

        for (int i = 0; i < listOfGrams.size(); i++) {
            if (listOfGrams.get(i).size() == 0) {
                listOfGrams.remove(i); 
                i--; 
            }
        }
        return listOfGrams; 
    }

    /**
     * Obtain a String[] array where indices are individual words of input String with punctuation and spaces removed.
     * @param text which is a line of text.
     * @return a String[] array where indices are individual words of input String with punctuation and spaces removed.
     */
    public static String[] getWords(String text) {
        ArrayList<String> words = new ArrayList<>(); 
        BreakIterator wb = BreakIterator.getWordInstance(); 
        wb.setText(text); 
        int start = wb.first(); 
        for (int end = wb.next(); 
             end != BreakIterator.DONE; 
             start = end, end = wb.next()) {
            String word = text.substring(start, end).toLowerCase(); 
            word = word.replaceAll("^\\s*\\p{Punct}+\\s*", "").replaceAll("\\s*\\p{Punct}+\\s*$", ""); 
            if (!word.equals(" ")) {
                words.add(word); 
            }
        }

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equals("")) {
                words.remove(i); 
                i--; 
            }
        }

        String[] wordsArray = new String[words.size()]; 
        words.toArray(wordsArray); 
        return wordsArray; 
    }

    /**
     * Searches current line for occurrence of given NGram
     *
     * @param line, the current line being searched through.
     * @param gram, the gram being searched for.
     * @return number of occurrences of the gram in the line.
     */
    public static long numOccurences(String line, String gram) throws IllegalArgumentException {

        String[] lineArray = getWords(line); 
        String[] gramArray = getWords(gram); 
        long total = 0; 

        //Error if gram length is longer than line length
        if (gramArray.length > lineArray.length) {
            throw new IllegalArgumentException(); 
        }

        //Iterate through line to find matching terms of length equal to gram length
        for (int i = 0; i <= lineArray.length - gramArray.length; i++) {
            int count = 0; 
            for (int j = 0; j < gramArray.length; j++) {
                if (lineArray[i + j].equals(gramArray[j])) {
                    count++; 
                }
            }
            if (count == gramArray.length) {
                total++; 
            }
        }
        return total; 
    }
}

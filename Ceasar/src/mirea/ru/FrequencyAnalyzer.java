package mirea.ru;

import java.io.*;
import java.util.*;

public class FrequencyAnalyzer {

    private String book = "";

    public List<Map.Entry<Character, Integer>> getTopLetters(FileReader book) {
        Map<Character, Integer> letterFrequency = new HashMap<>();
        try {
            int i;
            BufferedReader br = new BufferedReader(book, 999999);
            while ((i = br.read()) != -1) {
                i = ((char) i + "").toLowerCase().charAt(0);
                this.book = this.book + (char) i;
                if (Alphabet.isRusLetter((char) i)) {
                    if (!letterFrequency.isEmpty() && letterFrequency.containsKey((char) i)) {
                        letterFrequency.replace((char) i, letterFrequency.get((char) i) + 1);
                    } else {
                        letterFrequency.put((char) i, 1);
                    }
                }
            }

            List<Map.Entry<Character, Integer>> topLetters = new ArrayList(letterFrequency.entrySet());
            topLetters.sort(new LettersComparator());
            return topLetters;
        } catch (IOException e) {
            System.out.println("Что-то пошло не так в методе getTopLetters");
            return null;
        }
    }

    public List<Map.Entry<Character, Integer>> getTopLetters(String chapter) {
        Map<Character, Integer> letterFrequency = new HashMap<>();
        chapter = chapter.toLowerCase();
        char ch;
        for (int i = 0; i < chapter.length(); i++) {
            ch = chapter.charAt(i);
            if (Alphabet.isRusLetter(ch)) {
                if (!letterFrequency.isEmpty() && letterFrequency.containsKey(ch)) {
                    letterFrequency.replace(ch, letterFrequency.get(ch) + 1);
                } else {
                    letterFrequency.put(ch, 1);
                }
            }
        }

        List<Map.Entry<Character, Integer>> topLetters = new ArrayList(letterFrequency.entrySet());
        topLetters.sort(new LettersComparator());
        return topLetters;
    }

    public List<Map.Entry<String, Integer>> getTopBigrams(String text) {
        Map<String, Integer> bigramFrequency = new HashMap<>();
        text = text.toLowerCase();
        char ch1 = text.charAt(0), ch2;
        for (int i = 1; i < text.length(); i++) {
            ch2 = text.charAt(i);
            if (Alphabet.isRusLetter(ch1) && Alphabet.isRusLetter(ch2)) {
                String str = ch1 + "" + ch2;
                if (!bigramFrequency.isEmpty() && bigramFrequency.containsKey(str)) {
                    bigramFrequency.replace(str, bigramFrequency.get(str) + 1);
                } else {
                    bigramFrequency.put(str, 1);
                }
            }
            ch1 = ch2;
        }

        List topBigrams = new ArrayList(bigramFrequency.entrySet());
        topBigrams.sort(new BigramsComparator());

        return topBigrams.subList(topBigrams.size() - 3, topBigrams.size());
    }

    public List<Map.Entry<String, Integer>> getTopBigrams() {
        Map<String, Integer> bigramFrequency = new HashMap<>();
        book = book.toLowerCase();
        char ch1 = book.charAt(0), ch2;
        for (int i = 1; i < book.length(); i++) {
            ch2 = book.charAt(i);
            if (Alphabet.isRusLetter(ch1) && Alphabet.isRusLetter(ch2)) {
                String str = ch1 + "" + ch2;
                if (!bigramFrequency.isEmpty() && bigramFrequency.containsKey(str)) {
                    bigramFrequency.replace(str, bigramFrequency.get(str) + 1);
                } else {
                    bigramFrequency.put(str, 1);
                }
            }
            ch1 = ch2;
        }

        List topBigrams = new ArrayList(bigramFrequency.entrySet());
        topBigrams.sort(new BigramsComparator());

        return topBigrams.subList(topBigrams.size() - 3, topBigrams.size());
    }
}

class LettersComparator implements Comparator<Map.Entry<Character, Integer>> {

    @Override
    public int compare(Map.Entry<Character, Integer> e1, Map.Entry<Character, Integer> e2) {
        return e1.getValue().compareTo(e2.getValue());
    }
}

class BigramsComparator implements Comparator<Map.Entry<String, Integer>> {

    @Override
    public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
        return e1.getValue().compareTo(e2.getValue());
    }
}
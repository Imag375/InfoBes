package mirea.ru;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ceasar {

    private Alphabet alphabet = new Alphabet();
    private String chapter = "";

    public void inCode(int key, FileReader in, FileWriter out) {
        try {
            int i = -1;
            while ((i = in.read()) != -1) {
                int ch = alphabet.getNewChar(key, i);
                out.write(ch);
                chapter = chapter + (char) ch;
            }
        } catch (EOFException e) {
            System.out.println("Что-то пошло не так");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<Character, Character> getMap(List<Map.Entry<Character, Integer>> chapter, List<Map.Entry<Character, Integer>> book) {
        HashMap<Character, Character> dictionary = new HashMap<>();
        while (!chapter.isEmpty()) {
            dictionary.put(chapter.remove(0).getKey(), book.remove(0).getKey());
        }
        System.out.println(dictionary);
        return dictionary;
    }

    private HashMap<Character, Character> getDict(HashMap<Character, Character> dictionary, List<Map.Entry<String, Integer>> topBigramsCh, List<Map.Entry<String, Integer>> topBigramsB) {

        for (int i = 0; i < 3; i++) {
            dictionary.replace(topBigramsCh.get(i).getKey().charAt(0), topBigramsB.get(i).getKey().charAt(0));
            dictionary.replace(topBigramsCh.get(i).getKey().charAt(1), topBigramsB.get(i).getKey().charAt(1));
        }
        System.out.println(dictionary);
        return dictionary;
    }

    public void decoding(FileReader book) throws IOException {
        FrequencyAnalyzer fa = new FrequencyAnalyzer();

        List<Map.Entry<Character, Integer>> lettersBook = fa.getTopLetters(book);
        List<Map.Entry<Character, Integer>> lettersChapter = fa.getTopLetters(chapter);
        System.out.println(lettersChapter);
        System.out.println(lettersBook);
        List<Map.Entry<String, Integer>> topBigramsBook = fa.getTopBigrams();
        List<Map.Entry<String, Integer>> topBigramsChapter = fa.getTopBigrams(chapter);
        System.out.println(topBigramsChapter);
        System.out.println(topBigramsBook);
        FileWriter chapterDecod = new FileWriter("chapterDecod.txt", false);
        HashMap<Character, Character> dict = getDict(getMap(lettersChapter, lettersBook), topBigramsChapter, topBigramsBook);

        for (int i = 0; i < chapter.length(); i++) {
            char ch = chapter.charAt(i);
            if (alphabet.isRusLetter(ch)) {
                if (alphabet.isABC(ch)) {
                    chapterDecod.write((dict.get((ch + "").toLowerCase().charAt(0)) + "").toUpperCase().charAt(0));
                } else {
                    chapterDecod.write(dict.get(ch));
                }
            } else {
                chapterDecod.write(ch);
            }
        }

        chapterDecod.close();
    }
}

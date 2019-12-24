package mirea.ru;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Main {
    private static final int key = 3;

    public static void main(String[] args) {

        FileReader book = null;
        FileReader chapterIn = null;
        FileWriter chapterOut = null;

        try {

            book = new FileReader("War and Peace.txt");
            chapterIn = new FileReader("X_chapter.txt");
            chapterOut = new FileWriter("X_chapOut.txt", false);

            Ceasar ceasar = new Ceasar();
            ceasar.inCode(key, chapterIn, chapterOut);
            ceasar.decoding(book);

        } catch (IOException e) {

            System.out.println("Что-то пошло не так в методе main");
            System.out.println(e);

        } finally {
            try {

                if(chapterIn != null)
                    chapterIn.close();

                if(chapterOut != null)
                    chapterOut.close();

                if(book != null)
                    book.close();

            } catch (IOException err) {
                System.out.println("Что-то пошло не так в методе main при закрытии потоков");
                System.out.println(err);
            }
        }
    }
}

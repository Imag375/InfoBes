package mirea.ru;

import java.util.*;

public class Alphabet {
    private static final String abc1 = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";  //заглавные буквы
    private static final String abc2 = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";  //строчные буквы

    public static boolean isRusLetter(char ch) {
        return (abc1.indexOf(ch) != -1) || (abc2.indexOf(ch) != -1);
    }

    public int getNewChar(int key, int ch) {
        int i;
        if ((i = abc1.indexOf(ch)) != -1) {
            i += key;
            if (i >= 33) {
                i -= 33;
            }
            ch = abc1.charAt(i);
        } else if ((i = abc2.indexOf((char) ch)) != -1) {
            i += key;
            if (i >= 33) {
                i -= 33;
            }
            ch = abc2.charAt(i);
        }
        return ch;
    }

    public boolean isABC(char ch) {
        if (abc1.indexOf(ch) != -1) {
            return true;
        } else {
            return false;
        }
    }
}

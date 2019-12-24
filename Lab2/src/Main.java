import java.math.BigInteger;
import java.util.Random;

public class Main {

    public static Random rand = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws Exception {
        int g, p, a, b;
        g = getInt();
        p = getInt();
        System.out.printf("Алиса и Боб выбирают два числа. g = %d, p = %d\n", g, p);
        a = getInt();
        b = getInt();
        System.out.printf("Алиса и Боб выбирают два секретных числа. a = %d, b = %d\n", a, b);
        BigInteger A = BigInteger.valueOf(g).pow(a).mod(BigInteger.valueOf(p));
        BigInteger B = BigInteger.valueOf(g).pow(b).mod(BigInteger.valueOf(p));
        System.out.printf("Алиса и Боб обмениваются числами A и B. А = %d, B = %d\n", A, B);
        BigInteger keyA = BigInteger.valueOf(B.intValue()).pow(a).mod(BigInteger.valueOf(p));
        BigInteger keyB = BigInteger.valueOf(A.intValue()).pow(b).mod(BigInteger.valueOf(p));
        System.out.printf("Алиса и Боб генерируют ключи: A = %d, B = %d", keyA.intValue(), keyB.intValue());
    }

    public static void sleep(int time) throws InterruptedException {
        Thread.sleep(time);
    }

    public static int getInt() {
        int c;
        int f;
        do {
            c = rand.nextInt();
            c = Math.abs(c) % 1000;
            f = 0;
            for (int i = 2; i < c; i++) {
                if (c % i == 0) {
                    f = 1;
                }
            }
        } while (f == 1);
        return c;
    }
}


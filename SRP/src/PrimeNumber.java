import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

class PrimeNumber {
    private int length;
    private ArrayList<Integer> primes = new ArrayList<>();

    PrimeNumber(int length) {
        this.length = length;
        if (primes.isEmpty()) {
            this.primes = sieveOfEratosthenes(2000);
        }
    }

    BigInteger generate() {
        Random rand = new Random();
        BigInteger num = new BigInteger(length, rand);

        /*Старший и млайдший бит в 1*/
        num = num.setBit(0);
        num = num.setBit(length);
        //System.out.println("After setBit:" + num);

        boolean isPrime = false;
        loop:
        while (!isPrime) {
            /*Проверка делимости на малые простые числа*/
            for (Integer prime : primes) {
                if (num.remainder(BigInteger.valueOf(prime)).equals(BigInteger.valueOf(0))) {
                    //System.out.println(num + "/" + prime);
                    //System.out.println(num + " делится на простое число " + prime);
                    num = num.add(new BigInteger("2"));
                    continue loop;
                }
            }

            int i = 0;
            while (i < 5) {
                if (millerRabinPrimeTest(num)) {
                    //System.out.println("Тест№" + i + " пройден");
                } else {
                    //System.out.println("Тест№" + i + " не пройден");
                    num = num.add(new BigInteger("2"));
                    continue loop;
                }
                i++;
            }
            isPrime = true;
        }
        return num;
    }

    private boolean millerRabinPrimeTest(BigInteger num) {
        int b = 0;
        /*Кол-во делений на 2*/
        BigInteger tmp = num.subtract(BigInteger.valueOf(1));
        while (tmp.remainder(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))) {
            //System.out.println("tmp = " + tmp);
            tmp = tmp.divide(new BigInteger("2"));
            //System.out.println("tmp = " + tmp);
            b++;
        }
        //System.out.println("b = " + b);

        int m;
        m = num.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf((long) Math.pow(2, b))).intValue();
        //System.out.println("m = " + m);

        Random rand = new Random();
        BigInteger a = new BigInteger(length - 1, rand);

        int j = 0;
        BigInteger z;

        z = a.pow(m).mod(num);
        //System.out.println("z = " + z);

        if (z.equals(new BigInteger("1")) || z.equals(num.subtract(new BigInteger("1")))) {
            return true;
        }

        while (true) {
            if (j > 0 && z.equals(new BigInteger("1"))) {
                return false;
            }
            j++;
            //System.out.println("j = " + j);
            if (j < b && z.compareTo(num.subtract(new BigInteger("1"))) < 0) {
                z = z.pow(2).mod(num);
                //System.out.println("new z = " + z);
            } else {
                break;
            }
        }

        if (z.equals(num.subtract(new BigInteger("1")))) {
            return true;
        } else {
            if (j == b) {
                return false;
            }
        }
        return false;
    }

    /*Решето Эратосфена*/
    private ArrayList<Integer> sieveOfEratosthenes(int n) {
        boolean[] isComposite = new boolean[n + 1];
        for (int i = 2; i * i <= n; i++) {
            if (!isComposite[i]) {
                for (int j = i * i; i * j <= n; j++) {
                    isComposite[i * j] = true;
                }
            }
        }

        ArrayList<Integer> primes = new ArrayList<>();

        for (int i = 2; i <= n; i++) {
            if (!isComposite[i]) {
                primes.add(i);
            }
        }

        return primes;
    }

    boolean isPrime(BigInteger num) {
        /*Проверка делимости на малые простые числа*/
        for (Integer prime : primes) {
            if (num.remainder(BigInteger.valueOf(prime)).equals(BigInteger.valueOf(0))) {
                return false;
            }
        }

        int i = 0;
        while (i < 5) {
            if (millerRabinPrimeTest(num)) {
                //System.out.println("Тест№" + i + " пройден");
            } else {
                return false;
            }
            i++;
        }
        return true;
    }
}

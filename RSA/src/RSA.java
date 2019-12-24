import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;

public class RSA {
    public static void main(String[] args) {
        String text = "Hello, world!";

        PrimeNumber prime = new PrimeNumber(20);
        BigInteger p = prime.generate();
        BigInteger q = prime.generate();
        System.out.println("p = " + p);
        System.out.println("q = " + q);

        BigInteger n = p.multiply(q);
        System.out.println("n = p * q = " + n);

        BigInteger f = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = new BigInteger("5");
        while (!f.gcd(e).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }
        System.out.println(e + " взаимно-простое с f = (p - 1)(q - 1) = " + f);

        BigInteger d = e.modInverse(f);
        System.out.println("d = " + d);

        System.out.println("PublicKey = P(e, n) = (" + e + ", " + n + ")");
        System.out.println("SecretKey = S(d, n) = (" + d + ", " + n + ")");

        LinkedList<BigInteger> message = encrypt(text, e, n);
        System.out.println("Получено зашифрованное сообщение:" + message.toString());

        System.out.println("Расшифрованное сообщение:" + decrypt(message, d, n));
    }

    private static LinkedList<BigInteger> encrypt(String text, BigInteger e, BigInteger n){
        char[] chars = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        LinkedList<BigInteger> result = new LinkedList<>();
        while (chars.length >= 3){
            builder.append(String.format("%8s", Integer.toBinaryString(chars[0])).replace(' ', '0'));
            builder.append(String.format("%8s", Integer.toBinaryString(chars[1])).replace(' ', '0'));
            builder.append(String.format("%8s", Integer.toBinaryString(chars[2])).replace(' ', '0'));

            /*Шифруем блок открытым ключем*/
            result.add(new BigInteger(builder.toString(), 2).modPow(e, n));
            builder.delete(0, builder.length());
            chars = Arrays.copyOfRange(chars, 3, chars.length);
        }
        for (char aChar : chars) {
            builder.append(String.format("%8s", Integer.toBinaryString(aChar)).replace(' ', '0'));
        }
        result.add(new BigInteger(builder.toString(), 2).modPow(e, n));
        return result;
    }

    private static String decrypt(LinkedList<BigInteger> message, BigInteger d, BigInteger n){
        String tmp;
        StringBuilder result = new StringBuilder();
        StringBuilder block = new StringBuilder();
        for (BigInteger bigInteger : message) {
            /*Расшифровка*/
            tmp = bigInteger.modPow(d, n).toString(2);
            System.out.print("S(" + bigInteger + ") = " + bigInteger + "^d mod n = " + tmp + "\ntmp = ");
            while (tmp.length() >= 8) {
                block.insert(0, (char) Integer.parseInt(tmp.substring(tmp.length() - 8), 2));
                tmp = tmp.substring(0, tmp.length() - 8);
            }
            if (tmp.length() > 0) {
                block.insert(0, (char) Integer.parseInt(tmp, 2));
            }
            System.out.println(block.toString());
            result.append(block.toString());
            block.delete(0, block.length());
        }
        return result.toString();
    }
}

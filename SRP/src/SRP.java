import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

public class SRP {
    private static BigInteger q = new BigInteger("1676963");
    private static BigInteger N = new BigInteger("3353927");

    public static void main(String[] args) {
        PrimeNumber prime = new PrimeNumber(20);
        Random random = new Random();

        if (q == null) {
            generateQ();
        }

        BigInteger g = prime.generate();
        while (!isPrimitiveRoot(N, g)) {
            g = prime.generate();
        }

        System.out.println("q = " + q);
        System.out.println("N = 2*q+1 = " + N);
        System.out.println("g = генератор по модулю N = " + g);

        System.out.println("\nПри регистрации, клиент вычисляет следующее:");
        String s = "fds35JerJDL3f";
        String i = "USERNAME";
        String p = "password";
        System.out.println("s = " + s);
        System.out.println("i = " + i);
        System.out.println("p = " + p);

        BigInteger x = new BigInteger(Objects.requireNonNull(H(p, s)));
        System.out.println("x = H(s, p) = " + x);

        /*v - password verifier*/
        BigInteger v = g.modPow(x, N);
        System.out.println("v = g^x % N = " + v);

        System.out.println("\nПользователь:");
        int a = random.nextInt(100000);
        System.out.println("a = random() = " + a);

        BigInteger A = g.pow(a).mod(N);
        System.out.println("A = g^a % N = " + A);

        System.out.println("\nНа сервер передаётся пара из логина I и вычисленного A.");
        System.out.println("Сервер достаёт s & v, соответствующих логину:");
        int b = random.nextInt(100000);
        System.out.println("b = random() = " + b);

        BigInteger B = g.pow(b).mod(N).add(v.multiply(BigInteger.valueOf(3))).mod(N);
        System.out.println("B = (k*v + g^b % N) % N = " + B);

        /*Клиенту присылается пара из соли s и вычисленного B.*/
        System.out.println("\nОбе стороны вычисляют скремблер: u ");
        BigInteger u = new BigInteger(Objects.requireNonNull(H(B.toString(), A.toString())));
        System.out.println("u = H(A, B) = " + u);

        System.out.println("\nКлиент вычисляет общий ключ сессии:");
        BigInteger S = B.subtract(g.modPow(x, N).multiply(BigInteger.valueOf(3)))
                .modPow(u.multiply(x).add(BigInteger.valueOf(a)), N);
        System.out.println("S = ((B - k*(g^x % N)) ^ (a + u*x)) % N = " + S);
        BigInteger K = new BigInteger(Objects.requireNonNull(H(S.toString(), s)));
        System.out.println("K = H(S) =" + K);

        System.out.println("\nСервер со своей стороны так же вычисляет общий ключ сессии:");
        BigInteger S2 = v.modPow(u, N).multiply(A).modPow(BigInteger.valueOf(b), N);
        System.out.println("S2 = ((A*(v^u % N)) ^ b) % N = " + S2);
        BigInteger K2 = new BigInteger(Objects.requireNonNull(H(S2.toString(), s)));
        System.out.println("K2 = H(S) =" + K2);

        System.out.println("\nКлиент и сервер генерируют подтверждение m = H( H(N) XOR H(g), H(I), s, A, B, K):");
        BigInteger m = new BigInteger(Objects.requireNonNull(H(new BigInteger(Objects.requireNonNull(H(N.toString(), s)))
                .xor(new BigInteger(Objects.requireNonNull(H(g.toString(), s))))
                .add(new BigInteger(Objects.requireNonNull(H(i, s)))).add(A).add(B).add(K).toString(), s)));

        /*Сервер у себя вычисляет M используя свою копию K, и проверяет равенство.*/
        BigInteger m2 = new BigInteger(Objects.requireNonNull(H(new BigInteger(Objects.requireNonNull(H(N.toString(), s)))
                .xor(new BigInteger(Objects.requireNonNull(H(g.toString(), s))))
                .add(new BigInteger(Objects.requireNonNull(H(i, s)))).add(A).add(B).add(K2).toString(), s)));

        System.out.println(m + " == " + m2);
    }

    private static void generateQ() {
        PrimeNumber prime = new PrimeNumber(20);

        q = prime.generate();
        N = q.multiply(new BigInteger("2")).add(BigInteger.ONE);

        while (!prime.isPrime(N)) {
            q = prime.generate();
            N = q.multiply(new BigInteger("2")).add(BigInteger.ONE);
            System.out.println("N = " + N);
        }
    }

    /*Первообразный корень по модулю p*/
    private static boolean isPrimitiveRoot(BigInteger p, BigInteger g) {
        for (int i = 1; i < p.intValue(); i++) {
            if (g.modPow(BigInteger.valueOf(i), p).compareTo(BigInteger.valueOf(i)) != 0) {
                return true;
            }
        }
        return false;
    }

    private static byte[] H(String unsignedToken, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return sha256_HMAC.doFinal(unsignedToken.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}

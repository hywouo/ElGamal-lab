package cypher.ojld;

import java.math.BigInteger;

/**
 * 扩展欧几里德算法
 */
public class ExtendOjld {

    public static BigInteger cal(BigInteger x, BigInteger y) {
        EEA eea = new EEA();
        BigInteger a, b;
        a = x;
        b = y;
        BigInteger[] result = new BigInteger[3];
        result = EEA.extend_gcd(a, b);
        if (result[1].compareTo(BigInteger.ZERO) < 0) {
            return result[1].add(b);
        } else {
            return result[1];
        }

    }
}

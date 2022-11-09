package cypher;

import cypher.ojld.ExtendOjld;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class MyEIGamal {

    //随机质数的位数
    public static int BITLENGTH = 10;

    //生成一个位数为BITLENGTH的随机质数
    public static BigInteger genPrime() {
        return BigInteger.probablePrime(BITLENGTH, new Random());
    }

    //判断i是否为质数
    public static boolean isPrime(BigInteger i) {
        return i.isProbablePrime(1);
    }

    //生成质数p的本原根g,返回[p,g]
    public static BigInteger[] genPrivateRoot() {
        BigInteger q;
        BigInteger p;
        BigInteger g;
        do {
            q = genPrime();
            p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
        } while (!isPrime(p));
        do {
            int r = (int) (Math.random() * (Integer.parseInt(p.subtract(BigInteger.ONE).toString()) - 2) + 2 + 1);
            g = new BigInteger(String.valueOf(r));
        } while (new BigInteger(calnum(g, BigInteger.TWO, p)).equals(BigInteger.ONE) || new BigInteger(calnum(g, q, p)).equals(BigInteger.ONE));
        return new BigInteger[]{p, g};
    }

    //计算 a^b mod p
    public static String calnum(BigInteger a, BigInteger b, BigInteger p) {

        BigInteger y = BigInteger.ONE;

        while (true) {
            if (b.compareTo(BigInteger.ZERO) == 0) {
                return y.toString();
            }
            while (b.compareTo(BigInteger.ZERO) > 0 && (b.remainder(BigInteger.TWO)).compareTo(BigInteger.ZERO) == 0) {
                a = (a.multiply(a)).remainder(p);
                b = b.divide(BigInteger.TWO);
            }
            b = b.subtract(BigInteger.ONE);
            y = a.multiply(y).remainder(p);
        }
    }

    //快速幂求a^b
    public static BigInteger quickPow(BigInteger a, BigInteger b) {
        BigInteger res = BigInteger.ONE;
        while (b.compareTo(BigInteger.ZERO) != 0) {
            if ((b.and(BigInteger.ONE)).equals(BigInteger.ONE)) {
                res = res.multiply(a);
            }
            a = a.multiply(a);
            b = b.shiftRight(1);
        }
        return res;
    }

    //生成随机数k
    public static BigInteger genK(BigInteger p) {
        int r;
        BigInteger k;
        do {
            r = (int) (Math.random() * (Integer.parseInt(p.subtract(BigInteger.TWO).toString()) - 2) + 2 + 1);
            k = new BigInteger(String.valueOf(r));
        } while (!k.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE));
        return k;
    }

    //生成r
    public static BigInteger genR(BigInteger g, BigInteger p, BigInteger k) {
        return new BigInteger(calnum(g, k, p));
    }

    //生成k mod p-1 的逆k^(-1)
    public static BigInteger genDeK(BigInteger k, BigInteger p) {
        return ExtendOjld.cal(k, p.subtract(BigInteger.ONE));
    }

    //生成1 - （p-1）之间的随机数x
    public static BigInteger genX(BigInteger p) {
        int r = (int) (Math.random() * (Integer.parseInt(p.subtract(BigInteger.ONE).toString()) - 1) + 1 + 1);
        return new BigInteger(String.valueOf(r));
    }

    //生成公钥（p,g,y）
    public static BigInteger[] genPublicKey(BigInteger p, BigInteger g, BigInteger x) {
        BigInteger y = new BigInteger(calnum(g, x, p));
        return new BigInteger[]{p, g, y};
    }

    //生成签名(r,s)数组
    public static BigInteger[] genSignature(BigInteger g, BigInteger p, BigInteger k, BigInteger m, BigInteger x) {
        BigInteger r;
        BigInteger s;
        r = genR(g, p, k);
        s = genDeK(k, p).multiply(m.subtract(x.multiply(r))).mod(p.subtract(BigInteger.ONE));
        return new BigInteger[]{r, s};
    }

    //验证签名过程
    public static boolean checkSignature(BigInteger g, BigInteger m, BigInteger p, BigInteger y, BigInteger r, BigInteger s) {
        BigInteger v1 = new BigInteger(calnum(g, m, p));
        BigInteger v2 = (quickPow(y, r).multiply(quickPow(r, s))).mod(p);
        return v1.equals(v2);
    }

    public static void main(String[] args) {

        //生成p,g
        BigInteger[] pg = genPrivateRoot();
        BigInteger p = pg[0];
        BigInteger g = pg[1];

        //生成随机数x
        BigInteger x = genX(p);

        //生成公钥（p,g,y）
        BigInteger[] publicKey = genPublicKey(p, g, x);
        System.out.println("公钥(p,g,y): " + Arrays.toString(publicKey));
        System.out.println("私钥x: " + x);

        //生成随机数k
        BigInteger k1 = genK(p);
        BigInteger k2 = genK(p);
        System.out.println("k1 = " + k1);
        System.out.println("k2 = " + k2);

        //明文m
        BigInteger m = new BigInteger("200111624");
        System.out.println("消息m: " + m);

        BigInteger[] signature1 = genSignature(g, p, k1, m, x);
        BigInteger[] signature2 = genSignature(g, p, k2, m, x);

        System.out.println("使用随机数k1的签名信息(r,s): " + Arrays.toString(signature1));
        System.out.println("使用随机数k2的签名信息(r,s): " + Arrays.toString(signature2));

        boolean check1 = checkSignature(g, m, p, publicKey[2], signature1[0], signature1[1]);
        boolean check2 = checkSignature(g, m, p, publicKey[2], signature2[0], signature2[1]);

        BigInteger change = m.subtract(new BigInteger("12345"));

        System.out.println("签名1验证结果: " + check1);
        boolean change_check1 = checkSignature(g, change, p, publicKey[2], signature1[0], signature1[1]);
        System.out.println("消息"+m+" 被篡改为 " + change + " 篡改消息后的验证结果: " + change_check1);
        System.out.println("签名2验证结果: " + check2);
        boolean change_check2 = checkSignature(g, change, p, publicKey[2], signature2[0], signature2[1]);
        System.out.println("消息"+m+" 被篡改为 " + change + " 篡改消息后的验证结果: " + change_check2);
    }
}

package cypher.ojld;

import java.math.BigInteger;

public class EEA {
    public static BigInteger MinFactor(BigInteger a, BigInteger b)
    {
        //return the minist factor of a adn b
        BigInteger miner;

        if(a.compareTo(b) == 0){
            return BigInteger.ONE;
        }
        else if(a.compareTo(b) > 0){
            miner = b;
        }
        else{
            miner = a;
        }
        //init is two
        BigInteger init = BigInteger.ONE.add(BigInteger.ONE);
        while(true){
            if(init.compareTo(miner) > 0){
                break;
            }
            else {
                if (a.mod(init) == BigInteger.ZERO && b.mod(init) == BigInteger.ZERO){
                    return init;
                }
                else {
                    init = init.add(BigInteger.ONE);
                }
            }
        }
        return BigInteger.ONE;
    }

    public static BigInteger[] extend_gcd(BigInteger a,BigInteger b){
        BigInteger ans;
        BigInteger[] result=new BigInteger[3];
        if(b.compareTo(BigInteger.ZERO)==0)
        {
            result[0]=a;
            result[1]=BigInteger.ONE;
            result[2]=BigInteger.ZERO;
            return result;
        }
        BigInteger [] temp=extend_gcd(b,a.mod(b));
        ans = temp[0];
        result[0]=ans;
        result[1]=temp[2];
        result[2]=temp[1].subtract((a.divide(b)).multiply(temp[2]));
        return result;
    }
}


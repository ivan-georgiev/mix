/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Ivan
 */
public class RSA {

    private BigInteger e, d, n;

    public RSA(int size) {

        BigInteger p, q, phi;

        //Generate different random primes: p, q
        do {
            p = new BigInteger(size, 100, new Random());
            q = new BigInteger(size, 100, new Random());
        } while (p.compareTo(q) == 0);

        //Calculate n where n=p*q
        n = p.multiply(q);

        //Calculate f(n) where f(n) = (p - 1).(q - 1)
        phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        ///find e : gcd(e, f(n)) = 1 ; 1 < e < f(n)   //coprime
        do {
            e = new BigInteger(2 * size, new Random());
        } while ((e.compareTo(phi) != -1) || (e.compareTo(BigInteger.ONE) != 1) || (e.gcd(phi).compareTo(BigInteger.ONE) != 0));

        //Calculate d where (d*e) mod phi = 1 -> d= (1/e) mod phi
        d = e.modInverse(phi);


        System.out.println("Generated key values:"
                + "\np=" + p.toString()
                + "\n\nq=" + q.toString()
                + "\n\nn=" + n.toString()
                + "\n\nPhi=" + phi.toString()
                + "\n\ne=" + e.toString()
                + "\n\nd=" + d.toString()
                + "\n");
    }

    public BigInteger encrypt(BigInteger plaintext) {
        return plaintext.modPow(e, n);
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d, n);
    }
}

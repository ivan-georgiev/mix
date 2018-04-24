/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

/**
 *
 * @author Ivan
 */
public class Main {

    public static void main(String[] args) throws IOException {;

        String input;
        BufferedReader bufferRead;
        BigInteger plN, encN;


        System.out.print("Enter RSA bitsize: ");
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
        input = bufferRead.readLine();

        RSA rsa = new RSA(Integer.parseInt(input));


        System.out.print("Enter string to encode: ");
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
        input = bufferRead.readLine();
        System.out.print("\n");

        for (char ch : input.toCharArray()) {

            plN = BigInteger.valueOf((long) ch);
            encN = rsa.encrypt(plN);

            System.out.println("Char : " + ch);
            System.out.println("Plain Value : " + plN.toString());
            System.out.println("Encrypted Value : " + encN.toString());
            plN = rsa.decrypt(encN);
            System.out.println("Decrypted Value : " + plN.toString());
            System.out.println("Decrypted Char : " + ((char) plN.intValue()));
            System.out.println();

        }

    }
}

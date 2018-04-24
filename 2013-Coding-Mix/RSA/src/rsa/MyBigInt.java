/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;

/**
 *
 * @author Ivan
 */
public class MyBigInt {

    int[] nmb = new int[4];

    int Add(int a, int b) {

        int carry = a & b;
        int result = a ^ b;
        int shiftedcarry;

        while (carry != 0) {
            shiftedcarry = carry << 1;
            carry = result & shiftedcarry;
            result ^= shiftedcarry;
        }
        return result;
    }

    int Sub(int a, int b) {
        return a ^ b;
    }

    int Mul(int a, int b) {
        int result = 0;

        while (b != 0) // Iterate the loop till b==0
        {
            if ((b & 01) == 1) // Bitwise &  of the value of b with 01
            {
                result = result + a;     // Add a to result if b is odd .
            }
            a <<= 1;                   // Left shifting the value contained in 'a' by 1 
            // multiplies a by 2 for each loop
            b >>= 1;                   // Right shifting the value contained in 'b' by 1.
        }
        return result;
    }

    int Div(int a, int b) {

        int i = 0;
        while (a >= b) {
            a = a - b;
            i++;
        }
        return i;
    }

    int Mod(int a, int b) {
        while (a >= b) {
            a = a - b;
        }
        return a;
    }

    long powMod(long x, long n, long m) {

        long r = 1;

        while (n > 0) {
            if ((n & 1) == 1) {
                r = (r * x) % m;
            }
            x = (x * x) % m;
            n = n >> 1;
        }
        return r;
    }
//         In essence, if you're doing Q = N/D:
//
//    Align the most-significant ones of N and D.
//    Compute t = (N - D);.
//    If (t >= 0), then set the lsb of Q to 1, and set N = t.
//    Left-shift N by 1.
//    Left-shift Q by 1.
//    Go to step 2.
}

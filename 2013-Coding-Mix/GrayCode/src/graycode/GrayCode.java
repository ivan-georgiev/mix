/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graycode;

import java.util.ArrayList;

/**
 *
 * @author Ivan
 */
public class GrayCode {

    /**
     * @param args the command line arguments
     */
    ArrayList<String> grayLine = new ArrayList<String>();//gray code is stored here
    char[][] matrixG = new char[][]{
        {'0', '0', '0', '1', '1', '1', '1'},
        {'0', '1', '1', '0', '0', '1', '1'},
        {'1', '0', '1', '0', '1', '0', '1'}
    };

    public static void main(String[] args) {

        String tmp;
        GrayCode gc = new GrayCode();
        System.out.println("Static generator matrix codewords lister using Gray code\n");

        System.out.println("Matrix G:");
        for (int i = 0; i < gc.matrixG.length; i++) {
            System.out.print("| ");
            System.out.print(gc.matrixG[i]);
            System.out.println(" |");
        }
        System.out.println();

        //create gray code for the matrix
        gc.createGrayLineArraylist("", gc.matrixG.length, 0);
        System.out.println(gc.matrixG.length + "bit Gray code: " + gc.grayLine + "\n");

        //multiply the gray codes with the matrix to get the codewords
        for (int i = 0; i < gc.grayLine.size(); i++) {
            tmp = gc.multiplyVectorToMatrix(gc.grayLine.get(i));
            System.out.print("Code word " + (i + 1) + ": " + tmp);
            System.out.println(" ; Hamming weight: " + gc.getHammingWeight(tmp.toCharArray()));
        }

        System.out.println();
    }

    void createGrayLineArraylist(String prefix, int n, int charToAdd) {

        if (n == 0) {
            grayLine.add(prefix);
            return;
        }

        createGrayLineArraylist(prefix + charToAdd, n - 1, 0);
        createGrayLineArraylist(prefix + (charToAdd ^ 1), n - 1, 1);
    }

    String multiplyVectorToMatrix(String rowVector) {

        StringBuilder sb, res = new StringBuilder();

        for (int j = 0; j < matrixG[0].length; j++) {

            sb = new StringBuilder();

            for (int i = 0; i < matrixG.length; i++) {
                sb.append(matrixG[i][j]);
            }
            res.append(this.scalarMultiplyVectors(sb.toString().toCharArray(), rowVector.toCharArray()));
        }

        return res.toString();
    }

    Integer scalarMultiplyVectors(char[] vectorOne, char[] vectorTwo) {

        if (vectorOne.length != vectorTwo.length) {
            return null;
        }

        int sum = 0;
        for (int i = 0; i < vectorOne.length; i++) {
            sum ^= Character.getNumericValue(vectorOne[i]) & Character.getNumericValue(vectorTwo[i]);
        }

        return sum;
    }

    int getHammingWeight(char[] bitArray) {

        int sum = 0;

        for (int i = 0; i < bitArray.length; i++) {
            if (bitArray[i] == '1') {
                sum++;
            }
        }

        return sum;
    }
}

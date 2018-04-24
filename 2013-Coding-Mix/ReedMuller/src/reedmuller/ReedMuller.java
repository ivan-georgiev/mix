/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reedmuller;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Ivan
 */
public class ReedMuller {

    /**
     * @param args the command line arguments
     */
    ArrayList<Long> aEnc = new ArrayList<Long>();  //encodin matrix
    ArrayList<String> aEncCon = new ArrayList<String>(); //which row which Xi constains
    ArrayList<ArrayList<Long>> aDec; // stores characteristic vectors for each row
    int matrixSimElementsCount; // m
    int matrixColumns; //2^m

    public static void main(String[] args) {

        System.out.println("Reed-Muller Coding demo program\n");

        ReedMuller rm = new ReedMuller();
        Scanner scanner = new Scanner(System.in);

        int r, m;
        String input, output;
        Long encoded, encMod;
        System.out.println("Enter matrix dimensions R(r,m) [up to R(4,5), r<m]");
        System.out.print("m=");
        m = Integer.parseInt(scanner.next("[1-5]{1}"));

        System.out.print("r=");
        r = Integer.parseInt(scanner.next("[1-" + (m - 1) + "]{1}"));

        System.out.println("R(" + r + ", " + m + ") : ");
        //create encoding matrix
        rm.genEncodeMatrix(r, m);

        rm.printMatrix();

        System.out.println("This matrix can find up to " + (1 << m - r - 1) + " errors"); //2^(m-r-1)
        System.out.println("This matrix can correct up to " + ((1 << m - r - 1) - 1) + " errors");
        System.out.println();
        System.out.print("Enter BinaryString word with lenght " + rm.getMatrixSize() + " to be encoded and press Enter: ");

        input = scanner.next("[01]{" + rm.getMatrixSize() + "}");

        encoded = rm.encodeReedMuller(input);
        System.out.println("Encoded: " + rm.conLongToBitString(encoded, "", true));
        System.out.print("Type the modified encoded bitstring and press Enter: ");

        encMod = rm.conBitStringToLong(scanner.next("[01]{" + rm.matrixColumns + "}"));
        //encMod = encoded;

        //System.out.println("Encoded before modification: " + rm.conLongToBitString(encoded, "", true));
        System.out.println("Encoded after modification:  " + rm.conLongToBitString(encMod, "", true));

        //create decoding matrix
        rm.genDecodeMatrix();

        //decode using decoding matrix
        output = rm.decodeReedMuller(encMod);

        //result
        System.out.println("\nDecoded:   " + output);
        System.out.println("Input was: " + input);

        if (input.equals(output)) {
            System.out.println("Result: OK");
        } else {
            System.out.println("Result: NOK");
        }
    }

    //The print method does not work with negative numbers so this perform ~ only for the used bits 2^m
    Long invert(Long val) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.matrixColumns; i++) {
            sb.append(1);
        }

        val ^= conBitStringToLong(sb.toString());

        return val;
    }

    //encode using the algorithm
    Long encodeReedMuller(String plainWord) {

        char[] varAr = plainWord.toCharArray();
        long res = 0;

        for (int i = 0; i < varAr.length; i++) {
            if (varAr[i] == '1') {
                res ^= aEnc.get(i);
            }
        }

        return res;
    }

    //decode using the alogithm
    String decodeReedMuller(Long encodedWord) {

        StringBuilder sb = new StringBuilder();//buffer
        StringBuilder res = new StringBuilder();//result

        boolean problem = false;
        String firstChar = "1"; //init

        int zeros, ones, rowDegreeLenght, endIx = this.aDec.size() - 1;
        long sum;

        //start from last row
        rowDegreeLenght = this.aEncCon.get(this.aDec.size() - 1).length();

        for (int i = this.aDec.size() - 1; i > 0; i--) {

            //enter if different r degree or last element
            if ((this.aEncCon.get(i).length() != rowDegreeLenght) || i == 1) {
                //modify encodedWord
                encodedWord ^= this.multiplyVectorToMatrix(this.conBitStringToLong(sb.reverse().toString()), i + 1, endIx);

                //new block end index
                endIx = i;
                //this is the degree, using desc arraylist charcount
                rowDegreeLenght = aEncCon.get(i).length();
                //clean the buffer
                sb = new StringBuilder();
            }

            zeros = 0;
            ones = 0;

            //do scalar sum and count 0s and 1s
            for (int j = 0; j < this.aDec.get(i).size(); j++) {

                sum = scalarMultiplyVectors(encodedWord, aDec.get(i).get(j));

                if (sum == 1) {
                    ones++;
                } else if (sum == 0) {
                    zeros++;
                } else {
                    return null;
                }
            }

            //check if the char is 0 or 1 depending on count
            if (ones > zeros) {
                sb.append(1);
                res.append(1);
            } else if (ones < zeros) {
                res.append(0);
                sb.append(0);
            } else {
                sb.append(0);//some value, all next results will be wrong
                res.append("?");
                problem = true;
            }

        }
        
        //correct order
        res.reverse();

        //determ the first char if no error above, else set to ?
        if (!problem) {
            sum = 0;
            for (int i = 0; i < res.length(); i++) {

                if (res.charAt(i) == '1') {
                    sum ^= this.aEnc.get(i + 1);
                }
            }

            char[] bitArray = this.conLongToBitString(sum ^ encodedWord, "", true).toCharArray();
            zeros = 0;
            ones = 0;

            for (int i = 0; i < bitArray.length; i++) {

                if (bitArray[i] == '1') {
                    ones++;
                } else if (bitArray[i] == '0') {
                    zeros++;
                }
            }

            if (zeros > ones) {
                firstChar = "0";
            }
        } else {
            firstChar = "?";
        }

        return firstChar + res.toString();
    }

    int scalarMultiplyVectors(Long vectorOne, Long vectorTwo) {

        String bitString = this.conLongToBitString(vectorOne & vectorTwo, "", true);
        char[] bitArray = bitString.toCharArray();
        int sum = 0;
        for (int i = 0; i < bitArray.length; i++) {
            sum ^= Integer.parseInt(Character.toString(bitArray[i]));
        }

        return sum;
    }

    //create encoder matrix
    void genEncodeMatrix(int r, int m) {

        if (r >= m || m > 5) {
            System.out.println("Error in matrix dimension");
            return;
        }

        matrixSimElementsCount = m;
        matrixColumns = 1 << m;//2^m

        //create X1 .. Xm
        int n = matrixColumns;
        long val = ((long) 1 << n) - 1;
        for (int i = n; i > 0; i = i >> 1) {
            val = (val >> i) ^ val;
            aEnc.add(val);
        }

        //fill row indexes
        aEncCon.add("f1");
        for (int i = 1; i < m + 1; i++) {
            aEncCon.add("x".concat(String.valueOf(i)));
        }

        //int size;

        //Create depth level r
        for (int i = 2; i < r + 1; i++) {

            //size = aEnc.size();

            //foe each row of X1 .. Xm
            for (int j = 1; j < this.matrixSimElementsCount; j++) {
                buildWithRecursion(j, aEnc.get(0), i, "");
            }

            //check if correct number of rows are added
//            if (aEnc.size() != size + (fact(m) / (fact(i) * fact(m - i)))) {
//                System.out.println("Error!");
//                return;
//            }
        }
        // System.out.println(ac);
    }

    //crate decoding matrix 
    void genDecodeMatrix() {

        this.initStartValuesOfDecodeMatrix();
        this.doAllVariationsForTheDecodeMatrix();

    }

    //check Xi which are not in monomial r and add them for a each row
    void initStartValuesOfDecodeMatrix() {

        ArrayList<Long> el;

        aDec = new ArrayList<ArrayList<Long>>(this.aEnc.size());
        aDec.add(new ArrayList());

        String con;
        for (int i = 1; i < this.aEncCon.size(); i++) {

            el = new ArrayList<Long>();
            con = this.aEncCon.get(i);

            for (int j = 1; j <= this.matrixSimElementsCount; j++) {

                if (!con.contains(String.valueOf(j))) {

                    el.add(aEnc.get(j));

                }
            }
            this.aDec.add(el);
        }
    }

    //do all variations for the inited values
    void doAllVariationsForTheDecodeMatrix() {

        int size;

        for (int i = 1; i < this.aDec.size(); i++) {

            size = this.aDec.get(i).size();
//            System.out.println(this.aDec.get(i) + "-- init");

            doVariationsWithRecursion(this.aDec.get(i), null, i);

//             System.out.println(this.aDec.get(i) + "-- bef");
            //remove x1 .. xm stored values
            for (int j = 0; j < size; j++) {
                this.aDec.get(i).remove(0);
            }

//            System.out.println(this.aDec.get(i) + "\n");
//            for (int j = 0; j < this.aDec.get(i).size(); j++) {
//                System.out.println(this.conLongToBitString(this.aDec.get(i).get(j), " ", true));
//            }
//            System.out.println("---");

        }
    }

    ///recursion for the decoding combinations
    void doVariationsWithRecursion(ArrayList<Long> items, Long prVal, int index) {

        if (items.isEmpty()) {
            return;
        }

        ArrayList<Long> items_new;
        long val = items.get(0);
        long concValP;
        long concValN;

        if (prVal == null) {
            concValP = val;
            concValN = this.invert(val);
        } else {
            concValP = prVal & val;
            concValN = prVal & this.invert(val);
        }


        if (items.size() == 1) {
            this.aDec.get(index).add(concValP);
            this.aDec.get(index).add(concValN);

        } else {
            items_new = (ArrayList<Long>) items.clone();
            items_new.remove(0);

            doVariationsWithRecursion(items_new, concValP, index);
            doVariationsWithRecursion(items_new, concValN, index);

        }
    }

//recusrion for the encoding matrix
    void buildWithRecursion(int startRow, long prValue, int depth, String cList) {

        //check if end point, if so add to matrix
        if (depth == 1) {
            aEnc.add(prValue & aEnc.get(startRow));
            aEncCon.add(cList.concat("x".concat(String.valueOf(startRow))));
            return;
        }

        //otherwise call again for the elements left
        for (int i = startRow + 1; i < this.matrixSimElementsCount + 1; i++) {
            buildWithRecursion(i, prValue & aEnc.get(startRow), depth - 1, cList.concat("x").concat(String.valueOf(startRow)));
        }
    }

    void printMatrix() {

        for (int i = 0; i < aEnc.size(); i++) {
            System.out.println(conLongToBitString(aEnc.get(i), " ", false) + "  " + aEncCon.get(i));
        }
        System.out.println();
    }

    String conLongToBitString(long val, String separator, boolean preserveLenght) {
        long tmp = val;
        StringBuilder sb;

        sb = new StringBuilder();

        while (tmp > 0) {
            sb.append(tmp % 2).append(separator);
            tmp = tmp / 2;
        }

        if (preserveLenght) {//this will add 0s for smaller numbers

            int dif = this.matrixColumns - (sb.toString().replaceAll(separator, "").length());

            if (dif > 0) {
                for (int j = 0; j < dif; j++) {
                    sb.append("0").append(separator);
                }
            }
        }
        return sb.reverse().toString();
    }

    Long conBitStringToLong(String bitString) {

        char[] bitArrray = bitString.replaceAll(" ", "").toCharArray();
        long res = 0;
        for (int i = bitArrray.length - 1; i >= 0; i--) {

            if (bitArrray[i] == '1') {
                res = res + (long) Math.pow(2, bitArrray.length - i - 1);
            } else if (bitArrray[i] != '0') {
                return null;
            }
        }
        return res;
    }

    Long multiplyVectorToMatrix(Long rowVector, int sI, int eI) {

        char[][] arr = new char[eI - sI + 1][this.matrixColumns];
        StringBuilder sb, res = new StringBuilder();

        int index = 0;

        for (int i = sI; i <= eI; i++) {
            arr[index] = this.conLongToBitString(this.aEnc.get(i), "", false).toCharArray();
            index++;
        }

        for (int i = 0; i < arr[0].length; i++) {

            sb = new StringBuilder();

            for (int j = 0; j < arr.length; j++) {
                sb.append(arr[j][i]);
            }
            res.append(this.scalarMultiplyVectors(this.conBitStringToLong(sb.toString()), rowVector));
        }

        return this.conBitStringToLong(res.toString());
    }

    int getMatrixSize() {
        return aEnc.size();
    }

//    int factoriel(int n) {
//        if (n == 0) {
//            return 1;
//        }
//        return n * factoriel(n - 1);
//    }
}

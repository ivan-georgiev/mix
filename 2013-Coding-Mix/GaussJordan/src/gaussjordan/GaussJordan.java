/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gaussjordan;

/**
 *
 * @author Ivan
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GaussJordan {

    //global variable for the matrix
    int[][] a ;

    public static void main(String[] args) throws IOException {

        System.out.println("Gauss-Jordan Binary Parity-Check Matrix Console Generator\n");
        GaussJordan main = new GaussJordan();
        main.readBinaryMatrixFromFile("in.txt", "UTF-8");
        main.doGausJordan();

    }

    void doGausJordan() {

        if (a == null) {
            //matrix error
            return;
        }

        //print the start matrix
        System.out.println("Generator matrix:");
        printMatrix();

        //go column by column
        for (int j = 0; j < a.length; j++) {

            //fix diagonal
            //no need of the if because of the logic in formatAcordingIndex() , added just for clarity
            //if(a[j][j]==0) {
            formatAcordingToTheIndex(j, j);
            //}

            //check if success
            if (a[j][j] == 0) {
                System.out.println("Bad Matrix: [" + (j+1) + "," + (j+1) + "] can not be set to 1");
                return;
            }

            //fix others in the column
            for (int i = 0; i < a.length; i++) {

                //no need of the if because of the logic in formatAcordingIndex() , added just for clarity
                //if (i != j) {
                formatAcordingToTheIndex(i, j);
                //}
            }

            //print fixed matrix
            if (j == a.length - 1) {
                 System.out.println("Systematic form:");
            } else {
                System.out.println("Column " + (j+1) + " fixed:");
            }
            printMatrix();

        }

        //print the result
        System.out.println("Parity-check matrix:");
        printCheckMatrix();
    }

    void formatAcordingToTheIndex(int i, int j) {

        if (i != j && a[i][j] == 1) {
            //element must be 0
            xorRows(i, j);
            // printMatrix();
        } else if (i == j && a[i][j] == 0) {
            //element must be 1, check in the column for 1
            for (int k = i; k < a.length; k++) {
                if (a[k][j] == 1) {
                    xorRows(i, k);
                    //printMatrix();
                    break;
                }
            }
        }
        //else nothing to do for this element
    }

    void xorRows(int rowToBeModified, int rowToBeAdded) {
        for (int i = 0; i < a[0].length; i++) {
            a[rowToBeModified][i] ^= a[rowToBeAdded][i];
        }
    }
      void swapRows(int rowOne, int rowTwo) {
          int tmp;
        for (int j = 0; j < a[0].length; j++) {
          //  a[rowOne][i] ^= a[rowTwo][i];
            
            tmp=a[rowOne][j];
            a[rowOne][j] = a[rowTwo][j];
            a[rowTwo][j] = tmp;
        }
    }

    void printMatrix() {
        int n = a.length;
        int m = a[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(a[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    void printCheckMatrix() {

        int n = a.length;
        int m = a[0].length;

        for (int j = n; j < m; j++) {
        //for (int j = m - 1; j >= n; j--) {  //different order for reading
            for (int i = 0; i < n; i++) {
                System.out.print(a[i][j] + "  ");
            }
            
            //this will complete the row with the proper one from the Identity matrix
            printIndentityMatrixRow(m-n, j-n );  
            //printIndentityMatrixRow(m-n, m - j - 1);      //different order for reading      
            System.out.println();
        }

        System.out.println();
        System.out.println();
    }

    void printIndentityMatrixRow(int size, int row) {

        if (row >= size) {
            System.out.print("Error: Row number error");
        }

        for (int i = 0; i <= row; i++) {
            for (int j = 0; j < size; j++) {
                if (i == row) {
                    System.out.print(((i == j) ? 1 : 0) + "  ");
                }
            }
        }
    }

    void readBinaryMatrixFromFile(String fileName, String enc) throws IOException {

        File fFile = new File(fileName);
        Scanner scanner = new Scanner(fFile, enc);
        ArrayList<ArrayList<Integer>> ar = new ArrayList<ArrayList<Integer>>();
        StringTokenizer st;
        String row;
        ArrayList<Integer> rw;
        int tmp;
        int prevRowElementsCount = -1;

        while (scanner.hasNextLine()) {
            //System.out.println(scanner.next().toString());
            rw = new ArrayList<Integer>();
            ar.add(rw);
            row = scanner.nextLine().toString();
            st = new StringTokenizer(row);

            //first row
            if (prevRowElementsCount == -1) {
                prevRowElementsCount = st.countTokens();
            }

            //check for diff count
            if (prevRowElementsCount != st.countTokens()) {
                System.out.println("Error: not equal count columns");
                return;
            }

            while (st.hasMoreTokens()) {
                tmp = Integer.parseInt(st.nextToken());
                if (tmp == 0 || tmp == 1) {
                    rw.add(tmp);
                } else {
                    System.out.println("Error: Not binary matrix");
                    return;
                }
            }
        }

        if (ar.size() >  ar.get(1).size()) {
            System.out.println("Error: The columns are less then the rows");
            return;
        }

        //Array will be used instead of ArrayList 
        a = new int[ar.size()][ar.get(1).size()];

        for (int i = 0; i < ar.size(); i++) {

            for (int j = 0; j < ar.get(1).size(); j++) {
                a[i][j] = ar.get(i).get(j);
            }
        }
    }
}
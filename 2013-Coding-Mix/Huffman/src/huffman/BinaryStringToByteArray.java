/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ivan
 * 
 * This class is a standart encoder/decoder for binary strings to real bytes. 
 * 
 * Based on parts from the article 'Understanding the Huffman Data Compression Algorithm in Java'
 * of By Richard G. Baldwin
 * http://www.developer.com/java/other/article.php/3603066/Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
 * 
 */
public class BinaryStringToByteArray {

    private HashMap<String, Byte> encodingBitMap =
            new HashMap<String, Byte>();
    private HashMap<Byte, String> decodingBitMap =
            new HashMap<Byte, String>();

    public BinaryStringToByteArray() {

        for (int cnt = 0; cnt <= 255; cnt++) {
            StringBuilder workingBuf = new StringBuilder();
            if ((cnt & 128) > 0) {
                workingBuf.append("1");
            } else {
                workingBuf.append("0");
            }
            if ((cnt & 64) > 0) {
                workingBuf.append("1");
            } else {
                workingBuf.append("0");
            }
            if ((cnt & 32) > 0) {
                workingBuf.append("1");
            } else {
                workingBuf.append("0");
            }
            if ((cnt & 16) > 0) {
                workingBuf.append("1");
            } else {
                workingBuf.append("0");
            }
            if ((cnt & 8) > 0) {
                workingBuf.append("1");
            } else {
                workingBuf.append("0");
            }
            if ((cnt & 4) > 0) {
                workingBuf.append("1");
            } else {
                workingBuf.append("0");
            }
            if ((cnt & 2) > 0) {
                workingBuf.append("1");
            } else {
                workingBuf.append("0");
            }
            if ((cnt & 1) > 0) {
                workingBuf.append("1");
            } else {
                workingBuf.append("0");
            }
            encodingBitMap.put(workingBuf.toString(),
                    (byte) (cnt));
            decodingBitMap.put(
                    (byte) (cnt), workingBuf.toString());

        }
    }

    public ArrayList<Byte> encode(String binaryString) {

        ArrayList<Byte> result = new ArrayList<Byte>();
        String strBits;

        byte realBits;
        // it to be a multiple of 8.
        int toFill = 8 - ((binaryString.length() + 3) % 8);
        if (toFill == 8) {
            toFill = 0;
        }

        for (int cnt = 0; cnt < toFill; cnt++) {
            binaryString += "0";
        }

        switch (toFill) {
            case 0:
                binaryString += "000";
                break;
            case 1:
                binaryString += "001";
                break;
            case 2:
                binaryString += "010";
                break;
            case 3:
                binaryString += "011";
                break;
            case 4:
                binaryString += "100";
                break;
            case 5:
                binaryString += "101";
                break;
            case 6:
                binaryString += "110";
                break;
            case 7:
                binaryString += "111";
                break;
        }

        for (int cnt = 0; cnt < binaryString.length(); cnt += 8) {
            strBits = binaryString.substring(cnt, cnt + 8);
            realBits = encodingBitMap.get(strBits);
            result.add(realBits);
        }

        return result;
    }

    public String decodeText(ArrayList<Byte> byts) {

        StringBuilder workingBuf = new StringBuilder();
        String result;

        for (int i = 0; i < byts.size(); i++) {

            workingBuf.append(decodingBitMap.get(byts.get(i)));
        }

        String lst = workingBuf.substring(workingBuf.length() - 3);
        int size = Integer.parseInt(lst, 2);


        result = workingBuf.substring(0, workingBuf.length() - size - 3).toString();


        return result;
    }
}

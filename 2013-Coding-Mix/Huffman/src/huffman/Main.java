/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Ivan
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    private static ArrayList<Node> tree = new ArrayList<Node>();
    private static HashMap<Character, Node> leafs = new HashMap<Character, Node>();

    public static void main(String[] args) {

        System.out.println("\nHuffman coding Demo program\n");

        String word;
        String encodedWord;
        String decodedWord;
        BinaryStringToByteArray encoder = new BinaryStringToByteArray();

        word = readFile();
        //word = "this is the input word";  

        System.out.println("\n### Input ###");
        if (word == null) {
            System.out.println("Empty word.");
            System.exit(1);
        }

        System.out.println("Input sentence: " + word
                + "\nLenght: " + word.length());


        System.out.println("\n### Processing ###");
        //Create leafs and the tree
        createLeafs(word);
        constructTree();

        //create the alphabet starting from the root on index (tree.size()-1)

        //protection if one char is in the input sentence
        if (tree.size() == 1) {
            tree.get(0).huffmanRpr = "1";
        } else {
            fillAlph(tree.size() - 1, "");
        }
        printAlph();

        //Replace the chars with the ones from alphabet
        encodedWord = encodeTextToBinaryString(word);
        System.out.println("Encoded: " + encodedWord);

        //Do the real encoding
        ArrayList<Byte> realByteEncoded = encoder.encode(encodedWord);
        System.out.println("\nRealByte Encoded: " + realByteEncoded.toString());

        //Decode the real encoding
        String realByteDecoded = encoder.decodeText(realByteEncoded);

        //Decode the huffman alphabet using the tree.
        decodedWord = decodeText(realByteDecoded);

        System.out.println("\n### Output ###");
        System.out.println("Decoded: " + decodedWord);

        if (word == null ? decodedWord == null : word.equals(decodedWord)) {
            System.out.println("Result: OK");
        } else {
            System.out.println("Result: NOK");
        }


        //Print statistic
        System.out.println("\n### Statistic ###");
        System.out.println("Input sentence size = " + word.length() * 8 + " bits / " + word.length() + " bytes");
        System.out.println("Huffman encoded sentence size = " + encodedWord.length() + " bits");
        System.out.println("Huffman Ratio: " + (encodedWord.length() * 100 / (word.length() * 8)) + "%");
//        System.out.println("\nRealByte encoding price: "
//                + (realByteEncoded.size() * 8 - encodedWord.length())
//                + " bits");
//        System.out.println("RealByte encoded word size: "
//                + (realByteEncoded.size())
//                + " bytes");
//        System.out.println("RealByte Ratio: " + (realByteEncoded.size() * 800 / (word.length() * 8)) + "%");
        System.out.println();

    }

    private static void createLeafs(String word) {

        Node node;

        for (int i = 0; i < word.length(); i++) {

            char cur = word.charAt(i);

            if (leafs.containsKey(cur)) {

                leafs.get(cur).weight++;
            } else {

                node = new Node(cur);
                leafs.put(cur, node);
                tree.add(node);

            }
        }
    }

    private static void constructTree() {
        int unvisited = 0;
        Integer[] son;
        //when unvisited == 2 the root is created

        while (unvisited != 2) {
            //in son[] the two lowest elements indexes are saved : son[0]<son[1]
            son = new Integer[2];
            son[0] = null;
            son[1] = null;
            unvisited = 0;

            for (int i = 0; i < tree.size(); i++) {

                if (tree.get(i).visited) {
                    continue;
                } else {

                    unvisited++;
                    if (son[0] == null) {
                        son[0] = i;
                        continue;
                    }

                    if (son[1] == null) {

                        son[1] = i;

                        if (tree.get(son[1]).weight < tree.get(son[0]).weight) {
                            son[0] = son[0] ^ son[1];
                            son[1] = son[0] ^ son[1];
                            son[0] = son[0] ^ son[1];
                        }
                        continue;
                    }

                    if (tree.get(i).weight < tree.get(son[0]).weight) {
                        son[1] = son[0];
                        son[0] = i;
                        continue;
                    }
                    if (tree.get(i).weight < tree.get(son[1]).weight) {
                        son[1] = i;
                        continue;
                    }
                }
            }

            //protection if one char only is in the input sentence
            if (son[1] == null) {
                return;
            }

            tree.get(son[0]).visited = true;
            tree.get(son[1]).visited = true;
            tree.add(new Node(son[0], son[1], tree.get(son[0]).weight + tree.get(son[1]).weight));
        }

    }

    private static void fillAlph(int index, String s) {

        tree.get(index).huffmanRpr = s;

        if (tree.get(index).lnode != null) {
            fillAlph(tree.get(index).lnode, s.concat("0"));
        }
        if (tree.get(index).rnode != null) {
            fillAlph(tree.get(index).rnode, s.concat("1"));
        }

    }

    private static void printAlph() {
        System.out.println("Huffman alphabet:");


        List<Node> nodesByWeight = new ArrayList<Node>(leafs.values());

        Collections.sort(nodesByWeight, new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                return o2.weight - o1.weight;
            }
        });

        for (Node node : nodesByWeight) {

            System.out.println(node.ch + " : " + node.weight + " = " + node.huffmanRpr);
        }
        System.out.println("\nLeafs count: " + leafs.size());
        //System.out.println("Tree elements count: " + tree.size() + "\n");
    }

    private static String encodeTextToBinaryString(String word) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {

            char cur = word.charAt(i);

            result.append(leafs.get(cur).huffmanRpr);

        }
        return result.toString();
    }

    private static String decodeText(String binaryString) {

        StringBuilder res = new StringBuilder();
        StringBuilder buf;
        Node node;
        int i;
        Character chr;


        while (!binaryString.isEmpty()) {

            node = tree.get(tree.size() - 1);
            i = 0;
            chr = null;
            buf = new StringBuilder();
            buf.append(binaryString.charAt(i));

            while (chr == null && buf.length() <= binaryString.length()) {

                Node n = getLeafValue(node, buf.toString());

                if (n == null) {
                    i++;
                    buf.append(binaryString.charAt(i));
                } else {
                    chr = n.ch;
                    res = res.append(chr);
                }

            }

            binaryString = binaryString.substring(buf.length());

        }

        return res.toString();

    }

    private static Node getLeafValue(Node node, String path) {

        if (node.isLeaf()) {
            return node;
        }

        if (path.isEmpty()) {
            return null;
        }

        char ch = path.charAt(0);

        if (ch == '0') {
            node = getLeafValue(tree.get(node.lnode), path.substring(1));

        } else if (ch == '1') {
            node = getLeafValue(tree.get(node.rnode), path.substring(1));
        }

        return node;


    }

    private static String readFile() {
        String row = null;
        String curDir;
        try {
            File dir = new File(".");
            curDir = dir.getCanonicalPath().toString();
            System.out.println("Input file:\n" + curDir + File.separator + "inputword.txt");
            File fFile = new File(curDir + File.separator + "inputword.txt");
            Scanner scanner = new Scanner(fFile, "Unicode");
            if (scanner.hasNextLine()) {
                row = scanner.nextLine().toString();
            }
            return row;
        } catch (Exception ex) {
            System.out.println("File processing error:" + ex);
            return null;
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

/**
 *
 * @author Ivan
 */
public class Node {

    Character ch = null; // input char
    int weight = 1; //counter how many times is met in the input sentence
    String huffmanRpr = ""; //huffman code for the node
    boolean visited = false; //used to mark if the node is added to the tree
    Integer lnode = null; //left notde index
    Integer rnode = null; //right node index

    //node constructor
    public Node(int ln, int rn, int wght) {
        this.lnode = ln;
        this.rnode = rn;
        this.weight = wght;
    }

    //leaf constructor
    public Node(char c) {
        this.ch = c;
    }
    

    public boolean isLeaf() {
        if (this.lnode == null && this.rnode == null) {
            return true;
        } else {
            return false;
        }  

    }
}

package com.example.rto;

class Node {
    boolean isWord;
    char c;
    Node[] children;
    String information;

    Node(char c) {
        c = this.c;
        isWord = false;
        children = new Node[36];
    }
}

public class Trie {
    Node root = new Node('\0');

    // inserts into trie
    void insert(String word) {
        // if word is already present the trie will be
        if (search(word)) {
            System.out.println(word + " already exists");
            return;
        }

        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (curr.children[getIndex(c)] == null)
                curr.children[getIndex(c)] = new Node(c);
            curr = curr.children[getIndex(c)];
        }
        curr.isWord = true;
        curr.information = new String(
                "21/08/2008, 1234, Prathamesh, SUV, Petrol, Maruti Suzuki Ertiga, ACTIVE, 01/12/2021");
    }

    // Returns true if root has no children, else false
    boolean isEmpty(Node root) {
        for (int i = 0; i < 36; i++)
            if (root.children[i] != null)
                return false;
        return true;
    }

    // helper function to remove
    void remove(String key) {
        remove(root, key, 0);
        System.out.println(search(key));
    }

    Node remove(Node root, String key, int depth) {
        // If tree is empty
        if (root == null)
            return null;

        // If last character of key is being processed
        if (depth == key.length()) {

            // This node is no more end of word after
            // removal of given key
            if (root.isWord)
                root.isWord = false;

            // If given is not prefix of any other word
            if (isEmpty(root)) {
                root = null;
            }

            return root;
        }

        // If not last character, recur for the child
        // obtained using ASCII value
        int index = getIndex(key.charAt(depth));
        root.children[index] = remove(root.children[index], key, depth + 1);

        // If root does not have any child (its only child got
        // deleted), and it is not end of another word.
        if (isEmpty(root) && root.isWord == false) {
            root = null;
        }

        return root;
    }

    // search if a word is present in the trie or not
    boolean search(String word) {
        Node node = getNode(word);
        return node != null && node.isWord;
    }

    // get information about vehicle number
    void getInfo(String word) {
        System.out.println(getNode(word).information);
    }

    // search if the word starts with the given sequence
    boolean startsWith(String word) {
        return getNode(word) != null;
    }

    // returns true if the character is between '0'to '9'
    boolean isNumber(char c) {
        if ((c - 48) >= 0 && (c - 48) < 10)
            return true;
        return false;
    }

    // returns index of the character
    int getIndex(char c) {
        if (isNumber(c))
            return c - '0';
        return c - 'A' + 10;
    }

    // returns character at the given index
    char getChar(int i) {
        if (i >= 0 && i < 10)
            return (char) (i + '0');
        return (char) (i - 10 + 'A');
    }

    // returns node represented by end of the word
    Node getNode(String word) {
        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (curr.children[getIndex(c)] == null)
                return null;
            curr = curr.children[getIndex(c)];
        }
        return curr;
    }

    /* Display the contents of the Trie */
    void display() {
        StringBuilder sb = new StringBuilder();
        // we pass in a root node, string builder, and a level (index) to insert chars
        // at
        displayHelper(root, sb, 0);
    }

    void displayHelper(Node node, StringBuilder str, int level) {
        // base case for displaying a full word (key)
        // if a node is the end of a word (boolean), we print
        if (node.isWord) {
            // clear any chars remaining from previous words inserted into the string
            // builder
            str.delete(level, str.length());
            System.out.println(str.toString());
        }

        // loop through all the indices through a child array
        // if a non null child is found, append the character to the String 'str'
        // and recursively call the helper method on its child node
        for (int i = 0; i < 36; i++) {
            if (node.children[i] != null) {
                // insert a char at the level index

                // example: level = 2, char is 'y'
                // our current string builder contains:
                // t r a n k s b y e a....
                // _ _ _ _ _ _ _ _ _ _

                // now, we replace
                // t r y <--insert at index 2, we'll clear other chars at the base case
                // _ _ _
                str.insert(level, Character.toString(getChar(i)));
                displayHelper(node.children[i], str, level + 1);
            }
        }
    }
}

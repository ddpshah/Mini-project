package com.example.rto;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

class Node implements Parcelable {
    boolean isWord;
    char c;
    Node[] children;

    Node(char c) {
        this.c = c;
        isWord = false;
        children = new Node[36];
    }

    protected Node(Parcel in) {
        isWord = in.readByte() != 0;
        c = (char) in.readInt();
        children = in.createTypedArray(Node.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isWord ? 1 : 0));
        dest.writeInt((int) c);
        dest.writeTypedArray(children, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };
}

public class Trie implements Parcelable {
    Node root = new Node('\0');

    protected Trie(Parcel in) {
        root = in.readParcelable(Node.class.getClassLoader());
    }

    public static final Creator<Trie> CREATOR = new Creator<Trie>() {
        @Override
        public Trie createFromParcel(Parcel in) {
            return new Trie(in);
        }

        @Override
        public Trie[] newArray(int size) {
            return new Trie[size];
        }
    };

    public Trie() {

    }

    // inserts into trie
    int insert(String word) {
        // if word is already present the trie will be
        if (search(word).equals(word)) {
            return -1;
        }

        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (curr.children[getIndex(c)] == null)
                curr.children[getIndex(c)] = new Node(c);
            curr = curr.children[getIndex(c)];
        }
        curr.isWord = true;
        return 0;
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
    String search(String word) {
        Node node = getNode(word);
        if(node != null && node.isWord)
            return word;
        return "";
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(root, flags);
    }
}

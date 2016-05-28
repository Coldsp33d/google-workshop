package com.google.engedu.ghost;

import android.util.Log;
import java.util.HashMap;
import java.util.Random;

public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private Random random = new Random();

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {

        TrieNode temp = this;
        char key;
        //Log.i("TAG", s);
        for(int i = 0; i < s.length(); i++)
        {
            key = s.charAt(i);

            if(temp.children.containsKey(key)) {
                temp = temp.children.get(key);
            }
            else {
                TrieNode newNode = new TrieNode();
                temp.children.put(key, newNode);

                temp = newNode;
            }
        }
        temp.isWord = true;
    }

    public boolean isWord(String s) {
        char key;
        TrieNode temp = this;
        for(int i = 0; i < s.length(); i++)
        {
            key = s.charAt(i);

            if(temp.children.containsKey(key)) {
                temp = temp.children.get(key);
            }
            else return false;
        }
        return temp.isWord;
    }

    public String getAnyWordStartingWith(String s) // just gets a valid (prefix + character) string
    {
        char key;
        Log.i("PREFIX", s);
        if(s == "") {
            return Character.toString((char)((int)'a' + new Random().nextInt(27)));
        }

        TrieNode temp = this;
        for(int i = 0; i < s.length(); i++)
        {
            key = s.charAt(i);
            if(temp.children.containsKey(key)) {
                temp = temp.children.get(key);
            }
            else {
                return null;
            }
        }
        Object[] keys = temp.children.keySet().toArray();

        return s +  (Character)keys[new Random().nextInt(keys.length)];
    }

    public String getGoodWordStartingWith(String s) // gets a random but complete word starting with the given prefix
    {
        char key;
        Log.i("PREFIX", s);
        if(s == "") {
            return Character.toString((char)((int)'a' + new Random().nextInt(27)));
        }

        TrieNode temp = this;
        for(int i = 0; i < s.length(); i++)
        {
            key = s.charAt(i);
            if(temp.children.containsKey(key)) {
                temp = temp.children.get(key);
            }
            else {
                return null;
            }
        }
        return s.substring(0, s.length() - 1) + randomWord(s.charAt(s.length() - 1), temp);
    }

    public String randomWord(char c, TrieNode node) // returns a random but valid word
    {
        Object[] keys = node.children.keySet().toArray();
        if(node.isWord) {
            return c + "";
        }
        else if(keys.length == 0) {
            return c + "";
        }

        char randChar = (Character)keys[random.nextInt(keys.length)];
        return c + randomWord(randChar, node.children.get(randChar)) + "";
    }
}



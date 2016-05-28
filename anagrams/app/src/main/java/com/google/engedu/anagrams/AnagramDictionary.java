package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private int wordSize = DEFAULT_WORD_LENGTH;

    ArrayList<String> wordList = new ArrayList<>();
    HashSet<String> wordSet = new HashSet<>();
    HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    HashMap<Integer, ArrayList<String>> sizeToWord = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line, sortedLine;
        while((line = in.readLine()) != null) {
            line = line.trim();

            wordList.add(line);
            wordSet.add(line);

            sortedLine = sortLetters(line);

            if(!lettersToWord.containsKey(sortedLine))
            {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(line);
                lettersToWord.put(sortedLine, temp);
            }
            else lettersToWord.get(sortedLine).add(line);

            if(!sizeToWord.containsKey(line.length()))
            {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(line);
                sizeToWord.put(line.length(), temp);
            }
            else sizeToWord.get(line.length()).add(line);


        }
    }

    public boolean isGoodWord(String word, String base) {

        if(!wordSet.contains(word)) return false;

        for(int i = 0; i < word.length() - base.length(); i++)
        {
            if(word.substring(i, i + base.length()).equals(base))
                return false;
        }
        return true;
    }

    String sortLetters(String word)
    {
        char[] c = word.toCharArray();
        Arrays.sort(c);
        return new String(c);
    }

    /*public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        String temp1, temp2;

        for(String word : wordList)
        {
            temp1 = sortLetters(word);
            temp2 = sortLetters(targetWord);
            if(temp1.length() == temp2.length() && temp1.equals(temp2))
                result.add(word);
        }
        return result;
    }*/

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String temp;
        for(char c = 'a'; c < 'z'; c++)
        {
            temp = sortLetters(word + c);
            if(lettersToWord.containsKey(temp))
                result.addAll(lettersToWord.get(temp));
        }
        Log.i("TAG " + word, result.toString());
        return result;
    }

    public String pickGoodStarterWord() {
        //return "stop";
        ArrayList<String> tempList = sizeToWord.get(wordSize);
        Log.i("TAG", tempList.toString());
        Log.i("TAG", wordSize+"");
        int index = random.nextInt(tempList.size());
        String temp;

        do
        {
            temp = tempList.get(index);

            if (lettersToWord.get(sortLetters(temp)).size() < MIN_NUM_ANAGRAMS)
            {
                index = random.nextInt(tempList.size());
                continue;
            }

            break;
        }while(true);

        wordSize = wordSize + 1 > MAX_WORD_LENGTH ? wordSize : wordSize + 1;
        return temp;
    }
}

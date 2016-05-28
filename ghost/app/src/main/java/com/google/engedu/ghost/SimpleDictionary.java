package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());

        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {

        ArrayList<String> tempList = new ArrayList<>();
        if (prefix == "") words.get(new Random().nextInt(words.size()));


        int l = 0, u = words.size(), m, compare;
        String word, temp;

        /* Binary search */
        while (l <= u) {
            m = (u + l) / 2;

            word = temp = words.get(m);
            if(temp.length() > prefix.length()) {
                temp = word.substring(0, prefix.length());
            }

            compare = temp.compareTo(prefix);

            if(compare > 0) {
                u = m - 1;
            }
            else if(compare < 0) {
                l = m + 1;
            }
            else {
                tempList.add(word);

                while(++m < words.size()
                         && words.get(m).length() > prefix.length()
                         && words.get(m).substring(0, prefix.length()).equals(prefix))
                {
                    tempList.add(words.get(m));
                }
                Log.i("TAG", tempList.toString());
                return tempList.get(new Random().nextInt(tempList.size()));
            }
        }

        return null;
    }

    // not to be implemented for Unit 3
    @Override
    public String getGoodWordStartingWith(String prefix) {
        return getAnyWordStartingWith(prefix);
    }
}

# Google Workshop Assignment Solutions 

## 1. _Anagrams_

An anagram is a word formed by rearranging the letters of another word. For example, cinema is an anagram of iceman.

The mechanics of the game are as follows:

The game provides the user with a word from the dictionary.
The user tries to create as many words as possible that contain all the letters of the given word plus one additional letter. Note that adding the extra letter at the beginning or the end without reordering the other letters is not valid. For example, if the game picks the word 'ore' as a starter, the user might guess 'rose' or 'zero' but not 'sore'.
The user can give up and see the words that they did not guess.

![Anagrams](https://cswithandroid.withgoogle.com/content/assets/img/anagrams.png)

## 2. _Scarne's Dice_

Scarne’s Dice is a turn-based dice game where players score points by rolling a die and then:

* if they roll a 1, score no points and lose their turn
* if they roll a 2 to 6:
    * add the rolled value to their points
    * choose to either reroll or keep their score and end their turn
  
The winner is the first player that reaches (or exceeds) 100 points.

![Scarne's Dice](https://cswithandroid.withgoogle.com/content/assets/img/scarnes_dice.png)

## 3. _Ghost_

Ghost is a word game in which players take turns adding individual letters to a growing word fragment, trying not to be the one to complete a valid word. Each fragment must be the beginning of an actual word, and, for our purposes, we will consider 4 to be the minimum word length. The player who completes a word or creates a fragment that is not the prefix of a word loses the round.

So on player 1's turn, they can:

* challenge player 2's word if they think player 2 has formed a valid word of at least 4 letters. If the fragment is a word, then player 1 wins; if the fragment is not a word, then player 2 wins.
* challenge player 2's word if they think that no word can be formed with the current fragment. Then, player 2 must provide a valid word starting with the current fragment or lose.
* add a letter to move the fragment towards a valid word
* attempt to bluff player 2 by adding a letter that doesn't move the fragment towards a valid word

![Ghost](https://cswithandroid.withgoogle.com/content/assets/img/ghost.png)

_Note: Ghost has been implemented using both Binary Search as well as Trie trees.

## 4. _Puzzle8_

The idea is to slide tiles around to form an image or to recreate a numerical sequence. Each tile can slide either horizontally or vertically into the empty space in order to reorder the tiles.

![Puzzle8](https://cswithandroid.withgoogle.com/content/assets/img/15puzzleapp.png)



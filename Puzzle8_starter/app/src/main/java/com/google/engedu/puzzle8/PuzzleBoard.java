package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import org.apache.http.client.methods.HttpUriRequest;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };
    private ArrayList<PuzzleTile> tiles;

    private int steps;
    private PuzzleBoard previousBoard;

    public PuzzleBoard getPreviousBoard() { return previousBoard; }

    public int getSteps() { return steps; }

    public ArrayList<PuzzleBoard> getTiles() { return (ArrayList<PuzzleBoard>) tiles.clone(); }

    /* Edited - chops the image into tiles and adds it to the ArrayList */
    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        steps = 0;
        previousBoard = null;
        //Log.i("TAG", parentWidth+"");
        tiles = new ArrayList<>();
        PuzzleTile tile = null;
        bitmap = Bitmap.createScaledBitmap(bitmap, parentWidth, parentWidth, false);
        int tileSize = parentWidth / NUM_TILES;

        int ctr = 0;
        for (int i = 0; i < NUM_TILES; i++) {
            for (int j = 0; j < NUM_TILES; j++) {
                if(i == NUM_TILES - 1 && j == NUM_TILES - 1) {
                    tiles.add(null);
                }
                tile = new PuzzleTile(Bitmap.createBitmap(bitmap, i * tileSize, j * tileSize, tileSize, tileSize), ctr++);
                tiles.add(tile);
            }
        }
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        steps = otherBoard.steps + 1;
        previousBoard = otherBoard;
    }

    public void reset() {
        steps = 0;
        previousBoard = null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i / NUM_TILES, i % NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i / NUM_TILES, i % NUM_TILES)) {
                    return tryMoving(i / NUM_TILES, i % NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    /* Edited - changed to row major */
    private int XYtoIndex(int x, int y) {
        return y + x * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> possibilities = new ArrayList<>();
        PuzzleBoard temp;

        int nullX = 0, nullY = 0, neighbourX, neighbourY;
        for (int i = 0; i < NUM_TILES; i++)  // locate null tiles
        {
            for (int j = 0; j < NUM_TILES; j++) {
                if (tiles.get(XYtoIndex(i, j)) == null) {
                    nullX = i;
                    nullY = j;
                }
            }
        }
        for (int[] neighbour : NEIGHBOUR_COORDS) {
            neighbourX = nullX + neighbour[0];
            neighbourY = nullY + neighbour[1];

            if (neighbourX >= 0 && neighbourX < NUM_TILES && neighbourY >= 0 && neighbourY < NUM_TILES) {
                temp = new PuzzleBoard(this);
                temp.swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(neighbourX, neighbourY));

                if (!this.equals(temp)) {
                    possibilities.add(temp);
                }
            }
        }
        return possibilities;
    }

    /* Edited */
    public int priority() {

        int row, col;
        int manhattan = 0;
        PuzzleTile tile;
        for (int i = 0; i < NUM_TILES; i++)  // locate null tiles
        {
            for (int j = 0; j < NUM_TILES; j++) {
                tile = tiles.get(XYtoIndex(i, j));

                if(tile == null) continue;

                row = tile.getNumber() / NUM_TILES;
                col = tile.getNumber() % NUM_TILES;

                manhattan += Math.abs(i - row) + Math.abs(j - col);
            }
        }
        return manhattan + this.steps;
    }

    public int manhattan() {
        return priority() - steps;
    }
}
package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    /* Edited - Added new function so that the renderer does not complain */
    public PuzzleBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //activity = (Activity) context; // does not work for some reason
        animation = null;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = getWidth();
        if(width > 0)
            puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(400);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    /* Edited */
    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            ArrayList<PuzzleBoard> possibilities;
            for(int i = 0; i < NUM_SHUFFLE_STEPS; i++)
            {
                possibilities = puzzleBoard.neighbours();
                puzzleBoard = possibilities.get(random.nextInt(possibilities.size()));
            }
            //puzzleBoard.reset();
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    /* Edited */
    public void solve() {
        PuzzleBoard temp = null;
        ArrayList<PuzzleBoard> visited = new ArrayList<>();
        /*PriorityQueue<PuzzleBoard> priorityQueue= new PriorityQueue<>(10, new Comparator<PuzzleBoard>()
                {
                    @Override
                    public int compare(PuzzleBoard state1, PuzzleBoard state2)
                    {
                        if(state1.manhattan() == state2.manhattan() && state1.equals(state2)) {
                            return 0;
                        }

                        return (state1.priority() - state2.priority());
                    }
                });
*/
        CustomPriorityQueue priorityQueue = new CustomPriorityQueue();

        puzzleBoard.reset();
        priorityQueue.add(puzzleBoard);

        /*Log.i("TAG", puzzleBoard.getSteps() + "");
        Log.i("TAG", puzzleBoard.getPreviousBoard() == null? "Null" : puzzleBoard.getPreviousBoard().toString());*/
        while(!priorityQueue.isEmpty())
        {
            Log.i("TAG", "INSIDE QUEUE: " + priorityQueue.size());
            temp = priorityQueue.poll();
            visited.add(temp);

            if(temp.resolved())
                break;

            for(PuzzleBoard o : temp.neighbours()) {
                if (!visited.contains(o)) {
                    priorityQueue.add(o);
                }
            }
        }

        ArrayList<PuzzleBoard> solution = new ArrayList<>();
        solution.add(temp);
        while(temp.getPreviousBoard() != null) {
            solution.add(temp.getPreviousBoard());
            temp = temp.getPreviousBoard();
        }

        Collections.reverse(solution);

        this.animation = (ArrayList<PuzzleBoard>) solution.clone();

        invalidate();
    }

}

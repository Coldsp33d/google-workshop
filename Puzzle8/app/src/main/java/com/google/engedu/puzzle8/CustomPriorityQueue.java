package com.google.engedu.puzzle8;


import java.util.ArrayList;

/**
 * Created by shivadeviah on 29/05/16.
 *
 * Custom implementation of min-heap priority queue specially for PuzzleBoard objects.
 */
public class CustomPriorityQueue {

    private ArrayList<PuzzleBoard> queue;
    public CustomPriorityQueue()
    {
        queue = new ArrayList<>();
        queue.add(null);
    }

    public void add(PuzzleBoard board)
    {
        queue.add(board);
        buildHeap();
    }

    private void buildHeap()
    {
        for(int i = queue.size() / 2; i >= 1; i--)
        {
            heapify(i);
        }
    }

    private void heapify(int i)
    {
        int left = i * 2;
        int right = i * 2 + 1;
        int smallest;

        if(left <= queue.size() - 1  && queue.get(left).priority() < queue.get(i).priority())
            smallest = left;

        else smallest = i;

        if(right <= queue.size() - 1 && queue.get(right).priority() < queue.get(i).priority())
            smallest = right;

        if(smallest != i) {
            PuzzleBoard temp = queue.get(smallest);
            queue.set(smallest, queue.get(i));
            queue.set(i, temp);

            heapify(smallest);
        }
    }

    public PuzzleBoard poll()
    {
        PuzzleBoard min = queue.get(1);

        queue.set(1, queue.get(queue.size() - 1));
        queue.remove(queue.size() - 1);

        heapify(1);

        return min;
    }

    public boolean isEmpty()
    {
        return queue.size() == 1;
    }

    public int size()
    {
        return queue.size() - 1;
    }
}

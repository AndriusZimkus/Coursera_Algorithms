import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] rQ = (Item[]) new Object[1];
    private int n = 0;
    private int tail = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {


    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return tail;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        n++;
        if (n == rQ.length) {
            resize(2 * rQ.length);
        }

        rQ[tail++] = item;

    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        if (n > 0 && n == rQ.length / 4) {
            resize(rQ.length / 2);
        }
        int randomItem = StdRandom.uniform(tail);
        Item item = rQ[randomItem];
        rQ[randomItem] = rQ[tail - 1];
        rQ[tail - 1] = null;
        tail--;
        n--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int randomItem = StdRandom.uniform(tail);
        return rQ[randomItem];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();

    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] unusedNumbers = new int[n];
        private int itemsLeft = n;

        private RandomizedQueueIterator() {
            for (int i = 0; i < n; i++) {
                unusedNumbers[i] = i;
            }
        }


        public boolean hasNext() {
            return itemsLeft > 0;
        }

        public Item next() {
            if (itemsLeft == 0) {
                throw new java.util.NoSuchElementException();
            }
            itemsLeft--;
            int currentIndex = StdRandom.uniform(0, itemsLeft + 1);
            Item currentItem = rQ[unusedNumbers[currentIndex]];
            unusedNumbers[currentIndex] = unusedNumbers[itemsLeft];

            return currentItem;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = rQ[i];
        }
        rQ = copy;
    }

    // unit testing (required)
    public static void main(String[] args) {

        RandomizedQueue<Integer> queue = new RandomizedQueue<>();


        queue.enqueue(45);
        queue.dequeue();
        queue.enqueue(15);
        queue.dequeue();
        queue.enqueue(32);


        for (Integer b : queue) {
            StdOut.println(b);
        }


    }

}

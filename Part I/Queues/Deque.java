import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first = null;
    private Node last = null;
    private int size = 0;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;

    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("");
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.previous = null;
        if (oldFirst != null) {
            oldFirst.previous = first;
        } else {
            last = first;
        }

        size += 1;

    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("");
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        last.next = null;

        if (oldLast != null) {
            oldLast.next = last;
        } else {
            first = last;
        }
        size += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        if (size > 1) {
            first.previous = null;
        } else if (size == 1) {
            last = null;
        }

        size -= 1;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("");
        }
        Item item = last.item;
        last = last.previous;
        if (size > 1) {
            last.next = null;
        } else if (size == 1) {
            first = null;
        }

        size -= 1;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new java.util.NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;

            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    // unit testing (required)
    public static void main(String[] args) {
        
        Deque<Integer> testDeque = new Deque<>();
        testDeque.addLast(1);
        testDeque.addLast(2);
        testDeque.addLast(3);
        testDeque.removeFirst();

        System.out.println("printing Deque");

        for (Integer element : testDeque) {
            System.out.println(element);
        }


    }

}

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int itemCount = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            rq.enqueue(word);
        }

        int i = 0;
        for (String element : rq) {
            if (i == itemCount) {
                break;
            }
            System.out.println(element);
            i++;
        }

    }
}

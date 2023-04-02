import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;


public class RandomWord {

    public static void main(String[] args) {
        int i = 0;
        String champion = "";

        while (!StdIn.isEmpty()) {

            String word = StdIn.readString();
            //System.out.println(i + " " + StdIn.isEmpty());

            i++;

            if (StdRandom.bernoulli(1.0 / i)) {
                champion = word;
            }

        }
        System.out.println(champion);

    }
}

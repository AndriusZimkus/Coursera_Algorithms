import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {

        this.wordNet = wordnet;

    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {

        String champion = "";
        int championSum = 0;
        int[] nounSums = new int[nouns.length];


        for (int i = 0; i < nouns.length; i++) {
            String currentWord = nouns[i];
            int currentWordCumulativeSum = nounSums[i];

//            StdOut.println(currentWord + ": " + currentWordCumulativeSum);

            if (i + 1 < nouns.length) {


                for (int j = i + 1; j < nouns.length; j++) {
                    String comparisonWord = nouns[j];

                    int currentDistance = wordNet.distance(currentWord, comparisonWord);
                    nounSums[j] += currentDistance;
                    currentWordCumulativeSum += currentDistance;

                }
            }

            if (currentWordCumulativeSum > championSum || championSum == 0) {
                championSum = currentWordCumulativeSum;
                champion = currentWord;

            }
        }

        return champion;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
//        String synsets = "C:\\Reikalai\\Programavimas\\Algorithms\\wordnet\\src\\synsets.txt";
//        String hypernyms = "C:\\Reikalai\\Programavimas\\Algorithms\\wordnet\\src\\hypernyms.txt";
//        WordNet WordNetInstance = new WordNet(synsets, hypernyms);
//        Outcast outcast = new Outcast(WordNetInstance);
//        String nounsTxt = "C:\\Reikalai\\Programavimas\\Algorithms\\wordnet\\src\\outcast5.txt";
//
//        In in = new In(nounsTxt);
//        String[] nouns = in.readAllStrings();
//        String outcastWord = outcast.outcast(nouns);
//        StdOut.println(outcastWord);
    }
}

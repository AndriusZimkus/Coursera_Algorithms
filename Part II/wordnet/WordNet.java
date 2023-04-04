import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;

public class WordNet {

    private Digraph WordNetInstance;
    private SAP distanceSAP;
    private ArrayList<String> synsetsList = new ArrayList<>();
    private ST<String, ArrayList<Integer>> wordST = new ST<>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        In in = new In(synsets);
//        ArrayList<String> synsetsList = new ArrayList<>();

        String line = in.readLine();

        while (line != null) {
            String[] lineSplit = line.split(",");

            Integer number = Integer.parseInt(lineSplit[0]);
            String synset = lineSplit[1];

            synsetsList.add(synset);

            String[] synsetArray = synset.split(" ");

            for (String word : synsetArray) {
                if (wordST.contains(word)) {
                    ArrayList<Integer> numbers = wordST.get(word);
                    numbers.add(number);
                    wordST.put(word, numbers);
                } else {
                    ArrayList<Integer> numbers = new ArrayList<>();
                    numbers.add(number);
                    wordST.put(word, numbers);
                }
            }
            line = in.readLine();
        }


        WordNetInstance = new Digraph(synsetsList.size());

        in = new In(hypernyms);
        line = in.readLine();

        while (line != null) {
            String[] lineSplit = line.split(",");

            int v = Integer.parseInt(lineSplit[0]);

            if (lineSplit.length > 1) {
                int i = 1;

                while (i < lineSplit.length) {
                    int w = Integer.parseInt(lineSplit[i]);
                    WordNetInstance.addEdge(v, w);

                    i++;
                }
            }

            line = in.readLine();
        }

        this.distanceSAP = new SAP(WordNetInstance);

        Topological Top = new Topological(WordNetInstance);
        if (!Top.hasOrder()) {
            throw new IllegalArgumentException("not DAG");
        }
        int countOfZeroVertexOutDegree = 0;
        for (int i = 0; i < WordNetInstance.V(); i++) {
            if (WordNetInstance.outdegree(i) == 0) {
                countOfZeroVertexOutDegree++;
                if (countOfZeroVertexOutDegree > 1) {
                    throw new IllegalArgumentException("not rooted DAG");
                }
            }
        }

        DirectedCycle DC = new DirectedCycle(WordNetInstance);
        if (DC.hasCycle()) {
            throw new IllegalArgumentException("cycle");

        }


//        int outdegree
    }


    //     returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordST.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordST.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {

        ArrayList<Integer> nounAIndexes = wordST.get(nounA);
        ArrayList<Integer> nounBIndexes = wordST.get(nounB);
        return distanceSAP.length(nounAIndexes, nounBIndexes);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {

        ArrayList<Integer> nounAIndexes = wordST.get(nounA);
        ArrayList<Integer> nounBIndexes = wordST.get(nounB);

        int ancestorIndex = distanceSAP.ancestor(nounAIndexes, nounBIndexes);
        return synsetsList.get(ancestorIndex);
    }

    // do unit testing of this class
    public static void main(String[] args) {

        String synsets = "C:\\Reikalai\\Programavimas\\Algorithms\\wordnet\\src\\synsets.txt";
        String hypernyms = "C:\\Reikalai\\Programavimas\\Algorithms\\wordnet\\src\\hypernyms.txt";
        WordNet WordNetInstance = new WordNet(synsets, hypernyms);


        String nounA = "grandson";
        String nounB = "colour_wash";
        int distance = WordNetInstance.distance(nounA, nounB);

        StdOut.printf("distance = %d", distance);
        StdOut.println();

//        while (!StdIn.isEmpty()) {
//            String nounA = StdIn.readString();
//            String nounB = StdIn.readString();
//            int distance = WordNetInstance.distance(nounA, nounB);
//
//            StdOut.printf("distance = %d", distance);
//            StdOut.println();
//        }


    }
}

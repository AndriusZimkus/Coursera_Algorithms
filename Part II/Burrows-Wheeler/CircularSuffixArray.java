import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {


    private final String text;
    private final CircularSuffix[] circularArray;
    
    private static void sortParent(CircularSuffix[] csa) {
        CircularSuffix[] aux = new CircularSuffix[csa.length];
        sortMSD(csa, aux, 0, csa.length - 1, 0);
    }

    private static void sortMSD(CircularSuffix[] a, CircularSuffix[] aux, int lo, int hi, int d) {
        int radix = 256;
        if (hi <= lo) return;
        int[] count = new int[radix + 2];
        for (int i = lo; i <= hi; i++)
            count[a[i].charAt(d) + 2]++;
        for (int r = 0; r < radix + 1; r++)
            count[r + 1] += count[r];
        for (int i = lo; i <= hi; i++)
            aux[count[a[i].charAt(d) + 1]++] = a[i];
        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];
        for (int r = 0; r < radix; r++)
            sortMSD(a, aux, lo + count[r], lo + count[r + 1] - 1, d + 1);
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("s is null");
        }

        this.text = s;

        circularArray = new CircularSuffix[length()];
        for (int i = 0; i < length(); i++) {
            circularArray[i] = new CircularSuffix(i);
        }

        sortParent(circularArray);

    }

    private class CircularSuffix {

        private final int startingIndex;

        public CircularSuffix(int startingIndex) {
            this.startingIndex = startingIndex;
        }

        public int charAt(int i) {
            return i >= text.length() ? -1 : text.charAt((startingIndex + i) % text.length());
        }

        public int returnIndex() {
            return startingIndex;
        }

    }

    // length of s
    public int length() {
        return text.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length() - 1) {
            throw new IllegalArgumentException("i is outside range");
        }
        return circularArray[i].returnIndex();
    }

    // unit testing (required)
    public static void main(String[] args) {

//        String filename = args[0];
        String text = args[0];

//        In in = new In(filename);
//
//        String text = in.readAll();
        CircularSuffixArray csa = new CircularSuffixArray(text);
        StdOut.println(csa.length());
        StdOut.println(csa.index(3));

    }
}

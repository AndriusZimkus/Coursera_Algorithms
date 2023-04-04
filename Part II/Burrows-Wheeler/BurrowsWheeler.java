import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {

        String text = "";
        StringBuilder textBuilder = new StringBuilder();

        while (!BinaryStdIn.isEmpty()) {
            textBuilder.append(BinaryStdIn.readString());
        }

        text = textBuilder.toString();

        CircularSuffixArray csa = new CircularSuffixArray(text);

        char[] letters = new char[text.length()];


        for (int i = 0; i < csa.length(); i++) {
            // first
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
            }

            int indexFromI = csa.index(i);
            if (indexFromI == 0) {
                indexFromI = text.length();
            }

            letters[i] = text.charAt(indexFromI - 1);

        }


        for (int i = 0; i < text.length(); i++) {
            BinaryStdOut.write(letters[i]);
        }
        BinaryStdOut.close();

    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        StringBuilder textBuilder = new StringBuilder();

        int first = BinaryStdIn.readInt();
        while (!BinaryStdIn.isEmpty()) {
            textBuilder.append(BinaryStdIn.readChar());
        }
        String text = textBuilder.toString();

        int textLength = text.length();
        int radix = 256;
        int[] currentIndices = new int[radix];
        boolean[] contains = new boolean[radix];
        int[] next = new int[textLength];
        int[] count = new int[radix + 1];

        // Key-indexed counting sort
        for (int i = 0; i < textLength; i++) {
            count[text.charAt(i) + 1]++;
        }
        for (int r = 0; r < radix; r++)
            count[r + 1] += count[r];
        for (int i = 0; i < textLength; i++) {
            count[text.charAt(i)]++;

            if (!contains[text.charAt(i)]) {
                contains[text.charAt(i)] = true;
                currentIndices[text.charAt(i)] = count[text.charAt(i)] - 1;
            }
        }

        for (int i = 0; i < textLength; i++) {
            int currentLetter = text.charAt(i);
            int currentIndex = currentIndices[currentLetter];
            next[currentIndex] = i;
            currentIndices[currentLetter] += 1;
        }

        // recreate original String
        int currentIndex = first;
        int x = 0;
        while (x < textLength) {
            if (x != 0) {
                BinaryStdOut.write(text.charAt(currentIndex));
            }
            currentIndex = next[currentIndex];
            x += 1;
        }
        BinaryStdOut.write(text.charAt(first));
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {

        String sign = args[0];

        if (sign.equals("-")) {
            transform();
        } else if (sign.equals("+")) {
            inverseTransform();
        }

    }
}

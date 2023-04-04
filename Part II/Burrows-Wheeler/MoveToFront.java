import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int RADIX = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        char[] ascii = new char[RADIX];
        int[] locations = new int[RADIX];
        for (int i = 0; i < RADIX; i++) {
            ascii[i] = (char) i;
            locations[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char charRead = BinaryStdIn.readChar();
            int currentIndex = locations[charRead];
            char indexChar = (char) currentIndex;

            if (currentIndex > 0) {
                for (int i = currentIndex; i > 0; i--) {
                    ascii[i] = ascii[i - 1];
                    locations[ascii[i]] = i;
                }
            }
            ascii[0] = charRead;
            locations[charRead] = 0;
            BinaryStdOut.write(indexChar);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

        char[] ascii = new char[RADIX];
        for (int i = 0; i < RADIX; i++) {
            ascii[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char charRead = BinaryStdIn.readChar();

            char currentChar = ascii[charRead];
            BinaryStdOut.write(currentChar);
            if (charRead > 0) {
                for (int i = charRead; i > 0; i--) {
                    ascii[i] = ascii[i - 1];
                }
            }
            ascii[0] = currentChar;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {

        String sign = args[0];

        if (sign.equals("-")) {
            encode();
        } else if (sign.equals("+")) {
            decode();
        }

    }
}

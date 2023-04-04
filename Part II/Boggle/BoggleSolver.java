import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private boolean[][] marked;
    private final TrieSET26 trieDictionary = new TrieSET26();
    private String[][] boardLetters;


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        for (String word : dictionary) {
            trieDictionary.add(word);
        }
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        SET<String> allWords = new SET<>();
        boardLetters = new String[board.rows()][board.cols()];

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                String currentLetter = String.valueOf(board.getLetter(i, j));
                boardLetters[i][j] = currentLetter;
            }
        }
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {

                this.marked = new boolean[board.rows()][board.cols()];

                String currentWord = String.valueOf(board.getLetter(i, j));
                if (currentWord.equals("Q")) {
                    currentWord = "QU";
                }
                allWords = customDFS(i, j, board, currentWord, allWords, null);

            }
        }

        return allWords;
    }

    private SET<String> customDFS(int row, int col, BoggleBoard board, String currentWord, SET<String> allWords, TrieSET26.Node previousNode) {
        marked[row][col] = true;

        TrieSET26.Node node;
        if (previousNode == null) {
            node = trieDictionary.prefixNode(currentWord);
        } else {
            int letterCount = 1;
            if (currentWord.substring((currentWord.length() - 2), currentWord.length() - 1).equals("Q")) {
                letterCount = 2;
            }
            node = trieDictionary.get(previousNode, currentWord, currentWord.length() - letterCount);
        }

        if (node == null) {
            return allWords;
        }
        if (currentWord.length() > 2 && node.isString) {
            allWords.add(currentWord);

        }


        int currentRow;
        int currentColumn;
        String extendedWord;
//        StringBuilder extendedWord = new StringBuilder(currentWord);
        String currentLetter;


        // left up
        currentRow = row - 1;
        currentColumn = col - 1;
        if (row > 0 && col > 0 && !marked[currentRow][currentColumn]) {

//            currentLetter = String.valueOf(board.getLetter(currentRow, currentColumn));
            currentLetter = boardLetters[currentRow][currentColumn];
            if (currentLetter.equals("Q")) {
                currentLetter = "QU";
            }

            extendedWord = currentWord + currentLetter;

            allWords = customDFS(currentRow, currentColumn, board, extendedWord, allWords, node);
            marked[currentRow][currentColumn] = false;
        }


        // up
        currentRow = row - 1;
        currentColumn = col;
        if (row > 0 && !marked[currentRow][currentColumn]) {

//            currentLetter = String.valueOf(board.getLetter(currentRow, currentColumn));
            currentLetter = boardLetters[currentRow][currentColumn];
            if (currentLetter.equals("Q")) {
                currentLetter = "QU";
            }

            extendedWord = currentWord + currentLetter;

            allWords = customDFS(currentRow, currentColumn, board, extendedWord, allWords, node);
            marked[currentRow][currentColumn] = false;
        }

        // right up
        currentRow = row - 1;
        currentColumn = col + 1;
        if (row > 0 && col < board.cols() - 1 && !marked[currentRow][currentColumn]) {
//            currentLetter = String.valueOf(board.getLetter(currentRow, currentColumn));
            currentLetter = boardLetters[currentRow][currentColumn];
            if (currentLetter.equals("Q")) {
                currentLetter = "QU";
            }

            extendedWord = currentWord + currentLetter;

            allWords = customDFS(currentRow, currentColumn, board, extendedWord, allWords, node);
            marked[currentRow][currentColumn] = false;
        }

        // left
        currentRow = row;
        currentColumn = col - 1;
        if (col > 0 && !marked[currentRow][currentColumn]) {
//            currentLetter = String.valueOf(board.getLetter(currentRow, currentColumn));
            currentLetter = boardLetters[currentRow][currentColumn];
            if (currentLetter.equals("Q")) {
                currentLetter = "QU";
            }

            extendedWord = currentWord + currentLetter;

            allWords = customDFS(currentRow, currentColumn, board, extendedWord, allWords, node);
            marked[currentRow][currentColumn] = false;
        }
        // right
        currentRow = row;
        currentColumn = col + 1;
        if (col < board.cols() - 1 && !marked[currentRow][currentColumn]) {
//            currentLetter = String.valueOf(board.getLetter(currentRow, currentColumn));
            currentLetter = boardLetters[currentRow][currentColumn];
            if (currentLetter.equals("Q")) {
                currentLetter = "QU";
            }

            extendedWord = currentWord + currentLetter;

            allWords = customDFS(currentRow, currentColumn, board, extendedWord, allWords, node);
            marked[currentRow][currentColumn] = false;
        }

        // left down
        currentRow = row + 1;
        currentColumn = col - 1;
        if (row < board.rows() - 1 && col > 0 && !marked[currentRow][currentColumn]) {
//            currentLetter = String.valueOf(board.getLetter(currentRow, currentColumn));
            currentLetter = boardLetters[currentRow][currentColumn];
            if (currentLetter.equals("Q")) {
                currentLetter = "QU";
            }

            extendedWord = currentWord + currentLetter;

            allWords = customDFS(currentRow, currentColumn, board, extendedWord, allWords, node);
            marked[currentRow][currentColumn] = false;
        }

        // down
        currentRow = row + 1;
        currentColumn = col;
        if (row < board.rows() - 1 && !marked[currentRow][currentColumn]) {
//            currentLetter = String.valueOf(board.getLetter(currentRow, currentColumn));
            currentLetter = boardLetters[currentRow][currentColumn];
            if (currentLetter.equals("Q")) {
                currentLetter = "QU";
            }

            extendedWord = currentWord + currentLetter;

            allWords = customDFS(currentRow, currentColumn, board, extendedWord, allWords, node);
            marked[currentRow][currentColumn] = false;
        }

        // right down
        currentRow = row + 1;
        currentColumn = col + 1;
        if (row < board.rows() - 1 && col < board.cols() - 1 && !marked[currentRow][currentColumn]) {
//            currentLetter = String.valueOf(board.getLetter(currentRow, currentColumn));
            currentLetter = boardLetters[currentRow][currentColumn];
            if (currentLetter.equals("Q")) {
                currentLetter = "QU";
            }

            extendedWord = currentWord + currentLetter;

            allWords = customDFS(currentRow, currentColumn, board, extendedWord, allWords, node);
            marked[currentRow][currentColumn] = false;
        }

        marked[row][col] = false;
        return allWords;

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trieDictionary.contains(word)) {
            return 0;
        }
        if (word.length() > 7) {
            return 11;
        } else if (word.length() == 7) {
            return 5;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() > 2) {
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {

        boolean normalTesting = false;

        if (normalTesting) {
            In in = new In(args[0]);
            String[] dictionary = in.readAllStrings();
            BoggleSolver solver = new BoggleSolver(dictionary);

            BoggleBoard board = new BoggleBoard(args[1]);
            int score = 0;
            for (String word : solver.getAllValidWords(board)) {
                StdOut.println(word);
                score += solver.scoreOf(word);

            }
            StdOut.println("Score = " + score);
        } else {
            In in = new In("C:\\Reikalai\\Programavimas\\Algorithms\\Boggle\\src\\dictionary-yawl.txt");
            String[] dictionary = in.readAllStrings();

            BoggleSolver solver = new BoggleSolver(dictionary);

            long startingTime = System.currentTimeMillis();

            BoggleBoard board;
            int i = 0;
            while (true) {
                long currentTime = System.currentTimeMillis();

                board = new BoggleBoard();
//                solver.getAllValidWords(board);
//                i++;
//                if (currentTime - startingTime > 1000) {
//                    break;
//                }
                int score = 0;
                for (String word : solver.getAllValidWords(board)) {
//                    StdOut.println(word);
                    score += solver.scoreOf(word);

                }
                if (score == 2500) {
                    break;
                }
            }
            StdOut.println(board);
//            StdOut.println("Solved " + i + " boards per 1 second");
        }


    }
}




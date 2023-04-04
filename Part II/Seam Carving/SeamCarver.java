import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture instancePicture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("argument null");
        }

        this.instancePicture = new Picture(picture);


    }

    // current picture
    public Picture picture() {
        return new Picture(this.instancePicture);
    }

    // width of current picture
    public int width() {
        return this.instancePicture.width();
    }

    // height of current picture
    public int height() {
        return this.instancePicture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= width() || x < 0 || y >= height() || y < 0) {
            throw new IllegalArgumentException("x/y outside range");
        }
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        } else {
            return Math.pow(energyDifferentialX(x, y) + energyDifferentialY(x, y), 0.5);
        }
    }

    private double energyDifferentialX(int x, int y) {
//        if (x == 0 || x == width() - 1) {
//            return 1000;
//        }
        int rgb = instancePicture.getRGB(x - 1, y);
        int firstR = (rgb >> 16) & 0x000000FF;
        int firstG = (rgb >> 8) & 0x000000FF;
        int firstB = (rgb) & 0x000000FF;

        rgb = instancePicture.getRGB(x + 1, y);
        int secondR = (rgb >> 16) & 0x000000FF;
        int secondG = (rgb >> 8) & 0x000000FF;
        int secondB = (rgb) & 0x000000FF;

        return Math.pow(firstR - secondR, 2) + Math.pow(firstG - secondG, 2) + Math.pow(firstB - secondB, 2);
    }

    private double energyDifferentialY(int x, int y) {
//        if (y == 0 || y == height() - 1) {
//            return 1000;
//        }

        int rgb = instancePicture.getRGB(x, y - 1);
        int firstR = (rgb >> 16) & 0x000000FF;
        int firstG = (rgb >> 8) & 0x000000FF;
        int firstB = (rgb) & 0x000000FF;

        rgb = instancePicture.getRGB(x, y + 1);
        int secondR = (rgb >> 16) & 0x000000FF;
        int secondG = (rgb >> 8) & 0x000000FF;
        int secondB = (rgb) & 0x000000FF;

        return Math.pow(firstR - secondR, 2) + Math.pow(firstG - secondG, 2) + Math.pow(firstB - secondB, 2);
    }

    private void transposeImage() {
        Picture transposedPicture = new Picture(height(), width());
        for (int row = 0; row < instancePicture.height(); row++) {
            for (int col = 0; col < instancePicture.width(); col++) {

                transposedPicture.setRGB(row, col, instancePicture.getRGB(col, row));
            }
        }

        instancePicture = transposedPicture;


    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
//        int[] horizontalSeam = new int[width()];

        transposeImage();

        int[] horizontalSeam = findVerticalSeam();

        transposeImage();

        return horizontalSeam;

    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        // Constructing energy matrix
        double[][] energyMatrix = new double[height()][width()];
        for (int row = 0; row < instancePicture.height(); row++) {
            for (int col = 0; col < instancePicture.width(); col++) {
                energyMatrix[row][col] = energy(col, row);
            }
        }

        double[][] distTo = new double[height()][width()];
        for (int col = 0; col < width(); col++) {
            distTo[0][col] = 1000;
        }
        int[][] edgeTo = new int[height()][width()];

        for (int row = 0; row < height(); row++) {
            if (row >= height() - 1) {
                continue;
            }

            for (int col = 0; col < width(); col++) {
                // to the left
                double currentSum;
                double nextSum;
                if (col > 0) {
                    currentSum = distTo[row][col] + energyMatrix[row + 1][col - 1];
                    nextSum = distTo[row + 1][col - 1];

                    if (currentSum <= nextSum || nextSum == 0) {
                        distTo[row + 1][col - 1] = currentSum;
                        edgeTo[row + 1][col - 1] = col;

                    }
                }

                // to the center
                currentSum = distTo[row][col] + energyMatrix[row + 1][col];
                nextSum = distTo[row + 1][col];

                if (currentSum < nextSum || nextSum == 0) {
                    distTo[row + 1][col] = currentSum;
                    edgeTo[row + 1][col] = col;
                }

                // to the right
                if (col < width() - 1) {
                    currentSum = distTo[row][col] + energyMatrix[row + 1][col + 1];
                    nextSum = distTo[row + 1][col + 1];

                    if (currentSum < nextSum || nextSum == 0) {
                        distTo[row + 1][col + 1] = currentSum;
                        edgeTo[row + 1][col + 1] = col;

                    }
                }
            }

        }

        int minCol = 0;
        double minSum = 0;
        for (int col = 0; col < width(); col++) {
            if (distTo[height() - 1][col] < minSum || minSum == 0) {
                minCol = col;
                minSum = distTo[height() - 1][col];
            }
        }

        int[] seamArray = new int[height()];
        int nextCol = edgeTo[height() - 1][minCol];


        for (int row = height() - 1; row >= 0; row--) {
            seamArray[row] = nextCol;
            nextCol = edgeTo[row][nextCol];
        }

        return seamArray;

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("argument null");
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException("Wrong seam width");
        }
        if (height() <= 1) {
            throw new IllegalArgumentException("Width less or equal than 1");
        }

        transposeImage();

        removeVerticalSeam(seam);

        transposeImage();

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("argument null");
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException("Wrong seam height");
        }
        if (width() <= 1) {
            throw new IllegalArgumentException("Width less or equal than 1");
        }

        int previousColToRemove = seam[0];

        Picture newPicture = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            int colToRemove = seam[row];
            if (Math.abs(colToRemove - previousColToRemove) > 1) {
                throw new IllegalArgumentException("Successive entries in seam differ more than 1");
            }
            previousColToRemove = colToRemove;

            int currentColumn = 0;
            for (int col = 0; col < width(); col++) {
                if (col != colToRemove) {
                    newPicture.set(currentColumn, row, instancePicture.get(col, row));
                    currentColumn++;
                }
            }

        }

        instancePicture = newPicture;

    }

    //  unit testing (optional)
    public static void main(String[] args) {

        Picture SamplePicture = new Picture("C:\\Reikalai\\Programavimas\\Algorithms\\Seam Carving\\src\\seam_data\\3x4.png");

        SeamCarver SampleSeamCarver = new SeamCarver(SamplePicture);

//        double Energy = SampleSeamCarver.energy(1, 1);
//        PrintEnergy aa = new PrintEnergy(SampleSeamCarver);

    }

}

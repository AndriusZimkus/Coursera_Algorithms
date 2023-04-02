import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points is empty");
        }


        for (int i = 0; i < points.length; i++) {
            Point currentPoint = points[i];

            if (currentPoint == null) {
                throw new IllegalArgumentException("one of points is null");
            }

            if (points.length == 1) {
                break;
            }

            Point[] otherPoints = new Point[points.length - 1];
            int otherIterator = 0;
            for (int j = 0; j < points.length; j++) {
                Point otherPoint = points[j];

                if (otherPoint == null) {
                    throw new IllegalArgumentException("one of points is null");
                }


                if (j != i) {
                    otherPoints[otherIterator] = otherPoint;
                    otherIterator++;
                }
            }

            Arrays.sort(otherPoints, currentPoint.slopeOrder());


            int j = 1;
            int startIndex = 0;
            int endIndex = 0;
            double segmentSlope = otherPoints[0].slopeTo(currentPoint);
            if (segmentSlope == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("duplicate points");
            }
            double currentSlope;
            boolean addSegment = false;
            while (j < otherPoints.length) {
                currentSlope = otherPoints[j].slopeTo(currentPoint);


                if (segmentSlope == currentSlope) {
                    // segment continues, move end Index
                    endIndex = j;

                } else if ((segmentSlope != currentSlope)) {
                    // new segment
                    if (endIndex - startIndex >= 2) {
                        addSegment = true;
                    } else {
                        startIndex = j;
                        endIndex = j;
                        segmentSlope = currentSlope;
                    }


                }

                if (j + 1 == otherPoints.length && endIndex - startIndex >= 2) {
                    addSegment = true;
                }


                if (addSegment) {


                    // Find min, max of the row of points
                    Point[] pointsInSegment = new Point[endIndex - startIndex + 2];
                    pointsInSegment[0] = currentPoint;

                    for (int k = startIndex; k <= endIndex; k++) {
                        pointsInSegment[k - startIndex + 1] = otherPoints[k];
                    }

                    Point[] minMaxPoints = getMinMaxPoints(pointsInSegment);

                    // Add to segments if currentPoint is minPoint
                    if (currentPoint == minMaxPoints[0]) {
                        LineSegment newSegment = new LineSegment(minMaxPoints[0], minMaxPoints[1]);

                        lineSegments.add(newSegment);
                    }

                    addSegment = false;
                    startIndex = j;
                    endIndex = j;
                    segmentSlope = currentSlope;


                }


                j++;
            }


        }

    }     // finds all line segments containing 4 or more points

    public int numberOfSegments() {
        return lineSegments.size();

    }        // the number of line segments

    public LineSegment[] segments() {

        LineSegment[] segments = new LineSegment[numberOfSegments()];
        int i = 0;
        for (LineSegment segment : lineSegments) {
            segments[i] = segment;
            i++;
        }

        return segments;

    }        // the line segments

    private Point[] getMinMaxPoints(Point[] points) {
        Point[] tupleMinMaxPoints = new Point[2];
        Point minPoint = points[0];
        Point maxPoint = points[0];

        for (int i = 1; i < points.length; i++) {
            Point currentPoint = points[i];
            if (currentPoint.compareTo(minPoint) < 0) {
                minPoint = currentPoint;
            } else if (currentPoint.compareTo(maxPoint) > 0) {
                maxPoint = currentPoint;
            }
        }
        tupleMinMaxPoints[0] = minPoint;
        tupleMinMaxPoints[1] = maxPoint;

        return tupleMinMaxPoints;
    }

    public static void main(String[] args) {
        // read the n points from a file

//        StdOut.println(Double.POSITIVE_INFINITY == Double.POSITIVE_INFINITY);
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();


    }
}

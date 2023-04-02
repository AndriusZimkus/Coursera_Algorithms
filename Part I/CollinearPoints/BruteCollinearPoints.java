import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points is empty");
        }

        int pointCount;

        pointCount = points.length;

        if (points[0] == null || pointCount > 1 && points[1] == null || pointCount > 2 && points[2] == null) {
            throw new IllegalArgumentException("one of points is null");
        }

        if (pointCount > 1 && points[0].slopeTo(points[1]) == Double.NEGATIVE_INFINITY || pointCount > 2 && points[0].slopeTo(points[2]) == Double.NEGATIVE_INFINITY || pointCount > 2 && points[1].slopeTo(points[2]) ==
                Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException("duplicate points");
        }

        Point firstPoint;
        Point secondPoint;
        Point thirdPoint;
        Point fourthPoint;


        for (int i = 0; i < pointCount - 3; i++) {
            firstPoint = points[i];
            if (firstPoint == null) {
                throw new IllegalArgumentException("one of points is null");
            }
            for (int j = i + 1; j < pointCount - 2; j++) {
                secondPoint = points[j];
                if (secondPoint == null) {
                    throw new IllegalArgumentException("one of points is null");
                }
                for (int k = j + 1; k < pointCount - 1; k++) {
                    thirdPoint = points[k];
                    if (thirdPoint == null) {
                        throw new IllegalArgumentException("one of points is null");
                    }
                    for (int x = k + 1; x < pointCount; x++) {

                        fourthPoint = points[x];
                        if (fourthPoint == null) {
                            throw new IllegalArgumentException("one of points is null");
                        }

                        double oneTwoSlope = firstPoint.slopeTo(secondPoint);
                        double oneThreeSlope = firstPoint.slopeTo(thirdPoint);
                        double oneFourSlope = firstPoint.slopeTo(fourthPoint);

                        double twoThreeSlope = secondPoint.slopeTo(thirdPoint);
                        double twoFourSlope = secondPoint.slopeTo(fourthPoint);
                        double threeFourSlope = thirdPoint.slopeTo(fourthPoint);

                        if (oneTwoSlope == Double.NEGATIVE_INFINITY || oneThreeSlope == Double.NEGATIVE_INFINITY || oneFourSlope == Double.NEGATIVE_INFINITY || twoThreeSlope == Double.NEGATIVE_INFINITY || twoFourSlope == Double.NEGATIVE_INFINITY || threeFourSlope == Double.NEGATIVE_INFINITY) {
                            throw new IllegalArgumentException("duplicate points");
                        }

                        if (oneTwoSlope == oneThreeSlope && oneTwoSlope == oneFourSlope) {
//                            Į line segment įtraukti pirmą ir paskutinį taškus - lyginti - segmentas yra tik pirmas ir paskutiniai taškai
                            Point maxPoint = firstPoint;
                            Point minPoint = firstPoint;
                            if (firstPoint.compareTo(secondPoint) < 0) {
                                maxPoint = secondPoint;
                            } else {
                                minPoint = secondPoint;
                            }

                            if (thirdPoint.compareTo(minPoint) < 0) {
                                minPoint = thirdPoint;
                            } else if (thirdPoint.compareTo(maxPoint) > 0) {
                                maxPoint = thirdPoint;
                            }

                            if (fourthPoint.compareTo(minPoint) < 0) {
                                minPoint = fourthPoint;
                            } else if (fourthPoint.compareTo(maxPoint) > 0) {
                                maxPoint = fourthPoint;
                            }

                            LineSegment newSegment = new LineSegment(minPoint, maxPoint);

                            lineSegments.add(newSegment);


                        }


                    }
                }
            }
        }


    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }


    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[numberOfSegments()];
        int i = 0;
        for (LineSegment segment : lineSegments) {
            segments[i] = segment;
            i++;
        }

        return segments;

    }           // the line segments

    public static void main(String[] args) {
        // read the n points from a file
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();


    }
}

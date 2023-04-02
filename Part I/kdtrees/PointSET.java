import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class PointSET {

    private SET<Point2D> setOfPoints;

    public PointSET() {
        // construct an empty set of points
        setOfPoints = new SET<Point2D>();
    }

    public boolean isEmpty() {
        // is the set empty?
        return setOfPoints.isEmpty();
    }

    public int size() {
        // number of points in the set
        return setOfPoints.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        setOfPoints.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        return setOfPoints.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D x : setOfPoints) {
            StdDraw.point(x.x(), x.y());

        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException("rect is null");
        }

        ArrayList<Point2D> pointsInRange = new ArrayList<>();

        for (Point2D x : setOfPoints) {
            double currentX = x.x();
            double currentY = x.y();

            if (currentX <= rect.xmax() && currentX >= rect.xmin() && currentY <= rect.ymax() && currentY >= rect.ymin()) {
                pointsInRange.add(x);
            }

        }

        return pointsInRange;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        if (setOfPoints.isEmpty()) {
            return null;
        }
        Point2D champion = new Point2D(0, 0);
        double championDist = -1;

        for (Point2D x : setOfPoints) {
            double currentDist = p.distanceSquaredTo(x);
            if (currentDist < championDist || championDist == -1) {
                championDist = currentDist;
                champion = x;
            }

        }
        return champion;

    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)

        PointSET setOfPoints = new PointSET();
        Point2D x1 = new Point2D(0.372, 0.497);
        Point2D x2 = new Point2D(0.564, 0.413);
        Point2D x3 = new Point2D(0.7, 0.15);


        setOfPoints.insert(x1);
        setOfPoints.insert(x2);
        setOfPoints.insert(x3);


        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setPenRadius(0.05);
        setOfPoints.draw();

        StdDraw.setPenRadius(0.01);
        Point2D comparisonPoint = new Point2D(0.5, 0.5);
        Point2D nearestPoint = setOfPoints.nearest(comparisonPoint);
        comparisonPoint.drawTo(nearestPoint);

        RectHV testRect = new RectHV(0.1, 0.1, 0.8, 0.45);
        testRect.draw();

        StdDraw.show();

        for (Point2D y : setOfPoints.range(testRect)) {
            StdOut.println(y);
        }
//        0.372 0.497
//        0.564 0.413
//        0.226 0.577
//        0.144 0.179
//        0.083 0.510
//        0.320 0.708
//        0.417 0.362
//        0.862 0.825
//        0.785 0.725
//        0.499 0.208

    }

}

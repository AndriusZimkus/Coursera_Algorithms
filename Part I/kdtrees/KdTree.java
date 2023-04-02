import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    private Node root;
    private int size;

    public KdTree() {
        // construct an empty set of points
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        // is the set empty?
        return this.root == null;
    }

    public int size() {
        // number of points in the set
        return size;
    }

//    private int size(Node node) {
//        if (node == null) return 0;
//        return size(node.lb) + size(node.rt) + 1;
//    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        root = insert(root, p, true, 0, 1, 0, 1);
    }

    private Node insert(Node node, Point2D point, boolean isComparisonX, double xmin, double xmax, double ymin, double ymax) {

        if (node == null) {
            size += 1;
            return new Node(point, new RectHV(xmin, ymin, xmax, ymax), null, null);
        }

        if (point.x() == node.p.x() && point.y() == node.p.y()) {
            node.p = point;
            return node;
        }

        int cmp;

        if (isComparisonX) {
            if (point.x() < node.p.x()) {
                cmp = -1;
                // To the left
                // Min stays the same
                // Max is restricted
                xmax = node.p.x();
            } else {
                // To the right
                // Max stays the same
                // Min is restricted
                xmin = node.p.x();
                cmp = 1;

//                if (point.x() > node.p.x()) {
//                    cmp = 1;
//                } else cmp = 0;
            }

        } else {
            if (point.y() < node.p.y()) {
                ymax = node.p.y();
                cmp = -1;
            } else {
                ymin = node.p.y();
                cmp = 1;

//                if (point.y() > node.p.y()) {
//                    cmp = 1;
//                } else cmp = 0;
            }
        }


        if (cmp == -1) node.lb = insert(node.lb, point, !isComparisonX, xmin, xmax, ymin, ymax);
        else node.rt = insert(node.rt, point, !isComparisonX, xmin, xmax, ymin, ymax);
//        else node.p = point;
        return node;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D point, boolean isComparisonX) {

        if (node == null) return false;
        if (node.p == point) return true;
        if (node.p.x() == point.x() && node.p.y() == point.y()) return true;

        int cmp;
        if (isComparisonX) {
            if (point.x() < node.p.x()) {
                cmp = -1;
            } else cmp = 1;

        } else {
            if (point.y() < node.p.y()) {
                cmp = -1;
            } else cmp = 1;
        }

        if (cmp == -1) return contains(node.lb, point, !isComparisonX);
        else return contains(node.rt, point, !isComparisonX);
    }

    public void draw() {
        // draw all points to standard draw
        draw(root, true);
    }

    private void draw(Node node, boolean isComparisonX) {
        if (node == null) {
            return;
        }

        StdDraw.setPenRadius(0.005);
        if (isComparisonX) {
            StdDraw.setPenColor(StdDraw.RED);
            Point2D minPoint = new Point2D(node.p.x(), node.rect.ymin());
            Point2D maxPoint = new Point2D(node.p.x(), node.rect.ymax());
            minPoint.drawTo(maxPoint);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            Point2D minPoint = new Point2D(node.rect.xmin(), node.p.y());
            Point2D maxPoint = new Point2D(node.rect.xmax(), node.p.y());
            minPoint.drawTo(maxPoint);
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        node.p.draw();

        draw(node.lb, !isComparisonX);
        draw(node.rt, !isComparisonX);

    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException("rect is null");
        }
        ArrayList<Point2D> pointsInRange = new ArrayList<>();
        if (root == null) return null;
        return range(rect, root, pointsInRange, true);
    }

    private Iterable<Point2D> range(RectHV rect, Node node, ArrayList<Point2D> pointsInRange, boolean isComparisonX) {

        if (node.p.x() <= rect.xmax() && node.p.x() >= rect.xmin() && node.p.y() <= rect.ymax() && node.p.y() >= rect.ymin()) {
            pointsInRange.add(node.p);
        }

        // both left/right nodes null
        if (node.lb == null && node.rt == null) {
            return pointsInRange;
        }

        boolean doesLeftIntersect = false;
        boolean doesRightIntersect = false;

        if (node.lb != null) {
            doesLeftIntersect = node.lb.rect.intersects(rect);

        }
        if (node.rt != null) {
            doesRightIntersect = node.rt.rect.intersects(rect);
        }

        // only one of left/right nodes null
        if (node.lb == null) {

            if (doesRightIntersect) {
                pointsInRange = (ArrayList<Point2D>) range(rect, node.rt, pointsInRange, !isComparisonX);
            }
            return pointsInRange;
        }

        if (node.rt == null) {

            if (doesLeftIntersect) {
                pointsInRange = (ArrayList<Point2D>) range(rect, node.lb, pointsInRange, !isComparisonX);
            }
            return pointsInRange;
        }

        // both nodes left/right not null
        if (doesLeftIntersect) {
            pointsInRange = (ArrayList<Point2D>) range(rect, node.lb, pointsInRange, !isComparisonX);
        }
        if (doesRightIntersect) {
            pointsInRange = (ArrayList<Point2D>) range(rect, node.rt, pointsInRange, !isComparisonX);
        }

        return pointsInRange;
    }


    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        if (root == null) return null;
        Point2D champion = root.p;
        champion = nearest(p, root, champion, true);

        return champion;
    }

    private Point2D nearest(Point2D p, Node node, Point2D closestPoint, boolean isComparisonX) {

//        StdOut.println(node.p);
        double currentDistance = p.distanceSquaredTo(node.p);
        if (currentDistance < p.distanceSquaredTo(closestPoint)) {
            closestPoint = node.p;
        }

        double championDistance = p.distanceSquaredTo(closestPoint);

        double distanceToLeftRect = 0;
        double distanceToRightRect = 0;
        double distanceToLeftPoint = 0;
        double distanceToRightPoint = 0;


        // both left/right nodes null
        if (node.lb == null && node.rt == null) {
            return closestPoint;
        }


        if (node.lb != null) {
            distanceToLeftRect = pointDistanceToRect(p, node.lb.rect);

        }

        if (node.rt != null) {
            distanceToRightRect = pointDistanceToRect(p, node.rt.rect);
        }


        // only one of left/right nodes null
        if (node.lb == null) {

            if (distanceToRightRect < championDistance) {
                closestPoint = nearest(p, node.rt, closestPoint, !isComparisonX);
            }
            return closestPoint;
        }

        if (node.rt == null) {

            if (distanceToLeftRect < championDistance) {
                closestPoint = nearest(p, node.lb, closestPoint, !isComparisonX);
            }
            return closestPoint;
        }


        // both left/right nodes not null
        boolean firstLeft;

        if (isComparisonX) {
            firstLeft = p.x() < node.p.x();
        } else {
            firstLeft = p.y() < node.p.y();
        }

        if (firstLeft) {
            if (distanceToLeftRect < championDistance) {
                closestPoint = nearest(p, node.lb, closestPoint, !isComparisonX);
                championDistance = p.distanceSquaredTo(closestPoint);
            }
            if (distanceToRightRect < championDistance) {
                closestPoint = nearest(p, node.rt, closestPoint, !isComparisonX);
            }
        } else {
            if (distanceToRightRect < championDistance) {
                closestPoint = nearest(p, node.rt, closestPoint, !isComparisonX);
                championDistance = p.distanceSquaredTo(closestPoint);
            }
            if (distanceToLeftRect < championDistance) {
                closestPoint = nearest(p, node.lb, closestPoint, !isComparisonX);
            }
        }

        return closestPoint;

    }

    private double pointDistanceToRect(Point2D point, RectHV rect) {
        if (point.x() < rect.xmin()) {
            if (point.y() < rect.ymin()) return hypotSquared(point.x(), rect.xmin(), point.y(), rect.ymin());
            if (point.y() <= rect.ymax()) return hypotSquared(point.x(), rect.xmin(), point.y(), point.y());
            return hypotSquared(point.x(), rect.xmin(), point.y(), rect.ymax());
        } else if (point.x() <= rect.xmax()) {
            if (point.y() < rect.ymin()) return hypotSquared(point.x(), point.x(), point.y(), rect.ymin());
            if (point.y() <= rect.ymax()) return 0;
            return hypotSquared(point.x(), point.x(), point.y(), rect.ymax());
        } else {
            if (point.y() < rect.ymin()) return hypotSquared(point.x(), rect.xmax(), point.y(), rect.ymin());
            if (point.y() <= rect.ymax()) return hypotSquared(point.x(), rect.xmax(), point.y(), point.y());
            return hypotSquared(point.x(), rect.xmax(), point.y(), rect.ymax());
        }
    }

    private double hypotSquared(double x1, double x2, double y1, double y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D point, RectHV rect, Node left, Node right) {
            this.p = point;
            this.rect = rect;
            this.lb = left;
            this.rt = right;
        }


    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)


//        String filename = args[0];
//        In in = new In(filename);
//        KdTree setOfPoints = new KdTree();
//
//        while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            setOfPoints.insert(p);
//        }


        KdTree setOfPoints = new KdTree();
        Point2D x1 = new Point2D(0.7, 0.2);
        Point2D x2 = new Point2D(0.5, 0.4);
        Point2D x3 = new Point2D(0.2, 0.3);
        Point2D x4 = new Point2D(0.4, 0.7);
        Point2D x5 = new Point2D(0.9, 0.6);

        setOfPoints.insert(x1);
        setOfPoints.insert(x2);
        setOfPoints.insert(x3);
        setOfPoints.insert(x4);
        setOfPoints.insert(x5);

        Point2D comparisonPoint = new Point2D(0.35, 0.16);

        setOfPoints.nearest(comparisonPoint);

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        setOfPoints.draw();
        StdDraw.show();


    }

}

package data;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

public class BoardData {
    public final static int unity = 87;
    public final static int width = 696;
    public final static int height = 696;

    // Methods
    // Liste de tous les rectangles dans le plateau
    public static Vector<Rectangle> RECTANGLES() {
        Vector<Rectangle> rectangles = new Vector<>();
        for (int i = 0; i < height; i += unity) {
            for (int j = 0; j < width; j += unity) {
                rectangles.addElement(new Rectangle(j, i, unity, unity));
            }
        }
        return rectangles;

    }

    // Liste de tous les points sur le plateau
    public static Vector<Point> PointList() {
        Vector<Point> points = new Vector<>();
        for (Rectangle r : RECTANGLES()) {
            points.addElement(new Point(r.x, r.y));
        }
        return points;
    }

    // Retourne le rectangle correspondant a la zone cliquée
    public static Rectangle getRectangle(int clickedX, int clickedY) {
        Vector<Rectangle> rectangles = RECTANGLES();
        for (Rectangle rectangle : rectangles) {
            if (rectangle.contains(clickedX, clickedY)) {
                return rectangle;
            }
        }
        return null;
    }

}

//Reine
package pieces;

import java.awt.Point;
import java.util.Vector;

import data.Db;
import drawing.Draw;

public class Queen extends Piece {
    public Queen(int x, int y, int dx, int dy, String color, Draw draw) {
        super(x, y, dx, dy, color, draw);
    }

    @Override
    public boolean isValid(int x, int y, Vector<Piece> list) {
        if (isWithinBoard(x, y)) {
            if (canMoveInDirection(1, 1, x, y, list) ||
                    canMoveInDirection(-1, -1, x, y, list) ||
                    canMoveInDirection(1, -1, x, y, list) ||
                    canMoveInDirection(-1, 1, x, y, list)) {
                return true;
            }

            if (canMoveInDirection(0, 1, x, y, list) ||
                    canMoveInDirection(0, -1, x, y, list)) {
                return true;
            }

            if (canMoveInDirection(1, 0, x, y, list) ||
                    canMoveInDirection(-1, 0, x, y, list)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Vector<Point> pathPoints(Vector<Piece> db, Piece King) {

        Vector<Point> rVector = new Vector<>();
        Vector<Vector<Point>> Paths1 = PathsDiag(db);
        Vector<Vector<Point>> Paths2 = PathsMedia(db);

        for (Vector<Point> vector : Paths1) {
            if (vector.contains(new Point(King.getX(), King.getY()))) {
                rVector = vector;
                break;
            }
        }
        if (!rVector.isEmpty()) {
            return rVector;
        } else {
            for (Vector<Point> vector : Paths2) {
                if (vector.contains(new Point(King.getX(), King.getY()))) {
                    return vector;
                }
            }
        }
        return rVector;

    }
}

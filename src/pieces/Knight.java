//Chevalier
package pieces;

import java.awt.Point;
import java.util.Vector;
import data.*;
import drawing.Draw;

public class Knight extends Piece {
    public Knight(int x, int y, int dx, int dy, String color, Draw draw) {
        super(x, y, dx, dy, color, draw);
    }

    @Override
    public boolean isValid(int x, int y, Vector<Piece> list) {
        int unity = BoardData.unity;
        Point me = new Point(this.getX(), this.getY());
        Point aim = new Point(x, y);
        double sqrt = Math.sqrt(unity * unity + Math.pow(2 * unity, 2));

        if (me.distance(aim) == sqrt) {
            if (isCellEmpty(x, y, list)) {
                return true;
            }
            if (checkTreat(x, y, list)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Vector<Point> pathPoints(Vector<Piece> db, Piece King) {
        Vector<Point> ret = new Vector<>();
        ret.add(new Point(this.getX(), this.getY()));
        return ret;
    }
}
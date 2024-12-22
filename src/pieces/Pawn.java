//Pion
package pieces;

import java.awt.Point;
import java.util.Vector;

import data.Db;
import drawing.Draw;

public class Pawn extends Piece {
    public Pawn(int x, int y, int dx, int dy, String color, Draw draw) {
        super(x, y, dx, dy, color, draw);
    }

   

    @Override
    public boolean isValid(int x, int y, Vector<Piece> list) {
        int step1 = 0, step2 = 0;
        if (canMoveOnE(x, y) && checkTreat(x, y, list)) {
            return true;
        }

        if (this.getColor().equals("black")) {
            step2 = this.getY() + 2 * this.getDy();
            step1 = this.getY() + this.getDy();
        }
        if (this.getColor().equals("white")) {
            step2 = this.getY() - 2 * this.getDy();
            step1 = this.getY() - this.getDy();
        }

        if (isCellEmpty(x, y, list)) {
            if (inOrigin()) {
                if (x == this.getX() && y == step2) {
                    if (isCellEmpty(this.getX(), step1, list)) {
                        return true;
                    }
                }
                if (x == this.getX() && y == step1) {

                    return true;
                }
            } else {
                if (x == this.getX() && y == step1) {
                    return true;
                }
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

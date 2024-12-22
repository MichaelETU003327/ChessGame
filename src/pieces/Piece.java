package pieces;

import java.awt.image.*;
import java.io.File;
import java.util.Vector;

import data.BoardData;
import data.Db;
import drawing.Draw;

import java.awt.*;

import tools.Function;

public abstract class Piece {
    // Fields
    int x, x0;
    int y, y0;
    int dx;
    int dy;
    String color;
    Color caseColor = null;
    Draw draw;

    // Getters and Setters
    public int getX0() {
        return x0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getY0() {
        return y0;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Color getCaseColor() {
        return caseColor;
    }

    public void setCaseColor(Color caseColor) {
        this.caseColor = caseColor;
    }

    // Constructors
    public Piece() {
        super();
    }

    public Piece(int x, int y, int dx, int dy, String color, Draw draw) {
        this.x0 = x;
        this.x = x;
        this.y0 = y;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
        this.draw = draw;
    }

    // Methods
    // Lien de l'image

    public String getImagePath() {
        String link;
        String src = ".." + File.separator + "assets" + File.separator + "img";
        String classname = this.getClass().getSimpleName().toLowerCase();
        String ext = ".png";
        link = src + File.separator + color + "-" + classname + ext;
        return link;
    }

    // Pour dessiner l'image
    public void drawImage(Graphics g) {
        BufferedImage image = Function.getImage(getImagePath());
        if (image != null) {
            g.drawImage(image, this.x + 5, this.y + 5, 75, 75, caseColor, draw);
        }
    }

    // Rectangle associé aux coordonnees de la piece
    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, BoardData.unity, BoardData.unity);
    }

    // Retourne la piece correspondante
    public static synchronized Piece getPiece(int x, int y, Vector<Piece> list) {
        Vector<Piece> copy = new Vector<>(list);
        for (Piece piece : copy) {
            if (piece.getRect().contains(x, y)) {
                return piece;
            }
        }
        return null;
    }

    // Pour voir si la case est vide ou non
    public static boolean isCellEmpty(int x, int y, Vector<Piece> list) {
        boolean test = true;
        Piece piece = getPiece(x, y, list);
        if (piece != null) {
            test = false;
        }

        return test;
    }

    // Verification si les coordonnées sont hors limites
    public static boolean isWithinBoard(int x, int y) {
        return x >= 0 && x < BoardData.width && y >= 0 && y < BoardData.height;
    }

    // Verifie les ennemis
    public boolean checkTreat(int x, int y, Vector<Piece> list) {
        Piece found = getPiece(x, y, list);
        if (found != null) {
            if (!found.getColor().equals(this.getColor())) {
                return true;
            }
        }
        return false;
    }

    // Verifie si les deux pieces sont amis
    public boolean isFriend(Piece test) {

        boolean diff = !this.equals(test);
        return diff && this.color.equals(test.getColor());
    }

    // Verifie la validité du mouvement
    public abstract boolean isValid(int x, int y, Vector<Piece> list);

    //
    public abstract Vector<Point> pathPoints(Vector<Piece> db, Piece King);

    // Specifique aux pieces suivantes: Tour,Reine,Fou
    public boolean canMoveInDirection(int deltaX, int deltaY, int x, int y, Vector<Piece> db) {
        int unity = BoardData.unity;
        int j = unity;
        for (j = unity; isCellEmpty(this.getX() + j * deltaX, this.getY() + j * deltaY,
                db); j += unity) {
            if (x == this.getX() + j * deltaX && y == this.getY() + j * deltaY) {
                return true;
            }
            if (!isWithinBoard(this.getX() + j * deltaX, this.getY() + j * deltaY)) {
                break;
            }
        }
        if (checkTreat(this.getX() + j * deltaX, this.getY() + j * deltaY, db)) {
            if (x == this.getX() + j * deltaX && y == this.getY() + j * deltaY) {
                return true;
            }
        }
        return false;
    }

    // Diagonale
    public Vector<Vector<Point>> PathsDiag(Vector<Piece> db) {
        Vector<Vector<Point>> Paths = new Vector<>(4);
        for (int i = 0; i < 4; i++) {
            Paths.add(new Vector<>());
        }
        for (Point point : BoardData.PointList()) {
            if (this.canMoveInDirection(1, 1, point.x, point.y, db)) {
                Paths.get(0).add(point);
            }
            if (this.canMoveInDirection(-1, -1, point.x, point.y, db)) {
                Paths.get(1).add(point);
            }
            if (this.canMoveInDirection(-1, 1, point.x, point.y, db)) {
                Paths.get(2).add(point);
            }
            if (this.canMoveInDirection(1, -1, point.x, point.y, db)) {
                Paths.get(3).add(point);
            }
        }
        return Paths;
    }

    // Mediane
    public Vector<Vector<Point>> PathsMedia(Vector<Piece> db) {
        Vector<Vector<Point>> Paths = new Vector<>(4);
        for (int i = 0; i < 4; i++) {
            Paths.add(new Vector<>());
        }
        for (Point point : BoardData.PointList()) {
            if (this.canMoveInDirection(0, 1, point.x, point.y, db)) {
                Paths.get(0).add(point);
            }
            if (this.canMoveInDirection(0, -1, point.x, point.y, db)) {
                Paths.get(1).add(point);
            }
            if (this.canMoveInDirection(1, 0, point.x, point.y, db)) {
                Paths.get(2).add(point);
            }
            if (this.canMoveInDirection(-1, 0, point.x, point.y, db)) {
                Paths.get(3).add(point);
            }
        }
        return Paths;
    }

    // Premier mvt
    public boolean inOrigin() {
        return x == x0 && y == y0;
    }

    // Pour se deplacer
    public void move(int aimX, int aimY) {
        this.setX(aimX);
        this.setY(aimY);
    }

    // Pour eliminer un adversaire
    public void kill(Piece treat, Vector<Piece> list) {
        // Ajouter à une liste de suppression temporaire
        Vector<Piece> toRemove = new Vector<>();
        toRemove.add(treat);

        // Supprimer les pièces après le parcours
        list.removeAll(toRemove);
    }

    // Condition des pions pour eliminer
    public boolean canMoveOnE(int x, int y) {
        int[] diag = new int[3];
        diag[0] = this.getX() + this.getDx();
        diag[1] = this.getX() - this.getDx();
        if (this.getColor().equals("black")) {

            diag[2] = this.getY() + this.getDy();

        }
        if (this.getColor().equals("white")) {
            diag[2] = this.getY() - this.getDy();
        }

        if (y == diag[2]) {
            if (x == diag[0]) {
                return true;
            } else if (x == diag[1]) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}
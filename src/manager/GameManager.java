package manager;

import java.util.Vector;

import data.BoardData;
import data.Db;
import drawing.Draw;
import listening.Interpreter;

import java.awt.*;

import pieces.King;
import pieces.Pawn;
import pieces.Piece;

public class GameManager {
    // Fields
    private Draw draw;
    private Vector<Piece> pieces;
    private int turn;
    private Interpreter interpreter;

    // Getters and Setters
    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    public synchronized Vector<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(Vector<Piece> pieces) {
        this.pieces = pieces;
    }

    public String getTurn() {
        return TurnManager.get[turn];
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    // Constructors
    public GameManager(Draw draw) {
        this.draw = draw;
        this.pieces = new Db(draw).pieceList();
        this.interpreter = new Interpreter(this);
    }

    // Gestion des tours
    public void switchTurn() {
        turn = (turn + 1) % 2;
    }

    // Liste des mvts possibles pour une piece si le roi est en echec
    public Vector<Point> getFriendMoveList(Piece p) {
        Vector<Point> moves = new Vector<>();
        for (Rectangle cell : BoardData.RECTANGLES()) {
            if (p.isValid(cell.x, cell.y, pieces)) {
                moves.addElement(new Point(cell.x, cell.y));
            }
        }
        return moves;
    }

    // Liste des mvts possibles pour une piece si le roi n'est pas en echec
    public Vector<Point> getMoveList(Piece p) {
        Vector<Point> moves = new Vector<>();
        for (Rectangle cell : BoardData.RECTANGLES()) {
            if (p instanceof Pawn) {
                if (p.canMoveOnE(cell.x, cell.y)) {
                    moves.addElement(new Point(cell.x, cell.y));
                }
            } else {
                if (p.isValid(cell.x, cell.y, pieces)) {
                    moves.addElement(new Point(cell.x, cell.y));
                }
            }

        }
        return moves;
    }

    // Pour avoir le roi du tour actuel
    public Piece getKing() {
        for (Piece piece : pieces) {
            if (piece instanceof King) {
                if (piece.getColor().equals(getTurn())) {
                    return piece;
                }
            }
        }
        return null;
    }

    // Pour voir si le roi actuel est en echec
    public boolean echec() {
        Piece King = getKing();
        Vector<Piece> copy=new Vector<>(pieces);
        if (King != null) {
            for (Piece treat : copy) {
                if (!King.isFriend(treat)) {
                    Vector<Point> moves = getMoveList(treat);
                    if (moves.contains(new Point(King.getX(), King.getY()))) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    // Liste des pieces pouvant attaquer le roi
    public Vector<Piece> getTreatList() {
        Vector<Piece> treats = new Vector<>();
        Vector<Piece> copy=new Vector<>(pieces);
        Piece King = getKing();
        if (King != null) {
            for (Piece treat : copy) {
                if (!King.isFriend(treat)) {
                    Vector<Point> moves = getMoveList(treat);
                    if (moves.contains(new Point(King.getX(), King.getY()))) {
                        treats.addElement(treat);
                    }
                }

            }
        }
        return treats;
    }

    // Liste des amis pouvant couvrir le roi actuel
    public Vector<Piece> getFriendList() {
        Vector<Piece> friends = new Vector<>();
        Piece King = getKing();
        Vector<Piece> treats = getTreatList();
        Vector<Piece> copy=new Vector<>(pieces);
        if (King != null) {
            for (Piece friend : copy) {
                if (King.isFriend(friend)) {
                    Vector<Point> moves = getFriendMoveList(friend);
                    for (Point move : moves) {
                        for (Piece treat : treats) {
                            Vector<Point> treatPaths = treat.pathPoints(pieces, King);
                            if (treatPaths.contains(move)) {
                                if (!friends.contains(friend)) {
                                    friends.addElement(friend);
                                }
                            }
                        }
                    }
                }
            }
        }
        return friends;
    }

    // Liste de mouvement suggeré pour chaque ami pour sauver le roi
    public Vector<Point> getFriendMove(Piece friend) {
        Piece King = getKing();
        Vector<Point> ans = new Vector<>();
        Vector<Piece> treats = getTreatList();
        Vector<Point> moves = getFriendMoveList(friend);
        for (Point move : moves) {
            for (Piece treat : treats) {
                Vector<Point> treatPaths = treat.pathPoints(pieces, King);
                if (treatPaths.contains(move)) {
                    if (!ans.contains(move)) {
                        ans.addElement(move);
                    }
                }
            }
        }

        return ans;
    }

    // Verifie si sur l'endroit voulu , le roi est en echec ou non
    public boolean isKingInCheck(Piece King, int x, int y) {
        Vector<Piece> db = (Vector<Piece>) pieces.clone();
        db.remove(King);
        Piece p = Piece.getPiece(x, y, db);
        if (p != null) {
            if (King.checkTreat(x, y, db)) {
                db.remove(p);
            }
        }
        for (Piece piece : db) {
            if (!King.isFriend(piece)) {

                if (getMoveList(piece).contains(new Point(x, y)))
                    return true;
            }

        }
        return false;
    }

    // Verifie si le roi actuel peut encore bouger
    public boolean canMoveKing(Piece King) {
        for (Point point : getMoveList(King)) {
            if (!isKingInCheck(King, point.x, point.y)) {
                return true;
            }
        }
        return false;
    }

    // Nouvelle liste de point valide pour le roi
    public Vector<Point> getKingMoveList(Piece King) {
        Vector<Point> points = new Vector<>();
        for (Point point : getMoveList(King)) {
            if (!isKingInCheck(King, point.x, point.y)) {
                if (!points.contains(point)) {
                    points.addElement(point);
                }
            }
        }
        return points;
    }

    public Vector<Point> getFilteredMoveList(Piece piece) {
        Vector<Point> ret = new Vector<>();
        if (piece instanceof King) {
            ret = getKingMoveList(piece);
        } else {
            if (echec()) {
                ret = getFriendMove(piece);
            } else {
                ret = getFriendMoveList(piece);
            }
        }
        return ret;
    }

    public Vector<Piece> getMovables() {
        Piece king = getKing();
        Vector<Piece> movable = new Vector<>();
        if (echec()) {
            if (canMoveKing(king)) {
                movable.addElement(king);
            }
            movable.addAll(getFriendList());
        }
        return movable;
    }

    public boolean isGameOver() {
        Piece king = getKing();
        if (echec()) {
            if (!canMoveKing(king)) {
                if (getFriendList().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

}

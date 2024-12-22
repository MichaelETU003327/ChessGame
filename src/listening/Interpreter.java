package listening;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import data.BoardData;
import manager.*;
import pieces.Piece;

public class Interpreter {
    // Fields
    GameManager manager;
    Piece clicked = null;

    // Constructors
    public Interpreter(GameManager manager) {
        this.manager = manager;

    }

    public boolean lan() {
        if (manager.getDraw().getWindow().getGameMode() != null) {

            return manager.getDraw().getWindow().getGameMode().equals("LAN");
        }
        return false;
    }

    // Getters and Setters
    public GameManager getManager() {
        return manager;
    }

    public void setManager(GameManager manager) {
        this.manager = manager;
    }

    public Piece getClicked() {
        return clicked;
    }

    public void setClicked(Piece clicked) {
        this.clicked = clicked;
    }

    // Methods
    // Defaire un clic
    public void undo() {
        if (clicked != null) {
            clicked.setCaseColor(null);
        }
        clicked = null;
        resetColors();
    }

    // Rendre une case active
    public void setActive() {
        if (clicked != null) {
            clicked.setCaseColor(Color.BLUE);
        }
    }

    // Retablir les couleurs de chaque piece normales
    public void resetColors() {
        for (Piece piece : manager.getPieces()) {
            piece.setCaseColor(null);
        }
    }

    // Premier clic
    private void handlePieceSelection(Piece aimPiece) {
        // Vector<Piece> movable = manager.getMovables();
        if (!manager.isGameOver()) {
            if (clicked == null) {
                clicked = aimPiece;
                boolean isset = (clicked != null);
                if (isset) {

                    boolean tour = clicked.getColor().equals(manager.getTurn());
                    if (lan()) {
                        tour = tour && clicked.getColor().equals(manager.getDraw().getWindow().getColor());
                    }
                    if (tour) {

                        setActive();
                    } else {
                        undo();
                    }
                }
            }
        }

    }

    // Deuxieme clic
    private boolean handleMove(Rectangle aim, Piece aimPiece) {
        if (clicked != null && aim != null) {
            if (!clicked.equals(aimPiece)) {
                if (manager.getFilteredMoveList(clicked).contains(new Point(aim.x, aim.y))) {
                    if (lan()) {
                        manager.getDraw().getWindow().getNetManager().setMove(new Point(clicked.getX(), clicked.getY()),
                                new Point(aim.x, aim.y));
                    }
                    // manager.getDraw().setActiveCase(new Point(clicked.getX(), clicked.getY()));
                    clicked.kill(aimPiece, manager.getPieces());
                    clicked.move(aim.x, aim.y);
                    undo();
                    manager.switchTurn();
                    return true;
                } else if (aimPiece != null && clicked.isFriend(aimPiece)) {
                    undo();
                    clicked = aimPiece;
                    setActive();
                } else {
                    undo();
                }
            }
        }
        return false;
    }

    // Gerer les
    // deux clics

    public void execute(int x, int y) {
        Piece aimPiece = Piece.getPiece(x, y, manager.getPieces());
        Rectangle aim = BoardData.getRectangle(x, y);
        handlePieceSelection(aimPiece);
        handleMove(aim, aimPiece);
        manager.getDraw().repaint();

    }

    public void execute1(int x, int y) {
        Piece aimPiece = Piece.getPiece(x, y, manager.getPieces());
        // Vector<Piece> movable = manager.getMovables();
        if (!manager.isGameOver()) {
            if (clicked == null) {
                clicked = aimPiece;
                boolean isset = (clicked != null);
                if (isset) {
                    if (clicked.getColor().equals(manager.getTurn())) {
                        setActive();
                    } else {
                        undo();
                    }
                }
            }
        }
    }

    public void execute2(int x, int y) {
        Piece aimPiece = Piece.getPiece(x, y, manager.getPieces());
        Rectangle aim = BoardData.getRectangle(x, y);
        if (clicked != null && aim != null) {
            if (!clicked.equals(aimPiece)) {
                if (manager.getFilteredMoveList(clicked).contains(new Point(aim.x, aim.y))) {
                    clicked.move(aim.x, aim.y);
                    clicked.kill(aimPiece, manager.getPieces());
                    manager.switchTurn();
                    undo();
                } else if (aimPiece != null && clicked.isFriend(aimPiece)) {
                    undo();
                    clicked = aimPiece;
                    setActive();
                } else {
                    undo();
                }
            }
        }
    }
}

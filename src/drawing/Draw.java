package drawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JPanel;

import board.Board;
import data.BoardData;
import data.Db;
import listening.Lsnr;
import manager.GameManager;
import manager.TurnManager;
import pieces.Piece;
import windowing.Window;

public class Draw extends JPanel {
    // Fields
    private Window window;
    private Board board;
    private GameManager manager;
    private Point activeCase = null;

    // Constructors
    public Draw(Window window) {
        this.window = window;
        this.board = new Board(this);
        this.manager = new GameManager(this);
        
    }

    // Getters and Setters
    public Point getActiveCase() {
        return activeCase;
    }

    public void setActiveCase(Point activeCase) {
        this.activeCase = activeCase;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public GameManager getManager() {
        return manager;
    }

    public void setManager(GameManager manager) {
        this.manager = manager;
    }

    // Methods
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Piece clicked = manager.getInterpreter().getClicked();
        Graphics2D g2d = (Graphics2D) g;
        Piece currKing = manager.getKing();
        if (manager.echec()) {
            currKing.setCaseColor(Color.RED);
            for (Piece friend : manager.getFriendList()) {
                friend.setCaseColor(Color.GREEN);
            }
        }
        if (manager.isGameOver()) {
            System.out.println("echec et mat");
        }
        drawBoard(g);
        drawPieces(g);
        displayTurn(g);
        highlightValidMoves(g2d);
        if (activeCase != null && clicked == null) {
            paintH(activeCase.x, activeCase.y, Color.BLUE, g2d);

        }

    }

    // Pour dessiner le plateau
    private void drawBoard(Graphics g) {
        board.deploy(g);
    }

    // Pour dessiner les pieces
    private void drawPieces(Graphics g) {
        Vector<Piece> copy = new Vector<>(manager.getPieces());
        for (Piece piece : copy) {
            piece.drawImage(g);
        }
    }

    // Pour afficher le tour
    private void displayTurn(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString(manager.getTurn(), BoardData.width + BoardData.unity, 60);
    }

    // Pour marquer les cases valides
    private void highlightValidMoves(Graphics2D g2d) {
        Piece clicked = manager.getInterpreter().getClicked();
        Vector<Piece> pieces = manager.getPieces();
        if (clicked != null) {
            for (Point point : manager.getFilteredMoveList(clicked)) {
                if (!clicked.checkTreat(point.x, point.y, pieces)) {
                    paintH(point.x, point.y, Color.YELLOW, g2d);
                } else {
                    Piece treat = Piece.getPiece(point.x, point.y, pieces);
                    treat.setCaseColor(Color.RED);
                    repaint();
                }
            }

        }
    }

    public Rectangle highlight(int x, int y) {
        return new Rectangle(x, y, 75, 75);
    }

    public void paintH(int x, int y, Color color, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fill(highlight(x + 5, y + 5));
    }
}

package listening;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import data.BoardData;
import manager.GameManager;
import manager.TurnManager;
import pieces.Piece;
import windowing.Window;

public class Lsnr implements MouseListener {
    // Fields
    private Window window;
    private GameManager manager;

    // Constructors
    public Lsnr(Window window) {
        this.window = window;
        if (window.getDraw()!=null) {
            this.manager = window.getDraw().getManager();
            
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        manager.getInterpreter().execute(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

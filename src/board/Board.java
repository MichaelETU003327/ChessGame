package board;

import java.awt.*;
import java.io.Serializable;

import data.BoardData;
import drawing.*;

public class Board implements Serializable {

    // Fields
    public final static int unity = BoardData.unity;
    public final static int width = BoardData.width;
    public final static int height = BoardData.height;
    private Draw draw;

    // Getters

    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    // Constructors
    public Board(Draw draw) {
        this.draw = draw;
    }

    // Methods
    /* Deploye le plateau du jeu */
    public void deploy(Graphics g) {
        int i = 0, j = 0;
        for (i = 0; i < height; i += unity) {
            for (j = 0; j < width; j += unity) {
                int mod = j / unity;
                int mod2 = i / unity;
                if ((mod2 % 2 != 0 && mod % 2 == 0) || (mod2 % 2 == 0 && mod % 2 != 0)) {
                    g.setColor(Color.GRAY);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j, i, unity, unity);
            }
        }
    }
}

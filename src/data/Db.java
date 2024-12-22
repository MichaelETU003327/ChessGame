package data;

import java.util.Vector;

import drawing.Draw;
import pieces.*;

public class Db {
    // Fields
    Draw draw;

    // Constructors
    public Db(Draw draw) {
        this.draw = draw;
    }

    // Methods

    // Add a piece
    private void placePiece(int x, int y, String type, String color, Vector<Piece> pieces) {
        switch (type) {
            case "King":
                pieces.add(new King(x, y, BoardData.unity, BoardData.unity, color, draw));
                break;
            case "Queen":
                pieces.add(new Queen(x, y, BoardData.unity, BoardData.unity, color, draw));
                break;
            case "Rook":
                pieces.add(new Rook(x, y, BoardData.unity, BoardData.unity, color, draw));
                break;
            case "Knight":
                pieces.add(new Knight(x, y, BoardData.unity, BoardData.unity, color, draw));
                break;
            case "Bishop":
                pieces.add(new Bishop(x, y, BoardData.unity, BoardData.unity, color, draw));
                break;
            case "Pawn":
                pieces.add(new Pawn(x, y, BoardData.unity, BoardData.unity, color, draw));
                break;
            default:
                break;
        }
    }

    private static final String[][] POSITION_INITIALE = {
            { "Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook" },
            { "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn" },
            { null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null },
            { "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn" },
            { "Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook" }
    };

    // Piece list
    public Vector<Piece> pieceList() {
        Vector<Piece> pieces = new Vector<>();
        for (int i = 0; i < POSITION_INITIALE.length; i++) {
            for (int j = 0; j < POSITION_INITIALE[i].length; j++) {
                String pieceType = POSITION_INITIALE[i][j];
                if (pieceType != null) {
                    String color = (i < 2) ? "black" : "white";
                    placePiece(j * BoardData.unity, i * BoardData.unity, pieceType, color, pieces);
                }
            }
        }

        return pieces;
    }

    

}

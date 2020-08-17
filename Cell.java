import java.io.Serializable;

/**
 * @author jialiang Song 2410536s
 *
 * This class represents a grid (cell) on the chessboard.
 * Each grid has its own row and column numbers
 * and you can set or remove pieces on the grid.
 */
public class Cell implements Serializable {
    private int row;
    private int col;

    private boolean canMoveTo;
    private Piece piece;
    private boolean selected = false;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;

        if (row % 2 == 0) {
            canMoveTo = col % 2 == 0;
        } else {
            canMoveTo = col % 2 != 0;
        }

        if (canMoveTo) {
            if (row <= 3) {
                piece = new Piece(PieceType.Red);
            }

            if (row >= 6) {
                piece = new Piece(PieceType.Black);
            }
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean isCanMoveTo() {
        return canMoveTo;
    }

    public void clearPiece() {
        piece = null;
    }

    public boolean hasPiece() {
        return piece != null;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;

        if (getRow() == 0 && piece.getType() == PieceType.Black) {
            piece.setKing();
        }

        if (getRow() == 9 && piece.getType() == PieceType.Red) {
            piece.setKing();
        }
    }

}

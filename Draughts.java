import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/**
 * @author jialiang Song 2410536s
 *
 * The class Draughts represent the entire chess board.
 * It contains a 10*10 Cell matrix.
 * It will also judge whether a vertain piece moves legally.
 */
public class Draughts implements Serializable {
    private final int gridSize = 50;

    private Cell[][] board;

    public Draughts() {
        reset();
    }

    public void reset() {
        board = new Cell[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
    }

    public void clearSelect() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j].setSelected(false);
            }
        }
    }

    public Cell getCell(int x, int y, int clickX, int clickY) {
        int row = (clickY - y) / gridSize;
        int col = (clickX - x) / gridSize;
        row = 10 - row - 1;

        if (row >= 0 && col >= 0 && row < 10 && col < 10) {
            return board[row][col];
        }

        return null;
    }

    public int count(PieceType type) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Piece piece = board[i][j].getPiece();
                if (piece != null && piece.getType() == type) {
                    sum++;
                }
            }
        }

        return sum;
    }

    public void draw(int x, int y, Graphics graphics) {
        y += gridSize * 10;

        // draw grids
        for (int i = 0; i < 10; i++) {
            graphics.drawLine(x, y - i * gridSize, x + gridSize * 10, y - i * gridSize);
            graphics.drawLine(x + i * gridSize, y, x + gridSize * i, y - 10 * gridSize);
        }

        // draw background
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int cx = x + j * gridSize;
                int cy = y - i * gridSize - gridSize;

                if (board[i][j].isCanMoveTo()) {
                    graphics.setColor(new Color(250, 235, 215));
                } else {
                    graphics.setColor(new Color(255, 248, 220));
                }
                graphics.fillRect(cx, cy, gridSize, gridSize);

                if (board[i][j].isSelected()) {
                    graphics.setColor(new Color(10, 10, 10));
                    graphics.drawRect(cx, cy, gridSize - 1, gridSize);
                }

                Piece piece = board[i][j].getPiece();
                if (piece != null) {
                    if (piece.getType() == PieceType.Red) {
                        graphics.setColor(new Color(255, 0, 0));
                    } else {
                        graphics.setColor(new Color(0, 0, 0));
                    }
                    graphics.fillOval(cx + 10, cy + 10, gridSize - 20, gridSize - 20);

                    if (piece.isKing()) {
                        graphics.setColor(Color.green);
                        graphics.fillOval(cx + 20, cy + 20, gridSize - 40, gridSize - 40);
                    }
                }
            }
        }

    }

    public boolean move(Cell start, Cell end) {
        if (!start.hasPiece() || end.hasPiece() || !end.isCanMoveTo()) {
            return false;
        }

        Piece piece = start.getPiece();
        int dr = end.getRow() - start.getRow();
        int dc = end.getCol() - start.getCol();

        if (Math.abs(dr) == Math.abs(dc)) {
            // on same line
            int step = Math.abs(dr);
            if (step == 1) {
                if (piece.getType() == PieceType.Red && (dr > 0 || piece.isKing())) {
                    start.clearPiece();
                    end.setPiece(piece);

                    return true;
                } else if (piece.getType() == PieceType.Black && (dr < 0 || piece.isKing())) {
                    start.clearPiece();
                    end.setPiece(piece);
                    return true;
                }
            } else if (step == 2) {
                int midr = start.getRow() + (dr / Math.abs(dr));
                int midc = start.getCol() + (dc / Math.abs(dc));

                Piece midPiece = board[midr][midc].getPiece();
                if (midPiece != null && midPiece.getType() != piece.getType()) {
                    start.clearPiece();
                    end.setPiece(piece);
                    board[midr][midc].clearPiece();

                    return true;
                }
            }
        }

        return false;
    }
}

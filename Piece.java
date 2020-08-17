import java.io.Serializable;

/**
 * @author jialiang Song 2410536s
 *
 * This class represent a piece, each piece has its own type (PieceType)
 * and state (whether it is King...
 * or Queen maybe? I/m not sure..There are different rules in difeerent instructions)
 */

public class Piece  implements Serializable{
    private boolean king;
    private PieceType type;

    public Piece(PieceType type) {
        this.type = type;
        king = false;
    }

    public boolean isKing() {
        return king;
    }

    public void setKing() {
        this.king = true;
    }

    public PieceType getType() {
        return type;
    }
}

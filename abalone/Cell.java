package abalone;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Cell extends JPanel implements Cloneable{

    private int x;
    private int y;
    private Board board;
    private Marble marble;
    
    public Cell() {
    }
    
    /**
     * Non-Default constructor for class Cell.
     * 
     * @param x
     * @param y
     * @param board
     */
    public Cell(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    
    public Cell(int x, int y, Marble marble, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
        if(marble != null) {
            this.marble = marble;
            setMarble(marble);
        }
        
        
                
        
    }

    /**
     * Returns the row the cell is located in.
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns the column the Cell is located in.
     */
    public int getY() {
        return y;
    }

    public Board getBoard() {
        return board;
    }

    public Marble getMarble() {
        return marble;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setMarble(Marble marble) {
        this.marble = marble;
    }
    
    public boolean containsMarble() {
        return !(marble == null);
    }
}

package abalone;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import abalone.gameEnum.MarbleType;

@SuppressWarnings("serial")
public class Marble extends JPanel implements Comparable {
    
    public static final Color WHITE       = new Color(255, 255, 255);
    public static final Color DARK_WHITE  = new Color(168, 152, 146);
    public static final Color BLACK      = new Color(0, 0, 0 );
    public static final Color LIGHT_BLACK = new Color(51, 46, 43);

    private Board board;
    private Cell cell;
    private MarbleType type;
    private Color color;

    public Marble() {
    }

    public Marble(Cell cell, MarbleType type) {
        this.cell = cell;
        this.type = type;
        init();
    }

    public void init() {
        if (type == MarbleType.WHITE) {
            color = WHITE;
        } else if (type == MarbleType.BLACK) {
            color = BLACK;
        }
    }
    
    public Board getBoard() {
        return board;
    }

    public MarbleType getType() {
        return type;
    }

    public Cell getCell() {
        return cell;
    }
    
    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Color getColor() {
        return color;
    }
    
    public void setNormalColor() {
        if(getType() == MarbleType.WHITE) color = WHITE;
        if(getType() == MarbleType.BLACK) color = BLACK;
    }
    
    public void setDarkColor() {
        if(getType() == MarbleType.WHITE) color = DARK_WHITE;
        if(getType() == MarbleType.BLACK) color = LIGHT_BLACK;
    }

    public static void clearCells(ArrayList<Marble> marbles) {
        for(Marble marble : marbles) {
            marble.setCell(null);
        }
    }

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
    
	public String toString() {
	    return "x: " + getX() + ", y: " + getY();
	}

    // public void move1(ArrayList<Marble> marbles, Direction direction) {
    // Marble m1 = marbles.get(0);
    // int x = cell.getX() + board.getMoveSets().get(direction).x;
    // int y = cell.getY() + board.getMoveSets().get(direction).y;
    // board.getCellAt(x, y).setMarble(m1);
    // }
    //
    // public void move2(ArrayList<Marble> marbles, Direction direction) {
    // int xDirection = board.getMoveSets().get(direction).x;
    // int yDirection = board.getMoveSets().get(direction).y;
    // for(Marble marble : marbles) {
    // int x = marble.getCell().getX() + xDirection;
    // int y = marble.getCell().getY() + yDirection;
    // board.getCellAt(x, y).setMarble(marble);
    // }
    // }
    //
    // public void move3(ArrayList<Marble> marbles, Direction direction) {
    // int xDirection = board.getMoveSets().get(direction).x;
    // int yDirection = board.getMoveSets().get(direction).y;
    // for(Marble marble : marbles) {
    // int x = marble.getCell().getX() + xDirection;
    // int y = marble.getCell().getY() + yDirection;
    // board.getCellAt(x, y).setMarble(marble);
    // }
    // }

}

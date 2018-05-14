package abalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import abalone.gameEnum.Direction;
import abalone.gameEnum.MarbleType;
import boardFrame.GameFrame;

public class Agent {

    public static final int DEPTH = 4;

    private GameFrame frame;
    private MarbleType agentType;

    public Agent(GameFrame frame, MarbleType type) {
        this.frame = frame;
        this.agentType = type;
    }


    public void agentMove() {

        Multimap<ArrayList<Marble>, Direction> allMoves = ArrayListMultimap.create();
        Random r = new Random();


        Search searchObject = new Search(frame, agentType);

        Cell[][] copied = new Cell[Board.NUMBER_OF_ROWS][Board.NUMBER_OF_COLUMNS];
        cellCopy(frame.getBoard().returnAllCell(), copied, frame.getBoard());
        Board tempBoard = new Board(copied);
        Pair<Integer, Pair<ArrayList<Marble>, Direction>> maxPair = null;
        maxPair = searchObject.maxValue(tempBoard, Integer.MIN_VALUE, Integer.MAX_VALUE, DEPTH);

        
        System.out.println("chosen" + marblesToString(maxPair.getValue().getKey()) + maxPair.getValue().getValue().toString());
        move(maxPair.getValue().getKey(), maxPair.getValue().getValue());
        frame.getBoard().clearMarbles();
        frame.getBoard().setNumOfMove();
        frame.updateTotalTime();

        Board.PLAYER_TURN = Board.OPPONENT_MAP.get(Board.PLAYER_TURN);
        GameFrame.turnOver = true;

    }

    private void cellCopy(Cell[][] ori, Cell[][] copied, Board board) {

        for (int i = 0; i < ori.length; ++i) {
            for (int j = 0; j < ori[0].length; ++j) {
                if (ori[i][j] != null) {
                    if (copied[i][j] != null)
                        copied[i][j].setMarble(null);
                    else
                        copied[i][j] = new Cell(i, j, null, board);
                    if (ori[i][j].getMarble() != null) {
                        copied[i][j].setMarble(ori[i][j].getMarble());
                        copied[i][j].getMarble().setCell(copied[i][j]);
                    }
                }
            }
        }
    }

    public void move(ArrayList<Marble> marbles, Direction direction) {
        int xDirection = frame.getBoard().getMoveSets().get(direction).x;
        int yDirection = frame.getBoard().getMoveSets().get(direction).y;
        
        if (marbles.size() > 1) {
			Marble first = marbles.get(0);
			Marble second = marbles.get(1);
			int x = Board.MOVE_SETS.get(direction).x + first.getCell().getX();
			int y = Board.MOVE_SETS.get(direction).y + first.getCell().getY();
			if (x == second.getCell().getX() && y == second.getCell().getY()) {
				Collections.reverse(marbles);
			}
		}
        
        for (Marble marble : marbles) {
            int x = marble.getCell().getX();
            int y = marble.getCell().getY();
            int newX = marble.getCell().getX() + xDirection;
            int newY = marble.getCell().getY() + yDirection;
            
            Marble m = frame.getBoard().getCellAt(x,y).getMarble();
            frame.getBoard().getCellAt(x,y).setMarble(null);
            frame.getBoard().getCellAt(newX, newY).setMarble(m);
            m.setCell(frame.getBoard().getCellAt(newX, newY));
            
             
        }
    }

    public MarbleType getType() {
        return agentType;
    }
    
    public static String marblesToString(ArrayList<Marble> marbles) {
        String s = "";
        for (int i = 0; i < marbles.size(); i++) {
            if (i == marbles.size() - 1) {
                s += marbles.get(i).getCell().getX() + "," + marbles.get(i).getCell().getY();
            } else {
                s += marbles.get(i).getCell().getX() + "," + marbles.get(i).getCell().getY() + "/";
            }
        }
        return s;
    }
    
    
}

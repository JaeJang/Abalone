package abalone;

import java.awt.Point;
import java.util.ArrayList;

import abalone.gameEnum.Direction;
import abalone.gameEnum.MarbleType;
import boardFrame.GameFrame;

public class Log {
    public static ArrayList<String> blackHistory = new ArrayList<String>();
    public static ArrayList<String> whiteHistory = new ArrayList<String>();
    private String direction;
    private Marble marble;
    private Point point;
    private Board board;
    private String type;
    private String text;
    private String player;
    private int turn;
    private boolean enabled;

    public Log(Board board) {
        setBoard(board);
        setPlayer(board.PLAYER_TURN.toString());
        setTurn(board.getNumOfMove(board.getColourMap().get(board.PLAYER_TURN))
                + 1);
        setMarbleType(board.getColourMap().get(board.PLAYER_TURN));
        /*text = "Player = " + player + "\nMove = " + turn + "\nMarble = " + type
                + "\n";*/
        text = "";
        enabled = true;
    }

    public void enable(boolean bool) {
        enabled = bool;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setMarbleType(MarbleType type) {
        this.type = type.toString();
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setMarble(Marble marble) {
        this.marble = marble;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void addMove(Marble marble, Direction direction) {
        if (enabled) {
            setMarble(marble);
            setMarbleType(marble.getType());
            setDirection(direction.toString());
            setPoint(board.getMoveSets().get(direction));

            int oldX = marble.getCell().getX();
            int oldY = marble.getCell().getY();
            int newX = oldX + point.x;
            int newY = oldY + point.y;

            /*text += type + " marble at [" + oldX + "][" + (oldY / 2 -1) + "] moved to "
                    + direction + " at [" + newX + "][" + (newY / 2 -1) + "].\n";*/
            text = "";
        }
    }

    public void addEnemyKilled(Marble marble) {
        if (enabled) {
            int x = marble.getCell().getX();
            int y = marble.getCell().getY();
            text += type + " marble at [" + x + "][" + (y / 2 - 1)
                    + "] was pushed out.\n";
        }
    }

    public String getText() {
        return text;
    }

    public void addToLog() {
        if (enabled) {
            if (type.equals("WHITE")) {
                whiteHistory.add(text);
            } else {
                blackHistory.add(text);
            }
        }
    }

    public static void clearLogs() {
        ArrayList<String> blackHistory = new ArrayList<String>();
        ArrayList<String> whiteHistory = new ArrayList<String>();
    }
}

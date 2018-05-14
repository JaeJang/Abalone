package abalone;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import abalone.gameEnum.Direction;
import abalone.gameEnum.Layout;
import abalone.gameEnum.MarbleType;
import abalone.gameEnum.TURN;
import boardFrame.FrameMethods;

/**
 * 
 * @author Kimdoy
 *
 *         Class Board.
 * 
 *         This class will be the foundation of the game Abalone. The game is
 *         arranged similar to Chinese checkers but without the starting thus
 *         resulting in a hexagon with 5 units on each side. To account for the
 *         diagonal layout of the game, the board is made up of 2D array of
 *         cells with some of the cells being redundant...
 * 
 *         --------------------- //The '-' represents redundant cells
 *         ------O-O-O-O-O------ //the 'O' represents active cells
 *         -----O-O-O-O-O-O----- ----O-O-O-O-O-O-O---- ---O-O-O-O-O-O-O-O---
 *         --O-O-O-O-O-O-O-O-O-- ---O-O-O-O-O-O-O-O--- ----O-O-O-O-O-O-O----
 *         -----O-O-O-O-O-O----- ------O-O-O-O-O------ ---------------------
 * 
 *         Due to this nature, many for loops had to be created to set up the
 *         board.
 */
public class Board {

    public static final int NUMBER_OF_ROWS = 11;
    public static final int NUMBER_OF_COLUMNS = 21;
    public static final HashMap<TURN, TURN> OPPONENT_MAP = new HashMap<TURN, TURN>();
    static {
        OPPONENT_MAP.put(TURN.PLAYER1, TURN.PLAYER2);
        OPPONENT_MAP.put(TURN.PLAYER2, TURN.PLAYER1);
        OPPONENT_MAP.put(TURN.PLAYER, TURN.COMPUTER);
        OPPONENT_MAP.put(TURN.COMPUTER, TURN.PLAYER);
        OPPONENT_MAP.put(TURN.COMPUTER1, TURN.COMPUTER2);
        OPPONENT_MAP.put(TURN.COMPUTER2, TURN.COMPUTER1);
    }
    public static TURN PLAYER_TURN = TURN.PLAYER1;
    public static int layout_number = 0;

    private static String fileInputPos;
    private static String inputFileStarter;

    private ArrayList<Marble> marbles;
    public static final HashMap<Direction, Point> MOVE_SETS = new HashMap<Direction, Point>();
    static {
        MOVE_SETS.put(Direction.LEFT, new Point(0, -2));
        MOVE_SETS.put(Direction.RIGHT, new Point(0, 2));
        MOVE_SETS.put(Direction.TOP_LEFT, new Point(-1, -1));
        MOVE_SETS.put(Direction.TOP_RIGHT, new Point(-1, 1));
        MOVE_SETS.put(Direction.BOT_LEFT, new Point(1, -1));
        MOVE_SETS.put(Direction.BOT_RIGHT, new Point(1, 1));
    }
    private HashMap<TURN, MarbleType> colourMap;
    private Cell[][] cell;
    private Point firstCoordinate;
    private Point secondCoordinate;
    private Stack<Marble> stackOfMarbles;
    private int num_move_white = 0;
    private int num_move_black = 0;
    private HashMap<ArrayList<Marble>, ArrayList<Direction>> setOfAllMoves = new HashMap<ArrayList<Marble>, ArrayList<Direction>>();

    // Constructor for class Board.
    public Board() {
        cell = new Cell[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        marbles = new ArrayList<Marble>();
        // fileInputPosArray = new ArrayList<String>();
        colourMap = new HashMap<TURN, MarbleType>();
        firstCoordinate = new Point();
        secondCoordinate = new Point();
        init();
    }

    public Board(String inputFile) {
        cell = new Cell[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        marbles = new ArrayList<Marble>();
        colourMap = new HashMap<TURN, MarbleType>();
        firstCoordinate = new Point();
        secondCoordinate = new Point();
        setBoard();
        setMarblesWithFile(inputFile);
    }

    public Board(Cell[][] cells) {
        cell = new Cell[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        marbles = new ArrayList<Marble>();
        colourMap = new HashMap<TURN, MarbleType>();
        firstCoordinate = new Point();
        secondCoordinate = new Point();
        setBoardWithCells(cells);
        initializeColourMap();
    }

    public void setBoardWithCells(Cell[][] cells) {

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cell[0].length; j++) {
                if (cells[i][j] != null) {

                    if (cells[i][j].getMarble() != null) {
                        Marble marble = new Marble(cells[i][j], cells[i][j].getMarble().getType());
                        cell[i][j] = new Cell(i, j, marble, this);
                    } else {
                        cell[i][j] = new Cell(i, j, null, this);
                    }
                }
            }
        }
    }

    // Initializes the board.
    public void init() {
        setBoard();

        if (layout_number == 0)
            standardLayout();
        else if (layout_number == 1)
            belgiumDaisyLayout();
        else if (layout_number == 2)
            germanDaisyLayout();
        else if (layout_number == 3) {
            // TODO: call a layout method on the basis of input file
            System.out.println(inputFileStarter + fileInputPos);
            placeInputFile(inputFileStarter, fileInputPos);
        }

        initializeColourMap();
    }

    public Cell[][] returnAllCell() {
        return cell;
    }

    public void setAllCell(Cell[][] cells) {
        this.cell = cells;
    }

    public void initializeColourMap() {
        colourMap.put(PLAYER_TURN, MarbleType.BLACK);
        colourMap.put(OPPONENT_MAP.get(PLAYER_TURN), MarbleType.WHITE);
    }

    public HashMap<TURN, MarbleType> getColourMap() {
        return colourMap;
    }

    /**
     * Returns the Cell at the specified index. Does not check whether if the Cell
     * is null.
     * 
     * @param x
     *            as an int representing rows
     * @param y
     *            as an int representing columns
     * @return Cell
     */
    public Cell getCellAt(int x, int y) {
        return cell[x][y];
    }

    /**
     * ArrayList that stores Marbles. This List should only contain The marbles that
     * are in que to be moved.
     * 
     * @return ArrayList of Marble
     */
    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    /**
     * Adds the Marble in the parameter, will also set the color of the marble to be
     * darker to specify that the marble is currently highlighted.
     * 
     * @param marble
     */
    public void addMarbles(Marble marble) {
        marble.setDarkColor();
        marbles.add(marble);
    }

    /**
     * Clears the ArrayList of marbles.
     */
    public void clearMarbles() {
        marbles.clear();
    }

    public Stack<Marble> getMarbleStack() {
        return stackOfMarbles;
    }

    public void addMarbleToStack(Marble marble) {
        stackOfMarbles.push(marble);
    }

    /**
     * HashMap of Direction and Point. The enum Direction has 6 possibilities which
     * represent the possible moves in the game Abalone. The Point contains and x
     * and y which represents the coordinate change dependant on the Direction.
     * 
     * @return HashMaps<Direction, Point>
     */
    public HashMap<Direction, Point> getMoveSets() {
        return MOVE_SETS;
    }

    /**
     * firstCoordinate will always contain the position of the first marble that is
     * added to the ArrayList marbles.
     * 
     * @return Point
     */
    public Point getFirstCoordinates() {
        return firstCoordinate;
    }

    /**
     * This method is only to be called when adding the FIRST element to the
     * ArrayList marbles. It will store the coordinates of the cell of the specific
     * marble.
     * 
     * @param x
     *            as an int for rows
     * @param y
     *            as an int for columns
     */
    public void setFirstCoordinates(int x, int y) {
        firstCoordinate = new Point(x, y);
    }

    /**
     * secondCoordinate will always contain the position of the Cell that a Marble
     * is trying to move to.
     * 
     * @return Point
     */
    public Point getSecondCoordinates() {
        return secondCoordinate;
    }

    /**
     * This method is only to be called when MOVING the marbles.
     * 
     * @param x
     * @param y
     */
    public void setSecondCoordinates(int x, int y) {
        secondCoordinate = new Point(x, y);
    }

    /**
     * setBoard will only instantiate specific Cells. These Cells are chosen by how
     * the board is layed out. Look at the class comment to see how the cells are
     * laid out.
     */
    public void setBoard() {

        int counter = 2;

        for (int i = 5; i < NUMBER_OF_ROWS - 1; i++) {
            for (int j = counter; j < 24 - i; j++) {
                if (legalSpace(i, j)) {
                    cell[i][j] = new Cell(i, j, this);
                }
            }
            counter++;
        }

        counter = 6;
        for (int i = 1; i < 5; i++) {
            for (int j = counter; j < 14 + i; j++) {
                if (legalSpace(i, j)) {
                    cell[i][j] = new Cell(i, j, this);
                }
            }
            counter--;
        }
    }

    public void standardLayout() {
        placeRedMarbles();
        placeBlueMarbles();
    }

    public void belgiumDaisyLayout() {
        placeBlueMarblesDaisy(Layout.BELGIUM_DAISY);
        placeRedMarblesDaisy(Layout.BELGIUM_DAISY);
    }

    public void germanDaisyLayout() {
        placeBlueMarblesDaisy(Layout.GERMAN_DAISY);
        placeRedMarblesDaisy(Layout.GERMAN_DAISY);
    }

    public void setMarbles(ArrayList<Cell> cells, MarbleType color) {
        if (color.equals(MarbleType.BLACK)) {
            for (Cell cell : cells) {
                Marble marble = new Marble(cell, MarbleType.BLACK);
                cell.setMarble(marble);
            }
        } else if (color.equals(MarbleType.WHITE)) {
            for (Cell cell : cells) {
                Marble marble = new Marble(cell, MarbleType.WHITE);
                cell.setMarble(marble);
            }
        }
    }

    /**
     * Places the Red Marbles onto the board.
     */
    public void placeRedMarbles() {
        ArrayList<Cell> space = new ArrayList<Cell>();
        for (int i = 6; i < 15; i++) {
            if (legalSpace(1, i))
                space.add(cell[1][i]);//
        }
        for (int i = 5; i < 16; i++) {
            if (legalSpace(2, i))
                space.add(cell[2][i]); //
        }
        for (int i = 8; i < 13; i++) {
            if (legalSpace(3, i))
                space.add(cell[3][i]); //
        }
        setMarbles(space, MarbleType.WHITE);
    }

    /**
     * Places the Blue Marbles onto the board.
     */
    public void placeBlueMarbles() {
        ArrayList<Cell> space = new ArrayList<Cell>();
        for (int i = 8; i < 13; i++) {
            if (legalSpace(7, i))
                space.add(cell[7][i]); //
        }
        for (int i = 5; i < 16; i++) {
            if (legalSpace(8, i))
                space.add(cell[8][i]); //
        }
        for (int i = 6; i < 15; i++) {
            if (legalSpace(9, i))
                space.add(cell[9][i]); //
        }
        setMarbles(space, MarbleType.BLACK);
    }

    public void placeBlueMarblesDaisy(Layout layout) {
        ArrayList<Cell> cells = new ArrayList<Cell>();
        Cell firstCell = null;
        Cell secondCell = null;
        if (layout == Layout.BELGIUM_DAISY) {
            firstCell = cell[2][7];
            secondCell = cell[8][13];
        } else if (layout == Layout.GERMAN_DAISY) {
            firstCell = cell[3][6];
            secondCell = cell[7][14];
        }

        cells.addAll(getNeighbouringCells(firstCell));
        cells.addAll(getNeighbouringCells(secondCell));
        cells.add(firstCell);
        cells.add(secondCell);

        setMarbles(cells, MarbleType.WHITE);
    }

    public void placeRedMarblesDaisy(Layout layout) {
        ArrayList<Cell> cells = new ArrayList<Cell>();
        Cell firstCell = null;
        Cell secondCell = null;
        if (layout == Layout.BELGIUM_DAISY) {
            firstCell = cell[2][13];
            secondCell = cell[8][7];
        } else if (layout == Layout.GERMAN_DAISY) {
            firstCell = cell[3][14];
            secondCell = cell[7][6];
        }
        cells.addAll(getNeighbouringCells(firstCell));
        cells.addAll(getNeighbouringCells(secondCell));
        cells.add(firstCell);
        cells.add(secondCell);

        setMarbles(cells, MarbleType.BLACK);
    }

    /**
     * Iterates through the entire 2D Array of Cells and will return an ArrayList of
     * all NON Cells.
     * 
     * @return ArrayList<Cell>
     */
    public ArrayList<Cell> getCells() {
        ArrayList<Cell> cells = new ArrayList<Cell>();

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (legalSpace(i, j) && cell[i][j] != null) {
                    cells.add(cell[i][j]);
                }
            }
        }
        return cells;
    }

    /**
     * Looks around the Cell specified in the parameter and checks to see if it has
     * neighbors. A cell is considered a neighbour if and only if the Cell is beside
     * the current scoped Cell. It will add all Cell neighbours into an ArrayList
     * and return that list.
     * 
     * Also makes sure to not add any null cells.
     * 
     * @param cell
     * @return ArrayList<Cell>
     */
    public ArrayList<Cell> getNeighbouringCells(Cell cell) {

        ArrayList<Cell> neighbouringCells = new ArrayList<Cell>();
        int x = cell.getX();
        int y = cell.getY();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 || j == 0)
                    continue;
                if (this.cell[x + i][y + j] != null) {
                    neighbouringCells.add(this.cell[x + i][y + j]);
                }
            }
        }

        if (this.cell[x][y + 2] != null)
            neighbouringCells.add(this.cell[x][y + 2]);
        if (this.cell[x][y - 2] != null)
            neighbouringCells.add(this.cell[x][y - 2]);

        return neighbouringCells;
    }

    /**
     * This method will iterate through the getNeighbouringCells method and only add
     * the ones that does NOT contain a marble.
     * 
     * @param cell
     * @return ArrayList<Cell>
     */
    public ArrayList<Cell> getNeighbouringEmptyCells(Cell cell) {

        ArrayList<Cell> emptyCells = new ArrayList<Cell>();
        ArrayList<Cell> neighbouringCells = getNeighbouringCells(cell);

        for (Cell emptyCell : neighbouringCells) {
            if (emptyCell.getMarble() == null) {
                emptyCells.add(emptyCell);
            }
        }

        return emptyCells;
    }

    /**
     * Opposite of the method above. Iterates through the getNeighbouringCells
     * method and only adds the Cells that DO contain a marble.
     * 
     * @param cell
     * @return ArrayList<Marble>
     */
    public ArrayList<Marble> getNeighbouringMarbles(Cell cell) {

        ArrayList<Marble> marbleCells = new ArrayList<Marble>();
        ArrayList<Cell> neighbouringCells = getNeighbouringCells(cell);

        for (Cell marbleCell : neighbouringCells) {
            if (marbleCell.getMarble() != null) {
                marbleCells.add(marbleCell.getMarble());
            }
        }

        return marbleCells;
    }

    /**
     * NOTE: MAKE SURE THE CELL YOU USE FOR THIS METHOD CONTAINS A MARBLE.
     * 
     * Iterates through the method getNeighbouringMarbles and adds all marbles that
     * are the same color as the marble in the Cell.
     * 
     * @param cell
     * @return ArrayList<Marble>
     */
    public ArrayList<Marble> getNeighbouringFriends(Cell cell) {

        ArrayList<Marble> friends = new ArrayList<Marble>();
        ArrayList<Marble> neighbouringMarbles = getNeighbouringMarbles(cell);

        for (Marble friend : neighbouringMarbles) {
            if (friend.getType() == cell.getMarble().getType()) {
                friends.add(friend);
            }
        }

        return friends;
    }

    /**
     * NOTE: MAKE SURE THE CELL YOU USE FOR THIS METHOD CONTAINS A MARBLE.
     * 
     * Iterates through the method getNeighbouringMarbles and adds all marbles that
     * are NOT the same color as the marble in the Cell.
     * 
     * @param cell
     * @return ArrayList<Marble>
     */
    public ArrayList<Marble> getNeighbouringEnemies(Cell cell) {

        ArrayList<Marble> enemies = new ArrayList<Marble>();
        ArrayList<Marble> neighbouringMarbles = getNeighbouringMarbles(cell);

        for (Marble enemy : neighbouringMarbles) {
            if (enemy.getType() != cell.getMarble().getType()) {
                enemies.add(enemy);
            }
        }

        return enemies;
    }

    /**
     * return the next cell at certain direction
     * 
     * @param cell
     * @param direction
     * @return
     */
    public Cell getDestinationCell(Cell cell, Direction direction) {
        int xDirection = getMoveSets().get(direction).x;
        int yDirection = getMoveSets().get(direction).y;
        int xDestination = cell.getX() + xDirection;
        int yDestination = cell.getY() + yDirection;
        return getCellAt(xDestination, yDestination);
    }

    public ArrayList<Marble> findInlineEnemies(Marble m, Direction direction) {
        ArrayList<Marble> enemies = new ArrayList<Marble>();
        int xDirection = getMoveSets().get(direction).x;
        int yDirection = getMoveSets().get(direction).y;
        int x = m.getCell().getX() + xDirection;
        int y = m.getCell().getY() + yDirection;
        Cell cell = getCellAt(x, y);

        while (cell != null && cell.getMarble() != null) {

            if (!(cell.getMarble().getType().equals(m.getType()))) {
                enemies.add(cell.getMarble());
            } else {
                break;
            }
            x += xDirection;
            y += yDirection;
            cell = getCellAt(x, y);
        }
        return enemies;
    }

    /**
     * Will only return true if the given coordinate contains an ODD number AND an
     * EVEN number ei (1, 2), (3,6) etc.
     * 
     * This algorithm works because of how our 2D array is laid out.
     * 
     * @param x
     * @param y
     * @return
     */
    public boolean legalSpace(int x, int y) {
        if ((x % 2 == 0 && y % 2 == 1) || (x % 2 == 1 && y % 2 == 0)) {
            return true;
        }
        return false;
    }

    /**
     * Increment of number of movement for each players
     */
    public void setNumOfMove() {
        if (colourMap.get(PLAYER_TURN) == MarbleType.BLACK) {
            num_move_black++;
        } else {
            num_move_white++;
        }
    }

    /**
     * Return number of movement for each players turn
     * 
     * @param turn
     * @return
     */
    public int getNumOfMove(MarbleType type) {
        if (type == MarbleType.WHITE)
            return num_move_white;
        else
            return num_move_black;
    }

    /**
     * Get position information of Input file as a String ArrayList
     * 
     * @param array
     */
    public void setFileInputPos(String initPostion, String starter) {
        fileInputPos = initPostion;
        inputFileStarter = starter;
    }

    public void placeInputFile(String turn, String list) {
        if (turn.equals("w")) {
            PLAYER_TURN = OPPONENT_MAP.get(Board.PLAYER_TURN);
        }

        String[] positions = list.split(",");

        for (String position : positions) {

            char x = position.charAt(0);
            int y = Character.getNumericValue(position.charAt(1));
            char color = position.charAt(2);

            int xPosition = FrameMethods.X_POSITION.get(x);
            int yPosition = FrameMethods.whichRow(x, y);

            MarbleType marbleType = null;
            if (color == 'b') {
                marbleType = MarbleType.BLACK;
            } else if (color == 'w') {
                marbleType = MarbleType.WHITE;
            }
            Cell currentCell = getCellAt(xPosition, yPosition);
            if (currentCell == null) {
                System.out.println("it's null");
            }
            currentCell.setMarble(new Marble(currentCell, marbleType));
        }
    }

    public void addOneMarbleMove(Marble marble1, String player, Direction move) {
        ArrayList<Marble> marbles = new ArrayList<Marble>();
        ArrayList<Direction> moves = new ArrayList<Direction>();
        marbles.add(marble1);
        moves.add(move);
    }

    public void addTwoMarbleMoves(Marble marble1, Marble marble2, String player, Direction move) {
        ArrayList<Marble> marbles = new ArrayList<Marble>();
        ArrayList<Direction> moves = new ArrayList<Direction>();
        marbles.add(marble2);
        marbles.add(marble1);
        moves.add(move);
    }

    public void addThreeMarbleMoves(Marble marble1, Marble marble2, Marble marble3, String player, Direction move) {
        ArrayList<Marble> marbles = new ArrayList<Marble>();
        ArrayList<Direction> moves = new ArrayList<Direction>();
        marbles.add(marble3);
        marbles.add(marble2);
        marbles.add(marble1);
        moves.add(move);
    }

    public int computeX(Cell c1, Cell c2) {
        return c1.getX() - c2.getX();
    }

    public int computeY(Cell c1, Cell c2) {
        return c1.getY() - c2.getY();
    }

    public ArrayList<Cell> getCellFriends(Cell cell) {

        ArrayList<Cell> friends = new ArrayList<>();
        ArrayList<Cell> neighbours = getNeighbouringCells(cell);
        if (neighbours != null) {
            for (Cell c : neighbours) {
                if (c.getMarble() != null) {
                    if (c.getMarble().getType() == cell.getMarble().getType())
                        friends.add(c);
                }
            }
        }
        return friends;

    }

    public void generateMoveFile(ArrayList<String> allMoves) throws IOException {
        int filenum = FileInput.getFNum();

        File fnew = new File("output" + filenum + ".move");

        fnew.createNewFile();
        FileWriter writer = new FileWriter(fnew);

        for (String s : allMoves) {
            writer.write(s + "\n");
        }

        writer.close();
    }

    public boolean checkIfCellIsValidMove(Marble marble, Direction direction) {

        int x = marble.getCell().getX() + MOVE_SETS.get(direction).x;
        int y = marble.getCell().getY() + MOVE_SETS.get(direction).y;
        if (getCellAt(x, y) == null) {
            return false;
        }
        if (getCellAt(x, y).getMarble() != null) {
            return false;
        }
        return true;
    }

    public void generateAllStates() throws IOException {
        ArrayList<Marble> movingMarbles = new ArrayList<Marble>();
        for (ArrayList<Marble> allMovingMarbles : setOfAllMoves.keySet()) {

            for (Direction directions : setOfAllMoves.get(allMovingMarbles)) {
                movingMarbles.clear();
                int x = MOVE_SETS.get(directions).x;
                int y = MOVE_SETS.get(directions).y;
                for (Marble marble : allMovingMarbles) {
                    movingMarbles.add(marble);
                }
                generateStateSpace(movingMarbles, x, y);
            }
        }
        writeToBoardFile.writeToFile();
    }

    public void generateStateSpace(ArrayList<Marble> marblesToBeMoved, int x, int y) throws IOException {

        final Cell[][] newCells = cell.clone();
        final Board newBoard = new Board(newCells);
        for (Marble marble : marblesToBeMoved) {
            int oldX = marble.getCell().getX();
            int oldY = marble.getCell().getY();
            int newX = marble.getCell().getX() + x;
            int newY = marble.getCell().getY() + y;
            newBoard.getCellAt(oldX, oldY).setMarble(null);
            newBoard.getCellAt(newX, newY).setMarble(marble);

            for (int i = 0; i < newBoard.returnAllCell().length; i++) {
                for (int j = 0; j < newBoard.returnAllCell()[i].length; j++) {
                    if (newBoard.returnAllCell()[i][j] != null && newBoard.returnAllCell()[i][j].getMarble() != null) {
                        writeToBoardFile.addLineToList(
                                newBoard.returnAllCell()[i][j].getMarble().getType() + "," + i + "," + j + "/");
                    }
                }
            }
            writeToBoardFile.addLineToList("\n");
        }
    }


    public Multimap<ArrayList<Marble>, Direction> allMoves_inMap(MarbleType type) {
        Multimap<ArrayList<Marble>, Direction> allPossibleMoves = ArrayListMultimap.create();
        // Directions to check to eliminate duplicates
        Direction[] directionsToCheck = { Direction.RIGHT, Direction.BOT_RIGHT, Direction.BOT_LEFT };

        // Loop through the board and find the marbles of the colour passed in
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                if (cell[i][j] != null && cell[i][j].getMarble() != null)
                    if (cell[i][j].getMarble().getType() == type) {
                        // For each marbles, check only 3 directions to eliminate duplicates
                        for (Direction direction : directionsToCheck) {
                            ArrayList<Marble> friends = findInlineFriends(cell[i][j].getMarble(), direction);
                            for (int depth = 1; depth <= 3; depth++) {
                                if (friends.size() < depth) {
                                    break;
                                } else {
                                    ArrayList<Marble> collections = new ArrayList(friends.subList(0, depth));
                                    if ((depth == 1 && direction == Direction.RIGHT) || depth > 1) {
                                        // Unique pairs found, perform check
                                        checkAndAdd_withMap(collections, allPossibleMoves);
                                    }
                                }
                            }
                        }
                    }
            }
        }


        return allPossibleMoves;
    }

    public void checkAndAdd_withMap(ArrayList<Marble> collections, Multimap<ArrayList<Marble>, Direction> log) {
        for (Direction dir : MOVE_SETS.keySet()) {
            if (collections.size() == 1) {

                Cell destCell = getDestinationCell(collections.get(0), dir);
                if (destCell != null && destCell.getMarble() == null) {
                    // log.add(marblesToString(collections) + "|" + dir.toString());
                    log.put(collections, dir);
                }
            } else {
                Marble first = collections.get(0);
                Marble second = collections.get(1);
                int x = MOVE_SETS.get(dir).x + first.getCell().getX();
                int y = MOVE_SETS.get(dir).y + first.getCell().getY();
                if (x != second.getCell().getX() || y != second.getCell().getY()) {
                    Collections.reverse(collections);
                }

                // broadside
                if (isSideMove(collections, dir)) {
                    if (allDestEmpty(collections, dir)) {
                        log.put(collections, dir);
                    }
                    // inline
                } else {
                    Marble last = collections.get(collections.size() - 1);
                    Cell nextCell = getDestinationCell(last, dir);

                    if (nextCell != null) {
                        // no enemy
                        if (nextCell.getMarble() == null) {
                            Collections.reverse(collections);
                            log.put(collections, dir);
                            // enemies
                        } else if (nextCell.getMarble().getType() != collections.get(0).getType()) {
                            ArrayList<Marble> enemies = findInlineFriends(nextCell.getMarble(), dir);
                            if (collections.size() > enemies.size()) {
                                Marble lastEnemy = enemies.get(enemies.size() - 1);
                                Cell c = getDestinationCell(lastEnemy, dir);

                                Collections.reverse(collections);
                                Collections.reverse(enemies);
                                if (c == null) {
                                    enemies.remove(lastEnemy);
                                    if (enemies.size() == 0) {
                                        ArrayList<Marble> temp = collections;
                                        enemies.addAll(temp);
                                        log.put(enemies, dir);
                                        log.put(collections, dir);
                                    }
                                } else {
                                    if (c.getMarble() != null) {
                                        if (c.getMarble().getType() != collections.get(0).getType()) {
                                            Collections.reverse(collections);
                                            Collections.reverse(enemies);
                                            ArrayList<Marble> temp = collections;
                                            enemies.addAll(temp);
                                            log.put(enemies, dir);
                                        }
                                        ArrayList<Marble> temp = collections;
                                        enemies.addAll(temp);
                                        log.put(enemies, dir);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean allDestEmpty(ArrayList<Marble> marbles, Direction dir) {
        boolean allEmpty = true;
        for (Marble m : marbles) {
            Cell dest = getDestinationCell(m, dir);
            if (dest == null || dest.getMarble() != null) {
                allEmpty = false;
                break;
            }
        }
        return allEmpty;
    }

    public boolean isSideMove(ArrayList<Marble> marbles, Direction dir) {
        Marble first = marbles.get(0);
        Marble second = marbles.get(1);
        int xDiff = second.getCell().getX() - first.getCell().getX();
        int yDiff = second.getCell().getY() - first.getCell().getY();
        int x = MOVE_SETS.get(dir).x;
        int y = MOVE_SETS.get(dir).y;
        if (xDiff == x && yDiff == y) {
            return false;
        }
        return true;
    }

    public Cell getDestinationCell(Marble marble, Direction direction) {
        int x = MOVE_SETS.get(direction).x + marble.getCell().getX();
        int y = MOVE_SETS.get(direction).y + marble.getCell().getY();
        return cell[x][y];
    }

    public String marblesToString(ArrayList<Marble> marbles) {
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

    public MarbleType getCurrentColour() {
        return colourMap.get(PLAYER_TURN);
    }

    public ArrayList<Marble> findInlineFriends(Marble marble, Direction direction) {
        ArrayList<Marble> friends = new ArrayList<Marble>();
        friends.add(marble);
        int xDirection = getMoveSets().get(direction).x;
        int yDirection = getMoveSets().get(direction).y;
        int x = marble.getCell().getX() + xDirection;
        int y = marble.getCell().getY() + yDirection;
        Cell cell = getCellAt(x, y);

        while (cell != null && cell.getMarble() != null) {
            if ((cell.getMarble().getType().equals(marble.getType()))) {
                friends.add(cell.getMarble());
            } else {
                break;
            }
            x += xDirection;
            y += yDirection;
            cell = getCellAt(x, y);
        }
        return friends;
    }

    public void addToSetOfAllMoves(ArrayList<Marble> collections, Direction direction) {
        if (setOfAllMoves.containsKey(collections)) {
            setOfAllMoves.get(collections).add(direction);
        } else {
            ArrayList<Direction> directions = new ArrayList<Direction>();
            directions.add(direction);
            setOfAllMoves.put(collections, directions);
        }
    }


    public void setMarblesWithFile(String marblePosition) {

        String[] positions = marblePosition.split(",");

        for (String position : positions) {

            char x = position.charAt(0);
            int y = Character.getNumericValue(position.charAt(1));
            char color = position.charAt(2);

            int xPosition = FrameMethods.X_POSITION.get(x);
            int yPosition = FrameMethods.whichRow(x, y);

            // System.out.println(yPosition);
            MarbleType marbleType = null;
            if (color == 'b') {
                marbleType = MarbleType.BLACK;
            } else if (color == 'w') {
                marbleType = MarbleType.WHITE;
            }
            Cell currentCell = getCellAt(xPosition, yPosition);
            currentCell.setMarble(new Marble(currentCell, marbleType));
        }
    }

    public Direction getDirectionFromString(String direction) {

        if (direction.equals("RIGHT")) {
            return Direction.RIGHT;
        } else if (direction.equals("LEFT")) {
            return Direction.LEFT;
        } else if (direction.equals("TOP LEFT")) {
            return Direction.TOP_LEFT;
        } else if (direction.equals("TOP RIGHT")) {
            return Direction.TOP_RIGHT;
        } else if (direction.equals("BOTTOM LEFT")) {
            return Direction.BOT_LEFT;
        } else if (direction.equals("BOTTOM RIGHT")) {
            return Direction.BOT_RIGHT;
        }
        return null;
    }

    public void generateAllStateSpaces(ArrayList<String> allMoves, String input) {

        writeToBoardFile.openFile();
        for (String moves : allMoves) {
            List<String> listOfBlack = new LinkedList<>();
            List<String> listOfWhite = new LinkedList<>();
            Board tempBoard = new Board(input);

            String[] move = moves.split("\\|");

            Direction direction = getDirectionFromString(move[1]);

            int xMove = tempBoard.MOVE_SETS.get(direction).x;
            int yMove = tempBoard.MOVE_SETS.get(direction).y;
            String[] marbles = move[0].split("/");

            for (String marblePosition : marbles) {
                String[] position = marblePosition.split(",");
                int x = Integer.parseInt(position[0]);
                int y = Integer.parseInt(position[1]);
                Marble marble = tempBoard.getCellAt(x, y).getMarble();
                tempBoard.getCellAt(x, y).setMarble(null);
                tempBoard.getCellAt(x + xMove, y + yMove).setMarble(marble);
                marble.setCell(tempBoard.getCellAt(x + xMove, y + yMove));
            }

            for (Marble blackMarble : tempBoard.allColoredMarbles(MarbleType.BLACK)) {
                int x = blackMarble.getCell().getX();
                int y = blackMarble.getCell().getY();
                //System.out.print("BLACK," + x + "," + y + "/");
                Outputter outputter = new Outputter("" + x, "" + y, "BLACK");
                listOfBlack.add(outputter.properOutput());
                // writeToBoardFile.addLineToList(outputter.properOutput() + ",");
            }
            Collections.sort(listOfBlack);

            for (Marble whiteMarble : tempBoard.allColoredMarbles(MarbleType.WHITE)) {
                int x = whiteMarble.getCell().getX();
                int y = whiteMarble.getCell().getY();
                //System.out.print("WHITE," + x + "," + y + "/");
                Outputter outputter = new Outputter("" + x, "" + y, "WHITE");
                listOfWhite.add(outputter.properOutput());
                // writeToBoardFile.addLineToList(outputter.properOutput() + ",");
            }
            Collections.sort(listOfWhite);

            listOfBlack.addAll(listOfWhite);
            writeToBoardFile.writeToFile(listOfBlack);
            listOfBlack.clear();
            listOfWhite.clear();
            System.out.println();

        }
        writeToBoardFile.closeFile();
    }

    public ArrayList<Marble> allColoredMarbles(MarbleType type) {

        ArrayList<Marble> marbles = new ArrayList<Marble>();

        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            for (int col = 0; col < NUMBER_OF_COLUMNS; col++) {
                Cell cell = getCellAt(row, col);
                if (cell != null && cell.getMarble() != null && cell.getMarble().getType() == type) {
                    marbles.add(cell.getMarble());
                }
            }
        }

        return marbles;
    }

    public void printCells() {
        for(int i = 0; i < cell.length; ++i) {
            for(int j = 0; j < cell[i].length; ++j) {
                if(cell[i][j] != null) {
                    if(cell[i][j].getMarble() != null) {
                        if(cell[i][j].getMarble().getType() == MarbleType.BLACK) {
                            System.out.print(cell[i][j].getMarble().getType().toString() + "," + i + "," + j + "/");
                        }
                    }
                }
            }
        }
        System.out.print("  ");
        for(int i = 0; i < cell.length; ++i) {
            for(int j = 0; j < cell[i].length; ++j) {
                if(cell[i][j] != null) {
                    if(cell[i][j].getMarble() != null) {
                        if(cell[i][j].getMarble().getType() == MarbleType.WHITE) {
                            System.out.print(cell[i][j].getMarble().getType().toString() + "," + i + "," + j + "/");
                        }
                    }
                }
            }
        }
        System.out.println();
    }
    
    public void replaceCells(Cell[][] cells) {
        cell = new Cell[NUMBER_OF_COLUMNS][NUMBER_OF_COLUMNS];
        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells[0].length; j++) {
                if(cells[i][j] != null) {
                    cell[i][j] = cells[i][j];
                    if(cells[i][j].getMarble() != null) {
                        Marble marble = cells[i][j].getMarble();
                        cell[i][j].setMarble(marble);
                        marble.setCell(cell[i][j]);
                    }
                }
            }
            
        }
    }
    
    public void printBoard() {
		ArrayList<Cell> cells = getCells();
		for (Cell c : cells) {
			if (c.getMarble() != null) {
				if (c.getMarble().getType() == MarbleType.BLACK) {
					System.out.print(c.getMarble().getType().toString() + "," + c.getX() + "," + c.getY() + "/");
				}
			}
		}
		System.out.print("  ");
		for (Cell c : cells) {
			if (c.getMarble() != null) {
				if (c.getMarble().getType() == MarbleType.WHITE) {
					System.out.print(c.getMarble().getType().toString() + "," + c.getX() + "," + c.getY() + "/");
				}
			}
		}
		System.out.println();
	}
}

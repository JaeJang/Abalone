package boardFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import abalone.Agent;
import abalone.Board;
import abalone.Cell;
import abalone.FileInput;
import abalone.Log;
import abalone.Marble;
import abalone.Menu;
import abalone.gameEnum.Direction;
import abalone.gameEnum.MarbleType;
import abalone.gameEnum.STATE;
import abalone.gameEnum.TURN;
import boardFrame.GameFrame.BoardPanel.DrawingPanel;
import listener.AgentThread;
import listener.KeyInput;
import listener.MouseInput;
import listener.MyTimer;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {

    public static STATE state = STATE.MODE_SETTING;
    public static boolean isPVC = false;
    public static boolean turnOver = false;
    public static int turnLimit = 0;
    public static TURN TURN_SETTING;
    public Menu menu;
    public Agent agent1;
    public Agent agent2;
    public AgentThread agentThread;
    public Thread pvcAgentThread;
    FileInput fileInput;

    private Board board;
    private Marble marble;
    private ArrayList<DrawingPanel> panels;

    private BoardPanel boardpanel;
    private JPanel menuPanel;
    private MyTimer timer;
    private int sec_black;
    private int sec_white;
    private int msec_black;
    private int msec_white;
    private int totalSec_black;
    private int totalSec_white;
    private int totalmSec_black;
    private int totalmSec_white;
    

    public GameFrame(Board board) {
        setTitle("Abalone");
        this.board = board;
        panels = new ArrayList<DrawingPanel>();

        // Mouse and key listener for menu systems
        this.addMouseListener(new MouseInput(this));
        this.addKeyListener(new KeyInput(this, board));

        boardpanel = new BoardPanel();
        menu = new Menu();
        menuPanel = menu.layoutPanel();
        timer = new MyTimer(this);
        timer.setTimer();
    }

    public void init() {
        // System.out.println("GameFrame init called");

        // Game state is changed from mouse input and keyboard input
        // and init() method will be called
        getContentPane().removeAll();
        if (state == STATE.MODE_SETTING) {
            menuPanel = menu.gameModeSetPanel();
            add(menuPanel);
        } else if (state == STATE.TEAM_SETTING_PVP
                || state == STATE.TEAM_SETTING_PVC
                || state == STATE.TEAM_SETTING_CVC) {
            menuPanel = menu.teamSettingPanel();
            add(menuPanel);
        } else if (state == STATE.LAYOUT_SELECT) {
            menuPanel = menu.layoutPanel();
            add(menuPanel);
        } else if(state == STATE.FILE_INPUT) {
            fileInput = new FileInput(this);
            add(fileInput);
        } else if (state == STATE.TIME_SETTING) {
            menuPanel = menu.timeSettingPanel();
            add(menuPanel);
        } else if (state == STATE.TURN_LIMIT_SETTING) {
            menuPanel = menu.turnLimitSetting();
            add(menuPanel);
        } else if (state == STATE.GAME) {
            add(boardpanel);
        } else if (state == STATE.PASUSE) {
            menuPanel = menu.pausePanel();
            add(menuPanel);
        }

        revalidate();
        repaint();
    }

    public Marble getMarble() {
        if (marble != null) {
            return marble;
        }
        return null;
    }

    public void setMarble(Marble marble) {
        this.marble = marble;
    }

    public class BoardPanel extends JPanel {

        Font fnt1 = new Font("Impact", 2 , 330 );
        Font fnt2 = new Font("Impact", 2 , 70 );

        public BoardPanel() {
            setTitle("Abalone");
            for (Cell cell : board.getCells()) {
                add(cell);
            }
        }

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            // has a turn limit
            g.setColor(Color.black);
            g.setFont(fnt2);
            if (turnLimit != 0) {
                g.drawString("Turn Limit " + turnLimit, 0, 850 );
            } else {
                g.drawString("Turn Limit : No Limit" , 0, 850 );
            }

            // Player2(White) information
            player2Information(g);
            // Player1(Black) information
            player1Information(g);

            g.setColor(new Color(111, 37, 3));
            removeAllMouseListeners();
            Graphics2D g2d = (Graphics2D) g;
            drawHexagon(g2d);
            drawCells(g2d);
            addClickListener();
        }
        
        private void player2Information(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(910, 0, 850 , 450 );
            g.setColor(Color.black);
            g.setFont(fnt1);
            g.drawString("" + getScore(MarbleType.BLACK), 1050, 310 );
            g.setFont(fnt2);
            g.drawString("White", 1300, 110 );
            g.drawString(printTurnTime(sec_white, msec_white), 920,
                    190 );
            g.drawString(
                    "Total  "
                            + getTotalTime(totalSec_white, totalmSec_white),
                    1300, 200 );
            g.drawString("Turns " + board.getNumOfMove(MarbleType.WHITE), 1300,
                    290 );
        }
        private void player1Information(Graphics g) {
            g.setColor(Color.black);
            g.fillRect(910, 400 , 850 , 450 );
            g.setColor(Color.white);
            g.setFont(fnt1);
            g.drawString("" + getScore(MarbleType.WHITE), 1050, 750 );
            g.setFont(fnt2);
            g.drawString("Black", 1300, 550 );
            g.drawString(printTurnTime(sec_black, msec_black), 920,
                    630 );
            g.drawString(
                    "Total  "
                            + getTotalTime(totalSec_black, totalmSec_black),
                    1300, 640 );
            g.drawString("Turns " + board.getNumOfMove(MarbleType.BLACK), 1300,
                    730 );
        }

        /**
         * Draws the Hexagon aka Board.
         * 
         * @param g
         */
        public void drawHexagon(Graphics2D g) {

            Polygon p = new Polygon();
            for (int i = 0; i < 6; i++)
                p.addPoint((int) (440 + 440 * Math.cos(i * 2 * Math.PI / 6)),
                        (int) (410 + 420 * Math.sin(i * 2 * Math.PI / 6)));
            g.setClip(p);
            g.fillPolygon(p);
        }

        /**
         * Draws the Cells and Marbles. Empty Cells (Cells with no marbles) will
         * be displayed as Black circles, and cells that are occupied with a
         * Marble will be displayed as Red or Blue depending on the Marble.
         * 
         * @param g
         */
        public void drawCells(Graphics2D g) {

            DrawingPanel oval;
            int radius = 75;
            for (Cell cell : board.getCells()) {
                int x = cell.getX() * 75;
                int y = (cell.getY() - 1) * 45;
                if (cell.getMarble() == null) {
                    g.setColor(new Color(173, 105, 74));
                } else {
                    g.setColor(cell.getMarble().getColor());
                }
                oval = new DrawingPanel(y, x, radius, radius, cell, board);
                g.fill(oval.getOval());
                panels.add(oval);
            }
        }

        /**
         * Implements the mouseListener to every nessecary drawing.
         */
        public void addClickListener() {
            for (DrawingPanel panel : panels) {
                addMouseListener(panel);
            }
        }

        /**
         * Removes all mouseListeners. This is to prevent multiple listeners in
         * the same cell.
         */
        public void removeAllMouseListeners() {
            if (!panels.isEmpty()) {
                for (DrawingPanel panel : panels) {
                    removeMouseListener(panel);
                }
                panels.clear();
            }
        }

        class DrawingPanel extends JPanel implements MouseListener {

            private Ellipse2D oval;
            private Cell cell;
            private Marble marble;
            private final Board board;
            //private boolean isAgentTurn = false;

            public DrawingPanel(int x, int y, int radius1, int radius2,
                    Cell cell, Board board) {
                oval = new Ellipse2D.Double(x, y, radius1, radius2);
                this.cell = cell;
                this.board = board;
                marble = cell.getMarble();
            }

            public Ellipse2D getOval() {
                return oval;
            }

            public Cell getCell() {
                return cell;
            }

            public Marble getMarble() {
                return marble;
            }

            public void paint() {
                BoardPanel.this.repaint();
                showVictoryWindow();
            }

            public int loadedMarbles() {
                return board.getMarbles().size();
            }

            // Returns true if there are no Marbles stored in the ArrayList from
            // board.
            public boolean isMarbleEmpty() {
                return board.getMarbles().isEmpty();
            }

            // Returns true if the size of the ArrayList is between 0 and 3.
            public boolean checkMarbleValidList() {
                int numberOfMarbles = loadedMarbles();
                return (numberOfMarbles > 0 && numberOfMarbles <= 3);
            }

            // Returns true if the size of the ArrayList is between 0 and 2.
            public boolean checkMarbleArrayList() {
                int numberOfMarbles = loadedMarbles();
                return (numberOfMarbles > 0 && numberOfMarbles < 3);
            }

            // Returns true if the size of the ArrayList is 3. Cannot move more
            // than 3 marbles at a time.
            public boolean isMarbleFull() {
                return (loadedMarbles() == 3);
            }

            // Returns true if the cell currently in scope contains a marble.
            public boolean cellContainsMarble() {
                return (cell.getMarble() != null);
            }

            // Returns true if the cell in scope does NOT contain the same
            // marble contained in the ArrayList.
            public boolean notDuplicateMarble() {
                for (Marble marble : board.getMarbles()) {
                    if (cell.getMarble() == marble)
                        return false;
                }
                return true;
            }

            private boolean checkTurn() {
                if (cell.getMarble().getType() == board.getColourMap()
                        .get(Board.PLAYER_TURN)) {
                    return true;
                } else {
                    return false;
                }
            }

            // check if the third marble selected is in line with the first two
            // in queue
            private boolean validThirdMarble(ArrayList<Marble> marbles,
                    Marble thirdMarble) {
                Marble firstMarble = board.getMarbles().get(0);
                Marble secondMarble = board.getMarbles().get(1);

                int xDiff_12 = secondMarble.getCell().getX()
                        - firstMarble.getCell().getX();
                int yDiff_12 = secondMarble.getCell().getY()
                        - firstMarble.getCell().getY();
                int xDiff_23 = thirdMarble.getCell().getX()
                        - secondMarble.getCell().getX();
                int yDiff_23 = thirdMarble.getCell().getY()
                        - secondMarble.getCell().getY();

                if (xDiff_12 != xDiff_23 || yDiff_12 != yDiff_23) {
                    return false;
                }
                return true;
            }

            // check if a marble is enemy
            private boolean enemyMarble(Marble marble) {
                return board.getColourMap().get(Board.PLAYER_TURN) != marble.getType();
            }

            /**
             * READ ME!!! THIS METHOD IS STILL BROKEN!!!
             * 
             * NOTE: THIS METHOD WILL ONLY BE CALLED IF THERE IS WHETHER 1 OR 2
             * MARBLES IN THE ARRAYLIST FROM BOARD.
             * 
             * If the ArrayList currently contains 1 marble, the method will
             * make sure that the next added marble is a neighbouring friend of
             * the marble currently in the ArrayList.
             */
            public void addMarble() {
                if (loadedMarbles() == 1) {
                    for (Marble marble : board.getNeighbouringFriends(cell)) {
                        if (marble == board.getMarbles().get(0)) {
                            board.addMarbles(cell.getMarble());
                            break;
                        }
                    }
                } else if (loadedMarbles() == 2) {

                    board.addMarbles(cell.getMarble());
                }
                paint();
            }

            // NOTE: THIS IS CALLED ONLY WHEN YOU WANT TO ADD A MARBLE AND THE
            // ARRAYLIST IS EMPTY.
            // Simple adds the marble into the arrayList
            public void addMarbleAndCoordinate() {
                board.addMarbles(cell.getMarble());
                board.setFirstCoordinates(cell.getX(), cell.getY());
                paint();
            }

            public void resetMarbles() {
                for (Marble marble : board.getMarbles()) {
                    marble.setNormalColor();
                }

                board.clearMarbles();
                paint();
            }

            /**
             * checks if the move is an in-line move
             * 
             * @param xDirection
             *            x direction of the move
             * @param yDirection
             *            y direction of the move
             * @return true if the move is in-line, otherwise false
             */
            private boolean isInlineMove(int xDirection, int yDirection) {
                if (board.getMarbles().size() > 1) {
                    Marble firstMarble = board.getMarbles().get(0);
                    Marble secondMarble = board.getMarbles().get(1);
                    int xDiff_12 = firstMarble.getCell().getX()
                            - secondMarble.getCell().getX();
                    int yDiff_12 = firstMarble.getCell().getY()
                            - secondMarble.getCell().getY();

                    // the direction of the second marble form the
                    // first marble is not equal to the
                    // direction of the selected destination from the first
                    // marble, it is not an in-line move.
                    if (xDirection == xDiff_12 && yDirection == yDiff_12) {
                        return true;
                    }
                }
                return false;
            }

            /**
             * checks if the side move is valid
             * 
             * @param xDirection
             *            the x direction of the move
             * @param yDirection
             *            the y direction of the move
             * @return true if the side move is valid, false otherwise
             */
            private boolean validSideMove(int xDirection, int yDirection) {
                for (Marble marble : board.getMarbles()) {
                    Cell originalCell = marble.getCell();
                    int newX = originalCell.getX() + xDirection;
                    int newY = originalCell.getY() + yDirection;

                    // if there is a marble in any one of the destinations of
                    // the selected marbles or a destination is a null cell, it
                    // is not a valid move. return false.
                    if (board.getCellAt(newX, newY) == null
                            || board.getCellAt(newX, newY).containsMarble()) {
                        return false;
                    }
                }
                return true;
            }

            /**
             * checks for sumito situation
             * 
             * @param friends
             *            friend marbles in queue to be moved
             * @param enemies
             *            enemies marbles in queue to be moved
             * @return true if there are more friends than enemies, otherwise
             *         false
             */
            public boolean sumito(ArrayList<Marble> friends,
                    ArrayList<Marble> enemies) {

                if (friends.size() > enemies.size()) {
                    return true;
                }
                return false;
            }

            public ArrayList<Marble> mergeLists(ArrayList<Marble> list1,
                    ArrayList<Marble> list2) {
                ArrayList<Marble> list3 = new ArrayList<Marble>();
                for (Marble marble : list1) {
                    list3.add(marble);
                }
                for (Marble marble : list2) {
                    list3.add(marble);
                }
                return list3;
            }

            public void move(ArrayList<Marble> marbles, Direction direction,
                    Log log) {
                
                //System.out.println("what to move: " + board.marblesToString(marbles));

                int xDirection = board.getMoveSets().get(direction).x;
                int yDirection = board.getMoveSets().get(direction).y;

                for (Marble marble : marbles) {
                    int x = marble.getCell().getX();
                    int y = marble.getCell().getY();
                    int newX = marble.getCell().getX() + xDirection;
                    int newY = marble.getCell().getY() + yDirection;

                    
                    if (board.getCellAt(newX, newY) != null) {
                        log.addMove(marble, direction);

                        marble.getCell().setMarble(null);
                        board.getCellAt(x, y).setMarble(null);
                        
                        
                        marble.setCell(board.getCellAt(newX, newY));
                        board.getCellAt(newX, newY).setMarble(marble);
                        //board.getCellAt(x, y).setMarble(null);
                        marble.setNormalColor();
                    } else {
                        log.addEnemyKilled(marble);
                        marble.setCell(null);
                    }
                    System.out.println();
                }
                
                //System.out.println(log.getText());
                log.addToLog();
            }

            /**
             * performs an in-line move.
             * 
             * @param friends
             *            friend marbles to move
             * @param enemies
             *            enemy marbles to move
             * @param direction
             *            direction of the move
             * @return true if move is executed, false if not
             */
            public boolean inlineMove(ArrayList<Marble> friends,
                    ArrayList<Marble> enemies, Direction direction, Log log) {
                boolean moved = false;
                Collections.reverse(enemies);
                ArrayList<Marble> marblesToMove = mergeLists(enemies, friends);

                // if there are enemies involved
                if (enemies.size() > 0) {
                    Cell lastEnemyCell = enemies.get(0)
                            .getCell();
                    // get the destination cell of the enemy
                    Cell nextCell = board.getDestinationCell(lastEnemyCell,
                            direction);
                    // if the destination cell is null or is not occupied by
                    // marble
                    if (nextCell == null || !nextCell.containsMarble()) {
                    	System.out.println(Agent.marblesToString(marblesToMove)+ " " +direction.toString());
                        move(marblesToMove, direction, log);
                        moved = true;
                    }
                } else {
                    // no enemy
                    move(marblesToMove, direction, log);
                    moved = true;
                }

                return moved;
            }

            /**
             * Moves the marble to an empty cell.
             */
            public void moveMarbles() {
                
                Log log = new Log(board);
                board.setSecondCoordinates(cell.getX(), cell.getY());
                int x = board.getSecondCoordinates().x
                        - board.getFirstCoordinates().x;
                int y = board.getSecondCoordinates().y
                        - board.getFirstCoordinates().y;

                Direction direction = null;
                for (HashMap.Entry<Direction, Point> entry : board.getMoveSets()
                        .entrySet()) {

                    if (entry.getValue().x == x && entry.getValue().y == y) {
                        direction = entry.getKey();
                        // if enemies
                        if (isInlineMove(x, y)) {
                            // get friends
                            ArrayList<Marble> friends = board.getMarbles();
                            Marble firstFriend = friends.get(0);
                            // use last friend to find enemies
                            ArrayList<Marble> enemies = board
                                    .findInlineEnemies(firstFriend, direction);

                            // compare friends size and enemies size for sumito
                            if (sumito(friends, enemies)) {
                                // if the move is not executed, reset selected
                                // marbles
                                if (!inlineMove(friends, enemies, direction,
                                        log)) {
                                    resetMarbles();
                                } else {
                                    board.clearMarbles();
                                    board.setNumOfMove();
                                    
                                    if (board.getColourMap().get(Board.PLAYER_TURN) == MarbleType.BLACK) {
                                        totalSec_black += sec_black;
                                        totalmSec_black += msec_black;
                                    } else {
                                        totalSec_white += sec_white;
                                        totalmSec_white += msec_white;
                                    }
                                    
                                    Board.PLAYER_TURN = Board.OPPONENT_MAP.get(Board.PLAYER_TURN);
                                    turnOver = true;
                                    if(agent1 != null && agent2 == null) {
                                        pvcAgentThread = new Thread(new Runnable() {
                                            public void run() {
                                                agent1.agentMove();
                                            
                                            }
                                        });
                                        pvcAgentThread.start();
                                    }
                                    break;
                                }
                            }

                        } else {
                            // if not an in-line move, check if its valid side
                            // move
                            if (validSideMove(x, y)) {
                               
                                move(board.getMarbles(), direction, log);
                                
                                board.clearMarbles();
                                board.setNumOfMove();
                                if (board.getColourMap().get(Board.PLAYER_TURN) == MarbleType.BLACK) {
                                    totalSec_black += sec_black;
                                    totalmSec_black += msec_black;
                                } else {
                                    totalSec_white += sec_white;
                                    totalmSec_white += msec_white;
                                }
                                
                                Board.PLAYER_TURN = Board.OPPONENT_MAP.get(Board.PLAYER_TURN);
                                
                                turnOver = true;
                                if(agent1 != null && agent2 == null) {
                                    pvcAgentThread = new Thread(new Runnable() {
                                        public void run() {
                                            agent1.agentMove();
                                        
                                        }
                                    });
                                    pvcAgentThread.start();
                                }
                                break;
                            } else {
                                resetMarbles();
                            }
                        }
                    }
                }
                // if (direction == null) {
                // return;
                // } else {
                // board.getMarbles().get(0).move(board.getMarbles(),
                // direction);
                // board.clearMarbles();
                // }
                
                paint();
                revalidate();
                repaint();
            }
            
                
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if(Board.PLAYER_TURN == TURN.PLAYER || Board.PLAYER_TURN == TURN.PLAYER1 || Board.PLAYER_TURN == TURN.PLAYER2  ) {
                    
                    if ((e.getButton() == 1) && oval.contains(e.getX(), e.getY())) {
                        if (cellContainsMarble()
                                && !enemyMarble(cell.getMarble())) {
                            if (checkTurn()) {
                                if (isMarbleEmpty())
                                    addMarbleAndCoordinate();
                                else if (checkMarbleArrayList()
                                        && notDuplicateMarble()) {
                                    // if there are two already selected, make sure
                                    // the third is in-line with the other two
                                    if (board.getMarbles().size() == 2) {
                                        if (!validThirdMarble(board.getMarbles(),
                                                cell.getMarble())) {
                                            resetMarbles();
                                        }
                                    }
                                    addMarble();
                                } else if (!notDuplicateMarble()
                                        && checkMarbleValidList()) {
                                    resetMarbles();
                                }
                            }
                        } else {
                            if (checkMarbleValidList()) {
                                moveMarbles();
                                
                                
                                
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                /*System.out.println("released");
                if(isAgentTurn) {
                    agent1.agentMove();
                    isAgentTurn = false;
                }*/

            }
        }
    }

    
    /**
     * Reset the board This method is called in KeyInput Class when users clicks
     * Reset button
     */
    public void setBoardToNew() {
        board = new Board();
        Board.PLAYER_TURN = TURN_SETTING;
        board.initializeColourMap();
        sec_black = 0;
        sec_white = 0;
        msec_black = 0;
        msec_white = 0;
        totalSec_black = 0;
        totalSec_white = 0;
        totalmSec_black = 0;
        totalmSec_white = 0;
        turnOver = true;
        Log.clearLogs();
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Return scores(Number of opponent's marbles dropped out)
     * 
     * 
     * @param type
     * @return
     */
    public int getScore(MarbleType type) {
        int countMarble = 0;

        Cell[] cells = (Cell[]) board.getCells()
                .toArray(new Cell[board.getCells().size()]);
        for (int i = 0; i < cells.length; ++i) {
            if (cells[i].getMarble() != null) {

                if (cells[i].getMarble().getType() == type)
                    ++countMarble;
            }
        }
        return 14 - countMarble;
    }

    public void victoryWindow(MarbleType type) {
        timer.deleteTimer();
        String resultMessage;
        JPanel panel = new JPanel();

        if (type == null) {
            resultMessage = "It's a draw!";
        } else if (type == MarbleType.BLACK) {
            resultMessage = "Black won!";
        } else {
            resultMessage = "White won!";
        }

        JLabel message = new JLabel(resultMessage);
        message.setFont(new Font(null, Font.PLAIN, 20));

        JButton reset = new JButton("Reset");
        JButton exit = new JButton("Exit");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setBoardToNew();
                revalidate();
                repaint();
                Window win = SwingUtilities.getWindowAncestor(reset);
                win.dispose();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(message, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(reset, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weighty = 3.0;
        c.insets = new Insets(10, 20, 0, 0);
        panel.add(exit, c);

        Object[] options = {};
        JOptionPane.showOptionDialog(this, panel, "Vicotry!",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, null);
    }

    public void showVictoryWindow() {
        if (getScore(MarbleType.WHITE) == 6) {
            victoryWindow(MarbleType.BLACK);         
        } else if (getScore(MarbleType.BLACK) == 6) {
            victoryWindow(MarbleType.WHITE);            
        } else {
            // has turn limit
            if (turnLimit != 0) {
                // both players reached turn limit
                if (board.getNumOfMove(MarbleType.BLACK) == board
                        .getNumOfMove(MarbleType.WHITE)) {
                    if (board.getNumOfMove(MarbleType.WHITE) == turnLimit) {
                        // print draw message
                        victoryWindow(null);
                    }
                }
            }
        }
    }

    public void updateTime(int sec, int msec) {
        if (board.getColourMap().get(Board.PLAYER_TURN) == MarbleType.BLACK) {
            sec_black = sec;
            msec_black = msec;
        } else {
            sec_white = sec;
            msec_white = msec;
        }
    }

    public void updateTotalTime() {

        if (board.getColourMap().get(Board.PLAYER_TURN) == MarbleType.BLACK) {
            totalSec_black += sec_black;
            totalmSec_black += msec_black;
        } else {
            totalSec_white += sec_white;
            totalmSec_white += msec_white;
        }
    }

    private String getTotalTime(int sec, int msec) {
        int min = sec / 60;
        sec = sec % 60 + msec / 100;
        msec = msec % 100;
       
        return String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", msec);
    }
    
    /**
     * Print current player's turn time
     * 
     * @param sec updated seceond
     * @param msec updated milisecond
     * @return well formed time string
     */
    private String printTurnTime(int sec, int msec) {
        
        return String.format("%02d",sec) + ":" + String.format("%02d", msec);
        
    }
    public void setAgentThread() {
        pvcAgentThread = new Thread(new Runnable() {
            public void run() {
                agent1.agentMove();
            
            }
        });
    }
    
}
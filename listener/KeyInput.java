package listener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import abalone.Board;
import abalone.gameEnum.MarbleType;
import abalone.gameEnum.STATE;
import abalone.gameEnum.TURN;
import boardFrame.GameFrame;




public class KeyInput extends KeyAdapter{

    private GameFrame frame;
    private static String userInput = "";

    Board board;
    
    /*Thread t =new Thread(new Runnable() {
        public void run() {
                frame.agent1.agentMove();
            
        }
    });*/

    public KeyInput(GameFrame frame, Board board) {

        this.frame = frame;

        this.board = board;

    }

    

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (GameFrame.state == STATE.TIME_SETTING ||GameFrame.state == STATE.TURN_LIMIT_SETTING) {
            char temp = (char) key;
            
            //if the key is pressed from numpad, convert it to proper number
            if(KeyEvent.KEY_LOCATION_NUMPAD == e.getKeyLocation()) {
                temp = (char) (key - 48);
            }
            
            //if the key is digit, add it to userInput string and print it
            if(Character.isDigit(temp)) {
                userInput += temp;
                frame.menu.takeTimeOrTurnlimit(userInput, false);
                frame.repaint();
            }
            
            //when back space is pressed
            if (key == KeyEvent.VK_BACK_SPACE) {
                if(!userInput.equals("")) {
                    userInput = userInput.substring(0, userInput.length() - 1); 
                    frame.menu.takeTimeOrTurnlimit(userInput, true);
                    frame.repaint();
                    
                }
            }
            
            if(key == KeyEvent.VK_ENTER) {
                if(!userInput.equals("")) {
                    if(GameFrame.state == STATE.TIME_SETTING) {
                        MyTimer.time_limit = frame.menu.getUserTyped(); 
                        resetUserInput();
                        GameFrame.state = STATE.TURN_LIMIT_SETTING;
                        frame.init();
                    } else {
                        GameFrame.turnLimit = frame.menu.getUserTyped();
                        resetUserInput();
                        GameFrame.state = STATE.GAME;
                        frame.init();
                        if(frame.agent1 != null && frame.agent2 == null) {
                            if(frame.agent1.getType() == MarbleType.BLACK)
                                
                                frame.pvcAgentThread.start();
                        }
                        if(frame.agent2 != null && frame.agent1 != null) {
                            fightEachOther();
                        }
                    }
                } else if(userInput.equals("0")) {
                    if(GameFrame.state == STATE.TIME_SETTING) {
                        MyTimer.time_limit = Integer.MAX_VALUE; 
                        resetUserInput();
                        GameFrame.state = STATE.TURN_LIMIT_SETTING;
                        frame.init();
                    } else {
                        GameFrame.turnLimit = Integer.MAX_VALUE;
                        System.out.println(GameFrame.turnLimit);
                        resetUserInput();
                        GameFrame.state = STATE.GAME;
                        frame.init();
                        if(frame.agent1 != null && frame.agent2 == null) {
                            if(frame.agent1.getType() == MarbleType.BLACK)
                                
                                frame.pvcAgentThread.start();
                        }
                        if(frame.agent2 != null && frame.agent1 != null) {
                            fightEachOther();
                        }
                    }
                }
            }
        }
        
        /*if (GameFrame.state == STATE.TURN_LIMIT_SETTING) {
            char temp = (char) key;
            
            if(KeyEvent.KEY_LOCATION_NUMPAD == e.getKeyLocation()) {
                temp = (char) (key - 48);
            }
            
            if(Character.isDigit(temp)) {
                userInput += temp;
                frame.menu.takeTimeOrTurnlimit(userInput, false);
                frame.repaint();
            }
            
            if (key == KeyEvent.VK_BACK_SPACE) {
                if(!userInput.equals("")) {
                    userInput = userInput.substring(0, userInput.length() - 1); 
                    frame.menu.takeTimeOrTurnlimit(userInput, true);
                    frame.repaint();
                    
                }
            }
            
            
        }*/

        else if(GameFrame.state == STATE.GAME) {

            

            //When user presses ESC button, menu is appeared

            if(key == KeyEvent.VK_ESCAPE) {
                
                if(Board.PLAYER_TURN != TURN.COMPUTER1 && Board.PLAYER_TURN != TURN.COMPUTER2) {
                    
                    GameFrame.state = STATE.PASUSE;
                    frame.init();
                    if(frame.agentThread != null) {
                        synchronized (frame.agentThread) {
                            try {
                                frame.agentThread.wait();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else if(frame.agent1 != null) {
                        if(frame.pvcAgentThread.isAlive()) {
                            synchronized (frame.pvcAgentThread) {
                                try {
                                    frame.pvcAgentThread.wait();
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                }
                

            }

        }

    }

    
    private void fightEachOther() {
        
        frame.agentThread = new AgentThread(frame);
        frame.agentThread.start();
        
    }
    public static void resetUserInput() {
        userInput = "";
    }

}
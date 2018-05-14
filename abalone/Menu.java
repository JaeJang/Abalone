package abalone;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import abalone.gameEnum.STATE;
import boardFrame.GameFrame;

public class Menu extends JPanel{
    
    public static boolean menuSelected = false;
    
    private String userTyped = "";
    private int textPosition = 0;
    
    
    
    /**
     * Return JPanel which is appeared when the program starts
     * 
     * @return
     */
    public JPanel layoutPanel() {
        
        return new JPanel() {
            public void paintComponent(Graphics g) {
                g.setColor(Color.white);
                g.fillRect(0, 40, 1800,170);
                g.setColor(Color.black);
                g.fillRect(0, 250, 1800,170);
                g.setColor(Color.white);
                g.fillRect(0, 460, 1800,170);
                g.setColor(Color.black);
                g.fillRect(0, 670, 1800, 170);
                
                
                g.setFont(new Font("Impact", 2, 150));
                g.setColor(Color.black);
                g.drawString("Standard", 640, 185);
                g.setColor(Color.white);
                g.drawString("Belgium Daisy", 480, 395);
                g.setColor(Color.black);
                g.drawString("German Daisy", 500, 605);
                g.setColor(Color.white);
                g.drawString("Input File", 600, 815);
                
                
            }
        };
    }
    
    //
    /**
     *When user presses ESC button
     *the game is paused and menu is appeared 
     * 
     * @return
     */
    public JPanel pausePanel() {
        return new JPanel() {
            public void paintComponent(Graphics g) {
                g.setColor(Color.white);
                g.fillRect(0, 40, 1800,170);
                g.setColor(Color.black);
                g.fillRect(0, 355, 1800,170);
                g.setColor(Color.white);
                g.fillRect(0, 670, 1800,170);

                g.setFont(new Font("Impact", 2, 150));
                g.setColor(Color.black);
                g.drawString("Reset", 750, 185);
                g.setColor(Color.white);
                g.drawString("Main Menu", 600, 500);
                g.setColor(Color.black);
                g.drawString("Resume",670, 815);
            }
        };
    }
    
    @SuppressWarnings("serial")
    public JPanel timeSettingPanel() {
        return new JPanel() {
            public void paintComponent(Graphics g) {
                
                g.setColor(Color.white);
                g.fillRect(0, 40, 1800,170);
                g.setColor(Color.black);
                g.fillRect(0, 355, 1800,170);
                g.setColor(Color.white);
                g.fillRect(0, 670, 1800,170);
                
                g.setColor(Color.black);
                g.setFont(new Font("Impact", 2, 150));
                g.drawString("Insert time limit", 350, 185);
                g.setColor(Color.white);
                g.drawString(userTyped, 600, 500);
                g.drawString("Seconds",  700 + textPosition, 500);
                g.setColor(Color.black);
                if (!userTyped.equals("")){
                    g.drawString("NEXT", 700, 815);
                    
                }
            }
        };
    }
    
    public JPanel gameModeSetPanel() {
        return new JPanel() {
            public void paintComponent(Graphics g) {
                g.setColor(Color.white);
                g.fillRect(0, 40, 1800,170);
                g.setColor(Color.black);
                g.fillRect(0, 355, 1800,170);
                g.setColor(Color.white);
                g.fillRect(0, 670, 1800,170);
                
                g.setColor(Color.black);
                g.setFont(new Font("Impact", 2, 150));
                g.drawString("Player vs Player", 395, 185);
                g.setColor(Color.white);
                g.drawString("Player vs Computer", 300, 500);
                g.setColor(Color.black);
                g.drawString("Computer vs Computer", 200, 815);
            }
        };
    }
    
    public JPanel teamSettingPanel() {
        return new JPanel() {
            public void paintComponent(Graphics g) {
                g.setColor(Color.white);
                g.fillRect(0, 40, 1800,170);
                g.setColor(Color.black);
                g.fillRect(0, 355, 1800,170);
                g.setColor(Color.black);
                g.fillRect(0, 670, 1800,170);
                
                g.setColor(Color.black);
                g.setFont(new Font("Impact", 2, 150));
                
                if(GameFrame.state == STATE.TEAM_SETTING_PVP) {
                    g.drawString("SELECT BLACK PLAYER", 260, 185);
                    g.setColor(Color.white);
                    g.drawString("Player 1 ", 650, 500);
                    g.drawString("Player 2", 650, 815);
                    
                } else if(GameFrame.state == STATE.TEAM_SETTING_PVC) {
                    g.drawString("SELECT BLACK PLAYER", 260, 185);
                    g.setColor(Color.white);
                    g.drawString("Player", 660, 500);
                    g.drawString("Computer", 530, 815);
                    
                }else if(GameFrame.state == STATE.TEAM_SETTING_CVC) {
                    g.drawString("SELECT BLACK PLAYER", 260, 185);
                    g.setColor(Color.white);
                    g.drawString("Computer 1", 500, 500);
                    g.drawString("Computer 2", 500, 815);
                }
            }
        };
    }
   
    public JPanel turnLimitSetting() {
        return new JPanel() {
            public void paintComponent(Graphics g) {
                g.setColor(Color.white);
                g.fillRect(0, 40, 1800,170);
                g.setColor(Color.black);
                g.fillRect(0, 355, 1800,170);
                g.setColor(Color.white);
                g.fillRect(0, 670, 1800,170);
                
                g.setColor(Color.black);
                g.setFont(new Font("Impact", 2, 150));
                g.drawString("Insert turn limit", 350, 185);
                g.setColor(Color.white);
                g.drawString(userTyped, 600, 500);
                g.drawString("Turns",  700 + textPosition, 500);
                g.setColor(Color.black);
                if (!userTyped.equals("")){
                    g.drawString("GAME START", 500, 815);
                    
                }
            }
        };
    }
    
    public void drawScreenByState() {
        
    }
    
    public void takeTimeOrTurnlimit(String userTyped, boolean backSpacePressed) {
        this.userTyped = userTyped;
        if(backSpacePressed) {
            textPosition -= 50;
        } else {
            textPosition += 50;
        }
    }
    
    /**
     * convert time limit or turn limit user typed to integer
     * and return it
     * if user typed nothing, return -1  
     * 
     * @return
     */
    public int getUserTyped() {
        if(!userTyped.equals("")) {
            int temp = Integer.parseInt(userTyped);
            textPosition = 0;
            userTyped = "";
            return  temp;
            
        } else {
            return -1;
        }
    }
    
    
    
    
}

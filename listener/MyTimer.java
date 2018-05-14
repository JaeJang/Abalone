package listener;

import java.awt.Graphics;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import abalone.Board;
import abalone.Log;
import abalone.Marble;
import abalone.gameEnum.STATE;
import abalone.gameEnum.TURN;
import boardFrame.GameFrame;

public class MyTimer extends TimerTask {
    
    public static int time_limit = 0;
    
    /**
     * Maximum minutes.
     */
    private static final int MAX_MIN = 60;

    /**
     * Maximum seconds.
     */
    private static final double MAX_SEC = 60.0;

    /**
     * Increment of times.
     */
    private static final int INCRE = 1;

    /**
     * Delay of timer class.
     */
    private static final int DELAY = 10;
    
    /**
     * Timer object.
     */
    private Timer time;
    private GameFrame frame;
    private int msec;
    private int sec;
    
    private int black_msec = 0;
    private int white_msec = 0;
    private int black_sec = 0;
    private int white_sec = 0;
    private int black_min = 0;
    private int white_min = 0;
    
    public MyTimer(GameFrame frame) {
        msec = 0;
        sec = 0;
        this.frame = frame;
    }
    
    @Override
    public void run() {
        if(GameFrame.state == STATE.GAME) {
            msec += INCRE;
            if(time_limit != 0) {
                
                if(sec >= time_limit) {
                    frame.updateTotalTime();
                    Log log = new Log(frame.getBoard());
                    //System.out.println(log.getText());
                    log.addToLog();
                    frame.getBoard().setNumOfMove();
                    
                    Board.PLAYER_TURN = frame.getBoard().OPPONENT_MAP.get(Board.PLAYER_TURN);
                    
                    sec = 0;
                    msec = 0;
                    resetMarbles();
                    frame.showVictoryWindow();
                }
            }
            
            if(msec >= 100) {
                msec = 0;
                ++sec;
            }
            
            if(GameFrame.turnOver) {
                msec = 0;
                sec = 0;
                GameFrame.turnOver = false;
            }
            
        
        frame.updateTime(sec, msec);
        frame.repaint();
        }
    }
    
    public void deleteTimer() {
        time.cancel();
        time = null;
        
    }
    public void setTimer() {
        if(time == null) {
            time = new Timer();
            time.schedule(new MyTimer(frame), new Date(), DELAY);
        }
    }
    
    public void resetMarbles() {
        for (Marble marble : frame.getBoard().getMarbles()) {
            marble.setNormalColor();
        }
        frame.getBoard().clearMarbles();
        frame.repaint();
    }
    
    public String getPlayer1ToTal() {
        return black_min + ":" + black_sec + ":" + black_msec;
    }
    
    public String getPlayer2ToTal() {
        return white_min + ":" + white_sec + ":" + white_msec;
    }

}

package listener;

import abalone.gameEnum.MarbleType;
import boardFrame.GameFrame;

public class AgentThread extends Thread{
    
    GameFrame frame;
    
    public AgentThread(GameFrame frame) {
        setDaemon(true);
        this.frame = frame;
    }
    
    @Override
    public void run() {
        
        while(true) {
            frame.agent1.agentMove();
            if(scoreCheck()) {
                return;
            }
            frame.agent2.agentMove();
            if(over()) {
                return;
            }
        }
    }
    public boolean scoreCheck() {
        return frame.getScore(MarbleType.BLACK) >= 6 || frame.getScore(MarbleType.WHITE) >= 6;
    }
    public boolean over() {
        if(GameFrame.turnLimit == 0 && 
                (frame.getScore(MarbleType.BLACK) >= 6 ||frame.getScore(MarbleType.WHITE) >= 6)) {
            return true;
        }
        else if((GameFrame.turnLimit != 0 && 
                (frame.getBoard().getNumOfMove(MarbleType.BLACK) >= GameFrame.turnLimit ||
                frame.getBoard().getNumOfMove(MarbleType.WHITE) >= GameFrame.turnLimit)) 
                ||frame.getScore(MarbleType.BLACK) >= 6 ||frame.getScore(MarbleType.WHITE) >= 6){
            return true;
        }
        return false;
    }
}

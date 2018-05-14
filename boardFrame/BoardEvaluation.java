package boardFrame;

import java.awt.Point;
import java.util.ArrayList;

import abalone.Board;
import abalone.Cell;
import abalone.Marble;
import abalone.gameEnum.MarbleType;

/**
 * @author Kimdoy
 *
 */
public class BoardEvaluation {

    private static final int FIRST_RING_SCORE = 1;
    private static final int SECOND_RING_SCORE = 2;
    private static final int THIRD_RING_SCORE = 4;
    private static final int FOURTH_RING_SCORE = 8;
    private static final int FIFTH_RING_SCORE = 16;
    
    private static final int SCORE_OF_ONE_MARBLE = 100;
    
    private static final int SCORE_OF_ADJACENT_MARBLES = 5;
    
    private static final int MAX_MARBLES = 14;
    
    private static final int MAX_MARBLES_TO_LOSE = 6;
    
    private ArrayList<Marble> blackMarbles = new ArrayList<Marble>();
    private ArrayList<Marble> whiteMarbles = new ArrayList<Marble>();

    private Board board;

    private int totalPoints;
    private int totalWhitePoints;
    private int totalBlackPoints;
    private int marbleWeightWhite;
    private int marbleWeightBlack;
    private int marblePositionWhite;
    private int marblePositionBlack;
    private int marbleCountWhite;
    private int marbleCountBlack;

    public BoardEvaluation(Board board) {
        this.board = board;
        blackMarbles = board.allColoredMarbles(MarbleType.BLACK);
        whiteMarbles = board.allColoredMarbles(MarbleType.WHITE);
        run();
    }
    
    public void run() {
        evaluateMarbleWeightBlack();
        evaluateMarbleWeightWhite();
        evaluatePositionBlack();
        evaluatePositionWhite();
        evaluateBlackMarbleCount();
        evaluateWhiteMarbleCount();
        setTotalBlackPoints();
        setTotalWhitePoints();
    }

    public Board getBoard() {
        return board;
    }
    /**
     * @return the marbleWeightWhite
     */
    public int getMarbleWeightWhite() {
        return marbleWeightWhite;
    }



    /**
     * @return the marblWeightBlack
     */
    public int getMarblWeightBlack() {
        return marbleWeightBlack;
    }



    /**
     * @return the marblePositionWhite
     */
    public int getMarblePositionWhite() {
        return marblePositionWhite;
    }



    /**
     * @return the marblePositionBlack
     */
    public int getMarblePositionBlack() {
        return marblePositionBlack;
    }
    
    /**
     * @return the total black points
     */
    public int getTotalBlackPoints() {
        return totalBlackPoints;
    }
    
    /**
     * @return the total white points
     */
    public int getTotalWhitePoints() {
        return totalWhitePoints;
    }

    /**
     * @return the total points
     */
    public int getTotalPoints() {
        return totalPoints;
    }
    
    /**
     * @return total CURRENT count for black marbles
     */
    public int getBlackMarbleCount() {
        return marbleCountBlack;
    }
    
    /**
     * @return total CURRENT count for white marbles
     */
    public int getWhiteMarbleCount() {
        return marbleCountWhite;
    }
    
    /**
     * @return returns white points - black points
     * This number should be used when evaluating the next turn for WHITE
     */
    public int getWhiteDifference() {
        return totalWhitePoints - totalBlackPoints;
    }
    
    /**
     * @return returns black points - white points
     * This number should be used when evaluating the next turn for BLACK
     */
    public int getBlackDifference() {
        return totalBlackPoints - totalWhitePoints;
    }
   
    /**
     * @param marbleWeightWhite the marbleWeightWhite to set
     */
    public void setMarbleWeightWhite(int marbleWeightWhite) {
        this.marbleWeightWhite = marbleWeightWhite;
    }

    /**
     * @param marblWeightBlack the marblWeightBlack to set
     */
    public void setMarblWeightBlack(int marblWeightBlack) {
        this.marbleWeightBlack = marblWeightBlack;
    }

    /**
     * @param marblePositionWhite the marblePositionWhite to set
     */
    public void setMarblePositionWhite(int marblePositionWhite) {
        this.marblePositionWhite = marblePositionWhite;
    }

    /**
     * @param marblePositionBlack the marblePositionBlack to set
     */
    public void setMarblePositionBlack(int marblePositionBlack) {
        this.marblePositionBlack = marblePositionBlack;
    }
    
    /**
     * @param marbleCountBlack the total black marble count to set
     */
    public void setMarbleCountBlack(int marbleCountBlack) {
        this.marbleCountBlack = marbleCountBlack;
    }
    
    /**
     * @param marbleCountWhite the total white marble count to set
     */
    public void setMarbleCountWhite(int marbleCountWhite) {
        this.marbleCountWhite = marbleCountWhite;
    }
    
    /**
     * sets the total points for black
     */
    public void setTotalBlackPoints() {
        totalBlackPoints = marbleWeightBlack + marblePositionBlack + marbleCountBlack;
    }
    
    
    /**
     * Sets the total points for white
     */
    public void setTotalWhitePoints() {
        totalWhitePoints = marbleWeightWhite + marblePositionWhite + marbleCountWhite;
    }
    
    /**
     * Sets the total points for marbleCount BLACK
     */
    public void evaluateBlackMarbleCount() {
        setMarbleCountBlack(blackMarbles.size() * SCORE_OF_ONE_MARBLE);
    }
    
    /**
     * Sets the total points for marbleCount WHITE
     */
    public void evaluateWhiteMarbleCount() {
        setMarbleCountWhite(whiteMarbles.size() * SCORE_OF_ONE_MARBLE);
    }
    
    /**
     * Evaluates the total marble count score for WHITE marbles
     */
    public void evaluateMarbleWeightWhite() {
        setMarbleWeightWhite(evaluateMarbleWeight(MarbleType.WHITE));
    }
    
    /**
     * Evaluates the total marble count score for BLACK marbles
     */
    public void evaluateMarbleWeightBlack() {
        setMarblePositionBlack(evaluateMarbleWeight(MarbleType.BLACK));
    }

    public boolean gameOver() {
        
        return (deadBlackMarbles() == MAX_MARBLES_TO_LOSE || deadWhiteMarbles() == MAX_MARBLES_TO_LOSE);
    }
    public int deadBlackMarbles() {
        return MAX_MARBLES - marbleCountBlack;
    }
    
    public int deadWhiteMarbles() {
        return MAX_MARBLES - marbleCountWhite;
    }
    /**
     * @param type
     * @return a number that represents the total points awarded for marbles being
     * clumped up.
     */
    public int evaluateMarbleWeight(MarbleType type) {
        
        int totalScore = 0;
        
        if(type == MarbleType.BLACK) {
            for(Marble marble : blackMarbles) {
                Cell cell = marble.getCell();
                totalScore = totalScore + board.getNeighbouringFriends(cell).size();
            }
        } else if(type == MarbleType.WHITE) {
            for(Marble marble : whiteMarbles) {
                Cell cell = marble.getCell();
                totalScore = totalScore + board.getNeighbouringFriends(cell).size();
            }
        }
        totalScore = totalScore * SCORE_OF_ADJACENT_MARBLES;
        return totalScore;
    }

    public void evaluatePositionWhite() {
        
        int totalScore = 0;
        totalScore = evaluateFirstRing(MarbleType.WHITE)
                   + evaluateSecondRing(MarbleType.WHITE)
                   + evaluateThirdRing(MarbleType.WHITE)
                   + evaluateFourthRing(MarbleType.WHITE)
                   + evaluateFifthRing(MarbleType.WHITE);
        setMarblePositionWhite(totalScore);
    }

    public void evaluatePositionBlack() {
        
        int totalScore = 0;
        totalScore = evaluateFirstRing(MarbleType.BLACK)
                   + evaluateSecondRing(MarbleType.BLACK)
                   + evaluateThirdRing(MarbleType.BLACK)
                   + evaluateFourthRing(MarbleType.BLACK)
                   + evaluateFifthRing(MarbleType.BLACK);
       setMarblePositionBlack(totalScore);
    }
    
    public int evaluateFirstRing(MarbleType type) {

        int totalPoints = 0;

        for (Point point : Rings.firstRing()) {
            int x = point.x;
            int y = point.y;
            if (board.getCellAt(x, y).getMarble() == null) {
                continue;
            } else if (board.getCellAt(x, y).getMarble().getType() == type) {
                totalPoints = totalPoints + FIRST_RING_SCORE;
            }
        }
        return totalPoints;
    }
    
    public int evaluateSecondRing(MarbleType type) {

        int totalPoints = 0;

        for (Point point : Rings.secondRing()) {
            int x = point.x;
            int y = point.y;
            if (board.getCellAt(x, y).getMarble() == null) {
                continue;
            } else if (board.getCellAt(x, y).getMarble().getType() == type) {
                totalPoints = totalPoints + SECOND_RING_SCORE;
            }
        }
        return totalPoints;
    }
    
    public int evaluateThirdRing(MarbleType type) {

        int totalPoints = 0;

        for (Point point : Rings.thirdRing()) {
            int x = point.x;
            int y = point.y;
            if (board.getCellAt(x, y).getMarble() == null) {
                continue;
            } else if (board.getCellAt(x, y).getMarble().getType() == type) {
                totalPoints = totalPoints + THIRD_RING_SCORE;
            }
        }
        return totalPoints;
    }
    
    public int evaluateFourthRing(MarbleType type) {

        int totalPoints = 0;

        for (Point point : Rings.fourthRing()) {
            int x = point.x;
            int y = point.y;
            if (board.getCellAt(x, y).getMarble() == null) {
                continue;
            } else if (board.getCellAt(x, y).getMarble().getType() == type) {
                totalPoints = totalPoints + FOURTH_RING_SCORE;
            }
        }
        return totalPoints;
    }
    
    public int evaluateFifthRing(MarbleType type) {
        
        if(board.getCellAt(5, 10).getMarble() == null) {
            return 0;
        } else if(board.getCellAt(5, 10).getMarble().getType() == type) {
            return FIFTH_RING_SCORE;
        }
        return 0;
    }
}

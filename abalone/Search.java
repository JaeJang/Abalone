package abalone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.Multimap;

import abalone.gameEnum.Direction;
import abalone.gameEnum.MarbleType;
import boardFrame.BoardEvaluation;
import boardFrame.GameFrame;

public class Search {

	private GameFrame frame;

	private MarbleType myType, oppType;

	public Search(GameFrame frame, MarbleType myType) {
		this.frame = frame;
		this.myType = myType;
		if (myType == MarbleType.WHITE)
			oppType = MarbleType.BLACK;
		else
			oppType = MarbleType.WHITE;

	}


	public Pair<Integer, Pair<ArrayList<Marble>, Direction>> minValue(Board board, int alpha, int beta, int depth) {

		
		if (depth == 0) {
			BoardEvaluation evaluation = new BoardEvaluation(board);
			if (myType == MarbleType.BLACK) {

				return new Pair<>(evaluation.getBlackDifference(), null);
			} else if (myType == MarbleType.WHITE) {
				return new Pair<>(evaluation.getWhiteDifference(), null);
			}
		}

		Pair<Integer, Pair<ArrayList<Marble>, Direction>> v = new Pair<>();
		v.put(Integer.MAX_VALUE, null);

		Cell[][] originalCell = new Cell[Board.NUMBER_OF_ROWS][Board.NUMBER_OF_COLUMNS];
		cellCopy(board.returnAllCell(), originalCell, board);


		--depth;
		for (Map.Entry<Integer, Pair<ArrayList<Marble>, Direction>> treeMap : getEvaluations(board, "ASCENDING", oppType, myType).entrySet()) {

			Pair<ArrayList<Marble>, Direction> currentPair = new Pair<>(treeMap.getValue().getKey(), treeMap.getValue().getValue());

			resultBoard(currentPair, board);
			Pair<Integer, Pair<ArrayList<Marble>, Direction>> max = maxValue(board, alpha, beta, depth);
			max.setValue(currentPair);
			v = getMin(v, max);

			if (v.getKey() < alpha)
				return v;
			beta = Math.min(beta, v.getKey());

			cellCopy(originalCell, board.returnAllCell(), board);
			

		}

		return v;
	}

	public Pair<Integer, Pair<ArrayList<Marble>, Direction>> maxValue(Board board, int alpha, int beta, int depth) {
		
		if (depth == 0) {
			BoardEvaluation evaluation = new BoardEvaluation(board);
			if (myType == MarbleType.BLACK) {

				return new Pair<>(evaluation.getBlackDifference(), null);
			} else if (myType == MarbleType.WHITE) {
				return new Pair<>(evaluation.getWhiteDifference(), null);
			}
		}

		Pair<Integer, Pair<ArrayList<Marble>, Direction>> v = new Pair<>();
		v.put(Integer.MIN_VALUE, null);

		Cell[][] originalCell = new Cell[Board.NUMBER_OF_ROWS][Board.NUMBER_OF_COLUMNS];
		cellCopy(board.returnAllCell(), originalCell, board);

		--depth;

		for (Map.Entry<Integer, Pair<ArrayList<Marble>, Direction>> treeMap : getEvaluations(board, "DESCENDING", myType, myType).entrySet()) {

			Pair<ArrayList<Marble>, Direction> currentPair = new Pair<>(treeMap.getValue().getKey(), treeMap.getValue().getValue());

			
			resultBoard(currentPair, board);
			Pair<Integer, Pair<ArrayList<Marble>, Direction>> min = minValue(board, alpha, beta, depth);
			min.setValue(currentPair);
			v = getMax(v, min);

			if (v.getKey() > beta)
				return v;
			alpha = Math.max(alpha, v.getKey());

			cellCopy(originalCell, board.returnAllCell(), board);
		}
		return v;
	}

	public Pair<Integer, Pair<ArrayList<Marble>, Direction>> getMax(Pair<Integer, Pair<ArrayList<Marble>, Direction>> v,
			Pair<Integer, Pair<ArrayList<Marble>, Direction>> min) {
		if (min.getKey() > v.getKey()) {
			return min;
		} else
			return v;
	}

	public Pair<Integer, Pair<ArrayList<Marble>, Direction>> getMin(Pair<Integer, Pair<ArrayList<Marble>, Direction>> v,
			Pair<Integer, Pair<ArrayList<Marble>, Direction>> min) {
		if (min.getKey() < v.getKey()) {
			return min;
		} else
			return v;
	}

	public void resultBoard(Pair<ArrayList<Marble>, Direction> currentPair, Board board) {
		int xMove = board.getMoveSets().get(currentPair.getValue()).x;
		int yMove = board.getMoveSets().get(currentPair.getValue()).y;
		if (currentPair.getKey().size() > 1) {
			Marble first = currentPair.getKey().get(0);
			Marble second = currentPair.getKey().get(1);
			int x = board.getMoveSets().get(currentPair.getValue()).x + first.getCell().getX();
			int y = board.getMoveSets().get(currentPair.getValue()).y + first.getCell().getY();
			if (x == second.getCell().getX() && y == second.getCell().getY()) {
				Collections.reverse(currentPair.getKey());
			}
		}
		for (Marble marble : currentPair.getKey()) {
			int x = marble.getCell().getX();
			int y = marble.getCell().getY();
			Marble m = board.getCellAt(x, y).getMarble();
			board.getCellAt(x, y).setMarble(null);
			m.setCell(board.getCellAt(x + xMove, y + yMove));
			board.getCellAt(x + xMove, y + yMove).setMarble(m);
		}
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
	
	public TreeMap<Integer, Pair<ArrayList<Marble>, Direction>> getEvaluations(Board board, String sortOrder, MarbleType type,
            MarbleType typeDifference) {

        TreeMap<Integer, Pair<ArrayList<Marble>, Direction>> evaluations;

        if (sortOrder.equals("ASCENDING")) {
            evaluations = new TreeMap<Integer, Pair<ArrayList<Marble>, Direction>>();
        } else if (sortOrder.equals("DESCENDING")) {
            evaluations = new TreeMap<Integer, Pair<ArrayList<Marble>, Direction>>(Collections.reverseOrder());
        } else {
            evaluations = null;
        }

        Cell[][] originalCell = new Cell[Board.NUMBER_OF_ROWS][Board.NUMBER_OF_COLUMNS];
        cellCopy(board.returnAllCell(), originalCell, board);
        
        Multimap<ArrayList<Marble>, Direction> a = board.allMoves_inMap(type);
        for (Entry<ArrayList<Marble>, Direction> entry : a.entries()) {
            Pair<ArrayList<Marble>, Direction> p = new Pair<>();
            p.put(entry.getKey(), entry.getValue());
            resultBoard(p, board);
            BoardEvaluation boardBeingEvaluated = new BoardEvaluation(board);
            if (typeDifference == MarbleType.BLACK) {
                evaluations.put(boardBeingEvaluated.getBlackDifference(), p);
            } else if (typeDifference == MarbleType.WHITE) {
                evaluations.put(boardBeingEvaluated.getWhiteDifference(), p);
            }
            cellCopy(originalCell, board.returnAllCell(), board);
        }
        return evaluations;
    }
}

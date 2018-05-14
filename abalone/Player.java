package abalone;
import java.util.ArrayList;

public class Player {
    private Board board;
    private ArrayList<Marble> marbles;
    private int killCount;

    public Player(Board board, ArrayList<Marble> marbles) {
        this.board = board;
        this.marbles = marbles;
    }

    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    public int getKillCount() {
        return killCount;
    }

    public void incrementKillCount() {
        killCount++;
    }

//    // @return killedMarbled the enemyKilled as a result of this move
//    public Marble move(Cell origin, Cell destination) {
//        Marble killedMarble = null;
//
//        // make sure params passed in are not null
//        if (origin != null && destination != null) {
//            // make sure there is marble to move
//            if (origin.containMarble()) {
//                // make sure the marble belongs to player
//                if (isOwnMarble(origin.getMarble())) {
//                    int xDiff = destination.getX() - origin.getX();
//                    int yDiff = destination.getY() - origin.getY();
//
//                    if (validDestinationCell(xDiff, yDiff)) {
//                        ArrayList<Marble> friends = findFriends(
//                                origin.getMarble(), xDiff, yDiff);
//                        if (friends.size() == 1) {
//                            moveOne(friends.get(0), destination);
//                        }
//
//                        if (friends.size() == 2 || friends.size() == 3) {
//                            killedMarble = moveMoreThanOne(friends, destination,
//                                    xDiff, yDiff);
//                        }
//                    }
//                }
//            }
//        }
//
//        return killedMarble;
//    }
//
//    public void moveOne(Marble marbleToMove, Cell destination) {
//        // if destination is not null
//        if (destination != null) {
//            // if destination is not occupied
//            if (!destination.containMarble()) {
//                marbleToMove.getCell().setMarble(null);
//                marbleToMove.setCell(destination);
//                destination.setMarble(marbleToMove);
//            }
//        }
//    }

//    public Marble moveMoreThanOne(ArrayList<Marble> friends, Cell destination,
//            int xDiff, int yDiff) {
//        Marble killedMarble = null;
//        ArrayList<Marble> enemies = findEnemies(friends.get(friends.size() - 1),
//                xDiff, yDiff);
//        if (sumito(friends, enemies)) {
//            killedMarble = killEnemy(enemies, xDiff, yDiff);
//            // move enemies if any one by one starting from the one farthest
//            // away
//            for (int i = enemies.size() - 1; i >= 0; i--) {
//
//                Marble m = enemies.get(i);
//                int x = m.getCell().getX() + xDiff;
//                int y = m.getCell().getY() + yDiff;
//                moveOne(m, board.getCell()[x][y]);
//            }
//
//            // move friends one by one starting from the one farthest away
//            for (int i = friends.size() - 1; i >= 0; i--) {
//                Marble m = friends.get(i);
//                int x = m.getCell().getX() + xDiff;
//                int y = m.getCell().getY() + yDiff;
//                moveOne(m, board.getCell()[x][y]);
//            }
//        }
//        return killedMarble;
//    }

    // find adjacent friends in one direction
//    public ArrayList<Marble> findFriends(Marble m, int xDiff, int yDiff) {
//        ArrayList<Marble> friends = new ArrayList<Marble>();
//        int x = m.getCell().getX();
//        int y = m.getCell().getY();
//        Cell cell = m.getCell();
//
//        while (cell != null && cell.getMarble() != null) {
//
//            if (cell.getMarble().getColor().equals(m.getColor())) {
//
//                friends.add(cell.getMarble());
//            } else {
//                break;
//            }
//            y += yDiff;
//            x += xDiff;
//            cell = board.getCell()[x][y];
//        }
//        return friends;
//    }
//
//    // find adjacent enemies in one direction
//    public ArrayList<Marble> findEnemies(Marble m, int xDiff, int yDiff) {
//        ArrayList<Marble> enemies = new ArrayList<Marble>();
//        int x = m.getCell().getX() + xDiff;
//        int y = m.getCell().getY() + yDiff;
//        Cell cell = board.getCell()[x][y];
//
//        while (cell != null && cell.getMarble() != null) {
//
//            if (!(cell.getMarble().getType().equals(m.getType()))) {
//                enemies.add(cell.getMarble());
//            } else {
//                break;
//            }
//            y += yDiff;
//            x += xDiff;
//            cell = board.getCell()[x][y];
//        }
//        return enemies;
//    }
//
//    // make sure destination is one of the six admissible valid destinations
//    public boolean validDestinationCell(int xDiff, int yDiff) {
//        // moving left, return true
//        if (xDiff == 0 && yDiff == -2) {
//            return true;
//        }
//        // moving right, return true
//        if (xDiff == 0 && yDiff == 2) {
//            return true;
//        }
//        // moving to top left, return true
//        if (xDiff == -1 && yDiff == -1) {
//            return true;
//        }
//        // moving to top right, return true
//        if (xDiff == -1 && yDiff == 1) {
//            return true;
//        }
//        // moving to bottom left, return true
//        if (xDiff == 1 && yDiff == -1) {
//            return true;
//        }
//        // moving to bottom right, return true
//        if (xDiff == 1 && yDiff == 1) {
//            return true;
//        }
//        // else, not valid move
//        return false;
//    }
//
//    // check for sumito
//    public boolean sumito(ArrayList<Marble> friends,
//            ArrayList<Marble> enemies) {
//
//        if (friends.size() > enemies.size()) {
//            return true;
//        }
//        return false;
//    }

//    // Remove enemies from enemies list and marble collection if a move will
//    // result in killing an enemy.
//    // @return killedMarble the enemy killed
//    public Marble killEnemy(ArrayList<Marble> enemies, int xDiff, int yDiff) {
//        Marble killedMarble = null;
//        if (enemies.size() != 0) {
//            // System.out.println(enemies.size());
//            Marble lastEnemy = enemies.get(enemies.size() - 1);
//            int newX = lastEnemy.getCell().getX() + xDiff;
//            int newY = lastEnemy.getCell().getY() + yDiff;
//
//            if (board.getCellAt(newX, newY) == null) {
//                enemies.remove(lastEnemy);
//                board.removeMarble(lastEnemy);
//                incrementKillCount();
//                killedMarble = lastEnemy;
//            }
//        }
//        return killedMarble;
//    }
//
//    public boolean isOwnMarble(Marble m) {
//        if (marbles.contains(m)) {
//            return true;
//        }
//        return false;
//    }
}

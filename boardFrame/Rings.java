package boardFrame;

import java.awt.Point;
import java.util.ArrayList;

public class Rings {

    public Rings() {
    }

    public static ArrayList<Point> firstRing() {

        ArrayList<Point> firstRingCoordinates = new ArrayList<Point>();
        firstRingCoordinates.add(new Point(1, 6));
        firstRingCoordinates.add(new Point(1, 8));
        firstRingCoordinates.add(new Point(1, 10));
        firstRingCoordinates.add(new Point(1, 12));
        firstRingCoordinates.add(new Point(1, 14));
        firstRingCoordinates.add(new Point(2, 5));
        firstRingCoordinates.add(new Point(2, 15));
        firstRingCoordinates.add(new Point(3, 4));
        firstRingCoordinates.add(new Point(3, 16));
        firstRingCoordinates.add(new Point(4, 3));
        firstRingCoordinates.add(new Point(4, 17));
        firstRingCoordinates.add(new Point(5, 2));
        firstRingCoordinates.add(new Point(5, 18));
        firstRingCoordinates.add(new Point(6, 3));
        firstRingCoordinates.add(new Point(6, 17));
        firstRingCoordinates.add(new Point(7, 4));
        firstRingCoordinates.add(new Point(7, 16));
        firstRingCoordinates.add(new Point(8, 5));
        firstRingCoordinates.add(new Point(8, 15));
        firstRingCoordinates.add(new Point(9, 6));
        firstRingCoordinates.add(new Point(9, 8));
        firstRingCoordinates.add(new Point(9, 10));
        firstRingCoordinates.add(new Point(9, 12));
        firstRingCoordinates.add(new Point(9, 14));
        return firstRingCoordinates;
    }

    public static ArrayList<Point> secondRing() {

        ArrayList<Point> secondRingCoordinates = new ArrayList<Point>();

        secondRingCoordinates.add(new Point(2, 7));
        secondRingCoordinates.add(new Point(2, 9));
        secondRingCoordinates.add(new Point(2, 11));
        secondRingCoordinates.add(new Point(2, 13));
        secondRingCoordinates.add(new Point(3, 6));
        secondRingCoordinates.add(new Point(3, 14));
        secondRingCoordinates.add(new Point(4, 5));
        secondRingCoordinates.add(new Point(4, 15));
        secondRingCoordinates.add(new Point(5, 4));
        secondRingCoordinates.add(new Point(5, 16));
        secondRingCoordinates.add(new Point(6, 5));
        secondRingCoordinates.add(new Point(6, 15));
        secondRingCoordinates.add(new Point(7, 6));
        secondRingCoordinates.add(new Point(7, 14));
        secondRingCoordinates.add(new Point(8, 7));
        secondRingCoordinates.add(new Point(8, 9));
        secondRingCoordinates.add(new Point(8, 11));
        secondRingCoordinates.add(new Point(8, 13));

        return secondRingCoordinates;
    }

    public static ArrayList<Point> thirdRing() {

        ArrayList<Point> thirdRingCoordinates = new ArrayList<Point>();

        thirdRingCoordinates.add(new Point(3, 8));
        thirdRingCoordinates.add(new Point(3, 10));
        thirdRingCoordinates.add(new Point(3, 12));
        thirdRingCoordinates.add(new Point(4, 7));
        thirdRingCoordinates.add(new Point(4, 13));
        thirdRingCoordinates.add(new Point(5, 6));
        thirdRingCoordinates.add(new Point(5, 14));
        thirdRingCoordinates.add(new Point(6, 7));
        thirdRingCoordinates.add(new Point(6, 13));
        thirdRingCoordinates.add(new Point(7, 8));
        thirdRingCoordinates.add(new Point(7, 10));
        thirdRingCoordinates.add(new Point(7, 12));

        return thirdRingCoordinates;
    }

    public static ArrayList<Point> fourthRing() {

        ArrayList<Point> fourthRingCoordinates = new ArrayList<Point>();

        fourthRingCoordinates.add(new Point(4, 9));
        fourthRingCoordinates.add(new Point(4, 11));
        fourthRingCoordinates.add(new Point(5, 8));
        fourthRingCoordinates.add(new Point(5, 12));
        fourthRingCoordinates.add(new Point(6, 9));
        fourthRingCoordinates.add(new Point(6, 11));

        return fourthRingCoordinates;
    }
    
    public static ArrayList<Point> fifthRing() {

        ArrayList<Point> fifthRingCoordinate = new ArrayList<Point>();

        fifthRingCoordinate.add(new Point(5, 10));

        return fifthRingCoordinate;
    }
}
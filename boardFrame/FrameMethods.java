package boardFrame;

import java.util.TreeMap;

public class FrameMethods {

    public static final TreeMap<Character, Integer> X_POSITION = setXPosition();
    public static final int[] ROWS_A_AND_I = { 6, 8, 10, 12, 14 };
    public static final int[] ROWS_B_AND_H = { 5, 7, 9, 11, 13, 15 };
    public static final int[] ROWS_C_AND_G = { 4, 6, 8, 10, 12, 14, 16 };
    public static final int[] ROWS_D_AND_F = { 3, 5, 7, 9, 11, 13, 15, 17 };
    public static final int[] ROW_E = { 2, 4, 6, 8, 10, 12, 14, 16, 18 };

    public static TreeMap<Character, Integer> setXPosition() {

        TreeMap<Character, Integer> xPos = new TreeMap<Character, Integer>();

        int number = 56;
        for (int i = 65; i <= 73; i++) {
            xPos.put((char) i, (i - number));
            number = number + 2;
        }

        return xPos;
    }

    public static int whichRow(char letter, int number) {
        --number;
        if(letter == 'A') {
            return ROWS_A_AND_I[number];
        } else if(letter == 'I') {
            number -= 4;
            return ROWS_A_AND_I[number];
        }else if(letter == 'B') {
            return ROWS_B_AND_H[number];
        } else if(letter == 'H') {
            number -= 3;
            return ROWS_B_AND_H[number];
        }else if(letter == 'C' ) {
            return ROWS_C_AND_G[number];
        } else if(letter == 'G') {
            number -= 2;
            return ROWS_C_AND_G[number];
        }else if(letter == 'D') {
            return ROWS_D_AND_F[number];
        } else if(letter == 'F') {
            number -= 1;
            return ROWS_D_AND_F[number];
        }else if(letter == 'E') {
            return ROW_E[number];
        }
        return 0;
    }

}
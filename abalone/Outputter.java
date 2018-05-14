package abalone;

public class Outputter {
	String x_value;
	String marble_color;
	String y_value;
	private String x_string_value = " IHGFEDCBA";

	public static final int[] ROWS_A_AND_I = { 6, 8, 10, 12, 14 };
	public static final int[] ROWS_B_AND_H = { 5, 7, 9, 11, 13, 15 };
	public static final int[] ROWS_C_AND_G = { 4, 6, 8, 10, 12, 14, 16 };
	public static final int[] ROWS_D_AND_F = { 3, 5, 7, 9, 11, 13, 15, 17 };
	public static final int[] ROW_E = { 2, 4, 6, 8, 10, 12, 14, 16, 18 };

	Outputter(String x, String y, String marble) {
		this.x_value = x;
		this.y_value = y;
		this.marble_color = marble;
	}

	public String properOutput() {
		String output;

		// properly outputs xvalues
		int xValue = Integer.parseInt(x_value);
		char value = x_string_value.charAt(xValue);

		int someyvalue = Integer.parseInt(y_value);
		int yValue = whichRow(value, someyvalue);

		String newY = "" + yValue;

		char newColor = getColor(marble_color);

		output = value + newY + newColor;

		return output;
	}

	public static int whichRow(char letter, int number) {
		int result;
		if (letter == 'A') {
			result = getIndex(ROWS_A_AND_I, number);
			return ++result;
		} else if (letter == 'I') {
			int start = 5;
			result = getIndex(ROWS_A_AND_I, number);
			return start + result;
		} else if (letter == 'B') {
			result = getIndex(ROWS_B_AND_H, number);
			return  ++result;
		} else if (letter == 'H') {
			int start = 4;
			result = getIndex(ROWS_B_AND_H, number);
			return start + result;
		} else if (letter == 'C') {
			result = getIndex(ROWS_C_AND_G, number);
			return ++result;
		} else if (letter == 'G') {
			int start = 3;
			result = getIndex(ROWS_C_AND_G, number);
			return start + result;
		} else if (letter == 'D') {
			result = getIndex(ROWS_D_AND_F, number);
			return ++result;
		} else if (letter == 'F') {
			int start = 2;
			result = getIndex(ROWS_D_AND_F, number);
			return start + result;
		} else if (letter == 'E') {
			result = getIndex(ROW_E, number);
			return ++result;
		}
		return 0;
	}

	public static int getIndex(final int[] array, final int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				// Value found
				return i;
			}
		}
		// Value not found
		return -1;
	}

	public static char getColor(String marble) {
		if (marble.equals("WHITE"))
			return 'w';
		else
			return 'b';
	}

}
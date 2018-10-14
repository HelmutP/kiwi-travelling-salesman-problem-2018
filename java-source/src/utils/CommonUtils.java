package utils;

import java.util.Random;

public final class CommonUtils {

	public static String[] splitStringByWhitespaces(final String line) {
		return line.split(" ");
	}
	
	public static int getRandomNumberFromXtoY(int from, int to) {
		Random rand = new Random();
		return rand.nextInt(to - from + 1) + from;
	}
}

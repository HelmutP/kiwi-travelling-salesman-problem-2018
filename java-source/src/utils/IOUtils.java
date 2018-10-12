package utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public final class IOUtils {

	public static final String OUTPUT_FOLDER = "../output/";
	public static final String OUTPUT_FILE_NAME = "result-kiwi-challenge.txt";

	public static void saveResult(final String[] lines) 
		throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter(OUTPUT_FOLDER + OUTPUT_FILE_NAME, "UTF-8");
		boolean isFirstLine = true;
		for (String line : lines) {
			if (!isFirstLine) {
				writer.println();
			} else {
				isFirstLine = false;
			}
			writer.print(line);
		}
		writer.close();
	}
}

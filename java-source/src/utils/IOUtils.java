package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public final class IOUtils {

	protected static final String OUTPUT_FOLDER = "../output/";
	protected static final String OUTPUT_FILE_NAME = "result-kiwi-challenge.txt";

	protected static final String TESTS_INPUT_FOLDER = "../test/resources/input/";
	protected static final String TEST_INPUT_FILENAME_PATTERN = "test%s-input.txt";

	public static void saveResult(final String[] lines) throws FileNotFoundException, UnsupportedEncodingException {

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

	public static ArrayList<String> readInput(final String testCaseId) {
		
		ArrayList<String> lines = new ArrayList<String>();
		String filename = getInputFileName(testCaseId);

		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(filename));

			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
			
			return lines;

		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", filename);
			e.printStackTrace();
			return null;
		}
	}

	private static String getInputFileName(String testCaseId) {
		return TESTS_INPUT_FOLDER + String.format(TEST_INPUT_FILENAME_PATTERN, testCaseId);
	}
}

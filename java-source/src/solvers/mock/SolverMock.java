package solvers.mock;

import java.util.ArrayList;

import utils.IOUtils;

public final class SolverMock {

	public static void main(String[] args) throws Exception {
		pretendSomeActivity();

		IOUtils.saveResult(createResultStructure(), 999);
		System.out.println("Output successfuly created!");
	}

	private static void pretendSomeActivity() throws InterruptedException {
		Thread.sleep((long)(Math.random() * 1000));
	}

	private static String[] createResultStructure() {
		ArrayList<String> resultLines = new ArrayList<String>();
		resultLines.add("100");
		resultLines.add("ASD MXT 1 50");
		resultLines.add("MXT SKT 2 20");
		resultLines.add("SKT ASD 3 30");

		String[] stockArr = new String[resultLines.size()];
		return resultLines.toArray(stockArr);
	}
}

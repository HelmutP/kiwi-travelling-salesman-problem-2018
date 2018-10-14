package runners;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import dtos.ResultDto;
import solvers.genetic.GeneticSolver;
import utils.IOUtils;

public class GeneticSolverRunner {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

		GeneticSolver solver = null;

		if (args.length == 1) {
			solver = new GeneticSolver(args[0]);
		} else if (args.length == 0) {
			solver = new GeneticSolver("0");
		} else {
			throw new IllegalArgumentException();
		}

		ResultDto solution = solver.run();
		IOUtils.saveResult(solution.getFormattedOutput());
	}

}

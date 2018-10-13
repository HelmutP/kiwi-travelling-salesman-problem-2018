package runners;

import solvers.genetic.GeneticSolver;

public class GeneticSolverRunner {

	public static void main(String[] args) {

		GeneticSolver solver = null;

		if (args.length == 1) {
			solver = new GeneticSolver(args[0]);
		} else if (args.length == 0) {
			solver = new GeneticSolver("0");
		} else {
			throw new IllegalArgumentException();
		}
	}

}

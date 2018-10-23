package runners;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import runners.arguments.GeneticRunnerArgumentProcessor;
import solvers.genetic.GeneticSolver;

public class GeneticSolverRunner extends BaseRunner{

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		GeneticSolverRunner runner = new GeneticSolverRunner(args);
		runner.run();
	}

	public GeneticSolverRunner(String[] args) {
		super(args);
	}
	
	@Override
	protected void initArgumentsProcessor(String[] args) {
		argumentsProcessor = new GeneticRunnerArgumentProcessor(args);
	}

	@Override
	protected void initSolver() {
		solver = new GeneticSolver(argumentsProcessor.getTestCaseId());
	}

}

package runners;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;

import runners.arguments.GeneticRunnerArgumentProcessor;
import solvers.genetic.GeneticSolver;
import utils.IOUtils;

public class GeneticSolverRunner extends BaseRunner {

	private static final int THREADS_COUNT = 8;

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		HashMap<BaseRunner, Thread> threads = new HashMap<BaseRunner, Thread>();
		
		for (int i = 0; i < THREADS_COUNT; i++) {
			GeneticSolverRunner runner = new GeneticSolverRunner(args);	
			Thread thread = new Thread(runner);
			threads.put(runner, thread);
			threads.get(runner).start();
		}
		for (Entry<BaseRunner, Thread> threadRunnerPair : threads.entrySet()) {
			try {
				threadRunnerPair.getValue().join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		BaseRunner theBestSolutionRunner = null;
		for (Entry<BaseRunner, Thread> threadRunnerPair : threads.entrySet()) {
			if (theBestSolutionRunner == null ||
					threadRunnerPair.getKey().solution.getTotalCost() < theBestSolutionRunner.solution.getTotalCost()) {
				theBestSolutionRunner = threadRunnerPair.getKey();
			}
		}
		
		IOUtils.saveResult(theBestSolutionRunner.solution.getFormattedOutput(), Integer.valueOf(theBestSolutionRunner.getArgumentsProcessor().getTestCaseId()));
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

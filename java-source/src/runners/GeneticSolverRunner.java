package runners;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import runners.arguments.GeneticRunnerArgumentProcessor;
import solvers.base.BaseSolver;
import solvers.genetic.GeneticSolver;
import utils.IOUtils;
import utils.Timer;

public class GeneticSolverRunner extends BaseRunner {

	private static final int THREADS_COUNT = 8;

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		HashMap<BaseRunner, Thread> threads = new HashMap<BaseRunner, Thread>();
		
		Timer.setProgramStartTime(System.currentTimeMillis());

		BaseSolver solverExample = null;
		GeneticSolverRunner runner = null;
		for (int i = 0; i < THREADS_COUNT; i++) {
			if (i == 0) {
				runner = new GeneticSolverRunner(args);
				solverExample = runner.getSolver();
			} else {
				GeneticSolver geneticSolverExample = (GeneticSolver) solverExample;
				runner = new GeneticSolverRunner(args, geneticSolverExample.getRegionsCount(),
						geneticSolverExample.getStartCity(), geneticSolverExample.getStartRegion(),
						geneticSolverExample.getRegions(), geneticSolverExample.getCityRegions(),
						geneticSolverExample.getFlights());
			}
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
			if (theBestSolutionRunner == null || (threadRunnerPair.getKey().solution != null && theBestSolutionRunner.solution != null &&
					threadRunnerPair.getKey().solution.getTotalCost() < theBestSolutionRunner.solution.getTotalCost())) {
				theBestSolutionRunner = threadRunnerPair.getKey();
			}
		}
		
		IOUtils.saveResult(theBestSolutionRunner.solution.getFormattedOutput(), Integer.valueOf(theBestSolutionRunner.getArgumentsProcessor().getTestCaseId()));
		System.out.println("TIME OF EXECUTION " + Timer.getCurrentRunTime() / 1000.0 + " secs.");
	}

	public GeneticSolverRunner(String[] args) {
		super(args);
	}
	
	public GeneticSolverRunner(String[] args, int regionsCount, String startCity, String startRegion, ArrayList<String> regions,
			HashMap<String, String> cityRegions,
			HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
		super(args, regionsCount, startCity, startRegion, regions, cityRegions, flights);
	}

	@Override
	protected void initArgumentsProcessor(String[] args) {
		argumentsProcessor = new GeneticRunnerArgumentProcessor(args);
	}

	@Override
	protected void initSolver() {
		solver = new GeneticSolver(argumentsProcessor.getTestCaseId());
	}

	@Override
	protected void initSolver(int regionsCount, String startCity, String startRegion, ArrayList<String> regions,
			HashMap<String, String> cityRegions,
			HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
		solver = new GeneticSolver(regionsCount, startCity, startRegion, regions, cityRegions, flights);
	}

}

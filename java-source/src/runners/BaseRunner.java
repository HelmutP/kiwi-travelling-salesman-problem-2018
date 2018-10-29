package runners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dtos.ResultDto;
import runners.arguments.BaseRunnerArgumentsProcessor;
import solvers.base.BaseSolver;

public abstract class BaseRunner implements Runnable {

	protected BaseSolver solver;
	protected BaseRunnerArgumentsProcessor argumentsProcessor;
	protected ResultDto solution;

	protected abstract void initArgumentsProcessor(String[] args);
	protected abstract void initSolver();
	protected abstract void initSolver(int regionsCount, String startCity, String startRegion,
			ArrayList<String> regions, HashMap<String, String> cityRegions,
			HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights);

	public BaseRunner(String[] args) {
		initArgumentsProcessor(args);
		initSolver();
	}
	
	public BaseRunner(String[] args, int regionsCount, String startCity, String startRegion, ArrayList<String> regions,
			HashMap<String, String> cityRegions,
			HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
		initArgumentsProcessor(args);
		initSolver(regionsCount, startCity, startRegion, regions, cityRegions, flights);
	}
	
	public void run() {
		solution = solver.run();
	}
	
	public BaseRunnerArgumentsProcessor getArgumentsProcessor() {
		return argumentsProcessor;
	}
	public void setArgumentsProcessor(BaseRunnerArgumentsProcessor argumentsProcessor) {
		this.argumentsProcessor = argumentsProcessor;
	}
	public BaseSolver getSolver() {
		return solver;
	}
	public void setSolver(BaseSolver solver) {
		this.solver = solver;
	}
}

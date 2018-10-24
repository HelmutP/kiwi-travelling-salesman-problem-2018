package runners;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import dtos.ResultDto;
import runners.arguments.BaseRunnerArgumentsProcessor;
import solvers.base.BaseSolver;
import utils.IOUtils;

public abstract class BaseRunner implements Runnable {

	protected BaseSolver solver;
	protected BaseRunnerArgumentsProcessor argumentsProcessor;
	protected ResultDto solution;
	
	protected abstract void initArgumentsProcessor(String[] args);
	protected abstract void initSolver();
		
	public BaseRunner(String[] args) {
		initArgumentsProcessor(args);
		initSolver();
	}
	
	public void run() {
		solution = solver.run();
	}
}

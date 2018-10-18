package runners;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import dtos.ResultDto;
import runners.arguments.BaseRunnerArgumentsProcessor;
import solvers.base.BaseSolver;
import utils.IOUtils;

public abstract class BaseRunner 	{

	protected BaseSolver solver;
	protected BaseRunnerArgumentsProcessor argumentsProcessor;

	protected abstract void initArgumentsProcessor(String[] args);
	protected abstract void initSolver();
		
	public BaseRunner(String[] args) {
		initArgumentsProcessor(args);
		initSolver();
	}
	
	protected void run() throws FileNotFoundException, UnsupportedEncodingException {
		ResultDto solution = solver.run();
		IOUtils.saveResult(solution.getFormattedOutput());
	}
}

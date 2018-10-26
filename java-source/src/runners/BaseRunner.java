package runners;

import dtos.ResultDto;
import runners.arguments.BaseRunnerArgumentsProcessor;
import solvers.base.BaseSolver;

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
	
	public BaseRunnerArgumentsProcessor getArgumentsProcessor() {
		return argumentsProcessor;
	}
	public void setArgumentsProcessor(BaseRunnerArgumentsProcessor argumentsProcessor) {
		this.argumentsProcessor = argumentsProcessor;
	}
}

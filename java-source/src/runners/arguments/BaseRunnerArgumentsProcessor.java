package runners.arguments;

public abstract class BaseRunnerArgumentsProcessor {

	protected String[] arguments;

	public BaseRunnerArgumentsProcessor(String[] args) {
		super();
		arguments = args;
	}

	public String getTestCaseId() {
		if (arguments.length == 1) {
			return arguments[0];
		} else if (arguments.length == 0) {
			return "0";
		} else {
			throw new IllegalArgumentException();
		}
	}

	public abstract void processSolverSpecificArgs();
}

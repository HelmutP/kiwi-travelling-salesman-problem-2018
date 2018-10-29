package src.runners;

import src.runners.arguments.SARunnerArgumentProcessor;
import src.solvers.sa.SASolver;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import runners.BaseRunner;

public class SASolverRunner extends BaseRunner {
    private static final int THREADS_COUNT = 8;

    public SASolverRunner(String[] args) {
        super(args);
    }
    public SASolverRunner(String[] args, int regionsCount, String startCity, String startRegion, ArrayList<String> regions,
                               HashMap<String, String> cityRegions,
                               HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
        super(args, regionsCount, startCity, startRegion, regions, cityRegions, flights);
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        HashMap<BaseRunner, Thread> threads = new HashMap<BaseRunner, Thread>();

        utils.Timer.setProgramStartTime(System.currentTimeMillis());

        solvers.base.BaseSolver solverExample = null;
        SASolverRunner runner = null;
        for (int i = 0; i < THREADS_COUNT; i++) {
            if (i == 0) {
                runner = new SASolverRunner(args);
                solverExample = runner.getSolver();
            } else {
                SASolver SASolverExample = (SASolver) solverExample;
                runner = new SASolverRunner(args, SASolverExample.getRegionsCount(),
                        SASolverExample.getStartCity(), SASolverExample.getStartRegion(),
                        SASolverExample.getRegions(), SASolverExample.getCityRegions(),
                        SASolverExample.getFlights());
            }
            Thread thread = new Thread(runner);
            threads.put(runner, thread);
            threads.get(runner).start();
        }

        for (Map.Entry<BaseRunner, Thread> threadRunnerPair : threads.entrySet()) {
            try {
                threadRunnerPair.getValue().join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        BaseRunner theBestSolutionRunner = null;
        for (Map.Entry<BaseRunner, Thread> threadRunnerPair : threads.entrySet()) {
            if (theBestSolutionRunner == null || (threadRunnerPair.getKey().solution != null && theBestSolutionRunner.solution != null &&
                    threadRunnerPair.getKey().solution.getTotalCost() < theBestSolutionRunner.solution.getTotalCost())) {
                theBestSolutionRunner = threadRunnerPair.getKey();
            }
        }

        utils.IOUtils.saveResult(theBestSolutionRunner.solution.getFormattedOutput(), Integer.valueOf(theBestSolutionRunner.getArgumentsProcessor().getTestCaseId()));
        System.out.println("TIME OF EXECUTION " + utils.Timer.getCurrentRunTime() / 1000.0 + " secs.");

    }

    @Override
    protected void initArgumentsProcessor(String[] args) {
        argumentsProcessor = new SARunnerArgumentProcessor(args);
    }

    @Override
    protected void initSolver() {
        solver = new SASolver(argumentsProcessor.getTestCaseId());
    }

    @Override
    protected void initSolver(int regionsCount, String startCity, String startRegion, ArrayList<String> regions, HashMap<String, String> cityRegions, HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
        solver = new SASolver(argumentsProcessor.getTestCaseId());
    }
}

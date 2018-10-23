package solvers.genetic;

import java.util.ArrayList;

import dtos.ResultDto;
import solvers.base.BaseSolver;

public class GeneticSolver extends BaseSolver {
	
	private static final int POPULATION_SIZE = 20;
	private static final double MUTATION_RATE = 0.25;
	private static final double ELIMINATION_RATE = 0.1;
	private static final double TIME_LIMIT_SEC = 10;

	public GeneticSolver(String testCaseId) {
		super(testCaseId);
	}

	@Override
	public ResultDto run() {
		System.out.println("LOOKING FOR AWESOME SOLUTION USING EVOLUTION HAHA ...");

		ArrayList<String> regionsToVisit = initRegionsToVisit();
		ArrayList<ResultDto> population = createPopulation(regionsToVisit);
		ResultDto result = getTheBestSolution(population);
		
		System.out.println("DONE! THE STRONGEST IS THE WINNER!");
		return result;
	}

	private ResultDto getTheBestSolution(ArrayList<ResultDto> population) {
		ResultDto tempTheBestSolution = null;
		int tempTheBestSolutionPrice = 0;

		for (ResultDto result : population) {
			int currentSolutionPrice = result.getTotalCost();
			if (tempTheBestSolution == null || tempTheBestSolutionPrice > currentSolutionPrice) {
				tempTheBestSolution = result;
				tempTheBestSolutionPrice = currentSolutionPrice;
			}
		}
		return tempTheBestSolution;
	}

	private ArrayList<ResultDto> createPopulation(ArrayList<String> regionsToVisit) {
		ArrayList<ResultDto> population = new ArrayList<ResultDto>();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population.add(this.createPseudoRandomSolution(startCity, regionsToVisit, 1));	
		}
		return population;
	}

	private ArrayList<String> initRegionsToVisit() {
		ArrayList<String> regionsToVisit = new ArrayList<String>(regions);
		regionsToVisit.remove(startRegion);
		return regionsToVisit;
	}
}

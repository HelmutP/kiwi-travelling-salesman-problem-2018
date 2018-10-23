package solvers.genetic;

import java.util.ArrayList;
import java.util.List;

import dtos.ResultDto;
import solvers.base.BaseSolver;

public class GeneticSolver extends BaseSolver {
	
	private static final int POPULATION_SIZE = 20;
	private static final double MUTATION_RATE = 0.25;
	private static final double ELIMINATION_RATE = 0.1;
	private static final double TIME_LIMIT_MILISEC = 600000;
	private static final int IMPROVEMENT_CONVERGENCE_LIMIT = 10;

	private List<Double> bestPricesOfPopulations = new ArrayList<Double>();

	public GeneticSolver(String testCaseId) {
		super(testCaseId);
	}

	@Override
	public ResultDto run() {
		System.out.println("LOOKING FOR AWESOME SOLUTION USING EVOLUTION HAHA ...");

		ArrayList<String> regionsToVisit = initRegionsToVisit();
		ArrayList<ResultDto> population = createPopulation(regionsToVisit);
		
		boolean populationImproving = true;
		long startTime = System.currentTimeMillis();

		while(populationImproving || ((System.currentTimeMillis() - startTime)) > TIME_LIMIT_MILISEC) {

			// do mutations
			// eliminate weakest
			// add new replacement for weakest
			
			ResultDto tempBestResult = getTheBestSolution(population);
			populationImproving = isPopulationStillImproving(tempBestResult.getTotalCost());
		}

		ResultDto result = getTheBestSolution(population);
		
		System.out.println("DONE! THE STRONGEST IS THE WINNER!");
		return result;
	}

	private boolean isPopulationStillImproving(double currentAveragePopulationPrice) {
		bestPricesOfPopulations.add(currentAveragePopulationPrice);

		if (bestPricesOfPopulations.size() > IMPROVEMENT_CONVERGENCE_LIMIT) {
			List<Double> lastNBestPrices = bestPricesOfPopulations.subList(
					Math.max(bestPricesOfPopulations.size() - IMPROVEMENT_CONVERGENCE_LIMIT, 0),
					bestPricesOfPopulations.size());

			for (Double bestPrice : lastNBestPrices) {
				if (bestPrice.floatValue() != bestPrice.floatValue()) {
					return true;
				}
			}
			return false;
		}
		return true;
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

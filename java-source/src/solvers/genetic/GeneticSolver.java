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
	private static final int IMPROVEMENT_CONVERGENCE_LIMIT = 1000;

	private ResultDto theBestSolution = null;
	private ArrayList<Integer> weakestGuysIndexes = null;
	private ArrayList<String> regionsToVisit = null;
	
	private List<Double> bestPricesOfPopulations = new ArrayList<Double>();

	public GeneticSolver(String testCaseId) {
		super(testCaseId);
	}

	@Override
	public ResultDto run() {
		System.out.println("LOOKING FOR AWESOME SOLUTION USING EVOLUTION HAHA ...");

		regionsToVisit = initRegionsToVisit();
		ArrayList<ResultDto> population = createPopulation(regionsToVisit);
		
		boolean populationImproving = true;
		long startTime = System.currentTimeMillis();
		int generationCounter = 1;
		
		while(populationImproving && ((System.currentTimeMillis() - startTime)) < TIME_LIMIT_MILISEC) {
			System.out.println("CREATING GENERATION " + generationCounter + "...");
			
			evaluatePopulation(population);
			// do mutations
			population = repopulateWeakest(population);
			// eliminate weakest
			// add new replacement for weakest
			populationImproving = isPopulationStillImproving(theBestSolution.getTotalCost());

			System.out.println("GENERATION " + generationCounter + " IS IMPROVED: " + populationImproving + " AND THE BEST SOLUTION HAS PRICE: " + theBestSolution.getTotalCost()+ ".");
			generationCounter++;
		}

		System.out.println("DONE! THE STRONGEST IS THE WINNER!");
		return theBestSolution;
	}

	private ArrayList<ResultDto> repopulateWeakest(ArrayList<ResultDto> population) {
		ArrayList<ResultDto> repopulation = new ArrayList<ResultDto>();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			if (!weakestGuysIndexes.contains(i)) {
				repopulation.add(population.get(i));
			}
		}
		for (Integer weakGuyindex : weakestGuysIndexes) {
			repopulation.add(this.createPseudoRandomSolution(
					repopulation.get(0).getFlights().get(0).getDepartureAirport(), regionsToVisit, 1));
		}
		return repopulation;
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

	private void evaluatePopulation(ArrayList<ResultDto> population) {
		
		ResultDto tempTheBestSolution = null;
		int tempTheBestSolutionPrice = 0;
		ArrayList <Integer>tempWeakestGuysIndexes = new ArrayList<Integer>();
		Integer eliminatedGuysCount = Integer.valueOf((int) (POPULATION_SIZE * ELIMINATION_RATE));

		for (int i = 0; i < POPULATION_SIZE; i++) {
			ResultDto result = population.get(i);
			int currentSolutionPrice = result.getTotalCost();

			if (tempTheBestSolution == null || tempTheBestSolutionPrice > currentSolutionPrice) {
				tempTheBestSolution = result;
				tempTheBestSolutionPrice = currentSolutionPrice;
			}
			
			if (tempWeakestGuysIndexes.size() < eliminatedGuysCount) {
				tempWeakestGuysIndexes.add(i);
			} else {
				for (int j = 0; j < tempWeakestGuysIndexes.size(); j++) {
					if (population.get(tempWeakestGuysIndexes.get(j)).getTotalCost() < result.getTotalCost()) {
						tempWeakestGuysIndexes.remove(tempWeakestGuysIndexes.get(j));
						tempWeakestGuysIndexes.add(i);
					}
				}
			}
		}
		
		this.weakestGuysIndexes = tempWeakestGuysIndexes;
		this.theBestSolution = tempTheBestSolution;
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

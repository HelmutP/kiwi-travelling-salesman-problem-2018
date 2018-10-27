package solvers.genetic;

import java.util.ArrayList;
import java.util.List;

import dtos.FlightDto;
import dtos.ResultDto;
import solvers.base.BaseSolver;
import utils.CommonUtils;

public class GeneticSolver extends BaseSolver {
	
	private static final int POPULATION_SIZE = 50;
	private static final double MUTATION_RATE = 0.25;
	private static final double ELIMINATION_RATE = 0.1;
	private static final double TIME_LIMIT_MILISEC = 60000;
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
		
		while(populationImproving && (System.currentTimeMillis() - startTime) < TIME_LIMIT_MILISEC) {
			System.out.println("CREATING GENERATION " + generationCounter + "...");
			
			evaluatePopulation(population);
			population = mutate(population);
			population = repopulateWeakest(population);
			populationImproving = isPopulationStillImproving(theBestSolution.getTotalCost());

			System.out.println("GENERATION " + generationCounter + " IS IMPROVED: " + populationImproving + " AND THE BEST SOLUTION HAS PRICE: " + theBestSolution.getTotalCost()+ ".");
			generationCounter++;
		}

		System.out.println("DONE! THE STRONGEST IS THE WINNER!");
		return theBestSolution;
	}

	private ArrayList<ResultDto> mutate(ArrayList<ResultDto> population) {
		ArrayList<Integer> elementsToBeMutated = findElementsToBeMutated(false);
		ArrayList<ResultDto> mutatedElements = new ArrayList<ResultDto>();
		
		for (int i = 0; i < elementsToBeMutated.size(); i++) {
			ResultDto mutatedElement = new ResultDto(
					Integer.valueOf(population.get(elementsToBeMutated.get(i)).getTotalCost().intValue()),
					new ArrayList<FlightDto>(population.get(elementsToBeMutated.get(i)).getFlights()));
			mutatedElement = stopJourneySooner(mutatedElement);
			String restartDepartureAirport = 
					mutatedElement.getFlights().size() > 0
							? (mutatedElement.getFlights().get(mutatedElement.getFlights().size()-1)).getDestinationAirport()
							: startCity;
			mutatedElements.add(
					recreateSolution(restartDepartureAirport, regionsToVisit, mutatedElement.getFlights().size()+1, mutatedElement));			
		}
		return integrateMutatedElementsWithPopulation(population, mutatedElements, elementsToBeMutated);
	}

	private ArrayList<ResultDto> integrateMutatedElementsWithPopulation(ArrayList<ResultDto> population,
			ArrayList<ResultDto> mutatedElements, ArrayList<Integer> elementsToBeMutated) {
		
		ArrayList<ResultDto> newPopulation = new ArrayList<ResultDto>();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			if (!elementsToBeMutated.contains(i)) {
				newPopulation.add(population.get(i));
			}
		}
		for (ResultDto mutatedElement : mutatedElements) {
			newPopulation.add(mutatedElement);
		}
		return newPopulation;
	}

	private ResultDto stopJourneySooner(ResultDto mutatedElement) {
		Integer dayToStopJourney = CommonUtils.getRandomNumberFromXtoY(1, mutatedElement.getFlights().size());
		ArrayList<FlightDto> historyFlights = new ArrayList<FlightDto>(mutatedElement.getFlights());

		mutatedElement.setFlights(new ArrayList<FlightDto>());
		for (int i = 0; i < historyFlights.size(); i++) {
			if (dayToStopJourney >= i+1) {
				mutatedElement.addFlight(historyFlights.get(i));
			} else {
				break;
			}
		}
		
		return mutatedElement;
	}

	private ArrayList<Integer> findElementsToBeMutated(boolean includeRoulette) {
		ArrayList<Integer> elementsToMutate = new ArrayList<Integer>();
		Integer numberOfElementsToMutate = (int) (POPULATION_SIZE * MUTATION_RATE);
		
		if (includeRoulette) {

		} else {
			while(elementsToMutate.size() < numberOfElementsToMutate) {
				Integer randomIndexToBeMutated = CommonUtils.getRandomNumberFromXtoY(0, POPULATION_SIZE-1);
				if (!elementsToMutate.contains(randomIndexToBeMutated)) {
					elementsToMutate.add(randomIndexToBeMutated);
				}
			}
		}
		return elementsToMutate;
	}

	private ArrayList<ResultDto> repopulateWeakest(ArrayList<ResultDto> population) {
		ArrayList<ResultDto> repopulation = new ArrayList<ResultDto>();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			if (!weakestGuysIndexes.contains(i)) {
				repopulation.add(population.get(i));
			}
		}
		for (Integer weakGuyindex : weakestGuysIndexes) {
			repopulation.add(this.createPseudoRandomSolution(startCity, regionsToVisit, 1));
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
						tempWeakestGuysIndexes.remove(j);
						tempWeakestGuysIndexes.add(i);
						break;
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

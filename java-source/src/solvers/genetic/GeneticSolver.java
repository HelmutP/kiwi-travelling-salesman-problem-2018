package solvers.genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dtos.FlightDto;
import dtos.ResultDto;
import solvers.base.BaseSolver;
import utils.CommonUtils;
import utils.Timer;

public class GeneticSolver extends BaseSolver {
	
	private static final int POPULATION_SIZE = 20;
	private static final double MUTATION_RATE = 0.25;
	private static final double ELIMINATION_RATE = 0.2;
	private static final int IMPROVEMENT_CONVERGENCE_LIMIT = 300;

	private ResultDto theBestSolution = null;
	private ArrayList<Integer> weakestGuysIndexes = null;
	private ArrayList<String> regionsToVisit = null;

	private List<Double> bestPricesOfPopulations = new ArrayList<Double>();

	public GeneticSolver(String testCaseId) {
		super(testCaseId);
	}

	public GeneticSolver(int regionsCount, String startCity, String startRegion, ArrayList<String> regions,
			HashMap<String, String> cityRegions,
			HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
		super(regionsCount, startCity, startRegion, regions, cityRegions, flights);
	}

	@Override
	public ResultDto run() {
		System.out.println("LOOKING FOR AWESOME SOLUTION USING EVOLUTION HAHA ...");

		regionsToVisit = initRegionsToVisit();
		ArrayList<ResultDto> population = createPopulation(regionsToVisit);
		boolean populationImproving = true;
		int generationCounter = 1;
		
		System.out.println(Timer.getCurrentRunTime());
		while(populationImproving && Timer.doWeHaveEnoughTimeToContinue()) {
			System.out.println("CREATING GENERATION " + generationCounter + "...");
			
			evaluatePopulation(population);

			if (!Timer.doWeHaveEnoughTimeToContinue()) {
				break;
			}
			population = mutate(population);

			if (!Timer.doWeHaveEnoughTimeToContinue()) {
				break;
			}
			population = repopulateWeakest(population);

			if (!Timer.doWeHaveEnoughTimeToContinue()) {
				break;
			}
			populationImproving = isPopulationStillImproving(theBestSolution.getTotalCost());

			System.out.println("GENERATION " + generationCounter + " IS IMPROVED: " + populationImproving + " AND THE BEST SOLUTION HAS PRICE: " + theBestSolution.getTotalCost()+ ".");
			generationCounter++;
		}

		System.out.println("DONE! THE STRONGEST IS THE WINNER!");
		return theBestSolution;
	}

	private ArrayList<ResultDto> mutate(ArrayList<ResultDto> population) {
		ArrayList<Integer> elementsToBeMutated = findElementsToBeMutated(true, population);
		ArrayList<ResultDto> mutatedElements = new ArrayList<ResultDto>();

		for (int i = 0; i < elementsToBeMutated.size(); i++) {
			ResultDto mutatedElement = new ResultDto(
					Integer.valueOf(population.get(elementsToBeMutated.get(i)).getTotalCost().intValue()),
					new ArrayList<FlightDto>(population.get(elementsToBeMutated.get(i)).getFlights()));
			mutatedElement = stopJourneySooner(mutatedElement);

			if (!Timer.doWeHaveEnoughTimeToContinue()) {
				return population;
			}
			
			String restartDepartureAirport = 
					mutatedElement.getFlights().size() > 0
							? (mutatedElement.getFlights().get(mutatedElement.getFlights().size()-1)).getDestinationAirport()
							: startCity;
			mutatedElements.add(
					recreateSolution(restartDepartureAirport, regionsToVisit, mutatedElement.getFlights().size()+1, mutatedElement));			
			
			if (!Timer.doWeHaveEnoughTimeToContinue()) {
				return population;
			}
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
		
		if (!Timer.doWeHaveEnoughTimeToContinue()) {
			return population;
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

	private ArrayList<Integer> findElementsToBeMutated(boolean includeRoulette, ArrayList<ResultDto> population) {
		ArrayList<Integer> elementsToMutate = new ArrayList<Integer>();
		Integer numberOfElementsToMutate = (int) (POPULATION_SIZE * MUTATION_RATE);
		
		if (includeRoulette) {
			ArrayList<Integer> roulette = new ArrayList<Integer>();
			int totalCostOfPopulation = 0;
			
			for (ResultDto solutionDraft : population) {
				totalCostOfPopulation += solutionDraft.getTotalCost().intValue();
			}

			for (int i = 0; i < POPULATION_SIZE; i++) {
				int rouletteShare = (int) (((double) (totalCostOfPopulation - population.get(i).getTotalCost())) / ((double) (totalCostOfPopulation)) * 100.0 * (double) POPULATION_SIZE);
				for (int j = 0; j < rouletteShare; j++) {
					roulette.add(i);
				}
			}
			
			while(elementsToMutate.size() < numberOfElementsToMutate) {
				Integer ruletteIndex = CommonUtils.getRandomNumberFromXtoY(0, roulette.size()-1);
				if (!elementsToMutate.contains(roulette.get(ruletteIndex))) {
					elementsToMutate.add(roulette.get(ruletteIndex));
				}
			}
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
	
	public ResultDto getTheBestSolution() {
		return theBestSolution;
	}

	public void setTheBestSolution(ResultDto theBestSolution) {
		this.theBestSolution = theBestSolution;
	}

	public ArrayList<String> getRegionsToVisit() {
		return regionsToVisit;
	}

	public void setRegionsToVisit(ArrayList<String> regionsToVisit) {
		this.regionsToVisit = regionsToVisit;
	}

	public static int getPopulationSize() {
		return POPULATION_SIZE;
	}

	public static double getMutationRate() {
		return MUTATION_RATE;
	}

	public static double getEliminationRate() {
		return ELIMINATION_RATE;
	}

	public static int getImprovementConvergenceLimit() {
		return IMPROVEMENT_CONVERGENCE_LIMIT;
	}
}

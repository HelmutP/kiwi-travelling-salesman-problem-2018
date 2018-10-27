package solvers.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dtos.FlightDto;
import dtos.ResultDto;
import utils.CommonUtils;
import utils.IOUtils;

public abstract class BaseSolver {

	protected int regionsCount;
	protected String startCity;
	protected String startRegion;
	protected ArrayList<String> regions = new ArrayList<String>();
	protected HashMap<String, String> cityRegions = new HashMap<String, String>();
	protected HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>>
		flights = new HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>>();
	private ArrayList<Integer> oneWayJourneysHashes = new ArrayList<Integer>();
	
	public BaseSolver(final String testCaseId) {
		preprocessRawInputData(IOUtils.readInput(testCaseId));
	}

	private void preprocessRawInputData(final ArrayList<String> readRawInputData) {
		System.out.println("RUNNING INPUT DATA PREPROCESSING ... ");

		String[] firstLineValues = CommonUtils.splitStringByWhitespaces(readRawInputData.get(0));
		regionsCount = Integer.parseInt(firstLineValues[0]);
		startCity = firstLineValues[1];

		int regionCitiesPartShift = 1;
		for (int i = 0; i < regionsCount * 2; i+=2) {

			String region = readRawInputData.get(i + regionCitiesPartShift);
			List<String> citiesInRegion = 
					Arrays.asList(CommonUtils.splitStringByWhitespaces(
							readRawInputData.get(i + 1 + regionCitiesPartShift)));

			if (citiesInRegion.contains(startCity)) {
				startRegion = region;
			}

			for (String city : citiesInRegion) {
				cityRegions.put(city, region);
			}
			regions.add(region);	
		}

		int flightRowCounter = regionsCount * 2 + regionCitiesPartShift;
		while (readRawInputData.size() > flightRowCounter) {
			
			String[] currentLineValues = CommonUtils.splitStringByWhitespaces(readRawInputData.get(flightRowCounter));
			String departureAirport = currentLineValues[0];
			String destinationAirport = currentLineValues[1];
			Integer dayOfFlight = Integer.parseInt(currentLineValues[2]);
			String cost = currentLineValues[3];

			if (dayOfFlight != 0) {
				addFlightDataRow(departureAirport, destinationAirport, dayOfFlight, cost);
			} else {
				for (int currentDay = 1; currentDay <= regionsCount; currentDay++) {
					addFlightDataRow(departureAirport, destinationAirport, currentDay, cost);
				}
			}

			flightRowCounter++;
		}

		System.out.println("INPUT DATA PREPROCESSING DONE");
	}

	private void addFlightDataRow(String departureAirport, String destinationAirport, Integer dayOfFlight,
			String cost) {

		if (!flights.containsKey(departureAirport)) {
			flights.put(departureAirport, new HashMap<Integer, HashMap<String, List<List<String>>>>());
		}
		if (!flights.get(departureAirport).containsKey(dayOfFlight)) {
			flights.get(departureAirport).put(dayOfFlight, new HashMap<String, List<List<String>>>());
		}
		if (!flights.get(departureAirport).get(dayOfFlight).containsKey(cityRegions.get(destinationAirport))) {
			flights.get(departureAirport).get(dayOfFlight).put(cityRegions.get(destinationAirport), new ArrayList<List<String>>());
		}
		
		ArrayList<String> flightInfo = new ArrayList<String>();
		flightInfo.add(destinationAirport);
		flightInfo.add(cost);
		flights.get(departureAirport).get(dayOfFlight).get(cityRegions.get(destinationAirport)).add(flightInfo);
	}

	protected ResultDto createPseudoRandomSolution(String departureAirport, ArrayList<String> regionsToVisit, int day) {
		return recreateSolution(departureAirport, regionsToVisit, day, null);
	}

	protected ResultDto recreateSolution(String departureAirport, ArrayList<String> regionsToVisit, int day, ResultDto tempSolution) {

		String departureRegion = startRegion;
		ArrayList<String> preProcessedRegionsToVisit = null;
		if (tempSolution != null && tempSolution.getFlights().size() > 0) {
			preProcessedRegionsToVisit = new ArrayList<String>(regionsToVisit);
			for (FlightDto flight : tempSolution.getFlights()) {
				preProcessedRegionsToVisit.remove(cityRegions.get(flight.getDepartureAirport()));
				preProcessedRegionsToVisit.remove(cityRegions.get(flight.getDestinationAirport()));
				if (preProcessedRegionsToVisit.size() == 0 && !cityRegions.get(tempSolution.getFlights().get(tempSolution.getFlights().size()-1).getDestinationAirport()).equals(departureRegion)) {
					preProcessedRegionsToVisit.add(departureRegion);
				}
			}
		} else {
			preProcessedRegionsToVisit = regionsToVisit;
		}
		ResultDto solution = null;
		boolean solutionFound = false;
		boolean algorithmRestarted = false;

		while (!solutionFound) {

			ArrayList<String> regionsToVisitProcessing = null;
			int currentDay = 1;
			String currentAirport = null;
			boolean gotBackToOriginRegion = false;

			if (algorithmRestarted) {
				regionsToVisitProcessing = new ArrayList<String>(regionsToVisit);
				solution = new ResultDto();
				currentAirport = startCity;
			} else {
				regionsToVisitProcessing = new ArrayList<String>(preProcessedRegionsToVisit);
				solution = tempSolution != null ? tempSolution : new ResultDto();
				currentDay = day;
				currentAirport = tempSolution != null && tempSolution.getFlights().size() > 0
						? tempSolution.getFlights().get(tempSolution.getFlights().size()-1).getDestinationAirport()
						: departureAirport;
				if (regionsToVisitProcessing.size() == 0) {
					gotBackToOriginRegion = true;
					solutionFound = true;
				}
			}

			while (!gotBackToOriginRegion) {

				HashMap<Integer, HashMap<String, List<List<String>>>> flightsFromAirport = flights.get(currentAirport);
				if (flightsFromAirport == null) {
					oneWayJourneysHashes.add(CommonUtils.hashJourney(solution, null, departureAirport, currentDay));
					algorithmRestarted = true;
					break;
				}
				HashMap<String, List<List<String>>> accessibleRegions = flightsFromAirport.get(currentDay);
				ArrayList<List<String>> possibleFlights = new ArrayList<List<String>>();
				FlightDto nextFlight = null;

				if (accessibleRegions != null) {
					for (String desiredRegion : regionsToVisitProcessing) {
						List<List<String>> flightsToDesiredRegion = accessibleRegions.get(desiredRegion);
						if (flightsToDesiredRegion != null) {
							possibleFlights.addAll(flightsToDesiredRegion);
						}
					}
					
					nextFlight = getNextFlight(currentAirport, currentDay, possibleFlights, solution);
					if (nextFlight == null) {
						oneWayJourneysHashes.add(CommonUtils.hashJourney(solution, null, departureAirport, currentDay));
						algorithmRestarted = true;
						break;
					}
					solution.addFlight(nextFlight);
				} else {
					oneWayJourneysHashes.add(CommonUtils.hashJourney(solution, null, departureAirport, currentDay));
					algorithmRestarted = true;
					break;
				}

				if (regionsToVisitProcessing.contains(departureRegion)) {
					gotBackToOriginRegion = true;
				}

				currentAirport = nextFlight.getDestinationAirport();
				currentDay++;
				regionsToVisitProcessing.remove(cityRegions.get(currentAirport));
				if (regionsToVisitProcessing.isEmpty() && !gotBackToOriginRegion) {
					regionsToVisitProcessing.add(departureRegion);
				}
				if (regionsToVisitProcessing.isEmpty() && gotBackToOriginRegion) {
					solutionFound = true;
					break;
				}
			}
		}
		return solution;
	}

	private FlightDto getNextFlight(String currentAirport, int currentDay, ArrayList<List<String>> possibleFlights, ResultDto solution) {
		
		FlightDto nextFlight = null;
		boolean foundFlightIsValid = false;
		while(!foundFlightIsValid) {

			if (possibleFlights == null || possibleFlights.size() == 0) {
				return null;
			}

			List<String> foundFlight = possibleFlights.get(
					CommonUtils.getRandomNumberFromXtoY(0, possibleFlights.size() - 1));
			
			if (!isThisWayBlind(solution, foundFlight, currentAirport, currentDay)) {
				nextFlight = new FlightDto(currentAirport, foundFlight.get(0), currentDay, foundFlight.get(1));
				foundFlightIsValid = true;
			} else {
				possibleFlights.remove(foundFlight);
			}
		}
		return nextFlight;
	}

	private boolean isThisWayBlind(ResultDto solution, List<String> foundFlight, String departureAirport, Integer dayOfFlight) {
		
		if (foundFlight == null) {
			throw new IllegalStateException();
		}
		if (oneWayJourneysHashes.contains(CommonUtils.hashJourney(solution, foundFlight, departureAirport, dayOfFlight))) {
			return true;
		}
		return false;
	}

	public abstract ResultDto run();
}

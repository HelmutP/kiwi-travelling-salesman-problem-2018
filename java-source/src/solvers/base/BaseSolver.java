package solvers.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import utils.CommonUtils;
import utils.IOUtils;

public abstract class BaseSolver {

	protected int regionsCount;
	protected String startCity;
	protected String startRegion;
	protected HashMap<String, String> cityRegions = new HashMap<String, String>();
	protected HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>>
		flights = new HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>>();

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
}

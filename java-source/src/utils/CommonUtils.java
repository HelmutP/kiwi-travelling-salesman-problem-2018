package utils;

import java.util.List;
import java.util.Random;

import dtos.FlightDto;
import dtos.ResultDto;

public final class CommonUtils {

	public static String[] splitStringByWhitespaces(final String line) {
		return line.split(" ");
	}
	
	public static int getRandomNumberFromXtoY(int from, int to) {
		Random rand = new Random();
		return rand.nextInt(to - from + 1) + from;
	}
	
	public static Integer hashJourney(ResultDto solution, List<String> foundFlight, String departureAirport, Integer dayOfFlight) {

		StringBuilder foundJourneyString = new StringBuilder();
		for (FlightDto alreadyExistingFlight : solution.getFlights()) {
			foundJourneyString.append(alreadyExistingFlight.serialize());
		}

		if (foundFlight != null) {
			FlightDto foundFlightDto = new FlightDto(departureAirport, foundFlight.get(0), dayOfFlight, foundFlight.get(1));
			foundJourneyString.append(foundFlightDto.serialize());
		}

		return Integer.valueOf(foundJourneyString.toString().hashCode());
	}
}

package dtos;

import java.util.ArrayList;

public class ResultDto {

	private Integer totalCost;
	private ArrayList<FlightDto> flights;

	public ResultDto(Integer totalCost, ArrayList<FlightDto> flights) {
		setTotalCost(totalCost);
		setFlights(flights);
	}

	public ResultDto() {
		setFlights(new ArrayList<FlightDto>());
	}

	public void addFlight(FlightDto newFlight) {
		getFlights().add(newFlight);
	}

	private void recalculateTotalCost() {
		int newTotalCostSum = 0;
		for (FlightDto flight : flights) {
			newTotalCostSum += flight.getCost();
		}
		totalCost = Integer.valueOf(newTotalCostSum);
	}

	public String[] getFormattedOutput() {
		ArrayList<String> outputLinesList = new ArrayList<String>();
		
		String departureAirport = null;
		if (getFlights() != null && getFlights().size() > 0) {
			departureAirport = getFlights().get(0).getDepartureAirport();
		} else {
			throw new IllegalStateException();
		}
		outputLinesList.add(getTotalCost() + " " + departureAirport);
		
		for (FlightDto flight : getFlights()) {
			outputLinesList.add(
					flight.getDepartureAirport() + " "+
					flight.getDestinationAirport() + " " +
					flight.getDayOfFlight() + " " +
					flight.getCost());
		}

		return outputLinesList.toArray(new String[outputLinesList.size()]);
	}

	public Integer getTotalCost() {
		recalculateTotalCost();
		return totalCost;
	}
	private void setTotalCost(Integer totalCost) {
		this.totalCost = totalCost;
	}
	public ArrayList<FlightDto> getFlights() {
		return flights;
	}
	private void setFlights(ArrayList<FlightDto> flights) {
		this.flights = flights;
	}
}


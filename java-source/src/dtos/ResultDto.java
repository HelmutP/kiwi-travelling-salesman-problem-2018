package dtos;

import java.util.ArrayList;

public class ResultDto {

	private Integer totalCost = Integer.valueOf(0);
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
		updateTotalCost(newFlight);
	}

	private void updateTotalCost(FlightDto newFlight) {
		totalCost += newFlight.getCost();
	}

	public void recalculateTotalCost() {
		int newTotalCostSum = 0;
		for (FlightDto flight : flights) {
			newTotalCostSum += flight.getCost();
		}
		totalCost = Integer.valueOf(newTotalCostSum);
	}

	public String[] getFormattedOutput() {
		ArrayList<String> outputLinesList = new ArrayList<String>();

		outputLinesList.add(getTotalCost().toString());
		
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
		return totalCost;
	}
	private void setTotalCost(Integer totalCost) {
		this.totalCost = totalCost;
	}
	public ArrayList<FlightDto> getFlights() {
		return flights;
	}
	public void setFlights(ArrayList<FlightDto> flights) {
		this.flights = flights;
		recalculateTotalCost();
	}
}


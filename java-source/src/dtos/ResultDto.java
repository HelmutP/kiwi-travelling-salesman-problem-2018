package dtos;

import java.util.ArrayList;

public class ResultDto {

	private Integer totalCost;
	private ArrayList<Flight> flights;

	public ResultDto(Integer totalCost, ArrayList<Flight> flights) {
		setTotalCost(totalCost);
		setFlights(flights);
	}

	public ResultDto() {
		setFlights(new ArrayList<Flight>());
	}

	public ArrayList<Flight> getFinalJourney() {
		return getFlights();
	}

	public void addFlight(Flight newFlight) {
		getFlights().add(newFlight);
		recalculateTotalCost();
	}

	private void recalculateTotalCost() {
		int newTotalCostSum = 0;
		for (Flight flight : flights) {
			newTotalCostSum += flight.getCost();
		}
		totalCost = Integer.valueOf(newTotalCostSum);
	}

	public Integer getTotalCost() {
		return totalCost;
	}
	private void setTotalCost(Integer totalCost) {
		this.totalCost = totalCost;
	}
	private ArrayList<Flight> getFlights() {
		return flights;
	}
	private void setFlights(ArrayList<Flight> flights) {
		this.flights = flights;
	}
}


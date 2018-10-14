package dtos;

public class FlightDto {

	private String departureAirport;
	private String destinationAirport;
	private Integer dayOfFlight;
	private Integer cost;

	public FlightDto(String departureAirport, String destinationAirport, Integer dayOfFlight, String cost) {
		this(departureAirport, destinationAirport, dayOfFlight, Integer.parseInt(cost));
	}

	public FlightDto(String departureAirport, String destinationAirport, Integer dayOfFlight, Integer cost) {
		setDepartureAirport(departureAirport);
		setDestinationAirport(destinationAirport);
		setDayOfFlight(dayOfFlight);
		setCost(cost);
	}

	public String getDepartureAirport() {
		return departureAirport;
	}
	private void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}
	public String getDestinationAirport() {
		return destinationAirport;
	}
	private void setDestinationAirport(String destinationAirport) {
		this.destinationAirport = destinationAirport;
	}
	public Integer getDayOfFlight() {
		return dayOfFlight;
	}
	private void setDayOfFlight(Integer dayOfFlight) {
		this.dayOfFlight = dayOfFlight;
	}
	public Integer getCost() {
		return cost;
	}
	private void setCost(Integer cost) {
		this.cost = cost;
	}
}
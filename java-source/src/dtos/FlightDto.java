package dtos;


class Flight {

	private String departureAirport;
	private String destinationAirport;
	private Integer dayOfFlight;
	private Integer cost;

	public Flight(String departureAirport, String destinationAirport, Integer dayOfFlight, String cost) {
		this(departureAirport, destinationAirport, dayOfFlight, Integer.parseInt(cost));
	}

	public Flight(String departureAirport, String destinationAirport, Integer dayOfFlight, Integer cost) {
		setDepartureAirport(departureAirport);
		setDestinationAirport(destinationAirport);
		setDayOfFlight(dayOfFlight);
		setCost(cost);
	}

	public String getDepartureAirport() {
		return departureAirport;
	}
	public void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}
	public String getDestinationAirport() {
		return destinationAirport;
	}
	public void setDestinationAirport(String destinationAirport) {
		this.destinationAirport = destinationAirport;
	}
	public Integer getDayOfFlight() {
		return dayOfFlight;
	}
	public void setDayOfFlight(Integer dayOfFlight) {
		this.dayOfFlight = dayOfFlight;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
}
import java.io.*;
import java.util.*;

/**********************************UTILS************************/

class FlightDto {

    private String departureAirport;
    private String destinationAirport;
    private Integer dayOfFlight;
    private Integer cost;

    public FlightDto(String departureAirport, String destinationAirport, Integer dayOfFlight, String cost) {
        this(departureAirport, destinationAirport, dayOfFlight, Integer.parseInt(cost));
    }

    public FlightDto(String departureAirport, String destinationAirport, Integer dayOfFlight, Integer cost) {

        if (departureAirport == null || destinationAirport == null || dayOfFlight == null) {
            throw new IllegalStateException();
        }
        setDepartureAirport(departureAirport);
        setDestinationAirport(destinationAirport);
        setDayOfFlight(dayOfFlight);
        setCost(cost);
    }

    public String serialize() {
        return getDepartureAirport()+getDestinationAirport()+getCost().toString()+getDayOfFlight().toString();
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
final class CommonUtils {

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

final class IOUtils {

    protected static final String OUTPUT_FOLDER = "test/resources/solver-output/";
    protected static final String OUTPUT_FILE_NAME_PATTERN = "solver%s-output.txt";

    protected static final String TESTS_INPUT_FOLDER = "test/resources/input/";
    protected static final String TEST_INPUT_FILENAME_PATTERN = "test%s-input.txt";

    public static void saveResult(final String[] lines, final int testCaseId) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writer = new PrintWriter(OUTPUT_FOLDER + String.format(OUTPUT_FILE_NAME_PATTERN, testCaseId), "UTF-8");
        boolean isFirstLine = true;

        for (String line : lines) {
            if (!isFirstLine) {
                writer.println();
            } else {
                isFirstLine = false;
            }
            writer.print(line);
        }
        writer.close();
    }

    public static ArrayList<String> readInput(final String testCaseId) {

        ArrayList<String> lines = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        return lines;
    }

    private static String getInputFileName(String testCaseId) {
        return TESTS_INPUT_FOLDER + String.format(TEST_INPUT_FILENAME_PATTERN, testCaseId);
    }
}



class ResultDto {

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


class Timer {

    private static long programStartTime = -1;
    private static long maxProgramRunTime = -1;
    private static long timeForResultsCreation = (long) 1000;

    public static long getProgramStartTime() {
        return programStartTime;
    }

    public static void setProgramStartTime(long programStartTime) {
        Timer.programStartTime = programStartTime;
    }

    public static boolean doWeHaveEnoughTimeToContinue() {
        if (Timer.getCurrentRunTime() < (maxProgramRunTime - timeForResultsCreation)) {
            return true;
        }
        return false;
    }

    public static long getMaxProgramRunTime() {
        return maxProgramRunTime;
    }

    public static void setMaxProgramRunTime(int regionsCount, HashMap<String, String> cityRegions) {
        if (regionsCount <= 20 && cityRegions.keySet().size() < 50) {
            maxProgramRunTime = (long) 3000;
            timeForResultsCreation = (long) 500;
        } else if (regionsCount <= 100 && cityRegions.keySet().size() < 200) {
            maxProgramRunTime = (long) 5000;
            timeForResultsCreation = (long) 1000;
        } else {
            maxProgramRunTime = (long) 15000;
            timeForResultsCreation = (long) 3000;
        }
    }

    public static long getTimeForResultsCreation() {
        return timeForResultsCreation;
    }

    public static void setTimeForResultsCreation(long timeForResultsCreation) {
        Timer.timeForResultsCreation = timeForResultsCreation;
    }

    public static long getCurrentRunTime() {
        return System.currentTimeMillis() - Timer.getProgramStartTime();
    }
}


/***********************SOLVERS*****************************/
abstract class BaseSolver {

    protected int regionsCount;
    protected String startCity;
    protected String startRegion;

    protected ArrayList<String> regions = new ArrayList<String>();
    protected HashMap<String, String> cityRegions = new HashMap<String, String>();
    protected HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>>
            flights = new HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>>();
    private ArrayList<Integer> oneWayJourneysHashes = new ArrayList<Integer>();

    public BaseSolver(int regionsCount, String startCity, String startRegion, ArrayList<String> regions,
                      HashMap<String, String> cityRegions,
                      HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
        super();
        this.regionsCount = regionsCount;
        this.startCity = startCity;
        this.startRegion = startRegion;
        this.regions = regions;
        this.cityRegions = cityRegions;
        this.flights = flights;
    }

    public BaseSolver(final String testCaseId) {
        preprocessRawInputData(IOUtils.readInput(testCaseId));
        Timer.setMaxProgramRunTime(regionsCount, cityRegions);
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

    public int getRegionsCount() {
        return regionsCount;
    }

    public void setRegionsCount(int regionsCount) {
        this.regionsCount = regionsCount;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartRegion() {
        return startRegion;
    }

    public void setStartRegion(String startRegion) {
        this.startRegion = startRegion;
    }

    public ArrayList<String> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<String> regions) {
        this.regions = regions;
    }

    public HashMap<String, String> getCityRegions() {
        return cityRegions;
    }

    public void setCityRegions(HashMap<String, String> cityRegions) {
        this.cityRegions = cityRegions;
    }

    public HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> getFlights() {
        return flights;
    }

    public void setFlights(HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
        this.flights = flights;
    }

    public ArrayList<Integer> getOneWayJourneysHashes() {
        return oneWayJourneysHashes;
    }

    public void setOneWayJourneysHashes(ArrayList<Integer> oneWayJourneysHashes) {
        this.oneWayJourneysHashes = oneWayJourneysHashes;
    }
}

class SASolver extends BaseSolver {
    public SASolver(String testCaseId) {
        super(testCaseId);
    }

    @Override
    public ResultDto run() {
        System.out.println("LOOKING FOR AWESOME SOLUTION ...");

        ArrayList<String> regionsToVisit = new ArrayList<String>(regions);
        regionsToVisit.remove(startRegion);
        ResultDto initialTour = this.createPseudoRandomSolution(startCity, regionsToVisit, 1);
        double temp = 1000;
        double coolingRate = 0.3;

        ResultDto bestTour = initialTour; //TODO: clone?
        while (temp > 1) {
            ResultDto newTour = this.createPseudoRandomSolution(startCity, regionsToVisit, 1);
            int lowestCost = bestTour.getTotalCost();
            int newCost = newTour.getTotalCost();

            if (Math.random() < this.acceptanceProbability(lowestCost, newCost, temp)){
                bestTour = newTour; //TODO: clone?
            }
            temp *= 1 - coolingRate;
        }

        System.out.println("DONE!");
        System.out.println("Final solution cost: " + bestTour.getTotalCost());
        return bestTour;
    }

    private ResultDto createAlternateTour(ResultDto bestTour) {
        return bestTour;
    }

    private double acceptanceProbability(int lowestCost, int newCost, double temperature){
        // accept solution
        if (newCost < lowestCost){
            return 1.0;
        }
        return Math.exp((lowestCost - newCost) / temperature);
    }
}

/***************************RUNNERS***********************/

abstract class BaseRunner implements Runnable {

    protected BaseSolver solver;
    protected BaseRunnerArgumentsProcessor argumentsProcessor;
    public ResultDto solution;

    protected abstract void initArgumentsProcessor(String[] args);
    protected abstract void initSolver();
    protected abstract void initSolver(int regionsCount, String startCity, String startRegion,
                                       ArrayList<String> regions, HashMap<String, String> cityRegions,
                                       HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights);

    public BaseRunner(String[] args) {
        initArgumentsProcessor(args);
        initSolver();
    }

    public BaseRunner(String[] args, int regionsCount, String startCity, String startRegion, ArrayList<String> regions,
                      HashMap<String, String> cityRegions,
                      HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
        initArgumentsProcessor(args);
        initSolver(regionsCount, startCity, startRegion, regions, cityRegions, flights);
    }

    public void run() {
        solution = solver.run();
    }

    public BaseRunnerArgumentsProcessor getArgumentsProcessor() {
        return argumentsProcessor;
    }
    public void setArgumentsProcessor(BaseRunnerArgumentsProcessor argumentsProcessor) {
        this.argumentsProcessor = argumentsProcessor;
    }
    public BaseSolver getSolver() {
        return solver;
    }
    public void setSolver(BaseSolver solver) {
        this.solver = solver;
    }
}
class SASolverRunner extends BaseRunner {
    private static final int THREADS_COUNT = 8;

    public SASolverRunner(String[] args) {
        super(args);
    }
    public SASolverRunner(String[] args, int regionsCount, String startCity, String startRegion, ArrayList<String> regions,
                          HashMap<String, String> cityRegions,
                          HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
        super(args, regionsCount, startCity, startRegion, regions, cityRegions, flights);
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        HashMap<BaseRunner, Thread> threads = new HashMap<BaseRunner, Thread>();

        Timer.setProgramStartTime(System.currentTimeMillis());

        BaseSolver solverExample = null;
        SASolverRunner runner = null;
        for (int i = 0; i < THREADS_COUNT; i++) {
            if (i == 0) {
                runner = new SASolverRunner(args);
                solverExample = runner.getSolver();
            } else {
                SASolver SASolverExample = (SASolver) solverExample;
                runner = new SASolverRunner(args, SASolverExample.getRegionsCount(),
                        SASolverExample.getStartCity(), SASolverExample.getStartRegion(),
                        SASolverExample.getRegions(), SASolverExample.getCityRegions(),
                        SASolverExample.getFlights());
            }
            Thread thread = new Thread(runner);
            threads.put(runner, thread);
            threads.get(runner).start();
        }

        for (Map.Entry<BaseRunner, Thread> threadRunnerPair : threads.entrySet()) {
            try {
                threadRunnerPair.getValue().join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        BaseRunner theBestSolutionRunner = null;
        for (Map.Entry<BaseRunner, Thread> threadRunnerPair : threads.entrySet()) {
            if (theBestSolutionRunner == null || (threadRunnerPair.getKey().solution != null && theBestSolutionRunner.solution != null &&
                    threadRunnerPair.getKey().solution.getTotalCost() < theBestSolutionRunner.solution.getTotalCost())) {
                theBestSolutionRunner = threadRunnerPair.getKey();
            }
        }

        IOUtils.saveResult(theBestSolutionRunner.solution.getFormattedOutput(), Integer.valueOf(theBestSolutionRunner.getArgumentsProcessor().getTestCaseId()));
        System.out.println("TIME OF EXECUTION " + Timer.getCurrentRunTime() / 1000.0 + " secs.");

    }

    @Override
    protected void initArgumentsProcessor(String[] args) {
        argumentsProcessor = new SARunnerArgumentProcessor(args);
    }

    @Override
    protected void initSolver() {
        solver = new SASolver(argumentsProcessor.getTestCaseId());
    }

    @Override
    protected void initSolver(int regionsCount, String startCity, String startRegion, ArrayList<String> regions, HashMap<String, String> cityRegions, HashMap<String, HashMap<Integer, HashMap<String, List<List<String>>>>> flights) {
        solver = new SASolver(argumentsProcessor.getTestCaseId());
    }
}

abstract class BaseRunnerArgumentsProcessor {

    protected String[] arguments;

    public BaseRunnerArgumentsProcessor(String[] args) {
        super();
        arguments = args;
    }

    public String getTestCaseId() {
        if (arguments.length == 1) {
            return arguments[0];
        } else if (arguments.length == 0) {
            return "0";
        } else {
            throw new IllegalArgumentException();
        }
    }

    public abstract void processSolverSpecificArgs();
}


class SARunnerArgumentProcessor extends BaseRunnerArgumentsProcessor {
    public SARunnerArgumentProcessor(String[] args) {
        super(args);
    }

    @Override
    public void processSolverSpecificArgs() {
        // TODO
        return;
    }
}
package src.solvers.sa;

import dtos.ResultDto;

import java.util.ArrayList;

public class SASolver extends solvers.base.BaseSolver {
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

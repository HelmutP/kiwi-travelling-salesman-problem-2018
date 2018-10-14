package solvers.genetic;

import java.util.ArrayList;

import dtos.ResultDto;
import solvers.base.BaseSolver;

public class GeneticSolver extends BaseSolver {

	public GeneticSolver(String testCaseId) {
		super(testCaseId);
	}
	
	public ResultDto run() {
		System.out.println("LOOKING FOR AWESOME SOLUTION ...");

		ArrayList<String> regionsToVisit = new ArrayList<String>(regions);
		regionsToVisit.remove(startRegion);
		ResultDto result = this.createPseudoRandomSolution(startCity, regionsToVisit, 1);

		System.out.println("DONE!");
		return result;
	}
}

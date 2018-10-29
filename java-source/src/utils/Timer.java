package utils;

import java.util.HashMap;

public class Timer {

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

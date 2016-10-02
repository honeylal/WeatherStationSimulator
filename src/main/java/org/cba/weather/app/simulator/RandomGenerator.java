package org.cba.weather.app.simulator;

/**
 * The implemented class need to generate different random / dice selection
 * logic so that the simulation will be more realistic each time.
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public interface RandomGenerator {

	/**
	 * Returns a pseudo-random number between min and max
	 * 
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max -1
	 */
	public int getRandomNumber ( int min , int max );

	/**
	 * Implements the logic for randomly selecting extreme seasons.
	 * 
	 * @param ExtremeSeasonsPresent
	 * @return
	 */
	public boolean isSelectCurrentSimulationDayAsExtremeSeasonsDay ( boolean isExtremeSeasonsPresent );

	/**
	 * Implements the logic for randomly deciding do we need to add or subtract
	 * standard deviation from mean to predict the value current system time
	 * 
	 * @return
	 */
	public boolean isAddStandardDeviation ();
}

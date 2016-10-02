package org.cba.weather.app.simulator.impl;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

import org.cba.weather.app.simulator.RandomGenerator;

/**
 * 
 * This class implements the logics for different random / dice selection used
 * in the application for simulating more realistic values each time.
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class GameRandomGeneratorImpl implements RandomGenerator {

	/**
	 * Returns a pseudo-random number between min and max
	 * 
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max -1
	 */
	@Override
	public int getRandomNumber ( int min , int max ) {
		int randomNum = min;
		if ( max > min ) {
			randomNum = ThreadLocalRandom.current ()
											.nextInt ( min , max );
		}
		return randomNum;
	}
	
	/**
	 * Implements the logic for randomly selecting extreme seasons.
	 * <P>
	 * Dice logic : we get the current system time in Millisecond if it is
	 * completely divisible by 15 the current day season go to any of the
	 * extreme condition we added against the station.
	 * </P>
	 * 
	 * @param ExtremeSeasonsPresent
	 * @return
	 */
	@Override
	public boolean isSelectCurrentSimulationDayAsExtremeSeasonsDay ( boolean isExtremeSeasonsPresent ) {
		if ( isExtremeSeasonsPresent ) {
			Calendar calendar = Calendar.getInstance ();
			return ( calendar.getTimeInMillis () % 15 == 0 );
		} else {
			return false;
		}
	}
	
	/**
	 * Implements the logic for randomly deciding do we need to add or subtract
	 * standard deviation from mean to predict the value current system time
	 * 
	 * @return true if current system time completely divisible by 2
	 */
	@Override
	public boolean isAddStandardDeviation () {
		Calendar calendar = Calendar.getInstance ();
		return ( calendar.getTimeInMillis () % 2 == 0 );
	}
	
	
}

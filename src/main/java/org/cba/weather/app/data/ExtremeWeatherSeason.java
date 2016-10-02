package org.cba.weather.app.data;

/**
 * This class represent extreme weather season that come under one weather
 * station Eg: Floods, Cyclone etc
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class ExtremeWeatherSeason extends Season {

	private int sessonLength;

	/**
	 * @return Extreme weather events length in days
	 */
	public int getSessonLength () {
		return sessonLength;
	}

	public void setSessonLength ( int sessonLength ) {
		this.sessonLength = sessonLength;
	}

}

package org.cba.weather.app.exceptions;

/**
 * Throws any weather data input fields/line failed to process
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class WeatherDateException extends Exception {

	private static final long serialVersionUID = 1L;

	public WeatherDateException ( String s ) {
		super ( s );
	}
}

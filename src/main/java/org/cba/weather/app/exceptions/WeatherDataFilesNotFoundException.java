package org.cba.weather.app.exceptions;

/**
 * Throws if the data files not found
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class WeatherDataFilesNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public WeatherDataFilesNotFoundException ( String s ) {
		super ( s );
	}
}

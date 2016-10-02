package org.cba.weather.app.exceptions;

/**
 * Throws if date inputs show error
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class WeatherDataParseException extends Exception {

	private static final long serialVersionUID = 1L;

	public WeatherDataParseException ( String errorMessage ) {
		super ( errorMessage );
	}
}

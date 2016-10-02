package org.cba.weather.app.exceptions;

/**
 * Throws if season not configured or null
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class SeasonNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public SeasonNotFoundException ( String s ) {
		super ( s );
	}
}

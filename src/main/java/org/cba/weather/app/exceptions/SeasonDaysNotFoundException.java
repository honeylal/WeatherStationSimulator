package org.cba.weather.app.exceptions;
/**
 * Throws if season days not configured or null
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class SeasonDaysNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public SeasonDaysNotFoundException ( String s ) {
		super ( s );
	}
}

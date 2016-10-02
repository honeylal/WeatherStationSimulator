package org.cba.weather.app.data.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.cba.weather.app.data.WeatherStation;
import org.cba.weather.app.data.reader.WeatherDataReader;

/**
 * This class help to load the weather data against each weather stations (using
 * parallel execution with the help of Java thread pool so that
 * the application initialization become faster).
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class WeatherDataLoader implements Callable<WeatherStation> {

	private String weatherDataFilePath = "";
	private WeatherDataReader weatherDataReader = null;

	public WeatherDataLoader ( String weatherDataFilePath , WeatherDataReader weatherDataReader ) {
		this.weatherDataFilePath = weatherDataFilePath;
		this.weatherDataReader = weatherDataReader;
	}

	/**
	 * Thread that is taken care about loading the data from the files to the
	 * application This is against one station and one file
	 */
	public WeatherStation call () throws Exception {
		WeatherStation weatherStation = new WeatherStation ();
		BufferedReader weatherDataFilerBuffReader = null;
		try {
			weatherDataFilerBuffReader = new BufferedReader ( new FileReader ( weatherDataFilePath ) );
			String line;
			while ( ( line = weatherDataFilerBuffReader.readLine () ) != null ) {
				weatherDataReader.readAndInsertWeatherDataToWeatherStation ( line , weatherStation );
			}
		} catch ( IOException e ) {
			System.err.println ( "Error while loading the weather data" );
			e.printStackTrace ();
		} finally {
			try {
				if ( weatherDataFilerBuffReader != null )
					weatherDataFilerBuffReader.close ();
			} catch ( IOException ex ) {
				ex.printStackTrace ();
			}
		}
		return weatherStation;
	}

	
}

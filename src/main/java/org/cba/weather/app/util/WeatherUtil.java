package org.cba.weather.app.util;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.cba.weather.app.AppConstants;
import org.cba.weather.app.data.WeatherData;
import org.cba.weather.app.exceptions.WeatherDataFilesNotFoundException;

/**
 * holding the utility methods that used my the weather application
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 * @since 09-August-2016
 */
public class WeatherUtil {

	/**
	 * The formula for converting temperature in Celsius to Fahrenheit is
	 * Fahrenheit = (9/5) * (celsius + 32)
	 * 
	 * @param celsius
	 * @return temperature in fahrenheit
	 */
	public static float celsiusToFahrenheit ( float celsius ) {
		float fahrenheit = 0.0f;
		fahrenheit = ( 9.0f / 5.0f ) * ( celsius + 32 );
		return fahrenheit;
	}

	/**
	 * The formula for converting temperature in Fahrenheit to Celsius is
	 * Celsius = (5/9) * (Fahrenheit – 32)
	 * 
	 * @param fahrenheit
	 * @return temperature in celsius
	 */
	public static float fahrenheitToCelsius ( float fahrenheit ) {
		float celsius = 0.0f;
		celsius = ( 5.0f / 9.0f ) * ( fahrenheit - 32 );
		return celsius;
	}

	/**
	 * To check the string holding double data or not
	 * 
	 * @param str
	 * @return if double true else false
	 */
	public static boolean isDouble ( String str ) {
		try {
			Double.parseDouble ( str );
		} catch ( NumberFormatException nfe ) {
			return false;
		}
		return true;
	}

	/**
	 * To check the string holding integer data or not
	 * 
	 * @param str
	 * @return if integer true else false
	 */
	public static boolean isInteger ( String str ) {
		try {
			Integer.parseInt ( str );
		} catch ( NumberFormatException nfe ) {
			return false;
		}
		return true;
	}

	/**
	 * To check the string holding float data or not
	 * 
	 * @param str
	 * @return if float true else false
	 */
	public static boolean isFloat ( String str ) {
		try {
			Float.parseFloat ( str );
		} catch ( NumberFormatException nfe ) {
			return false;
		}
		return true;
	}

	/**
	 * Find the arithmetic mean (average) from input double list Formula: (The
	 * sum of all of the numbers in a list)/ (number of items in that list)
	 * 
	 * @param values
	 * @return mean
	 */
	public static float getMean ( ArrayList < Float > values ) {
		float mean = 0.00f;
		if ( null != values && values.size () > 0 ) {
			float sum = 0.00f;
			for ( float currvalue : values ) {
				sum += currvalue;
			}
			mean = sum / values.size ();
		}
		return mean;
	}

	/**
	 * Calculate "Sample Standard Deviation" of given numbers with respect to
	 * its mean formula: square root of (average of the squared differences from
	 * the Mean /size -1)
	 * 
	 * @param values
	 * @return
	 */
	public static float getStandardDeviation ( ArrayList < Float > values , float mean ) {
		float standardDeviation = 0.00f;
		if ( null != values && values.size () > 0 ) {
			// Find difference between value and and its mean square it and sum
			// it
			float sq_diff_sum = 0.0f;
			for ( float currvalue : values ) {
				float diff = currvalue - mean;
				sq_diff_sum += ( diff * diff );
			}
			// The average of the squared differences from the Mean / Size -1
			// Sample Standard Deviation formula
			float variance = sq_diff_sum / ( (float) ( values.size () - 1 ) );
			standardDeviation = (float) Math.sqrt ( variance );
		}
		return standardDeviation;
	}

	/**
	 * @param date
	 *            expected format dd-MM-yyyy hh:mm a
	 * @return return the date in ISO8601 date and time format
	 */
	public static String getISO8601Datetime ( String date ) {
		String iso8601FormatedOutPutDate = "";
		SimpleDateFormat inputDateFormater = new SimpleDateFormat ( "dd-MM-yyyy hh:mm a" );
		DateFormat iso8601FormatedDate = new SimpleDateFormat ( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
		Date inputDate;
		try {
			inputDate = inputDateFormater.parse ( date );
			iso8601FormatedOutPutDate = iso8601FormatedDate.format ( inputDate );
		} catch ( ParseException e ) {
			System.err.println ( "Error parsing getISO8601Datetime input date" );
			e.printStackTrace ();
		}
		return iso8601FormatedOutPutDate;
	}

	/**
	 * @param folderPath
	 * @return return list of text files filepath in the input folder
	 * @throws WeatherDataFilesNotFoundException
	 *             If data folder not found or the folder not having any text
	 *             files
	 */
	public static ArrayList < String > getWeatherStationDataFilesFromFolder ( String folderPath )
			throws WeatherDataFilesNotFoundException {
		ArrayList < String > weatherStationDataFiles = new ArrayList < String > ();
		File folder = new File ( folderPath );
		File[] listOfFiles = folder.listFiles ();
		if ( null != listOfFiles ) {
			for ( File currFile : listOfFiles ) {
				if ( currFile.isFile () && currFile.getName ()
													.endsWith ( ".txt" ) ) {
					weatherStationDataFiles.add ( currFile.getAbsolutePath () );
				}
			}
		}
		if ( weatherStationDataFiles.size () < 1 ) {
			throw new WeatherDataFilesNotFoundException ( "No weather data files found in ("
					+ folderPath + ") \n Application initialization failed!" );
		}
		return weatherStationDataFiles;
	}
	
	
	/**
	 * Calculating relative humidity with  temperature and dewpoint.
	 * Formula : 
	 * 		RH: =100*( EXP((17.625*TD)/(243.04+TD)) / EXP((17.625*T)/(243.04+T)) )
	 * 			RH : relative humidity
	 * 			TD : dewpoint
	 * 			T  : temperature
	 * @return
	 */
	public static int calculateRelativeHumidity(float temperature, float dewPoint) {
		int relativeHumidity = 0;
		if(temperature != 0.0 && dewPoint != 0.0){
			relativeHumidity = (int) ( 100 * (
												( ( 17.625 * dewPoint ) / ( 243.04 + dewPoint ) ) 
												/
												( ( 17.625 * temperature ) / ( 243.04 + temperature ) )
					                		)
					          		  );
		}
		return relativeHumidity;
	}

}

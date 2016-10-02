package org.cba.weather.app.data.reader.impl;

import org.cba.weather.app.data.WeatherStation;
import org.cba.weather.app.data.reader.WeatherDataReader;
import org.cba.weather.app.exceptions.WeatherDataParseException;


import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Unit test for GameWeatherDataReaderImpl.
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class GameWeatherDataReaderImplTest extends TestCase {

	WeatherDataReader gameWeatherDataReader = new GameWeatherDataReaderImpl ();
	
	/**
	 * testing addWeatherStationDetails positive test cases 
	 */
	public void testAddWeatherStationDetailsForPositiveCase () {
		WeatherStation weatherStation = new WeatherStation ();
		try {
			gameWeatherDataReader.addWeatherStationDetails ( "WEATHER_STATION_DETAILS:Cochin International Airp|10.1520|76.4019|26" ,
					weatherStation );
		} catch ( WeatherDataParseException exp ) {
			Assert.assertFalse ( "Error while parsing weather data , Details :" + exp.getLocalizedMessage () , true );
		}
		Assert.assertEquals ( "Cochin International Airp" , weatherStation.getStationName () );
		Assert.assertEquals ( 10.1520 , weatherStation.getLatitude () );
		Assert.assertEquals ( 76.4019 , weatherStation.getLongitude () );
		Assert.assertEquals ( 26.0 , weatherStation.getAltitude () );
	}
	
	/**
	 * testing addWeatherStationDetails negative cases 
	 */
	public void testAddWeatherStationDetailsForNegativeCase () {
		WeatherStation weatherStation = new WeatherStation ();
		boolean caughtWeatherDataParseException = false;
		try {
			gameWeatherDataReader.addWeatherStationDetails ( "WEATHER_STATION_DETAILS:Cochin International Airp|10.1520|76.4019|26aa" ,
					weatherStation );

		} catch ( WeatherDataParseException exp ) {
			caughtWeatherDataParseException = true;
		}
		Assert.assertTrue ( "Expecting WeatherDataParseException for testAddWeatherStationDetailsForNegativeCase" ,
				caughtWeatherDataParseException );
	}

	/**
	 * testing addSeasonsToWeatherStation positive test cases 
	 */
	public void testAddSeasonsToWeatherStationForPositiveCase () {
		WeatherStation weatherStation = new WeatherStation ();
		try {
			gameWeatherDataReader.addSeasonsToWeatherStation ( "WEATHER_SEASONS:WINTER@1,2,12|SUMMER@3,4,5|SWMONSOON@6,7,8|NEMONSOON@9,10,11" ,
					weatherStation );
			Assert.assertNotNull ( weatherStation.getSeason ( "WINTER" ) );
			Assert.assertNotNull ( weatherStation.getSeason ( "SUMMER" ) );
			Assert.assertNotNull ( weatherStation.getSeason ( "SWMONSOON" ) );
			Assert.assertNotNull ( weatherStation.getSeason ( "NEMONSOON" ) );
			Assert.assertNull ( weatherStation.getSeason ( "XXX" ) );
		
		} catch ( WeatherDataParseException e ) {
			e.printStackTrace ();
		}
	}
	
	/**
	 * testing addSeasonsToWeatherStation negative cases 
	 */
	public void testAddSeasonsToWeatherStationForNegativeCase () {
		WeatherStation weatherStation = new WeatherStation ();
		boolean caughtWeatherDataParseException = false;
		try {
			gameWeatherDataReader.addSeasonsToWeatherStation ( "WEATHER_SEASONS:WINTER@1,2,12|SUMMER@3,4x,5|SWMONSOON@6,7,8|NEMONSOON@9,10,11" ,
					weatherStation );		
		} catch ( WeatherDataParseException e ) {
			caughtWeatherDataParseException = true;
		}
		Assert.assertTrue ( "Expecting WeatherDataParseException for testAddSeasonsToWeatherStationForNegativeCase" ,
				caughtWeatherDataParseException );
	}
	
	/**
	 * testing addExtremeSeasonsToWeatherStation positive test cases 
	 */
	public void testAddExtremeSeasonsToWeatherStationPositiveCase () {
		WeatherStation weatherStation = new WeatherStation ();
		try {
			gameWeatherDataReader.addExtremeSeasonsToWeatherStation ( "EXTREME_SEASONS_WEATHER:CYCLONE@3|FLOODS@5" ,
					weatherStation );
			Assert.assertNotNull ( weatherStation.getExtremeWeatherSeason ( "CYCLONE" ) );
			Assert.assertNotNull ( weatherStation.getExtremeWeatherSeason ( "FLOODS" ) );
			Assert.assertTrue ( weatherStation.isExtremeSeasonPresent () );
		
		} catch ( WeatherDataParseException e ) {
			e.printStackTrace ();
		}
	}
	
	/**
	 * testing addExtremeSeasonsToWeatherStation negative cases 
	 */
	public void testAddExtremeSeasonsToWeatherStationNegativeCase () {
		WeatherStation weatherStation = new WeatherStation ();
		boolean caughtWeatherDataParseException = false;
		try {
			gameWeatherDataReader.addExtremeSeasonsToWeatherStation ( "EXTREME_SEASONS_WEATHER:CYCLONE@3&FLOODS@5" ,
					weatherStation );
		} catch ( WeatherDataParseException e ) {
			caughtWeatherDataParseException = true;
		}
		Assert.assertTrue ( "Expecting WeatherDataParseException for testAddSeasonsToWeatherStationForNegativeCase" ,
				caughtWeatherDataParseException );
	}

}

package org.cba.weather.app.data.reader;

import org.cba.weather.app.data.WeatherStation;
import org.cba.weather.app.exceptions.SeasonNotFoundException;
import org.cba.weather.app.exceptions.WeatherDataParseException;

/**
 * The implemented class need to implements weather data file reading and
 * parsing logic
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public interface WeatherDataReader {
	
	/**
	 * 
	 * This method is the entry point of data parsing. This method read the Key
	 * from each line and based on the key call different data initilizarion or
	 * insertion methods
	 * 
	 * @param line
	 * @param weatherStation
	 * @throws WeatherDataParseException
	 * @throws SeasonNotFoundException
	 */
	public void readAndInsertWeatherDataToWeatherStation ( String line ,WeatherStation weatherStation ) 
			throws WeatherDataParseException, SeasonNotFoundException;

	/**
	 * Adding the weather station details Expected format :
	 * WEATHER_STATION_DETAILS:Station name|Latitude|Longitude|Elevation Eg :
	 * WEATHER_STATION_DETAILS:Cochin International Airp|10.1520|76.4019|26
	 * 
	 * @param data
	 * @throws WeatherDataParseException
	 */
	public void addWeatherStationDetails ( final String data , WeatherStation weatherStation )
			throws WeatherDataParseException;

	/**
	 * Adding the season data from the file
	 * Expected format WEATHER_SEASONS:WINTER@1,2,12|SUMMER@3,4,5|SWMONSOON@6,7,8|NEMONSOON@9,10,11
	 * Each season is divided by '|' symbol
	 * SeasonName@monthInNumber,monthInNumber
	 * 
	 * WINTER@1,2,12  >> Winter season in Jan,feb and Dec
	 * @throws WeatherDataParseException 
	 */
	public void addSeasonsToWeatherStation ( String data , WeatherStation currWeatherStation )
			throws WeatherDataParseException;

	/**
	 * Adding the ExtremeSeasons
	 * EXTREME_SEASONS_WEATHER:CYCLONE@3
	 * 
	 * @param data
	 * @throws WeatherDataParseException 
	 */
	public void addExtremeSeasonsToWeatherStation ( String data , WeatherStation currWeatherStation )
			throws WeatherDataParseException;

}

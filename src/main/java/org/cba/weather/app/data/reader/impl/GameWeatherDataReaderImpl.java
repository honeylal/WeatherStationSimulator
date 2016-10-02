package org.cba.weather.app.data.reader.impl;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.cba.weather.app.AppConstants;
import org.cba.weather.app.data.ExtremeWeatherSeason;
import org.cba.weather.app.data.Season;
import org.cba.weather.app.data.SeasonDay;
import org.cba.weather.app.data.WeatherData;
import org.cba.weather.app.data.WeatherStation;
import org.cba.weather.app.data.reader.WeatherDataReader;
import org.cba.weather.app.exceptions.SeasonNotFoundException;
import org.cba.weather.app.exceptions.WeatherDataParseException;

/**
 * This class is responsible for reading the weather station historical data
 * from the weather station data files(The same data is used as reference to
 * simulate the weather). Also holding the logic how to parse each line in the
 * data file and populate the weather station object.
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class GameWeatherDataReaderImpl implements WeatherDataReader {

	/**
	 * 
	 * This method is the entry point of data parsing. This method read the Key
	 * from each line and based on the key call different data initialization or
	 * insertion methods
	 * 
	 * @param line
	 * @param weatherStation
	 * @throws WeatherDataParseException
	 * @throws SeasonNotFoundException
	 */
	public void readAndInsertWeatherDataToWeatherStation ( String line ,
			WeatherStation weatherStation ) throws WeatherDataParseException, SeasonNotFoundException {
		if ( isCommentLine ( line ) ) {
			return;
		} else if ( line.contains ( AppConstants.WEATHER_STATION_DETAILS_ENTRY_KEY ) ) {
			addWeatherStationDetails ( line , weatherStation );
		} else if ( line.contains ( AppConstants.WEATHER_SEASONS_ENTRY_KEY ) ) {
			addSeasonsToWeatherStation ( line , weatherStation );
		} else if ( line.contains ( AppConstants.EXTREME_SEASONS_WEATHER_ENTRY_KEY ) ) {
			addExtremeSeasonsToWeatherStation ( line , weatherStation );
		} else if ( line.contains ( AppConstants.CURRENT_SEASON_NAME_ENTRY ) ) {
			addCurrentSeasonDetailsToWeatherStationForDataLoad ( line , weatherStation );
		} else if ( line.contains ( AppConstants.CURRENT_EXTREME_SEASONS_NAME_ENTRY ) ) {
			addCurrentExtremeSeasonDetailsToWeatherStationForDataLoad ( line , weatherStation );
		} else if ( line.contains ( AppConstants.CURRENT_SEASON_DAY_ENTRY ) ) {
			addCurrentDayToWeatherStatioForDataLoad ( line , weatherStation );
		} else if ( line.contains ( AppConstants.WETHER_DATA_AM )
				|| line.contains ( AppConstants.WETHER_DATA_PM ) ) {
			addWeatherDataToWeatherStation ( line , weatherStation );
		}
	}

	
	/**
	 * Adding the weather data to one season 
	 * @param currentSeasonName
	 * @param currDay
	 * @param isExtremeSeason
	 * @param weatherData
	 * @throws WeatherDataParseException 
	 */
	public void addWeatherDataToWeatherStation ( String weatherData , WeatherStation weatherStation ) throws WeatherDataParseException {
		try {
			Season currentSeason = null;
			if ( weatherStation.isCurrentDataloadIsForExtremeWeatherSeason () ) {
				currentSeason = weatherStation.getCurrentExtremeSeasonForDataLoad ();
			} else {
				currentSeason = weatherStation.getCurrentSeasonForDataLoad ();
			}
			if ( null != currentSeason ) {
				SeasonDay currSeasonDay = currentSeason.getSeasonDay ( weatherStation.getCurrentDayForDataLoad () );
				if ( null != currSeasonDay ) {
					WeatherData currWeatherData = createWetherData ( weatherData );
					currSeasonDay.addDayWeatherData ( currWeatherData.getTime () , currWeatherData );
				} else {
					SeasonDay seasonDay = new SeasonDay ();
					WeatherData currWeatherData = createWetherData ( weatherData );
					seasonDay.addDayWeatherData ( currWeatherData.getTime () , currWeatherData );
					currentSeason.addSeasonDay ( weatherStation.getCurrentDayForDataLoad () ,
							seasonDay );
				}
			}
		} catch ( Exception exception ) {

			throw new WeatherDataParseException ( "Error while parsing WeatherData \n"
					+ "Expected format : 12:00 AM,27.0,22.0,74,1009,4.0,Calm,Calm,-,N/A,,Haze,0,2014-12-31 18:30:00 \n "
					+ "Actual value : " + weatherData + "\n Error :" + exception.getMessage () );
		}

	}
	
	
	/**
	 * Adding the weather station details 
	 * Expected format : WEATHER_STATION_DETAILS:Station name|Latitude|Longitude|Elevation
	 * Eg : WEATHER_STATION_DETAILS:Cochin International Airp|10.1520|76.4019|26
	 * @param data
	 * @throws WeatherDataParseException 
	 */
	public void addWeatherStationDetails ( String data , WeatherStation weatherStation ) throws WeatherDataParseException {
		try {
			String stationDetails = removeEntryKeyFromData ( data ,
					AppConstants.WEATHER_STATION_DETAILS_ENTRY_KEY );

			StringTokenizer stationDetailTokens = new StringTokenizer ( stationDetails ,
																		"|" );
			if ( stationDetailTokens.hasMoreElements () ) {
				weatherStation.setStationName ( stationDetailTokens.nextElement ()
																	.toString ()
																	.trim () );
			}
			if ( stationDetailTokens.hasMoreElements () ) {
				weatherStation.setLatitude ( Double.valueOf ( stationDetailTokens.nextElement ()
																					.toString () ) );
			}
			if ( stationDetailTokens.hasMoreElements () ) {
				weatherStation.setLongitude ( Double.valueOf ( stationDetailTokens.nextElement ()
																					.toString () ) );
			}
			if ( stationDetailTokens.hasMoreElements () ) {
				weatherStation.setAltitude ( Double.valueOf ( stationDetailTokens.nextElement ()
																					.toString () ) );
			}
		} catch ( Exception exception) {

			throw new WeatherDataParseException ( "Error while parsing WEATHER_STATION_DETAILS \n"
					+ "Expected format : WEATHER_STATION_DETAILS:Station name|Latitude|Longitude|Elevation \n "
					+ "Actual value : " + data + "\n Error :" + exception.getMessage () );
		}
	}
	


	
	/**
	 * Adding the season data from the file
	 * Expected format WEATHER_SEASONS:WINTER@1,2,12|SUMMER@3,4,5|SWMONSOON@6,7,8|NEMONSOON@9,10,11
	 * Each season is divided by '|' symbol
	 * SeasonName@monthInNumber,monthInNumber
	 * 
	 * WINTER@1,2,12  >> Winter season in Jan,feb and Dec
	 * @throws WeatherDataParseException 
	 */
	public void addSeasonsToWeatherStation ( String data , WeatherStation currWeatherStation ) throws WeatherDataParseException {
		try {
			String seasonData = removeEntryKeyFromData ( data ,
					AppConstants.WEATHER_SEASONS_ENTRY_KEY );

			Season weatherSeason = new Season ();
			List < String > seasonDataList = getAllSeasons ( seasonData );
			for ( String currSeasonData : seasonDataList ) {
				List < String > seasonDetailList = getSeasonNameAndSeasonMappingFromSeasonData ( currSeasonData );
				if ( seasonDetailList.size () >= 2 ) {
					// getting the season name
					weatherSeason.setSeasonName ( seasonDetailList.get ( 0 ) );
					// getting season month mapping
					List < String > seasonMonths = getSeasonMonthMappingList ( seasonDetailList.get ( 1 ) );
					for ( String currMonthForMapping : seasonMonths ) {
						currWeatherStation.addMonthSeasonMapping ( Integer.parseInt ( currMonthForMapping ) ,
								weatherSeason.getSeasonName () );
					}
				}else{ // expecting data in SUMMER@3,4,5 but here its failed 
					throw new WeatherDataParseException (currSeasonData);
				}
				currWeatherStation.addSeasonsToWeatherStation ( weatherSeason );
			}
		} catch ( Exception exception ) {
			throw new WeatherDataParseException ( "Error while parsing WEATHER_SEASONS \n"
					+ "Expected format : WEATHER_SEASONS:WINTER@1,2,12|SUMMER@3,4,5|SWMONSOON@6,7,8|NEMONSOON@9,10,11 \n "
					+ "Actual value : " + data + "\n Error " + exception.getMessage () );
		}

	}
	
	/**
	 * Adding the ExtremeSeasons
	 * EXTREME_SEASONS_WEATHER:CYCLONE@3
	 * 
	 * @param data
	 * @throws WeatherDataParseException 
	 */
	public void addExtremeSeasonsToWeatherStation ( String data , WeatherStation currWeatherStation )
					throws WeatherDataParseException {
		try {
			String extremeSeasonsData = removeEntryKeyFromData ( data ,
					AppConstants.EXTREME_SEASONS_WEATHER_ENTRY_KEY );

			ExtremeWeatherSeason extremeWeatherSeason = new ExtremeWeatherSeason ();
			List < String > seasonDataList = getAllSeasons ( extremeSeasonsData );
			for ( String currExtremeSeasonData : seasonDataList ) {
				List < String > extremeSeasonDetailList = getSeasonNameAndSeasonMappingFromSeasonData ( currExtremeSeasonData );
				if ( extremeSeasonDetailList.size () >= 2 ) {
					// getting the season name
					extremeWeatherSeason.setSeasonName ( extremeSeasonDetailList.get ( 0 ) );
					// getting the number of days this season length
					extremeWeatherSeason.setSessonLength ( Integer.parseInt ( extremeSeasonDetailList.get ( 1 ) ) );
				}else{ // expecting data in {seasonName}@{seasonDays} but here its failed 
					throw new WeatherDataParseException (currExtremeSeasonData);
				}
				currWeatherStation.addExtremeSeasonsToWeatherStation ( extremeWeatherSeason );
			}
		} catch ( Exception exception ) {
			throw new WeatherDataParseException ( "Error while parsing EXTREME_SEASONS_WEATHER \n"
					+ "Expected format : EXTREME_SEASONS_WEATHER:CYCLONE@3|FLOODS@5 \n "
					+ "Actual value : " + data + "\n Error :" + exception.getMessage () );
		}
	}
	
	/**
	 * This line from the input data file means the below data after this line
	 * are related to season WINTER also its a normal season
	 * 
	 * @param data
	 *            SEASON_NAME:WINTER
	 * @param weatherStation
	 */
	public void addCurrentSeasonDetailsToWeatherStationForDataLoad ( String data ,
			WeatherStation weatherStation ) throws SeasonNotFoundException {

		String seasonName = removeEntryKeyFromData ( data , AppConstants.CURRENT_SEASON_NAME_ENTRY );
		weatherStation.setCurrentDataloadIsForExtremeWeatherSeason ( false );
		weatherStation.setCurrentSeasonNameForDataLoad ( seasonName );
	}

	/**
	 * This line from the input data file means the below data after this line
	 * are related to extreme season EX1
	 * 
	 * @param data
	 *            EXTREME_SEASONS_NAME:EX1
	 * @param weatherStation
	 */
	public void addCurrentExtremeSeasonDetailsToWeatherStationForDataLoad ( String data ,
			WeatherStation weatherStation ) throws SeasonNotFoundException {

		String extremeSeasonName = removeEntryKeyFromData ( data ,
				AppConstants.CURRENT_EXTREME_SEASONS_NAME_ENTRY );
		weatherStation.setCurrentDataloadIsForExtremeWeatherSeason ( true );
		weatherStation.setCurrentSeasonNameForDataLoad ( extremeSeasonName );
	}
	
	/**
	 * 
	 * @param data SEASON_DAY:1
	 * @param weatherStation
	 */
	public void addCurrentDayToWeatherStatioForDataLoad ( String data ,
			WeatherStation weatherStation ) {
		String dayEntry = removeEntryKeyFromData ( data , AppConstants.CURRENT_SEASON_DAY_ENTRY );
		weatherStation.setCurrentDayForDataLoad ( dayEntry );
	}
	
	/**
	 * Creating weatherData object from the ',' separated weather input string
	 * 
	 * Format 
	 * TimeIST,TemperatureC,Dew PointC,Humidity,Sea Level
	 * PressurehPa,VisibilityKm,Wind Direction,Wind SpeedKm/h,Gust
	 * SpeedKm/h,Precipitationmm,Events,Conditions,WindDirDegrees,DateUTC
	 * 
	 * Example Data 12:30,AM,26.0,23.0,83,1008,4.0,East,3.7,-,N/A,,Haze,80,2015-06-15 19:00:00
	 * 
	 * @param data
	 * @return
	 * @throws WeatherDataParseException 
	 */
	public static WeatherData createWetherData ( String data ) throws WeatherDataParseException {
		WeatherData weatherData = new WeatherData ();
		String weatherDetails[] = data.split ( "\\," );
		weatherData.setTime ( weatherDetails[AppConstants.TIME_INDEX] );
		weatherData.setTemperature ( weatherDetails[AppConstants.TEMPERATURE_INDEX] );
		weatherData.setDewPoint ( weatherDetails[AppConstants.DEWPOINT_INDEX] );
		weatherData.setHumidity ( weatherDetails[AppConstants.HUMIDITY_INDEX] );
		weatherData.setPressure ( weatherDetails[AppConstants.PRESSURE_HPA_INDEX] );
		weatherData.setWeatherCondition ( weatherDetails[AppConstants.CONDITIONS_INDEX] );
		return weatherData;
	}

	/**
	 * Splits input string around matches of '|' Format
	 * Expected sample format : WINTER@1,2,12|SUMMER@3,4,5|SWMONSOON@6,7,8|NEMONSOON@9,10,11
	 * 
	 * @param data
	 * @return
	 */
	private List < String > getAllSeasons ( String data ) {
		List < String > seasons = Arrays.asList ( data.split ( "\\s*\\|\\s*" ) );
		return seasons;
	}

	/**
	 * 
	 * Splits input string around matches of '@' Format
	 * Expected format : {seasonName}@{SeasonMapping} Eg : SUMMER@3,4,5
	 * 
	 * Note : for Season SeasonMapping is month
	 *        For Extreme season SeasonMapping is days
	 * 
	 * @param data
	 *            SUMMER@3,4,5
	 * @return List < String >
	 *            list @index 0 Season name, @index2 mapping data
	 */
	private List < String > getSeasonNameAndSeasonMappingFromSeasonData ( String data ) {
		List < String > seasonDetail = Arrays.asList ( data.split ( "\\s*\\@\\s*" ) );
		return seasonDetail;
	}

	/**
	 * Splits input string around matches of ',' Format
	 * 
	 * Expected format : {month},{month},{month} Eg : 3,4,5
	 * @param data
	 * @return
	 */
	private List < String > getSeasonMonthMappingList ( String data ) {
		List < String > seasonMonth = Arrays.asList ( data.split ( "\\s*,\\s*" ) );
		return seasonMonth;
	}
	
	/**
	 * If the line start with # then its a comments 
	 * @param line
	 * @return
	 */
	public boolean isCommentLine ( String line ) {
		return line.startsWith ( "#" );
	}
	
	/**
	 * Remove EntryKey so that we can parse the actual data 
	 * 
	 * Eg : WEATHER_SEASONS:WINTER@1,2,12|SUMMER@3,4,5|SWMONSOON@6,7,8|NEMONSOON@9,10,11
	 *      to
	 *      WINTER@1,2,12|SUMMER@3,4,5|SWMONSOON@6,7,8|NEMONSOON@9,10,11
	 * @param line
	 * @param entryKey
	 * @return
	 */
	public String removeEntryKeyFromData ( String line , String entryKey ) {
		return line.replaceAll ( entryKey , "" );
	}
}

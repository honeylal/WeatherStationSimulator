package org.cba.weather.app;

import java.util.Arrays;
import java.util.List;

/**
 * Store the application constants
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class AppConstants {

	public static final int WEATHER_STATION_NAME = 0;
	public static final int WEATHER_STATION_LATITUDE = 1;
	public static final int WEATHER_STATION_LONGITUDE = 2;
	public static final int WEATHER_STATION_ELEVATION = 3;

	public static final int RANDOM_SELECTION_DAY_RANGE = 5;

	public static final String WEATHER_STATION_DETAILS_ENTRY_KEY = "WEATHER_STATION_DETAILS:";
	public static final String WEATHER_SEASONS_ENTRY_KEY = "WEATHER_SEASONS:";
	public static final String SEASON_DATE_RANGE_ENTRY_KEY = "SEASON_DATE_RANGE:";
	public static final String EXTREME_SEASONS_WEATHER_ENTRY_KEY = "EXTREME_SEASONS_WEATHER:";

	public static final String CURRENT_EXTREME_SEASONS_NAME_ENTRY = "EXTREME_SEASONS_NAME:";
	public static final String CURRENT_SEASON_NAME_ENTRY = "SEASON_NAME:";
	public static final String CURRENT_SEASON_DAY_ENTRY = "SEASON_DAY:";

	public static final String WETHER_DATA_AM = " AM,";
	public static final String WETHER_DATA_PM = " PM,";

	public static final String WETHER_DATA_FOLDER_NAME = " PM,";
	
	
	public static final List<String> WEATHER_FORECAST_READING_TIMES_LIST = Arrays.asList(
			"12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM",
			"6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM",
			"12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM",
			"6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM");
	
	/**
	 * Format TimeIST,TemperatureC,Dew PointC,Humidity,Sea Level
	 * PressurehPa,VisibilityKm,Wind Direction,Wind SpeedKm/h,Gust
	 * SpeedKm/h,Precipitationmm,Events,Conditions,WindDirDegrees,DateUTC
	 */
	public static final int TIME_INDEX = 0;
	public static final int TEMPERATURE_INDEX = 1;
	public static final int DEWPOINT_INDEX = 2;
	public static final int HUMIDITY_INDEX = 3;
	public static final int PRESSURE_HPA_INDEX = 4;
	public static final int CONDITIONS_INDEX = 11;
}




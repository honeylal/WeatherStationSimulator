package org.cba.weather.app.data;

import java.util.HashMap;
import java.util.List;

import org.cba.weather.app.AppConstants;

/**
 * This class represents one day weather details against one weather season
 * 
 * <P>
 * SeasonDay having a list of WeatherData with respected different time in a
 * single day. WeatherData class that actually holding the weather reading
 * information like temperature, pressure, dewPoint, weatherCondition related to
 * different times in the day.
 * </P>
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class SeasonDay {

	private String dayCalendarDate = "";
	private HashMap < String , WeatherData > dayWeatherData = new HashMap < String , WeatherData > ();

	
	public HashMap < String , WeatherData > getDayWeatherData () {
		return dayWeatherData;
	}
	
	public void setDayWeatherData ( HashMap < String , WeatherData > dayWeatherData ) {
		this.dayWeatherData = dayWeatherData;
	}

	public void addDayWeatherData ( String time , WeatherData currWeatherData ) {
		dayWeatherData.put ( time , currWeatherData );
	}

	public WeatherData getDayWeatherData ( String time ) {
		return dayWeatherData.get ( time );
	}

	public String getDayCalendarDate () {
		return dayCalendarDate;
	}

	public void setDayCalendarDate ( String dayCalendarDate ) {
		this.dayCalendarDate = dayCalendarDate;
	}

	/**
	 * This will print the Season details in the expected syntax
	 * 
	 * @param headerInfo
	 *            Weather station details in the expected syntax (the same
	 *            appended at the beginning of season details before print)
	 */
	public void printMe ( String headerInfo ) {
		List < String > weatherForecastTimes = AppConstants.WEATHER_FORECAST_READING_TIMES_LIST;
		System.out.println ();
		for ( String currTime : weatherForecastTimes ) {
			WeatherData weatherData = dayWeatherData.get ( currTime );
			weatherData.printMe ( headerInfo , dayCalendarDate );
		}
	}

	public String toString () {
		StringBuilder details = new StringBuilder ();
		details.append ( "Total entries:" );
		details.append ( dayWeatherData.size () );
		return details.toString ();
	}
}

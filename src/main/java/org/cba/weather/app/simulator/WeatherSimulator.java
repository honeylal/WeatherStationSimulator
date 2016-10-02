package org.cba.weather.app.simulator;

import java.util.ArrayList;
import java.util.HashMap;

import org.cba.weather.app.data.ConditionReadings;
import org.cba.weather.app.data.DewPointReadings;
import org.cba.weather.app.data.PressureReadings;
import org.cba.weather.app.data.SeasonDay;
import org.cba.weather.app.data.TemperatureReadings;
import org.cba.weather.app.data.WeatherStation;
import org.cba.weather.app.exceptions.SeasonDaysNotFoundException;
import org.cba.weather.app.exceptions.SeasonNotFoundException;
import org.cba.weather.app.exceptions.WeatherDateException;

/**
 * The implemented class need to implements weather simulation logic
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public interface WeatherSimulator {

	/**
	 * This method return simulated season days for the given date range against
	 * given weather station
	 * 
	 * @param currWeatherStation
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws SeasonNotFoundException
	 * @throws SeasonDaysNotFoundException
	 * @throws WeatherDateException 
	 */
	public ArrayList < SeasonDay > simulateWeather ( WeatherStation currWeatherStation ,
			String startDate , String endDate ) throws SeasonNotFoundException ,
			SeasonDaysNotFoundException, WeatherDateException;

	/**
	 * This method return one SeasonDay weather for the given weather station against one season.
	 * Here randomDayRange mean how may random days values we need to take from the current season 
	 * as an input reference to simulated weather.
	 * 
	 * @param currWeatherStation
	 * @param seasonName
	 * @param randomDayRange
	 * @param isExtremeSeasons
	 * @return
	 * @throws SeasonNotFoundException
	 * @throws SeasonDaysNotFoundException
	 */
	public SeasonDay getWeatherSimulationForDay ( WeatherStation currWeatherStation ,
			String seasonName , int randomDayRange , boolean isExtremeSeasons )
			throws SeasonNotFoundException , SeasonDaysNotFoundException;

	/**
	 * 
	 * This method read temperature, pressure, dewPoint and condition form the
	 * random season days, store the values against different
	 * WEATHER_FORECAST_READING_TIMES_LIST.
	 * 
	 * @param temperatureReadingsForSelectedDays
	 * @param pressureReadingsForSelectedDays
	 * @param dewPointReadingsForSelectedDays
	 * @param conditionReadingsForSelectedDays
	 * @param randomSeasonDays
	 */
	public void readAndCategorizeWeatherDataFromAllSelectedSeasonDaysForPrediction (
			HashMap < String , TemperatureReadings > temperatureReadingsForSelectedDays ,
			HashMap < String , PressureReadings > pressureReadingsForSelectedDays ,
			HashMap < String , DewPointReadings > dewPointReadingsForSelectedDays ,
			HashMap < String , ConditionReadings > conditionReadingsForSelectedDays ,
			ArrayList < SeasonDay > randomSeasonDays );
	
	/**
	 * This method needs to implements the logic for simulating the weather data
	 * from randomly selected days against the reading times.
	 * 
	 * @param isAddStandardDeviationToMean
	 * @param standardDeviationDivFactor
	 * @param temperatureReadingsForSelectedDays
	 * @param pressureReadingsForSelectedDays
	 * @param dewPointReadingsForSelectedDays
	 * @param conditionReadingsForSelectedDays
	 * @return
	 */
	public SeasonDay simulateWeatherInRelationWithRandomSeasonDayReadings (
			boolean isAddStandardDeviationToMean , float standardDeviationDivFactor ,
			HashMap < String , TemperatureReadings > temperatureReadingsForSelectedDays ,
			HashMap < String , PressureReadings > pressureReadingsForSelectedDays ,
			HashMap < String , DewPointReadings > dewPointReadingsForSelectedDays ,
			HashMap < String , ConditionReadings > conditionReadingsForSelectedDays ) ;
	
	
	/**
	 * 
	 * This method need to implement the logic for loading data against each
	 * weather stations.
	 * 
	 * @param weatherStationDataFiles
	 *            location where the data file
	 * @return list of weather station that holding the historical weather data
	 *         from the weather data files
	 */

	public ArrayList < WeatherStation > loadWeatherStationsDataFromDataFiles (
			ArrayList < String > weatherStationDataFiles );
	
}

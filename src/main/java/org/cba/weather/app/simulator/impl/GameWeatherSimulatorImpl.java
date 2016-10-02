package org.cba.weather.app.simulator.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.cba.weather.app.AppConstants;
import org.cba.weather.app.data.ConditionReadings;
import org.cba.weather.app.data.DewPointReadings;
import org.cba.weather.app.data.PressureReadings;
import org.cba.weather.app.data.Season;
import org.cba.weather.app.data.SeasonDay;
import org.cba.weather.app.data.TemperatureReadings;
import org.cba.weather.app.data.WeatherData;
import org.cba.weather.app.data.WeatherStation;
import org.cba.weather.app.data.loader.WeatherDataLoader;
import org.cba.weather.app.data.reader.WeatherDataReader;
import org.cba.weather.app.exceptions.SeasonDaysNotFoundException;
import org.cba.weather.app.exceptions.SeasonNotFoundException;
import org.cba.weather.app.exceptions.WeatherDateException;
import org.cba.weather.app.simulator.RandomGenerator;
import org.cba.weather.app.simulator.WeatherSimulator;
import org.cba.weather.app.util.WeatherUtil;

/**
 * 
 * This class implements the logics for artificially simulates weather one or
 * more different weather stations and outputs weather data in a standard
 * format. We need to provide the historical weather information against each
 * weather station to simulate the weather. Also holding the logic to load the
 * historic weather data against each weather stations (using parallel execution
 * with the help of Java thread pool so that the application initialization
 * become faster even if we have multiple stations)
 * 
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class GameWeatherSimulatorImpl implements WeatherSimulator {

	private WeatherDataReader weatherDataReader;
	private RandomGenerator randomGenerator;
	private static final SimpleDateFormat weatherInputDateFormater = new SimpleDateFormat("dd-MM-yyyy");
	
	/**
	 * 
	 * @param weatherDataReader
	 * @param randomGenerator
	 */
	public GameWeatherSimulatorImpl (WeatherDataReader weatherDataReader, RandomGenerator randomGenerator){
		this.weatherDataReader = weatherDataReader;
		this.randomGenerator = randomGenerator;
	}

	/**
	 * Simulate weather against currWeatherStation for date range startDate
	 * 
	 * 
	 * <h1>The logic behind</h1>
	 * <p>
	 * First validate the date range then create a list of days from the date
	 * range. For each day we need to find season that mapped against the month
	 * for the current weather station (this we get from the Weather station
	 * season mapping, but we need to provide the Calendar.MONTH for current
	 * date) then Call getWeatherSimulationForDay()
	 * </p>
	 * 
	 * @param currWeatherStation
	 * @param startInputDate
	 *            Expected format : dd-MM-yyyy
	 * 
	 * @param endInputDate
	 *            Expected format : dd-MM-yyyy
	 * @return List of simulated SeasonDay
	 * @throws SeasonNotFoundException
	 * @throws SeasonDaysNotFoundException
	 * @throws WeatherDateException
	 */
	@Override
	public ArrayList < SeasonDay > simulateWeather ( WeatherStation currWeatherStation ,
			String startInputDate , String endInputDate ) throws SeasonNotFoundException ,
			SeasonDaysNotFoundException, WeatherDateException {

		Calendar startDate = Calendar.getInstance ();
		Calendar endDate = Calendar.getInstance ();
		ArrayList < SeasonDay > simulatedWeatherSeasonDays = new ArrayList < SeasonDay > ();

		validateAndSetInputDatesIntoCalendarDate ( startInputDate ,
				endInputDate ,
				startDate ,
				endDate );
		
		// for each day in the given date range
		for ( ; startDate.before ( endDate ); startDate.add ( Calendar.DATE , 1 ) ) {
			String seasonName = "";
			boolean isExtremeSeasonDay = randomGenerator.isSelectCurrentSimulationDayAsExtremeSeasonsDay ( currWeatherStation.isExtremeSeasonPresent () );
			if ( isExtremeSeasonDay ) {
				seasonName = currWeatherStation.getRandomExtremeSeasonName ();
			} else {
				seasonName = currWeatherStation.getMappedSeasonNameForMonth ( startDate.get ( Calendar.MONTH ) + 1 );
			}

			SeasonDay simulateWeatherSeasonDay = getWeatherSimulationForDay ( currWeatherStation ,
					seasonName ,
					AppConstants.RANDOM_SELECTION_DAY_RANGE ,
					isExtremeSeasonDay );
			// setting the date for simulateWeatherSeasonDay
			simulateWeatherSeasonDay.setDayCalendarDate ( weatherInputDateFormater.format ( startDate.getTime () ) );
			if ( null != simulateWeatherSeasonDay ) {
				simulatedWeatherSeasonDays.add ( simulateWeatherSeasonDay );
			}
		}
		return simulatedWeatherSeasonDays;
		
	}

	/**
	 * 
	 * @param startDate
	 *            Expected format : dd-MM-yyyy
	 * @param endDate
	 *            Expected format : dd-MM-yyyy
	 * @param start
	 * @param end
	 * @throws WeatherDateException
	 *             if date format is not matching or end date before start date
	 */
	private void validateAndSetInputDatesIntoCalendarDate ( String startDate , String endDate ,
			Calendar start , Calendar end ) throws WeatherDateException {
		try {
			start.setTime ( weatherInputDateFormater.parse ( startDate ) );
			end.setTime ( weatherInputDateFormater.parse ( endDate ) );
			end.add ( Calendar.DATE , 1 );
			if ( end.before ( start ) ) {
				throw new WeatherDateException ( "Start date is before end date" );
			}
		} catch ( ParseException e ) {
			System.err.println ( "Error in simulateWeather input date format /n(Expected format : dd-MM-yyyy) " );
			throw new WeatherDateException ( "Error in simulateWeather input date format /n(Expected format : dd-MM-yyyy) \n"
					+ e.getMessage () );
		}
	}

	/**
	 * 
	 * This method load the weather data against each weather stations (using
	 * parallel execution with the help of Java thread pool so that the
	 * application initialization  become faster even if we have multiple stations).
	 * 
	 * @param weatherStationDataFiles
	 * @return
	 */
	public ArrayList < WeatherStation > loadWeatherStationsDataFromDataFiles (
			ArrayList < String > weatherStationDataFiles ) {

		ArrayList < WeatherStation > loadedWeatherStations = new ArrayList < WeatherStation > ();
		ArrayList < Future < WeatherStation >> dataLoadrJobs = new ArrayList < Future < WeatherStation >> ();
		ThreadPoolExecutor dataLoadrJobExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool ( 3 );

		// loading weather station data for each weather station from weather station data file
		for ( String currWeatherStationDataFile : weatherStationDataFiles ) {
			WeatherDataLoader weatherStationDataLoader = new WeatherDataLoader (	currWeatherStationDataFile ,
																	weatherDataReader );
			// Loading the weather station data (parallel loading)
			Future < WeatherStation > weatherStationDataLoadrJob = dataLoadrJobExecutor.submit ( weatherStationDataLoader );
			dataLoadrJobs.add ( weatherStationDataLoadrJob );
		}

		for ( Future < WeatherStation > currDataLoadJob : dataLoadrJobs ) {
			try {
				WeatherStation newWeatherStation = currDataLoadJob.get ();
				loadedWeatherStations.add ( newWeatherStation );
				System.out.println ( "Data load completed for weather station :"
						+ newWeatherStation.getStationName () );

			} catch ( InterruptedException
					| ExecutionException e ) {
				System.err.println ( "Error while loading the weather data" );
				e.printStackTrace ();
			}
		}
		dataLoadrJobExecutor.shutdown ();
		return loadedWeatherStations;
	}
	

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
			throws SeasonNotFoundException , SeasonDaysNotFoundException {
	
		SeasonDay simulatedDay = new SeasonDay ();
		HashMap < String , TemperatureReadings > temperatureReadingsForSelectedDays = new HashMap < String , TemperatureReadings > ();
		HashMap < String , PressureReadings > pressureReadingsForSelectedDays = new HashMap < String , PressureReadings > ();
		HashMap < String , DewPointReadings > dewPointReadingsForSelectedDays = new HashMap < String , DewPointReadings > ();
		HashMap < String , ConditionReadings > conditionReadingsForSelectedDays = new HashMap < String , ConditionReadings > ();

		// this will decide do we need to add or subtract standard deviation
		// from mean to predict the value
		boolean isAddStandardDeviationToMean = randomGenerator.isAddStandardDeviation ();
		float standardDeviationDivFactor = randomGenerator.getRandomNumber ( 1 , 5 );

		Season currSeason = getSeasonFromWeatherStation ( currWeatherStation ,
				seasonName ,
				isExtremeSeasons );
		validateSeason ( seasonName , currSeason );

		// Get 5 random Season Day from the current season
		ArrayList < SeasonDay > randomSeasonDays = getRandomDaysFromSeason ( randomDayRange ,
				currSeason );

		readAndCategorizeWeatherDataFromAllSelectedSeasonDaysForPrediction ( temperatureReadingsForSelectedDays ,
				pressureReadingsForSelectedDays ,
				dewPointReadingsForSelectedDays ,
				conditionReadingsForSelectedDays ,
				randomSeasonDays );

		simulatedDay = simulateWeatherInRelationWithRandomSeasonDayReadings ( isAddStandardDeviationToMean ,
				standardDeviationDivFactor ,
				temperatureReadingsForSelectedDays ,
				pressureReadingsForSelectedDays ,
				dewPointReadingsForSelectedDays ,
				conditionReadingsForSelectedDays );

		return simulatedDay;
	}

	/**
	 * 
	 * This method read temperature, pressure, dewPoint and condition form the
	 * random season days, store the values against different
	 * WEATHER_FORECAST_READING_TIMES_LIST.
	 * Example : if we select 5 random days then temperature for 10:AM from
	 * these 5 days are stored in temperatureReadingsForSelectedDays HashMap as
	 * “10:AM” as key and values as TemperatureReadings(internally hold the list
	 * of temperature readings)
	 * 
	 * @see TemperatureReadings
	 * 
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
			ArrayList < SeasonDay > randomSeasonDays ) {
		// reading temperature pressure dewPoint and condition from the season
		// day entry
		for ( SeasonDay currSeasonDay : randomSeasonDays ) {
			// against weather reading times
			for ( String readingTime : AppConstants.WEATHER_FORECAST_READING_TIMES_LIST ) {
				WeatherData currWeatherData = currSeasonDay.getDayWeatherData ( readingTime );
				if ( null != currWeatherData ) {

					if ( null == temperatureReadingsForSelectedDays.get ( readingTime ) ) {
						temperatureReadingsForSelectedDays.put ( readingTime ,
								new TemperatureReadings () );
					}
					TemperatureReadings currTimeTempReading = temperatureReadingsForSelectedDays.get ( readingTime );
					currTimeTempReading.getTemperatureRedingValues ()
										.add ( currWeatherData.getTemperature () );

					if ( null == pressureReadingsForSelectedDays.get ( readingTime ) ) {
						pressureReadingsForSelectedDays.put ( readingTime , new PressureReadings () );
					}
					PressureReadings currPressureReadings = pressureReadingsForSelectedDays.get ( readingTime );
					currPressureReadings.getPressureRedingValues ()
										.add ( currWeatherData.getPressure () );

					if ( null == dewPointReadingsForSelectedDays.get ( readingTime ) ) {
						dewPointReadingsForSelectedDays.put ( readingTime , new DewPointReadings () );
					}
					DewPointReadings currDewPointReadings = dewPointReadingsForSelectedDays.get ( readingTime );
					currDewPointReadings.getDewPointRedingValues ()
										.add ( currWeatherData.getDewPoint () );

					if ( null == conditionReadingsForSelectedDays.get ( readingTime ) ) {
						conditionReadingsForSelectedDays.put ( readingTime ,
								new ConditionReadings () );
					}
					ConditionReadings currConditionReadings = conditionReadingsForSelectedDays.get ( readingTime );
					currConditionReadings.setConditionReadingsValue ( currWeatherData.getWeatherCondition () );
				}
			}
		}
	}
	

	/**
	 * This method implements the logic for simulating the weather data
	 * from randomly selected days against the reading times.
	 * 
	 * 
	 * @param isAddStandardDeviationToMean
	 * @param standardDeviationDivFactor
	 * @param temperatureReadingsForSelectedDays
	 * @param pressureReadingsForSelectedDays
	 * @param dewPointReadingsForSelectedDays
	 * @param conditionReadingsForSelectedDays
	 */
	public SeasonDay simulateWeatherInRelationWithRandomSeasonDayReadings (
			boolean isAddStandardDeviationToMean , float standardDeviationDivFactor ,
			HashMap < String , TemperatureReadings > temperatureReadingsForSelectedDays ,
			HashMap < String , PressureReadings > pressureReadingsForSelectedDays ,
			HashMap < String , DewPointReadings > dewPointReadingsForSelectedDays ,
			HashMap < String , ConditionReadings > conditionReadingsForSelectedDays ) {

		SeasonDay simulatedDay = new SeasonDay ();
		for ( String weatherReadingTime : AppConstants.WEATHER_FORECAST_READING_TIMES_LIST ) {
			WeatherData generatedWeatherData = new WeatherData ();

			float temperature = 0.0f;
			if ( null != temperatureReadingsForSelectedDays.get ( weatherReadingTime ) )
				temperature = simulateNewDataWithRelatedToListOfPreviousReadings ( temperatureReadingsForSelectedDays.get ( weatherReadingTime )
																														.getTemperatureRedingValues () ,
						isAddStandardDeviationToMean ,
						standardDeviationDivFactor );
			float pressure = 0.0f;
			if ( null != pressureReadingsForSelectedDays.get ( weatherReadingTime ) )
				pressure = simulateNewDataWithRelatedToListOfPreviousReadings ( pressureReadingsForSelectedDays.get ( weatherReadingTime )
																												.getPressureRedingValues () ,
						isAddStandardDeviationToMean ,
						standardDeviationDivFactor );
			float dewPoint = 0.0f;
			if ( null != dewPointReadingsForSelectedDays.get ( weatherReadingTime ) )
				dewPoint = simulateNewDataWithRelatedToListOfPreviousReadings ( dewPointReadingsForSelectedDays.get ( weatherReadingTime )
																												.getDewPointRedingValues () ,
						isAddStandardDeviationToMean ,
						standardDeviationDivFactor );
			String MostFrequestCondition = "--";
			if ( null != conditionReadingsForSelectedDays.get ( weatherReadingTime ) )
				MostFrequestCondition = conditionReadingsForSelectedDays.get ( weatherReadingTime )
																		.getMostFrequentConditionSeason ();

			generatedWeatherData.setTime ( weatherReadingTime );
			generatedWeatherData.setTemperature ( temperature );
			generatedWeatherData.setPressure ( pressure );
			generatedWeatherData.setDewPoint ( dewPoint );
			generatedWeatherData.setWeatherCondition ( MostFrequestCondition );

			simulatedDay.addDayWeatherData ( weatherReadingTime , generatedWeatherData );

		}
		return simulatedDay;
	}

	/**
	 * 
	 * This method select random season days from the current season, Random
	 * index selection is based on the RandomGenerator getRandomNumbe
	 * implementation
	 * 
	 * @param randomDayRange
	 * @param currSeason
	 * @param totalDummyDays
	 * @param randomSeasonDays
	 */
	private ArrayList < SeasonDay > getRandomDaysFromSeason ( int randomDayRange , Season currSeason ) {
		ArrayList < SeasonDay > randomSeasonDays = new ArrayList < SeasonDay > ();
		int totalSeasonDays = currSeason.getTotalSeasonDayCount ();
		// randomly selecting the days from the current season
		for ( int i = 0; i < randomDayRange; i++ ) {
			int randomSeasonDayIndex = randomGenerator.getRandomNumber ( 1 , totalSeasonDays + 1 );
			randomSeasonDays.add ( currSeason.getSeasonDay ( randomSeasonDayIndex ) );
		}
		return randomSeasonDays;
	}

	/**
	 * This method check the current season object has at least one season day
	 * in it.
	 * 
	 * @param seasonName
	 * @param currSeason
	 * @return
	 * @throws SeasonNotFoundException
	 * @throws SeasonDaysNotFoundException
	 */
	private void validateSeason ( String seasonName , Season currSeason )
			throws SeasonNotFoundException , SeasonDaysNotFoundException {
		if ( null == currSeason ) {
			throw new SeasonNotFoundException ( "The season (" + seasonName
					+ ") is not configured in the system!!!" );
		}
		if ( currSeason.getTotalSeasonDayCount () < 1 ) {
			throw new SeasonDaysNotFoundException ( "The season (" + seasonName
					+ ") not having any days data!!!" );
		}

	}

	/**
	 * @param currWeatherStation
	 * @param seasonName
	 * @param isExtremeSeasons
	 * @return
	 */
	private Season getSeasonFromWeatherStation ( WeatherStation currWeatherStation ,
			String seasonName , boolean isExtremeSeasons ) {
		Season currSeason;
		if ( isExtremeSeasons ) {
			currSeason = currWeatherStation.getExtremeWeatherSeason ( seasonName );
		} else {
			currSeason = currWeatherStation.getSeason ( seasonName );
		}
		return currSeason;
	}

	/**
	 * This function holding the logic for simulating the reading from previous
	 * readings
	 * 
	 * <p>
	 * - Here is the logic (Consider this case : We have 5 temperature entries
	 * for 01:00 PM with that how we are predicting).
	 * 
	 * - First we find the mean of these 5 values.<br>
	 * - Next we find the standard deviation.<br>
	 * - So this is saying the most frequent predictive values range from <br>
	 * (mean- standard deviation) to (mean + standard deviation)<br>
	 * - As the next step we find the appliedDeviation from StandardDeviation by
	 * dividing its by standardDeviationDivFactor, the resulting applied
	 * appliedDeviation is added or subtracted from the mean to get the
	 * simulated value.
	 * 
	 * </p>
	 * 
	 * @param dataList
	 * @param isAddStandardDeviation
	 *            this flag will decide either we need to add or subtract the
	 *            appliedDeviation from mean to simulate the value
	 * 
	 * @param standardDeviationDivFactor
	 *            this Div factor help to find the applied deviation value.
	 *            Logic : appliedDeviation = 
	 *                    StandardDeviation / standardDeviationDivFactor
	 * @return
	 */
	public float simulateNewDataWithRelatedToListOfPreviousReadings ( ArrayList < Float > dataList ,
			boolean isAddStandardDeviation , float standardDeviationDivFactor ) {
		float value = 0.0f;
		float meanData = WeatherUtil.getMean ( dataList );
		float sampleStandardDeviation = WeatherUtil.getStandardDeviation ( dataList , meanData );
		float appliedDeviation = sampleStandardDeviation / standardDeviationDivFactor;

		if ( isAddStandardDeviation ) {
			value = meanData + appliedDeviation;
		} else {
			value = meanData + appliedDeviation;
		}
		return value;
	}
	
}
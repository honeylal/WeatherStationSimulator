package org.cba.weather.app.data.reader.impl;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.cba.weather.app.data.ConditionReadings;
import org.cba.weather.app.data.DewPointReadings;
import org.cba.weather.app.data.PressureReadings;
import org.cba.weather.app.data.Season;
import org.cba.weather.app.data.SeasonDay;
import org.cba.weather.app.data.TemperatureReadings;
import org.cba.weather.app.data.WeatherStation;
import org.cba.weather.app.data.reader.WeatherDataReader;
import org.cba.weather.app.exceptions.WeatherDataFilesNotFoundException;
import org.cba.weather.app.simulator.RandomGenerator;
import org.cba.weather.app.simulator.WeatherSimulator;
import org.cba.weather.app.simulator.impl.GameRandomGeneratorImpl;
import org.cba.weather.app.simulator.impl.GameWeatherSimulatorImpl;
import org.cba.weather.app.util.WeatherUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Unit test for GameWeatherSimulatorImpl.
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class GameWeatherSimulatorImplTest extends TestCase{
	
	// Hold the weather station data for all the JUnit test method in the same class
	private static ArrayList < WeatherStation > weatherStations = null; 

	/**
	 * This method will read the test weather station data from the data file
	 * (loading will happen only once for all the Junit test methods in this
	 * class)
	 * 
	 * @throws WeatherDataFilesNotFoundException
	 */
	private  void loadData() throws WeatherDataFilesNotFoundException{
		if(null == weatherStations){
			weatherStations = new ArrayList < WeatherStation > ();
			RandomGenerator randomGenerator = new GameRandomGeneratorImpl ();
			WeatherDataReader gameWeatherDataReader = new GameWeatherDataReaderImpl ();
			WeatherSimulator gameWeatherSimulator = new GameWeatherSimulatorImpl (	gameWeatherDataReader ,
																				randomGenerator );
			StringBuilder weatherStationDataFolder = new StringBuilder ();
			weatherStationDataFolder.append ( Paths.get ( "." )
													.toAbsolutePath ()
													.normalize ()
													.toString () )
									.append ( File.separator )
									.append ( "weatherTestData" );
			ArrayList < String > weatherStationDataFiles = WeatherUtil.getWeatherStationDataFilesFromFolder ( weatherStationDataFolder.toString () );
			weatherStations = gameWeatherSimulator.loadWeatherStationsDataFromDataFiles ( weatherStationDataFiles );
		}
	}
	

	
	/**
	 * testing weather station data properly loading from the data files
	 */
	public void testWeatherStationDataAreProperlyLoadingFromTheDataFiles () {
		boolean caughtException = false;
		try {

			loadData ();

			Assert.assertNotNull ( weatherStations );
			Assert.assertEquals ( 1 , weatherStations.size () );
			Assert.assertNotNull ( weatherStations.get ( 0 ) );
			Assert.assertEquals ( "Test Airp|10.15,76.4,26.0" ,
					weatherStations.get ( 0 )
									.getWeatherStationDetails () );
			// testing all season are loaded
			Assert.assertNotNull ( weatherStations.get ( 0 )
													.getSeason ( "WINTER" ) );
			Assert.assertNotNull ( weatherStations.get ( 0 )
													.getSeason ( "SUMMER" ) );
			Assert.assertNotNull ( weatherStations.get ( 0 )
													.getSeason ( "SWMONSOON" ) );
			Assert.assertNotNull ( weatherStations.get ( 0 )
													.getSeason ( "NEMONSOON" ) );

			// testing extreme season loaded
			Assert.assertNotNull ( weatherStations.get ( 0 )
													.getExtremeWeatherSeason ( "CYCLONE" ) );

			// testing one season
			Season testSeason = weatherStations.get ( 0 )
												.getSeason ( "WINTER" );
			Assert.assertNotNull ( testSeason );
			Assert.assertEquals ( 9 , testSeason.getTotalSeasonDayCount () );

		} catch ( Exception e ) {
			e.printStackTrace ();
			caughtException = true ;
		}
		Assert.assertFalse ( "Exception while testing testWeatherStationDataAreProperlyLoadingFromTheDataFiles " ,
				caughtException );
	}

	/**
	 * for testing simulateWeather implementation 
	 */
	public void testTheSimulateWeatherFunctionalityOfGameWeatherSimulatorImpl () {
		boolean caughtException = false;
		try {
			RandomGenerator randomGenerator = new GameRandomGeneratorImpl ();
			WeatherDataReader gameWeatherDataReader = new GameWeatherDataReaderImpl ();
			WeatherSimulator gameWeatherSimulator = new GameWeatherSimulatorImpl (	gameWeatherDataReader ,
																					randomGenerator );
			loadData ();

			ArrayList < SeasonDay > simulatedDays = gameWeatherSimulator.simulateWeather ( weatherStations.get ( 0 ) ,
					"01-01-2015" ,
					"05-01-2015" );
			Assert.assertNotNull ( simulatedDays );
			Assert.assertEquals ( 5 , simulatedDays.size () );
			
			// checking for all days values present 
			for(SeasonDay currDay : simulatedDays){
				Assert.assertEquals ( 24 , currDay.getDayWeatherData ().size ());
			}

		} catch ( Exception e ) {
			e.printStackTrace ();
			caughtException = true;
		}
		Assert.assertFalse ( "Exception while testing testTheSimulateWeatherFunctionalityOfGameWeatherSimulatorImpl " ,
				caughtException );
	}
	
	/**
	 * testing the weather data simulation logic
	 */
	public void testSimulateWeatherInRelationWithRandomSeasonDayReadings () {
		boolean caughtException = false;
		try{
			RandomGenerator randomGenerator = new GameRandomGeneratorImpl ();
			WeatherDataReader gameWeatherDataReader = new GameWeatherDataReaderImpl ();
			WeatherSimulator gameWeatherSimulator = new GameWeatherSimulatorImpl (	gameWeatherDataReader ,
																					randomGenerator );
			loadData ();
			
			WeatherStation weatherStation = weatherStations.get ( 0 );
			Season season = weatherStation.getSeason ( "WINTER" );
			ArrayList < SeasonDay > selectedSeasonDay = new ArrayList<SeasonDay> ();
			selectedSeasonDay.add (season.getSeasonDay ( 1 ));
			selectedSeasonDay.add (season.getSeasonDay ( 2 ));
			selectedSeasonDay.add (season.getSeasonDay ( 3 ));
			selectedSeasonDay.add (season.getSeasonDay ( 4 ));
			selectedSeasonDay.add (season.getSeasonDay ( 5 ));

			HashMap < String , TemperatureReadings > temperatureReadingsForSelectedDays = new HashMap < String , TemperatureReadings > ();
			HashMap < String , PressureReadings > pressureReadingsForSelectedDays = new HashMap < String , PressureReadings > ();
			HashMap < String , DewPointReadings > dewPointReadingsForSelectedDays = new HashMap < String , DewPointReadings > ();
			HashMap < String , ConditionReadings > conditionReadingsForSelectedDays = new HashMap < String , ConditionReadings > ();

			gameWeatherSimulator.readAndCategorizeWeatherDataFromAllSelectedSeasonDaysForPrediction ( temperatureReadingsForSelectedDays ,
					pressureReadingsForSelectedDays ,
					dewPointReadingsForSelectedDays ,
					conditionReadingsForSelectedDays ,
					selectedSeasonDay );
	  
			SeasonDay simulatedDay = gameWeatherSimulator.simulateWeatherInRelationWithRandomSeasonDayReadings ( true ,
					1 ,
					temperatureReadingsForSelectedDays ,
					pressureReadingsForSelectedDays ,
					dewPointReadingsForSelectedDays ,
					conditionReadingsForSelectedDays );
			
			
			ArrayList < Float > temperatureReadingsFor10AM = temperatureReadingsForSelectedDays.get ( "10:00 AM" )
																								.getTemperatureRedingValues ();
			ArrayList < Float > pressureReadingsFor10AM = pressureReadingsForSelectedDays.get ( "10:00 AM" )
																							.getPressureRedingValues ();
			ArrayList < Float > dewPointReadingsFor10AM = dewPointReadingsForSelectedDays.get ( "10:00 AM" )
																							.getDewPointRedingValues ();
			// calculating the predicted temperature
			float calculatedTemperatureValue = 0.0f;
			float temperatureMeanData = WeatherUtil.getMean ( temperatureReadingsFor10AM );
			float temperatureStandardDeviation = WeatherUtil.getStandardDeviation ( temperatureReadingsFor10AM ,
					temperatureMeanData );

			calculatedTemperatureValue = temperatureMeanData + temperatureStandardDeviation;
			// comparing temperature with simulated value
			Assert.assertEquals ( calculatedTemperatureValue ,
					simulatedDay.getDayWeatherData ( "10:00 AM" )
								.getTemperature () );
			
			// calculating the predicted pressure
			float calculatedPressureValue = 0.0f;
			float pressureMeanData = WeatherUtil.getMean ( pressureReadingsFor10AM );
			float pressureStandardDeviation = WeatherUtil.getStandardDeviation ( pressureReadingsFor10AM ,
					pressureMeanData );

			calculatedPressureValue = pressureMeanData + pressureStandardDeviation;
			// comparing pressure with simulated value
			Assert.assertEquals ( calculatedPressureValue ,
					simulatedDay.getDayWeatherData ( "10:00 AM" )
								.getPressure () );

			// calculating the predicted DewPoint
			float calculatedDewPointValue = 0.0f;
			float dewPointMeanData = WeatherUtil.getMean ( dewPointReadingsFor10AM );
			float dewPointStandardDeviation = WeatherUtil.getStandardDeviation ( dewPointReadingsFor10AM ,
					dewPointMeanData );

			calculatedDewPointValue = dewPointMeanData + dewPointStandardDeviation;
			// comparing DewPoint with simulated value
			Assert.assertEquals ( calculatedDewPointValue ,
					simulatedDay.getDayWeatherData ( "10:00 AM" )
								.getDewPoint () );

		}catch (Exception e){
			e.printStackTrace ();
			caughtException = true;
		}
		Assert.assertFalse ( "Exception while testing testSimulateWeatherInRelationWithRandomSeasonDayReadings " ,
				caughtException );
	}

}

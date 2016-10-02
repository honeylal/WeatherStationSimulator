package org.cba.weather.app;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.cba.weather.app.data.SeasonDay;
import org.cba.weather.app.data.WeatherStation;
import org.cba.weather.app.data.reader.WeatherDataReader;
import org.cba.weather.app.data.reader.impl.GameWeatherDataReaderImpl;
import org.cba.weather.app.exceptions.SeasonDaysNotFoundException;
import org.cba.weather.app.exceptions.SeasonNotFoundException;
import org.cba.weather.app.exceptions.WeatherDataFilesNotFoundException;
import org.cba.weather.app.exceptions.WeatherDateException;
import org.cba.weather.app.simulator.RandomGenerator;
import org.cba.weather.app.simulator.WeatherSimulator;
import org.cba.weather.app.simulator.impl.GameRandomGeneratorImpl;
import org.cba.weather.app.simulator.impl.GameWeatherSimulatorImpl;
import org.cba.weather.app.util.WeatherUtil;

/**
 * 
 * Entry point of WeatherStationSimulator Application This class responsible for
 * loading and initializing the WeatherStation and call any of the
 * WeatherSimulator implementation (currently we have only one) and simulate the
 * weather conditions
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class WeatherStationSimulatorApp {

	private ArrayList < WeatherStation > weatherStations = new ArrayList < WeatherStation > ();

	private WeatherSimulator weatherSimulator;

	public WeatherStationSimulatorApp ( WeatherSimulator weatherSimulator ) {
		this.weatherSimulator = weatherSimulator;
	}

	public static void main ( String[] args ) {

		try {

			// Injecting different implementations on the creation time
			// TODO we can replace it with Spring DI, so that for injecting 
			// different implementation will not affect code changes
			RandomGenerator randomGenerator = new GameRandomGeneratorImpl ();
			WeatherDataReader gameWeatherDataReader = new GameWeatherDataReaderImpl ();
			WeatherSimulator gameWeatherSimulator = new GameWeatherSimulatorImpl (	gameWeatherDataReader ,
																				randomGenerator );
			WeatherStationSimulatorApp simulatorApp = new WeatherStationSimulatorApp ( gameWeatherSimulator );

			
			simulatorApp.init ();
			ArrayList < WeatherStation > loadedWeatherStations = simulatorApp.getWeatherStations ();
			
			//test Simulation 
			WeatherStation weatherStation = loadedWeatherStations.get ( 0 );
			ArrayList < SeasonDay > simulatedDays = simulatorApp.simulateWeather ( weatherStation ,
					"01-01-2013" ,
					"03-01-2013" );
			// print the simulated weather 
			for (SeasonDay currSeasonDay : simulatedDays){
				currSeasonDay.printMe ( weatherStation.getWeatherStationDetails () );
			}

		} catch ( SeasonNotFoundException
				| SeasonDaysNotFoundException
				| WeatherDataFilesNotFoundException
				| WeatherDateException e ) {
			System.err.println ( "The data are not configured for some seasons!!!!" );
			e.printStackTrace ();
		}

	}
    
	/**
	 * This function initialise the application with weather station data
	 * 
	 * @throws WeatherDataFilesNotFoundException
	 */
	public void init () throws WeatherDataFilesNotFoundException {
		
		System.out.println ( "Initializing the application with weather station data..." );
		String dataFolderPath = getWeatherStationDataFolderAbsolutePath ();
		ArrayList < String > weatherStationDataFiles = WeatherUtil.getWeatherStationDataFilesFromFolder ( dataFolderPath );
		weatherStations = weatherSimulator.loadWeatherStationsDataFromDataFiles ( weatherStationDataFiles );
		System.out.println ( "Application weather station data initialization completed!" );
	}
    
    
	/**
	 * @return weather data folder path (/weatherData from the Jar file path)
	 *         where the sample data files against each weather station stored
	 */
	public String getWeatherStationDataFolderAbsolutePath () {
		StringBuilder weatherStationDataFolder = new StringBuilder ();
		weatherStationDataFolder.append ( Paths.get ( "." )
												.toAbsolutePath ()
												.normalize ()
												.toString () )
								.append ( File.separator )
								.append ( "weatherData" );
		return weatherStationDataFolder.toString ();
	}

	/**
	 * @return Weather station count
	 */
	public int getWeatherStationCount () {
		return weatherStations.size ();
	}

	/**
	 * @return all loaded weather stations
	 */
	public ArrayList < WeatherStation > getWeatherStations () {
		return weatherStations;
	}

	public void setWeatherStations ( ArrayList < WeatherStation > weatherStations ) {
		this.weatherStations = weatherStations;
	}

	
	public WeatherSimulator getWeatherSimulator () {
		return weatherSimulator;
	}

	
	public void setWeatherSimulator ( WeatherSimulator weatherSimulator ) {
		this.weatherSimulator = weatherSimulator;
	}

	/**
	 * Simulate weather against currWeatherStation for date range startDate to
	 * endDate Using the current weather simulator implementation that injected
	 * in WeatherStationSimulatorApp
	 * 
	 * @param currWeatherStation
	 * @param startDate
	 *            Expected format : dd-MM-yyyy
	 * @param endDate
	 *            Expected format : dd-MM-yyyy
	 * @return
	 * @throws SeasonNotFoundException
	 * @throws SeasonDaysNotFoundException
	 */
	public ArrayList < SeasonDay > simulateWeather ( WeatherStation currWeatherStation ,
			String startDate , String endDate ) throws SeasonNotFoundException ,
			SeasonDaysNotFoundException, WeatherDateException {
		return weatherSimulator.simulateWeather ( currWeatherStation , startDate , endDate );
	}
 	
}

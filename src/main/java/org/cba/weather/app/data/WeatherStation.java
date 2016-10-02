package org.cba.weather.app.data;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.cba.weather.app.exceptions.SeasonNotFoundException;
import org.cba.weather.app.util.WeatherUtil;

/**
 * This class represents one WeatherStation, this class hold the historical
 * weather data for this WeatherStation, the same is used to artificially
 * simulates the weather conditions.
 * 
 * <P>
 * WeatherStation having list of Seasons (store different weather seasons that
 * come under that weather station eg Winter, Summer, Monsoon etc.. we can
 * configure up to 12 seasons against one weather station) and ExtremeWeatherSeason
 * (store different extreme season that come under that weather station example
 * Floods, Cyclone etc.... Note: If you need more fancy, add more details in to
 * it.... ). ExtremeWeatherSeason is a sub class of Seasons (so it has all the
 * public features of Seasons and some extra .....). For every Season we can
 * configure n number of SeasonDay’s (represents one day weather details) more
 * number of SeasonDay’s help the application to generate more random weather
 * readings. SeasonDay having a list of WeatherData with respected different
 * time in a single day. WeatherData class that actually holding the weather
 * reading information like temperature, pressure, dewPoint, weatherCondition
 * etc related to different times in the day.
 * </P>
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class WeatherStation {

	private String stationName;
	private double latitude;
	private double longitude;
	private double altitude;

	private String currentSeasonNameForDataLoad = "";
	private int currentDayForDataLoad = 0;
	private boolean currentDataloadIsExtremeWeatherSeason = false;

	// to store moth to seasons mapping
	TreeMap < Integer , String > seasonMapping = new TreeMap < Integer , String > ();
	// to store different seasons that come under this weather station
	HashMap < String , Season > weatherStationSeasons = new HashMap < String , Season > ();

	// to store some extreme weather season that come under this weather station
	HashMap < String , ExtremeWeatherSeason > weatherStationExtremeSeasons = new HashMap < String , ExtremeWeatherSeason > ();
	ArrayList < String > extremeSeasonsNames = new ArrayList < String > ();

	public HashMap < String , Season > getWeatherStationSeasons () {
		return weatherStationSeasons;
	}

	public void setWeatherStationSeasons ( HashMap < String , Season > weatherStationSeasons ) {
		this.weatherStationSeasons = weatherStationSeasons;
	}

	public HashMap < String , ExtremeWeatherSeason > getWeatherStationExtremeSeasons () {
		return weatherStationExtremeSeasons;
	}

	public void addExtremeSeasonsToWeatherStation ( ExtremeWeatherSeason extremeWeatherSeason ) {
		weatherStationExtremeSeasons.put ( extremeWeatherSeason.getSeasonName () ,
				extremeWeatherSeason );
		extremeSeasonsNames.add ( extremeWeatherSeason.getSeasonName () );
	}

	public void addSeasonsToWeatherStation ( Season season ) {
		weatherStationSeasons.put ( season.getSeasonName () , season );
	}

	public void setWeatherStationExtremeSeasons (
			HashMap < String , ExtremeWeatherSeason > weatherStationExtremeSeasons ) {
		this.weatherStationExtremeSeasons = weatherStationExtremeSeasons;
	}

	public String getStationName () {
		return stationName;
	}

	public void setStationName ( String stationName ) {
		this.stationName = stationName;
	}

	public double getLatitude () {
		return latitude;
	}

	public void setLatitude ( double latitude ) {
		this.latitude = latitude;
	}

	public double getLongitude () {
		return longitude;
	}

	public void setLongitude ( double longitude ) {
		this.longitude = longitude;
	}

	public double getAltitude () {
		return altitude;
	}

	public String toString(){
		return "" + stationName+
				"|" +latitude+
				"|" +longitude+
				"|" +altitude;
	}
	
	/**
	 * @return "," separated triple containing latitude longitude and elevation(altitude) 
	 */
	public String getPosition(){
		StringBuffer position = new StringBuffer();
		position.append ( Math.round ( getLatitude () * 100.0 ) / 100.0 )
				.append ( "," )
				.append ( Math.round ( getLongitude () * 100.0 ) / 100.0 )
				.append ( "," )
				.append ( Math.round ( getAltitude () * 100.0 ) / 100.0 );
		return position.toString ();
	}

	/**
	 * Return format 
	 * Station Name|Position
	 * @return
	 */
    public String getWeatherStationDetails (){
    	return getStationName()+ "|" +getPosition();
    }
	
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	public void addMonthSeasonMapping (int month, String seasonName){
		seasonMapping.put(month, seasonName);
	}
	
	public String getMappedSeasonNameForMonth (int month){
		return seasonMapping.get(month);
	}
	
	public boolean isExtremeSeasonPresent () {
		if ( null != weatherStationExtremeSeasons && weatherStationExtremeSeasons.size () > 0 ) {
			return true;
		} else {
			return false;
		}
	}
	
	//TODO  need to move to common class 
	public String getRandomExtremeSeasonName () {
		Calendar calendar = Calendar.getInstance ();
		return extremeSeasonsNames.get ( (int) ( ( calendar.getTimeInMillis () % extremeSeasonsNames.size () ) ) );
	}

	public Season getSeason ( String seasonName ) {
		return weatherStationSeasons.get ( seasonName );
	}

	public ExtremeWeatherSeason getExtremeWeatherSeason ( String seasonName ) {
		return weatherStationExtremeSeasons.get ( seasonName );
	}

	/**'
	 * For inspecting the data after data load 
	 */
	public void printMe (){
		System.out.println(toString());
		System.out.println("Total seasons" + seasonMapping.size());
		for (Map.Entry<String, Season> entry : weatherStationSeasons.entrySet()) {
			String seasonName = entry.getKey();
			Season currSeason = entry.getValue();
			System.out.println(seasonName);
			currSeason.printMe(getWeatherStationDetails());
		}
		for (Map.Entry<String, ExtremeWeatherSeason> entry : weatherStationExtremeSeasons
				.entrySet()) {
			String seasonName = entry.getKey();
			ExtremeWeatherSeason currSeason = entry.getValue();
			System.out.println(seasonName);
			currSeason.printMe(getWeatherStationDetails());
		}
	}
	
	public String getCurrentSeasonNameForDataLoad () {
		return currentSeasonNameForDataLoad;
	}

	public void setCurrentSeasonNameForDataLoad ( String currentSeasonForDataLoad )
			throws SeasonNotFoundException {
		if ( isCurrentDataloadIsForExtremeWeatherSeason () ) {
			if ( null == getExtremeWeatherSeason ( currentSeasonForDataLoad ) ) {
				throw new SeasonNotFoundException ( "Season " + currentSeasonForDataLoad
						+ " not configured!" );
			}
		}
		this.currentSeasonNameForDataLoad = currentSeasonForDataLoad;
	}

	public int getCurrentDayForDataLoad () {
		return currentDayForDataLoad;
	}

	public void setCurrentDayForDataLoad ( int currentDayForDataLoad ) {
		this.currentDayForDataLoad = currentDayForDataLoad;
	}

	public void setCurrentDayForDataLoad ( String currentDayForDataLoad ) {
		boolean isCurrentDayForDataLoadIsInt = WeatherUtil.isInteger ( currentDayForDataLoad );
		if ( isCurrentDayForDataLoadIsInt ) {
			setCurrentDayForDataLoad ( Integer.parseInt ( currentDayForDataLoad ) );
		}
	}

	public boolean isCurrentDataloadIsForExtremeWeatherSeason () {
		return currentDataloadIsExtremeWeatherSeason;
	}

	public void setCurrentDataloadIsForExtremeWeatherSeason (
			boolean currentDataloadIsExtremeWeatherSeason ) {
		this.currentDataloadIsExtremeWeatherSeason = currentDataloadIsExtremeWeatherSeason;
	}

	public Season getCurrentSeasonForDataLoad () {
		Season currSeason = null;
		currSeason = weatherStationSeasons.get ( currentSeasonNameForDataLoad );
		return currSeason;
	}

	public Season getCurrentExtremeSeasonForDataLoad () {
		Season currSeason = null;
		currSeason = weatherStationExtremeSeasons.get ( currentSeasonNameForDataLoad );
		return currSeason;
	}
}

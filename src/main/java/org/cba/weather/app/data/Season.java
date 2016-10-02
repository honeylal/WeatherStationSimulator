package org.cba.weather.app.data;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * This class represents one weather season present in that weather station ans
 * store historical weather readings different days present in the season in
 * SeasonDay format
 * 
 * <P>
 * For every Season we can configure n number of SeasonDay's (represents one day
 * weather details) more number of SeasonDay's help the application to generate
 * more random weather readings. SeasonDay having a list of WeatherData with
 * respected different time in a single day. WeatherData class that actually
 * holding the weather reading information like temperature, pressure, dewPoint,
 * weatherCondition related to different times in the day.
 * </P>
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */

public class Season {

	private String seasonName;
	private TreeMap < Integer , SeasonDay > seasonDays = new TreeMap < Integer , SeasonDay > ();

	public String getSeasonName () {
		return seasonName;
	}

	public void setSeasonName ( String seasonName ) {
		this.seasonName = seasonName;
	}

	public void addSeasonDay ( int seasonDayIndex , SeasonDay newSeasonDay ) {
		seasonDays.put ( seasonDayIndex , newSeasonDay );
	}

	public SeasonDay getSeasonDay ( int seasonIndex ) {
		return seasonDays.get ( seasonIndex );
	}

	public int getTotalSeasonDayCount () {
		return seasonDays.size ();
	}

	public String toString () {
		return seasonName + "| SeasonDays:" + seasonDays.size ();
	}

	/**
	 * For inspecting the data after data load
	 */
	public void printMe ( String headerInfo ) {
		System.out.println ( toString () );
		for ( Map.Entry < Integer , SeasonDay > entry : seasonDays.entrySet () ) {
			Integer dayId = entry.getKey ();
			SeasonDay seasonDay = entry.getValue ();
			System.out.println ( seasonName + "|" + dayId );
			seasonDay.printMe ( headerInfo );
		}
	}
}

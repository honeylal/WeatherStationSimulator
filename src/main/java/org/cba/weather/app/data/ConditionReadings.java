package org.cba.weather.app.data;

import java.util.HashMap;

/**
 * This class holds weather condition values (respected to one read time) of
 * randomly selected weather days for weather condition prediction
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class ConditionReadings {
	HashMap<String, Integer> conditionReadingsValues = new HashMap<String, Integer>();

	private int mostFrequentConditionCount = 0;
	private String mostFrequentConditionSeason = "--";
	
	public HashMap < String , Integer > getConditionReadingsValues () {
		return conditionReadingsValues;
	}

	/**
	 * Add the conditions into list and store most frequent condition into
	 * mostFrequentConditionSeason
	 * 
	 * @param condition
	 */
	public void setConditionReadingsValue ( String condition ) {

		if ( conditionReadingsValues.get ( condition ) == null ) {
			conditionReadingsValues.put ( condition , 1 );
			if ( mostFrequentConditionCount < 1 ) {
				mostFrequentConditionCount = 1;
				mostFrequentConditionSeason = condition;
			}
		} else {
			int newCount = conditionReadingsValues.get ( condition ) + 1;
			if ( newCount > mostFrequentConditionCount ) {
				mostFrequentConditionCount = newCount;
				mostFrequentConditionSeason = condition;
			}
			conditionReadingsValues.put ( condition , newCount );
		}
	}
	
	public String getMostFrequentConditionSeason () {
		return mostFrequentConditionSeason;
	}
	
	
}

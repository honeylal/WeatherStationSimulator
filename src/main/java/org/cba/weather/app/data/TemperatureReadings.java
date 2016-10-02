package org.cba.weather.app.data;

import java.util.ArrayList;

/**
 * This class holds Temperature values (respected to one read time) of randomly
 * selected weather days for Temperature prediction.
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class TemperatureReadings {

	private ArrayList < Float > temperatureRedingValues = new ArrayList < Float > ();

	public ArrayList < Float > getTemperatureRedingValues () {
		return temperatureRedingValues;
	}

	public void setTemperatureRedingValues ( ArrayList < Float > temperatureRedingValues ) {
		this.temperatureRedingValues = temperatureRedingValues;
	}
}

package org.cba.weather.app.data;

import java.util.ArrayList;

/**
 * This class holds pressure values (respected to one read time) of randomly
 * selected weather days for Pressure prediction.
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class PressureReadings {

	private ArrayList < Float > pressureRedingValues = new ArrayList < Float > ();

	public ArrayList < Float > getPressureRedingValues () {
		return pressureRedingValues;
	}

	public void setPressureRedingValues ( ArrayList < Float > pressureRedingValues ) {
		this.pressureRedingValues = pressureRedingValues;
	}

}

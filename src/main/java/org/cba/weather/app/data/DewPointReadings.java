package org.cba.weather.app.data;

import java.util.ArrayList;

/**
 * This class holds due points values (respected to one read time) of randomly
 * selected weather days for due points prediction
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class DewPointReadings {

	private ArrayList < Float > dewPointRedingValues = new ArrayList < Float > ();

	public ArrayList < Float > getDewPointRedingValues () {
		return dewPointRedingValues;
	}

	public void setDewPointRedingValues ( ArrayList < Float > dewPointRedingValues ) {
		this.dewPointRedingValues = dewPointRedingValues;
	}
}

package org.cba.weather.app.data;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.cba.weather.app.exceptions.WeatherDataParseException;
import org.cba.weather.app.util.WeatherUtil;

/**
 * 
 * WeatherData class that actually holding the weather reading information like
 * temperature, pressure, dewPoint, weatherCondition related to different times
 * in the day
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class WeatherData {

	private String time;
	private int minute = 0;
	private String weatherCondition = "NORMAL";
	private float temperature = 0.0f;
	private float pressure = 0.0f;
	private float dewPoint = 0.0f;
	int humidity = 0;
	private NumberFormat temperetureFormat = new DecimalFormat ( "+#0.00" );
	private NumberFormat pressureFormat = new DecimalFormat ( "#00" );
	

	public String toString () {
		return getTime () + "|" +
				getTemperature () + "|" +
				getHumidity () + "|" +
				getPressure () + "|" +
				getWeatherCondition ();
		
	}

	public float getTemperature () {
		return temperature;
	}

	public void setTemperature ( float temperature ) {
		this.temperature = temperature;
	}

	/**
	 * Set the Temperature and do the data validation also
	 * @param dewPointValue
	 * @throws WeatherDataParseException
	 */
	public void setTemperature ( String temperatureValue ) throws WeatherDataParseException {
		boolean isInputTemperatureIsFloat = WeatherUtil.isFloat ( temperatureValue );
		if ( isInputTemperatureIsFloat ) {
			temperature = Float.valueOf ( temperatureValue );
		}else{
			throw new WeatherDataParseException ("Error in temperature input : " + temperatureValue);
		}
	}

	public float getPressure () {
		return pressure;
	}

	public void setPressure ( float pressure ) {
		this.pressure = pressure;
	}

	/**
	 * Set the Pressure and do the data validation also
	 * @param dewPointValue
	 * @throws WeatherDataParseException
	 */
	public void setPressure ( String pressureValue ) throws WeatherDataParseException {
		boolean isInputPressureIsFloat = WeatherUtil.isFloat ( pressureValue );
		if ( isInputPressureIsFloat ) {
			pressure = Float.valueOf ( pressureValue );
		}else{
			throw new WeatherDataParseException ("Error in pressure input : " + pressureValue);
		}
	}

	public float getDewPoint () {
		return dewPoint;
	}

	public void setDewPoint ( float dewPoint ) {
		this.dewPoint = dewPoint;
	}
	
	/**
	 * Set the DewPoint and do the data validation also
	 * @param dewPointValue
	 * @throws WeatherDataParseException
	 */
	public void setDewPoint ( String dewPointValue ) throws WeatherDataParseException {
		boolean isInputDewPointIsFloat = WeatherUtil.isFloat ( dewPointValue );
		if ( isInputDewPointIsFloat ) {
			dewPoint = Float.valueOf ( dewPointValue );
		}else{
			throw new WeatherDataParseException ("Error in dewPoint input : " + dewPointValue);
		}
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinugte(int minute) {
		this.minute = minute;
	}

	public String getWeatherCondition() {
		return weatherCondition;
	}

	public void setWeatherCondition(String weatherCondition) {
		this.weatherCondition = weatherCondition;
	}


	public int getHumidity() {
		return WeatherUtil.calculateRelativeHumidity ( getTemperature() , getDewPoint () );
	}

	public void setHumidity ( int humidity ) {
		this.humidity = humidity;
	}

	/**
	 * Set the Humidity and do the data validation also
	 * @param dewPointValue
	 * @throws WeatherDataParseException
	 */
	public void setHumidity ( String humidityValue ) {
		boolean isHumidityIsInteger = WeatherUtil.isInteger ( humidityValue );
		if ( isHumidityIsInteger ) {
			humidity = Integer.parseInt ( humidityValue );
		}
	}

	/**
	 * This will print the Season details in the expected syntax
	 * 
	 * @param headerInfo
	 *            Weather station + Season details in the expected syntax (the
	 *            same appended at the beginning of season details before print)
	 * 
	 * @param date
	 *            the date against weather is simulated
	 */
	public void printMe(String headerInfo, String date) {
		String output = "";
		if (getTemperature() == 0.0 && getHumidity() == 0.0
				&& getPressure() == 0.0) {
			output = headerInfo + "|"
					+ WeatherUtil.getISO8601Datetime(date + " " + time) + "|"
					+ "--|"
					+ "--|"
					+ "--|--";
			System.out.println(output);
		} else {
			output = headerInfo + "|"
					+ WeatherUtil.getISO8601Datetime(date + " " + time) + "|"
					+ getWeatherCondition() + "|"
					+ temperetureFormat.format(getTemperature()) + "|"
					+ pressureFormat.format(getPressure()) + "|"
					+ getHumidity();
		}
		System.out.println(output);
	}
	
}

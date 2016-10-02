package org.cba.weather.app;

import java.text.ParseException;
import java.util.ArrayList;

import org.cba.weather.app.util.WeatherUtil;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for WeatherUtil.
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class WeatherUtilTest 
    extends TestCase
{
    /**
     * Testing isDouble
     */
    public void testIsDouble(){
    	String trueCaseValue = "5.0";
    	String falseCaseValue = "N/A";
    	Assert.assertTrue(WeatherUtil.isDouble(trueCaseValue));
    	Assert.assertFalse(WeatherUtil.isDouble(falseCaseValue));
    }
    
    /**
     * testing getMean
     */
    public void testGetMean(){
    	ArrayList<Float> testValues = new ArrayList<Float>();
    	testValues.add(10.05f);
    	testValues.add(11.05f);
    	testValues.add(10.09f);
    	testValues.add(12.05f);
    	testValues.add(13.05f);
    	testValues.add(10.15f);
    	testValues.add(19.05f);
    	testValues.add(15.55f);
    	Assert.assertEquals(WeatherUtil.getMean(testValues), 12.63, 0.001);
    }

    /**
     * testing getStandardDeviation
     */
    public void testGetStandardDeviation(){
    	ArrayList<Float> testValues = new ArrayList<Float>();
    	testValues.add(10.05f);
    	testValues.add(11.05f);
    	testValues.add(10.09f);
    	testValues.add(12.05f);
    	testValues.add(13.05f);
    	testValues.add(10.15f);
    	testValues.add(19.05f);
    	testValues.add(15.55f);
    	float mean = WeatherUtil.getMean(testValues);
    	Assert.assertEquals(WeatherUtil.getStandardDeviation(testValues, mean),3.207,0.001);
    	
    }
}

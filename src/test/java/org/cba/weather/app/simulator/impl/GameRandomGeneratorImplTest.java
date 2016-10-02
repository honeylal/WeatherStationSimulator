package org.cba.weather.app.simulator.impl;

import org.cba.weather.app.simulator.RandomGenerator;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Unit test for GameRandomGeneratorImpl.
 * 
 * @author honeylalm@gmail.com
 * @version 1.0
 */
public class GameRandomGeneratorImplTest extends TestCase {
	
	/**
	 * testing the random impl
	 */
	public void testGetRandomNumber () {
		RandomGenerator randomGenerator = new GameRandomGeneratorImpl ();
		int random = randomGenerator.getRandomNumber ( 2 , 10 );
		Assert.assertTrue ( "The value should be >= min and < max " , ( random >= 2 && random < 10 ) );
	}

}

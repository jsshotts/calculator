package test;

import static org.junit.Assert.*;
import org.junit.Test;
import logic.BasicLogic;

public class Negative {
	BasicLogic calc = new BasicLogic();
	double threshold = 0.001;
	
	@Test
	public void testAdd() { assertEquals(calc.add(-5, 1.3), -3.7, threshold); }
	@Test
	public void testMult() { assertEquals(calc.divide(-18, 9.1), -1.978, threshold); }

}

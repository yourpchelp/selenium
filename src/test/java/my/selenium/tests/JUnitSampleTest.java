package my.selenium.tests;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class JUnitSampleTest extends JUnitTestReporter {

	@Test
	public void sampleTest0() {
		assertTrue(1 < 2);
	}

	@Ignore
	@Test
	public void sampleTest1() {
		assertTrue(1 > 2);
	}

	@Test
	public void sampleTest2() {
		assertTrue(1 < 2);
	}

	@Ignore
	@Test
	public void sampleTest4() {
		assertTrue(1 > 2);
	}
}

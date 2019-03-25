package my.selenium.suites;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import my.selenium.tests.*;
@Ignore
@RunWith(Suite.class)
@SuiteClasses({JUnitSampleTest.class, Test3.class})
public class TestSuite {

}

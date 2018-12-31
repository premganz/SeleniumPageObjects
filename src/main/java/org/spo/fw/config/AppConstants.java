package org.spo.fw.config;

import org.openqa.selenium.StaleElementReferenceException;

public class AppConstants {
	public static final int LONG_SECTION_THRESHOLD=300;
	public static final Class[] EXCEPTIONS_WARRANTING_RETRY= {
		StaleElementReferenceException.class,
	};
	public static final long SLP=1000; //Sleep base factor which allows for scaling according to environment. eg: USe Thread.sleep(2*SLP) instead of Thread.sleep(2000)
}

package org.spo.fw.config;

import org.openqa.selenium.StaleElementReferenceException;

public class AppConstants {
	public static final int LONG_SECTION_THRESHOLD=300;
	public static final Class[] EXCEPTIONS_WARRANTING_RETRY= {
		StaleElementReferenceException.class,
	};
}

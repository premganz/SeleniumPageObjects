package org.spo.fw.web;

import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;



public class Lib_Utils {


	

	

	public String splitString(String input, String delimiter, int idx) {
		return input.split("\\"+delimiter)[idx];
	}

	public String generateRandomAscString(int maxLen) {
		return RandomStringUtils.randomAscii(maxLen);
	}
	
	public String generateRandomString( int maxLen) {
		return RandomStringUtils.randomAlphanumeric(maxLen);
	}
}

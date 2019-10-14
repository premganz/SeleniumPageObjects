/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.setup;

public class AppConstantsMYP {

	public static final boolean UNIT_TEST_MODE=false;
	enum runModes { AT, Nightly, diffCacheStore, diffCacheRetrieve , Local}
	public static final String CUSTOM_DIFF_MODE="CacheRetrieve";
	public static final String NIGHTLY ="Nightly";
	public static final String AT="AT";
	public static final String IGNORE_KNOWN_ISSUES="IGNORE_KNOWN_ISSUES";
	public static final String LOCAL="Local";
	public static final String ZIP_INSTALLATION_DIRECTORY = "\"C:/Program Files/7-Zip/7z\"";
	public static final String CACHE_STORE = "CACHE_STORE";
	
	
	//Some RandomNumber configs
	public static final int RAND_REC=5; //(0<x<17) for episode name
	public static long SLP=1000;
	public static String DIFF="DIFF";


}

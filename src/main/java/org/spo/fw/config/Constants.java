/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.config;

public class Constants {
	public enum LifeCycleState  {NULL, STARTED , READY, STOPPED};
	public enum LogLevel{ERROR, DEBUG, TRACE,FILEONLY,INFO};
	public enum LogMode{CONSOLE, SWING};
	public static enum ProfileLevel{NONE, PAGE_LOAD, EXHASUTIVE};
	protected static final String HTTP = "http://";
	protected static final String SLASH = "/";
	public static final int SE_SCRIPT_AT_MODE = 3;
	public static final int SE_SCRIPT_DEV_MODE = 2;
	
	public static enum OS_Supported {Windows}//, Linux}
	
	
	public static final String QUERY_EQUALS_EXP = "=";
	public static final String QUERY_CONCAT_EXP = "&";
	public static final String ROBOT_SUCCESS = "SUCCESS";
	public static final String ROBOT_FAIL = "FAIL";
}

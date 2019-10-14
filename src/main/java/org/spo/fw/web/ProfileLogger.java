/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.web;

import org.apache.log4j.Logger;

public class ProfileLogger {
	protected static String msg = "";
	
	protected  static Logger profileLog = Logger.getLogger("org.spo.fw.web.ProfileLogger");
	public static void messageToHold(String x) {
		msg=msg+x+'\n';
	}
	
	public static void closeAndPushToLog(String x) {
		profileLog.info(msg+x);
		msg="";
	}
}
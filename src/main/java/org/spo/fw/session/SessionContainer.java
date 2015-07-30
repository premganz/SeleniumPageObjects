package org.spo.fw.session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author prem
 * This will hold logs and other objects that are common to the session while all other things can be created and torn
 * down, this remains from the main thread start to its exit.
 *
 */

public class SessionContainer {
	public static ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<String>();
	
	
	
	private static StringBuffer multiExecutionLog = new StringBuffer();
	//private static Logger1 sessionLog = new Logger1("org.spo.fw.session.SessionContainer"); 
	
//	public  static Logger1 getSessionLogger(){
//		return sessionLog;
//	}
	public  static void storeLogToSession(String logEntry){
		multiExecutionLog.append(logEntry+'\n');
	}
	
//	public  static void storeLogToSession(KeyWordsScript script, String message){
//		multiExecutionLog.append(logEntry+'\n');
//	}
	
	public static String printSessionLog(){
		String toReturn = multiExecutionLog.toString();
		//multiExecutionLog = new StringBuffer();
		//String toReturn = sessionLog.buf.toString();		
		//sessionLog = new Logger1("org.spo.fw.session.SessionContainer");
		return toReturn;
	}
	
	
}

package org.spo.fw.session;

import java.util.LinkedHashMap;
import java.util.Map;
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
	private static Map<String,Object> sessionMemory = new LinkedHashMap<String,Object>();
	private static Map<String,Object> sessionRequestMemory = new LinkedHashMap<String,Object>();
	public static enum SCOPE{REQ,SES}
	
	public static void store(SessionContainer.SCOPE scope, String key, Object value){
		if(scope.equals(SCOPE.REQ)){
		sessionRequestMemory.put(key, value);
		}else{
			sessionMemory.put(key, value);
		}
	}
	
	public static Object get(SessionContainer.SCOPE scope, String key){
		if(scope.equals(SCOPE.REQ)){
		Object returnable = sessionRequestMemory.get(key);
		sessionRequestMemory.remove(key);
		return returnable;
		}else{
			return sessionRequestMemory.get(key);
		}
	}
	
	
	
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

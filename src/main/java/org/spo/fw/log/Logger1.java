package org.spo.fw.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.openqa.selenium.WebDriverException;
import org.spo.fw.config.Constants.LogLevel;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.session.SessionContainer;
import org.spo.fw.web.KeyWords_Utils;
import org.spo.fw.web.Lib_Utils;

import com.ibm.icu.util.Calendar;

//TODO  extends Logger
public class Logger1{
	String logName;
	public static StringBuffer buf=new StringBuffer();
	int logLevel=1;

	
	public enum LogMode{FILEONLY, FILECONSOLE};
	public enum LogType{STRING, EXCEPTION};

	public  static boolean TRACE_ENABLED = LogLevel.TRACE.equals(SessionContext.logLevel);
	public  static boolean DEBUG_ENABLED=true;
	public  static boolean SYSOUT_ENABLED =false;//This is a control point for System.out.println stmts.
	public static String output = ""; 
	public static boolean beenWritten=false;

	public  void setLogLevel(String level){
		if(LogLevel.TRACE.equals(level)){
			TRACE_ENABLED=true;
		}
	}
	

	public Logger1(String name){		
		logName= name;
		if(LogLevel.TRACE.equals(SessionContext.logLevel)){
			TRACE_ENABLED = true;
			DEBUG_ENABLED=true;
		}else if(LogLevel.DEBUG.equals(SessionContext.logLevel)){
			TRACE_ENABLED = false;
			DEBUG_ENABLED=true;
		}else if(LogLevel.INFO.equals(SessionContext.logLevel)){
			TRACE_ENABLED = false;
			DEBUG_ENABLED=false;
		}
		TRACE_ENABLED = LogLevel.TRACE.equals(SessionContext.logLevel);
		//setLogLevel1(SessionContext.logLevel);
		//LoggingThread logger = new LoggingThread();
		//logger.setLog(this);
		//logger.start();
	}

	public void debug(Object string) {
		printLog(string,LogLevel.DEBUG); 

	}
	public void error(String string) {
		printLog(string,LogLevel.ERROR);

	}
	public void info(String string) {
		printLog(string,LogLevel.INFO);

	}
	public void info(Exception  e) {
		printLog("An "+e.getClass().getName()+ " was thrown ",LogLevel.INFO);
				if(e.getCause()!=null){
					printLog("Cause was "+e.getCause().getClass().getName()+ " message was "+e.getMessage(),LogLevel.INFO);	
				}
				if(e.getMessage()!=null){
					printLog("Message was "+e.getMessage(),LogLevel.INFO);	
				}
				StackTraceElement[] trace = e.getStackTrace();
				for(StackTraceElement elem:trace){
					if(elem.getClassName().startsWith("java") || !elem.getClassName().startsWith(SessionContext.appConfig.domainPackageName) 	|| elem.getClassName().contains("reflect")){
						continue;
					}else{
						
							System.err.println(elem.toString());
						
					}
				}
				printLog("Message was "+e.getMessage(),LogLevel.INFO);	

	}
	public void debug(Exception e) {
		logException(e.getStackTrace(),false);

	}
	public void error(Exception e) {
		printLog("An "+e.getClass().getName()+ " was thrown ", LogLevel.ERROR);
		if(e instanceof WebDriverException){			
			//printLog("CAUSE: "+'\n'+(WebDriverException)e.getCause(),LogLevel.ERROR);//Commented out because mostly this is only screenshotexception
			logExceptionWebDriver(e.getStackTrace(),false);
		}else if(e instanceof SPOException){			
			printLog("MESSAGE: "+'\n'+e.getMessage(),LogLevel.ERROR);
			logExceptionWebDriver(e.getStackTrace(),false);
		}else{
			printLog(e.getMessage(),LogLevel.ERROR);
			printLog(e.getCause(),LogLevel.ERROR);
			logException(e.getStackTrace(),false);	
		}
	}
	public void trace(Exception e) {
		//print1(e.getMessage());
		//print1(e.getCause());


	}
	public void trace(String x) {
		printLog(x, LogLevel.TRACE);

	}

	
	private String getClassName(String logName){
		if(logName.contains(".")){
			return logName.substring(logName.lastIndexOf("."));	
		}
		return logName;
		
	}

	public void printLog(Object logObject , LogLevel level){		
		boolean printOn=true;
		if(logObject==null){
			buf.append("RECEIVED AN EXCEPTION WITH NO MESAGE"+'\n');
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String output=logObject.toString();
		output=Lib_Utils.printifyUrl(output);
		output =  sdf.format(Calendar.getInstance().getTime())+":"+
		Thread.currentThread().getName()+":"+level+":"
				+getClassName(logName)+":"+logObject.toString();

		//Print Filters
		
		if(level.equals(LogLevel.INFO)){
			System.out.println('\n'+logObject.toString()+'\n') ;
			printOn=false;
		}
		if(!TRACE_ENABLED && level.equals(LogLevel.TRACE)){
			//output=KeyWords_Utils.findAndReplaceNonAsciiChars(output.toCharArray());
			printOn=false;
		}if(!DEBUG_ENABLED && level.equals(LogLevel.DEBUG)){
			//output=KeyWords_Utils.findAndReplaceNonAsciiChars(output.toCharArray());
			printOn=false;
		}
		if(level.equals(LogLevel.ERROR)){
			buf.append("################ERROR#################"+'\n'+output.toUpperCase()+'\n');
		}
		if(level.equals(LogLevel.FILEONLY)){
			printOn=false;
		}if(printOn){
			//general case
			buf.append(output+'\n');
			if(level.equals(LogLevel.ERROR) || level.equals(LogLevel.INFO)){
			System.out.println("################ERROR#################"+output+'\n') ;
			}else{
				System.out.println(output) ;
			}
			//setOutput(output);
			SessionContainer.logQueue.offer(output);
		}

	}


	public synchronized  String pollValue() throws Exception{
		
		if(beenWritten){
				wait();
				return "";
		}else{
		
			beenWritten=true;
			notifyAll();
			return output;
			
		}
		
	}
	public  synchronized void setOutput(String output1){
	
		if(!beenWritten){
			try {
				wait();
			} catch (InterruptedException e) {			
				e.printStackTrace();
			}
		}else{
		
			output=output1;
			notifyAll();
			beenWritten=false;
		}
		}
	

	public void logException(StackTraceElement[] trace,boolean toFile ){
		for(StackTraceElement elem:trace){
			if(elem.getClassName().startsWith("org.python.core.") || elem.getClassName().startsWith("robot") 
					|| elem.getClassName().contains("reflect")){
				continue;
			}else{
				if(toFile){
					printLog(elem.toString(), LogLevel.FILEONLY);
				}else{
					System.err.println(elem.toString());
				}
			}
		}


	}
	//For webdriver exception it is of little use to get the full trace. Only where it occured is important
	public void logExceptionWebDriver(StackTraceElement[] trace,boolean toFile ){
		for(StackTraceElement elem:trace){
			if(!elem.getClassName().startsWith("org.spo.fw") ){
				continue;
			}
			if(elem.getClassName().startsWith("org.spo.fw.launch") || elem.getClassName().startsWith("org.spo.fw.runners") 
					|| elem.getClassName().contains("reflect")){
				continue;
			}
			else{
				if(toFile){
					printLog(elem.toString(), LogLevel.FILEONLY);
				}else{
					System.err.println(elem.toString());
				}
				break;
			}
		}


	}

	public static void writeLog() {
		FileWriter writer2;
		try {
			writer2 = new FileWriter(new File("log.out"));
			writer2.write(buf.toString());
			writer2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.err);
		}catch (IOException e) {
			e.printStackTrace(System.err);
		}catch(Exception e){
			e.printStackTrace(System.err);
		}

	}
	
	public void logToFile(String fileName,String content) {
		FileWriter writer2;
		try {
			File f = new File(fileName);
			if(!f.exists())
				f=new File(fileName);
			
			
			writer2 = new FileWriter(f);
			writer2.write(content);
			writer2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.err);
		}catch (IOException e) {
			e.printStackTrace(System.err);
		}catch(Exception e){
			e.printStackTrace(System.err);
		}

	}
}

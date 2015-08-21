package org.spo.fw.launch;

import java.io.File;
import java.util.List;

import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.config.StrategyRules;
import org.spo.fw.config.Constants.LogLevel;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.log.Logger1;
import org.spo.fw.log.LoggingThread;
import org.spo.fw.service.DriverFactory;
import org.spo.fw.service.ProcessMonitor;
import org.spo.fw.service.RestrictedOSCmdRouter;
import org.spo.fw.service.RobotCache;
import org.spo.fw.service.proxy.ProxyServerController;


/**@author prem 
 * 
 * Gateway for running the 2.0.0 version, (Older users can still call KeyWords directly).
 * 
 * A Proxy to startup the KeyWords , handles cleanup of exes in addtion.
 * Needs Read Write Permission in pwd and for the first argument location executable permissions.
 * 
 * 
 * Also note the spawns external proceses outside of the jvm, but kills them before the jvm duly exits.
 * If you use Chrome browser as option this will run a proxy server (browsermob) and kills it before jvm duly exits.
 * 
 * 
 */


public class BasicLauncher {

	
	/**
	 * 
	 * RobotProxy is just a command line interface to enable building up a config object 
	 * What we need is a launcher that removes the config responsibility from the scripts themselves.
	 * The scripts just contain the 
	 * 
	 *
	 */
	
	static Logger1 log = new Logger1("Startup");
	static ProcessMonitor processMonitor;//enforcing singleton
	public static void startServices(RunStrategy strategy) throws SPOException{
		if(strategy.logLevel!=null && strategy.logLevel.equals(LogLevel.TRACE)){
		LoggingThread logger = new LoggingThread();		
		Thread reader=new Thread(logger);
		reader.setDaemon(true);		
		reader.start();
		}
		StrategyRules.apply(strategy);
		DriverFactory.init(strategy);
		SessionContext.publishStrategy(strategy);
		if(processMonitor!=null) processMonitor.interrupt();
		processMonitor = new ProcessMonitor();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		processMonitor.start();
		
		
	}
	
	public static void cleanUp() throws SPOException{
		DriverFactory.stop();
		ProxyServerController.stop();
		RobotCache.resetElemCache();
		processMonitor.interrupt();
		Logger1.writeLog();		
		
	}
	
	public static void launchRobot(RunStrategy strategy, List<String> result1,  File additionalTests) throws SPOException{
		
		result1.add(additionalTests.getAbsolutePath());
		String[] args_to_passOn = result1.toArray(new String[result1.size()]);
		
		try{
			startServices(strategy);
			StringBuffer buf = new StringBuffer();
			for(String x:args_to_passOn){
				buf.append(x);
			}
			log.debug("Starting robot framework with "+buf.toString());
			org.robotframework.RobotFramework.main(args_to_passOn);	
			additionalTests.delete();
			
			Thread.sleep(10000);
		}
		
		catch(Exception e3){
			e3.printStackTrace();			
			log.error("Something really unexpected happened");
		}finally{
			cleanUp();
		}
	}
	
	public static void launchAsSeleniumScript(RunStrategy strategy, SeleniumScript script ) throws SPOException{
		try{
			startServices(strategy);
			script.init();
			script.startUp();
		}catch (Exception e){
			log.debug(e);
			e.printStackTrace();
		}finally{
			cleanUp();	
		}
		
		
	}
	
	public static SeleniumScript launchSeleniumScriptInline(RunStrategy strategy, SeleniumScript script ){
	
		try{
			script.startUp();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return script;
	}
	

	

}

/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.launch.robot;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.launch.BasicLauncher;


/**@author prem 
 * 
 * Gateway for running the 2.0.0 version, (Older users can still call ServiceHub directly).
 * 
 * A Proxy to startup the ServiceHub , handles cleanup of exes in addtion.
 * Needs Read Write Permission in pwd and for the first argument location executable permissions.
 * 
 * 
 * Also note the spawns external proceses outside of the jvm, but kills them before the jvm duly exits.
 * If you use Chrome browser as option this will run a proxy server (browsermob) and kills it before jvm duly exits.
 * 
 * 
 */


public class RobotGatewayProxy {
	static CLIAdapter cliadapter= new CLIAdapter() ;
	public static void run(RunStrategy defaultStrategy, String[] args) throws Exception {

		//1. Logging Setup
		StringBuilder buffer = new StringBuilder();
		Logger logger = Logger.getLogger("RobotProxy");
		
		logger.trace("==============================================================================");
		logger.trace(buffer);
		logger.trace("==============================================================================");
		//2. Setting up a dummy test file for releasing driver resources by calling quit
		try{
			//FIXME//TODO : this is rather crude, it assumes that the execution directory is same as test case driectory, try to append the 
			//path to temp.txt into the last argument for RobotFramework
			StringBuffer  buf = new StringBuffer();
			buf.append("***Settings***"+'\n');
			buf.append("Library           org.spo.fw.web.ServiceHub"+'\n');
			buf.append("***Test Cases***"+'\n');
			buf.append("SystemQuit"+'\n');
			//buf.append("      [Tags]       Run"+'\n');
			buf.append("       quit"+'\n');

			File f = new File("temp.txt");
			FileWriter writer1 = new FileWriter(f);
			writer1.write(buf.toString());
			writer1.close();

			//3. Processing startupArgs delegated to mapcCLI method.
			Map<String,String> options = new LinkedHashMap<String,String>();
			List<String> result = new LinkedList<String>(Arrays.asList(args));
			//4. Setting system properties  and Run Strategy

			//5.Initializing other Services- Delegated to basiclauncher
			//6.Launching Robot
			List<String> result1 = new LinkedList<String>();
			for(int i = 0;i<result.size();i++){

				if(result.get(i).contains("-browser")||result.get(i).contains("-driverPath")||
						result.get(i).contains("-textScreensPath")||result.get(i).contains("-logLevel")){
					i++;
				}
				else if(result.get(i).contains("-isProxyServerRequired")){

				}else{
					result1.add(result.get(i));	
				}

			}

			BasicLauncher.launchRobot(cliadapter.mapCLIToStrategy(defaultStrategy,args), result1, f);

			//7.Cleanup Delegated to basicLauncher


		}catch(Exception e3){
			e3.printStackTrace();
			logger.error("Something really unexpected happened");
		}
	}





}

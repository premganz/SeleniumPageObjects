package org.spo.fw.launch.robot;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.spo.fw.config.Constants;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.itf.ExtensibleService;


public class CLIAdapter {
	
	 
	
	
	
	public  RunStrategy mapCLIToStrategy(RunStrategy defaultStrategy, String[] args) throws Exception {
		
		Map<String,String> options = new LinkedHashMap<String,String>();
		List<String> result = new LinkedList<String>(Arrays.asList(args));
		try{
			for(int i = 0;i<result.size();i++){
				if(result.get(i).startsWith("-")){
					int m = i+1;
					if(result.size()>(m)){
						options.put(new String (result.get(i)),new String(result.get(i+1)));	
					}

				}

			}
			if(!options.containsKey("-browser")){
				options.put("-browser","phantom");
			}

			//4. Setting system properties  and Run Strategy
			if(options.get("-browser").contains("phantom")){
				System.getProperties().put("phantomjs.binary.path", options.get("-driverPath"));	
			}else if(options.get("-browser").contains("ie")){
				System.getProperties().put("webdriver.ie.driver", options.get("-driverPath"));
				defaultStrategy.isVisibleBrowser= true;
			}else if(options.get("-browser").contains("chrome")){
				System.getProperties().put("webdriver.chrome.driver", options.get("-driverPath"));
				defaultStrategy.isVisibleBrowser= true;
			}else if(options.get("-browser").contains("html")){

			}else{
				System.getProperties().put("phantomjs.binary.path", options.get("-driverPath"));	
			}

			System.getProperties().put("KeyWords", "org.spo.fw.web.KeyWords");
			if(options.containsKey("-textScreensPath")){
				System.getProperties().put("textScreens.path", options.get("-textScreensPath"));//Custom added to provide root dir for checkPageAgainstFile keywordl
				defaultStrategy.textFilesPath= options.get("-textScreensPath");
			}
			if(options.containsKey("-isProxyServerRequired")){
				defaultStrategy.isProxyServerRequired=true;
			}if(options.containsKey("-noBasicAuth")){
				defaultStrategy.requireBasicAuthUrlPrefix= false;
			}else{
				defaultStrategy.requireBasicAuthUrlPrefix= true;
			}
			defaultStrategy.browserName = options.get("-browser");
			defaultStrategy.driverPath = options.get("-driverPath");
			if("TRACE".equals(options.get("-logLevel"))){
				defaultStrategy.logLevel = Constants.LogLevel.TRACE;	
			}
		}catch(Exception e3){
			e3.printStackTrace();
			
		}
		return defaultStrategy;
	}



	

}

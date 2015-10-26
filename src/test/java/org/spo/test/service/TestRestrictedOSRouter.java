package org.spo.test.service;

import java.util.Date;

import junit.framework.TestCase;

import org.seleniumhq.jetty7.util.log.Log;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.RestrictedOSCmdRouter;
import org.spo.fw.web.Lib_KeyWordsCore;
import org.spo.fw.web.ServiceHub;

public class TestRestrictedOSRouter extends TestCase{
	ServiceHub robot = new ServiceHub();		
	
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	
	
	//Testing with multiple files 
	
	
	//FIXME uncomment test
	public void testRunOSCmd() throws Exception{
		//throws illegal argument exception when used with unsupported os
		String timeStamp = new Date().toString();
		//RestrictedOSCmdRouter implMock = Matchers.any(RestrictedOSCmdRouter.class);
		String os = "Windows"; //LINUX1
		String cmd = "dir"; //ls
		String[] cmds = { "dir2", "quit"};
		//Mockito.when(implMock.execArbitraryCommand(os, cmd)).thenThrow(SPOException.class);
//		BufferedReader error_buffer = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));
//		String line = "";
//		while((line=error_buffer.readLine())!=null){
//			System.out.println("Reading error Stream "+line);
//			if(line.isEmpty()){
//				error_buffer.skip(1000);
//			}
//			//buf.append(line);
//		}
		Lib_KeyWordsCore coreLib = new Lib_KeyWordsCore();
		String result = "FAILED";
		//Runtime.getRuntime().exec(cmds);
		if(System.getProperties().containsKey("message-robot") ){
			log.debug("System property message is ["+System.getProperty("message-robot")+"]");
		}else{
			log.debug("System property message is not set");
		}
	if(System.getProperties().containsKey("message-robot") && System.getProperty("message-robot").equals("dev")){
		try{
			result = RestrictedOSCmdRouter.execArbitraryCommand(os, cmd, "C:/");
		}catch(Exception e){
			e.printStackTrace();
		}
		assertTrue(!result.equals(""));
	}	
	else if(System.getProperties().containsKey("message-robot") && System.getProperty("message-robot").equals("cm")){
		assertTrue(result.equals("FAILED"));
	}else{
		//assertTrue(!result.equals(""));
	}
		
		
//		URL resourceUrl = getClass().getResource("/test_TestWeRobot.bat");
//		String resourcePath = resourceUrl.toURI().getPath();
//		String result = robot.runOSCmd(resourcePath);
//		assertTrue(result.contains("dir"));


	}
	

	
}

package org.spo.test;


import org.junit.Test;
import org.spo.fw.config.AppConfig;
import org.spo.fw.config.Constants.LogLevel;
import org.spo.fw.config.SessionContext;
import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.meta.fixture.runner.SimpleScriptStubStatic;
import org.spo.fw.meta.fixture.runner.StubScriptRunner;
import org.spo.fw.service.ProcessMonitor;
import org.spo.fw.service.RestrictedOSCmdRouter;
import org.springframework.util.Assert;

public class Test_ProcessMonitor {
			
	StubKeyWords stubLibrary;
	
	
	
	//@Test 
	//MAnual runs only
	public void testPRocessMonitorBasic() throws Exception{
		SimpleScriptStubStatic script = new CheckProcessMonitor();
		try{
			StubScriptRunner runner = new ScriptRunner_TestServices();
			runner.launchScript(script);
		}catch(Exception e){
			e.printStackTrace();
			Assert.isTrue(false);
			return;
		}
		Assert.isTrue(!script.isFailed());
		
	}
	
	
	class ScriptRunner_TestServices extends StubScriptRunner{
		@Override
		public void init() {
			super.init();
		strategy.appConfig.UNSTABLE_PCS="chromedriver.exe";
		strategy.appConfig.WEBDRIVER_TIMEOUT=1;	
		strategy.logLevel=LogLevel.TRACE;
		}
		
	}
	
	
	class CheckProcessMonitor extends SimpleScriptStubStatic{
		@Override
		public void init() {
		
		}
		
		@Override
		public void execute() throws Exception {
			if(SessionContext.appConfig!=null){	
				System.out.println(RestrictedOSCmdRouter.execArbitraryCommand("Windows","start chromedriver.exe","c:\\works\\"));
				ProcessMonitor monitor = new ProcessMonitor();
				monitor.start();	
				Thread.sleep(80000);
				String x =RestrictedOSCmdRouter.execArbitraryCommand("Windows","tasklist /FI \"IMAGENAME eq "+"chromedriver.exe"+"\"","c:\\");
				System.out.println(x);
				failed=x.contains("chromedriver.exe");
			}
			
			
		}
	}
	
		
}

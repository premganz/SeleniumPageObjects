package org.spo.fw.meta.fixture;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.spo.fw.itf.SeleniumScript;

public abstract class TestRunnerTemplate {
	String pageName;
Map<Integer, String> page_events= new LinkedHashMap<Integer,String>() ;
Map<Integer, String> domain_events= new LinkedHashMap<Integer,String>() ;
 



	public static void runTest(SeleniumScript script){
		
		try{
			StubScriptRunner runner = new StubScriptRunner();
			runner.launchScript(script);
		}catch(Exception e){
			e.printStackTrace();
			Assert.assertTrue(false);
			return;
		}
		Assert.assertFalse(script.isFailed());
		
	}
}

package org.spo.fw.specific.scripts.setup;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.spo.fw.itf.SeleniumScript;

/**
 * 
 * 
 * 
 * @author pg
 * 
 * 
 * Example Usage 
 * 	@Test
	public void b_testALayout() {TestRunnerTemplateMYP.runTest("b_testALayout",new SimpleScriptMYP() {		
		public  void start_test() throws Exception {
			kw.navigateByName("Home");
			failed= kw.checkPageLayoutForm	("Home","a/"+"Home").isFailed();
		}
	}
  );}
 * 
 *
 */






public abstract class TestRunnerTemplateMYP {
	String pageName;
Map<Integer, String> page_events= new LinkedHashMap<Integer,String>() ;
Map<Integer, String> domain_events= new LinkedHashMap<Integer,String>() ;
public static ScriptRunnerMYP runner;


public static void runTest(String testName, SeleniumScript script){
	
	try{
		runner = new ScriptRunnerMYP();
		runner.setTestName(testName);
		runner.launchScript(script);
	}catch(Exception e){
		e.printStackTrace();
		Assert.assertTrue(false);
		return;
	}
	Assert.assertFalse(script.isFailed());
	
}

	public static void runTest(SeleniumScript script){
		
		try{
			runner = new ScriptRunnerMYP();
			runner.launchScript(script);
		}catch(Exception e){
			e.printStackTrace();
			Assert.assertTrue(false);
			return;
		}
		Assert.assertFalse(script.isFailed());
		
	}
	
	public static void runTestWithCustomRunner(SeleniumScript script, ScriptRunnerMYP customRunner){
		
		try{			
			customRunner.launchScript(script);
		}catch(Exception e){
			e.printStackTrace();
			Assert.assertTrue(false);
			return;
		}
		Assert.assertFalse(script.isFailed());
		
	}
}

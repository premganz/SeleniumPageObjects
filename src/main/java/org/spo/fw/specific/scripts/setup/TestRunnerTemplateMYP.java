package org.spo.fw.specific.scripts.setup;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.junit.Assert;
import org.spo.fw.config.SessionContext;
import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.log.Logger1;
import org.spo.fw.specific.scripts.dll.Lib_Content_Diff2;
import org.spo.fw.specific.scripts.dll.Lib_Content_Diff3;


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
	static Logger1 log = new Logger1("TestRunnerTemplate");


	public static boolean runTest(String testId, SeleniumScript script){
		if(testId.contains("$")){
			testId = testId.substring(testId.indexOf("$"),testId.length()-1);
		}
		try{
			runner = new ScriptRunnerMYP();
			runner.setTestName(testId);

			runner.init();
			runner.initStrategy(script);

			if(SessionContext.testEnv.equals(AppConstantsMYP.LOCAL)){			
			//	script.getKw().setContentProvider(new Lib_Content_LocalRunsRet());
				log.info("This is the rerun mode from a cache scenario ");
				runner.launchScript(script);
				
			}
			if(SessionContext.testEnv.equals(AppConstantsMYP.CACHE_STORE)){
//				log.trace("The testEnv is  "+SessionContext.testEnv+ " so using Diff2 lib");
//				script.getKw().setContentProvider(new Lib_Content_LocalRunsStore());
//				if(script.getScriptType().equals(ScriptType.DOWNLOAD_VERIFY)) {
//					script.getKw().setContentProvider(new Lib_Content_Downloads_Cacheable());
//					script.getKw().impl_nav.getNavContainer().getDefaulModel().getFactory().removeValidator("(.*)");
//					
//				}
				runner.launchScript(script);
				
			}
			if(SessionContext.testEnv.equals(AppConstantsMYP.CUSTOM_DIFF_MODE)){
				log.trace("The testEnv is  "+SessionContext.testEnv+ " so using Diff2 lib");
				script.getKw().impl_page.setContent_provider(new Lib_Content_Diff2());
				if(isNotCacheable(script)){
					//	((SimpleScriptMYP)script).setForcePass(true);
				}
				runner.launchScript(script);
			}
			
			
			if(SessionContext.testEnv.equals(AppConstantsMYP.CUSTOM_DIFF_MODE)){			
				SessionContext.appConfig.URL_UNIT_TEST_MODE="http://test.com/";
				SessionContext.appConfig.customProperties.put("DCT_DB","sdsd");
				script.getKw().impl_page.setContent_provider(new Lib_Content_Diff3());
				log.info("This is the rerun mode from a cache scenario ");
				if(isNotCacheable(script)){
					//	((SimpleScriptMYP)script).setForcePass(true);
				}
				runner.launchScript(script);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			Assert.assertTrue(false);
			return true;
		}


		if(script.isFailed()){
			try {
				logReport(testId, "FAILED", script.getFailureMessage());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Assert.assertFalse(script.isFailed());
		return script.isFailed();

	}

	//@Deprecated
	public static void runTest(SeleniumScript script){
		runTest(script.getClass().getName(),script);
	}

	public static void runTestWithCustomRunner(SeleniumScript script, ScriptRunnerMYP customRunner){

		try{			
			customRunner.initStrategy(script);
			customRunner.launchScript(script);
		}catch(Exception e){
			e.printStackTrace();
			Assert.assertTrue(false);
			return;
		}
		Assert.assertFalse(script.isFailed());

	}

	private static boolean isNotCacheable(SeleniumScript script) {
		String name = script.getClass().getSimpleName();
		log.trace(name);
		if(name.equals("HomeXXXXX") || name.equals("Exiter")||
				name.equals("Exiter")){
			log.debug("Not cacheable so skipping "+script.getClass().getSimpleName());return true;
		}

		return false;
	}

	public static void logReport(String testName, String status, String summaryLog) throws Exception{

		File file = new File("Report_Basic.txt");
		List<String> lines = FileUtils.readLines(file);
		int i =0;
		String sep = "==================================================================================="+'\n';
		lines.add(sep);
		for(String line:lines){
			if(line.equals(sep)){
				i++;
			}
		}
		i++;
		lines.add(String.format("%3d %-42s %15s %45s", i, testName, status, WordUtils.wrap(summaryLog,45)));	
		lines.add('\n'+"------------------------------------------------------------------------------------"+'\n'); 
		FileUtils.writeLines(file, lines);




	}
}

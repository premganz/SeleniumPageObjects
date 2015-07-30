package org.spo.fw.selenium;

import java.util.LinkedHashMap;
import java.util.Map;

import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.TestDataException;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.SeleniumScriptParametrized;
import org.spo.fw.log.Logger1;
import org.spo.fw.web.KeyWords;




/**
 * 
 * 
 * @author Prem
 * Run Selenium Scripts from Robot.
 * 
 * In Straightforward manner this can be handled by compiling tests in a class and passing the driver instance 
 * from a keyword of the Library , getting output if any from the script and running it. This calls for doing some things over again, like throwing 
 * assertion in case of exception. Also it reduces re usability across projects.
 * 
 *  We standardize it by having scripts to implement seleniumscript interface that provides a start() method and execute() 
 *  method, the start is handled in super class.
 *  
 *  So it is simpler and more standardized now to include a selenium script in a robot script.
 *  Create a java class extending this class, override the execute() method.
 *  If a failure condition is met, set boolean failed to true. Thats it.
 *  
 *  In the robot script import the RobotLibrariesFacade, which provides methods for calling 
 *  a) The script out of context (with a new web driver instance) or in context 
 *  b) Script can be launched with or without parameterization (string params)
 *  c) Script can be post processed for output params.
 *  d) Script can be launched for limited scope of controls (as given by ids or xpath lists)
 *  
 *
 */



public abstract class KeyWordsScript implements SeleniumScriptParametrized{
	public Map<String,String> outParams= new LinkedHashMap<String,String>();
	public Map<String,String> inParams= new LinkedHashMap<String,String>();
	private Map<String,String> strategyParams= new LinkedHashMap<String,String>();
	protected Logger1 log = new Logger1(this.getClass().getName());
	protected  KeyWords kw =new KeyWords();//Keyword Proxy
	protected String testServerModuleName;
	protected String failureMessage;
	@Override
	public String getFailureMessage() {
		return failureMessage;
	}

public void init(){
	
}

	public String getTestServerModuleName() {
		return testServerModuleName;
	}


	public void setTestServerModuleName(String testServerModuleName) {
		this.testServerModuleName = testServerModuleName;
	}


	public boolean isFailed() {
		return failed;
	}



	public void setFailed(boolean failed) {
		this.failed = failed;
	}



	//workflow control
	public final void execute() throws Exception{
		int mode =0;
		if(SessionContext.appConfig.eclipseMode){
			mode = start_eclipseMode();
		}else{
			mode = start_robotMode();			
		}
		start_test(mode);
		
	}


	public abstract int start_eclipseMode() throws Exception;
	public abstract int start_robotMode() throws Exception;
	@Deprecated
	public  String start_TestRecordSet_mode2() throws Exception{return "";}
	@Deprecated
	public  String start_TestRecordSet_mode3() throws Exception{return "";}
	
	//This will be called with mode==0 for unit test, 2 for dev adn 3 for AT 
	public  abstract String start_test(int mode) throws Exception;
	public void startUp(){
		//Rely on injected driver from constraint/via robot session or create new.
		//call on execute
		//quit driver and see script status to raise error.
		try {		
			kw.create(constraint.webDriver);
			log.debug("Working with :"+this.getClass().getName());
			
				if (kw.getDriver()==null){
					kw.create("","");
				}
			execute();
			log.info("SCRIPT EXECUTION FINISHED");
			if(failed){
				log.info("EXECUITON FAILED? :"  + failed);
				throw new AssertionError(failureMessage);
			}
			if(kw.getDriver()!=null){//Some Test scripts are simple file processors not browser based.
				kw.quitDriver();
				
				//driver.quit();	
			}

		} 
		catch (TestDataException e) {
			log.info(e);
			throw new AssertionError(e.getMessage());
		}catch (SPOException e) {
			log.info(e);
			throw new AssertionError(e.getMessage());
		}catch (Exception e) {
			log.error(e);
			throw new AssertionError("An unhandled system exception occured");
		}
		
		
	}


	public void setInParamMap(Map<String, String> inParam) {
		this.inParams = inParam;
	}

	public KeyWords getKw() {
		return kw;
	}

	public void setKw(KeyWords kw) {
		this.kw = kw;
	}

	public Map<String, String> getStrategyParams() {
		return strategyParams;
	}

	public void setStrategyParams(Map<String, String> strategyParams) {
		this.strategyParams = strategyParams;
	}

	protected ScriptConstraint constraint ;
	protected boolean failed;

	public Map<String, String> getInParams() {
		return inParams;
	}

	public void setInParams(Map<String, String> inParams) {
		this.inParams = inParams;
	}

	public String getOutParam(String outKey) {
		return outParams.get(outKey);
	}

	public Map<String, String> getOutMap() {
		return outParams;
	}


	public void setInParam(String inKey, String inValue) {
		inParams.put(inKey, inValue);
	}


	public void setScriptConstraint(ScriptConstraint constraint) {
		this.constraint = constraint;

	}

}

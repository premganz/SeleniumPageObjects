package org.spo.fw.selenium;

import java.awt.Toolkit;
import java.util.LinkedHashMap;
import java.util.Map;

import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.exception.UnexpectedWebDriverException;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.itf.SeleniumScriptParametrized;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.DriverFactory;
import org.spo.fw.web.ServiceHub;


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
 *  In the robot script import the CustomScriptProvider, which provides methods for calling 
 *  a) The script out of context (with a new web driver instance) or in context 
 *  b) Script can be launched with or without parameterization (string params)
 *  c) Script can be post processed for output params.
 *  d) Script can be launched for limited scope of controls (as given by ids or xpath lists)
 *  
 *
 */

public abstract class JunitScript implements SeleniumScriptParametrized, ExtensibleService{
	protected Map<String,String> outParams= new LinkedHashMap<String,String>();
	protected Map<String,String> inParams= new LinkedHashMap<String,String>();
	protected Map<String,String> strategyParams= new LinkedHashMap<String,String>();

	protected Logger1 log = new Logger1(this.getClass().getName());
	protected  ServiceHub kw=new ServiceHub();//Keyword Proxy


	protected ScriptConstraint scriptConstraint = new ScriptConstraint() ;
	protected String testServerModuleName;
	protected String failureMessage;	
	protected boolean failed;
	@Override
	public abstract void execute() throws Exception; 

	@Override
	public RunStrategy customizeStrategy(RunStrategy strategy) {
		return strategy;
	}

	protected void recreateDriver(){
		init();
		kw.create("", "");
	}
	public void init(){
		if(kw==null){
			kw =new ServiceHub();
		}
		kw.init();

	}



	//public  abstract String start_test(int mode) throws Exception;
	public void startUp(){
		//Rely on injected driver from constraint/via robot session or create new.
		//call on execute
		//quit driver and see script status to raise error.		
		//init();
		try {
		if(!SessionContext.isBrowserLess){
			if(scriptConstraint.webDriver!=null){
				kw.create(scriptConstraint.webDriver);	
			}else{
				kw.create("","");
			}
		}
		}catch(UnexpectedWebDriverException e){
			e.printStackTrace();
		}
		try {

			log.debug("Working with :"+this.getClass().getName());
			execute();
			log.info("SCRIPT EXECUTION FINISHED");
			if(failed){
				String pageContentToPrint =  kw.doPrintPageAsTextFormatted();
				log.info("SCREENSHOT : "+'\n'+pageContentToPrint);
				log.error("SCRIPT FAILED "  );
				log.info("############# FAIL #############");

			}else{
				log.info("********* SUCCESS **************");
			}
			System.out.print("\007");
		     System.out.flush();
			Toolkit.getDefaultToolkit().beep();
			if(kw.getDriver()!=null){//Some Test scripts are simple file processors not browser based.
				//	kw.quitDriver();
			}

		}catch(UnexpectedWebDriverException e){
			e.printStackTrace();
			//if DriverFactory uses static instances, it is important that the DriverFactory is messaged that this has crashed.
			DriverFactory.reportCrashOfDriver();
			kw.create(scriptConstraint.webDriver);
			log.info("RETRYING!!");
			try {
				if (kw.getDriver()==null){
					kw.create("","");
				}
				execute();
			} catch (Exception e1) {
				log.error(e1);
				failed=true;
			}
			log.info("SCRIPT EXECUTION FINISHED");
			if(failed){
				log.error("EXECUITON FAILED? :"  + failed);

			}
			if(kw.getDriver()!=null){//Some Test scripts are simple file processors not browser based.
				//	kw.quitDriver();
			}
			//leaving no side effects

		}
		catch (SPOException e) {
			log.info(e);
			e.printStackTrace();
			if(e.getCause()!=null){
				e.getCause().printStackTrace();
			}
			failed=true;
		}
		catch (Exception e) {
			log.info(e);
			e.printStackTrace();
			failed=true;
		}finally{
			String pageContentToPrint =  kw.doPrintPageAsTextFormatted();
			log.info("SCREENSHOT : "+'\n'+pageContentToPrint);
		}

	}


	public void setInParamMap(Map<String, String> inParam) {
		this.inParams = inParam;
	}

	public ServiceHub getKw() {
		return kw;
	}

	public void setKw(ServiceHub kw) {
		this.kw = kw;
	}

	public Map<String, String> getStrategyParams() {
		return strategyParams;
	}

	public void setStrategyParams(Map<String, String> strategyParams) {
		this.strategyParams = strategyParams;
	}



	public boolean isFailed() {
		return failed;
	}



	public void setFailed(boolean failed) {
		this.failed = failed;
	}



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
		this.scriptConstraint = constraint;

	}

	@Override
	public String getFailureMessage() {
		return failureMessage;
	}



	public String getTestServerModuleName() {
		return testServerModuleName;
	}


	public void setTestServerModuleName(String testServerModuleName) {
		this.testServerModuleName = testServerModuleName;
	}


}

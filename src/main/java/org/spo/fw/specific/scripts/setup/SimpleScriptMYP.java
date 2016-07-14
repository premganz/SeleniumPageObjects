package org.spo.fw.specific.scripts.setup;

import org.spo.fw.config.SessionContext;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.selenium.JunitScript;




/**
 * 
 * 
 * @author Prem
 * Run Selenium Scripts from Robot.
 Project specific implementation of JUnitScript.
 *  
 *
 */



public abstract class SimpleScriptMYP extends JunitScript{

	protected void init_kw() throws Exception{
		init();
		kw.create("","");
		
	}
	@Override
	public  void init() {
		ApplicationNavContainerImpl navContainer = new ApplicationNavContainerMYP(){
			@Override
			public void init() {
				super.init();
				if(SessionContext.testEnv.equals("AT")){
					getDefaulModel().changePageState("Home", "AT:url="+SessionContext.appConfig.URL_AT);					
				}else{
					getDefaulModel().changePageState("Home", "Nightly:");		
					getDefaulModel().getFactory().addValidator("(.*)", new PageValidatorMYP());
					getDefaulModel().getFactory().addValidator("Home", null);
				}
				
			}
		};
		//navContainer.init();
		
		
		kw.setNavContainer(navContainer);
		
		super.init();//Defaults do not have necessary pre conditions so both pre and post init super class
		
	}


	public abstract void start_test() throws Exception;

	@Override
	public final void execute() throws Exception {
	//	init();
		start_test();

	     
	     
	}


}

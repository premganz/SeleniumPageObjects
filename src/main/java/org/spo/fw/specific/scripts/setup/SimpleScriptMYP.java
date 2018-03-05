package org.spo.fw.specific.scripts.setup;

import org.spo.fw.config.SessionContext;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.selenium.JunitScript;
import org.spo.fw.selenium.ScriptType;
import org.spo.fw.web.ServiceHubShortCircuit;






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
				}
					
					getDefaulModel().getFactory().addValidator("(.*)", new PageValidatorMYP());
					//getDefaulModel().getFactory().addValidator("Home", null);
					getDefaulModel().getFactory().addValidator("Dashboard", null);
					
			}
		};
		//navContainer.init();
		
		
			kw.setNavContainer(navContainer);
		
	
//		if(!isNotCacheable() && SessionContext.testEnv.equals(AppConstantsMYP.CACHE_RET)){			
//			SessionContext.appConfig.URL_UNIT_TEST_MODE="http://test/test_NIGHTLY/";
//			SessionContext.appConfig.customProperties.put("DCT_test_DB","dct_test_nightly");
//			kw.setContentProvider(new Lib_Content_Diff3());
//		}
		super.init();//You initialized kw then you changed some internals, now you will have to reinit it. Defaults do not have necessary pre conditions so post init super class
		//TODO
		if(SessionContext.testEnv.equals(AppConstantsMYP.LOCAL)) {//Disabling webdriver invocations and enabling only page compare
			kw = new ServiceHubShortCircuit();
			kw.setNavContainer(navContainer);
			kw.setContentProvider(new Lib_Content_LocalRunsRet());
			kw.init();
			
		}
		if(SessionContext.testEnv.equals(AppConstantsMYP.CACHE_STORE)){
			log.trace("The testEnv is  "+SessionContext.testEnv+ " so using Diff2 lib");
			kw.setContentProvider(new Lib_Content_LocalRunsStore());
			if(this.getScriptType().equals(ScriptType.DOWNLOAD_VERIFY)) {
				this.getKw().setContentProvider(new Lib_Content_Downloads_Cacheable());
				this.getKw().impl_nav.getNavContainer().getDefaulModel().getFactory().removeValidator("(.*)");
			}
			kw.init();
			
			
		}
	}


	public abstract void start_test() throws Exception;

	@Override
	public final void execute() throws Exception {
	//	init();
		start_test();

	     
	     
	}


}

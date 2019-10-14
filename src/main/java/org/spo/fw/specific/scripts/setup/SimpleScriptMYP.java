
/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.spo.fw.config.SessionContext;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.selenium.JunitScript;
import org.spo.fw.selenium.ScriptType;
import org.spo.fw.shared.DiffMessage;
import org.spo.fw.specific.scripts.dll.Lib_Content_Downloads_Cacheable;
import org.spo.fw.specific.scripts.dll.Lib_Content_LocalRunsRet;
import org.spo.fw.specific.scripts.dll.Lib_Content_LocalRunsStore;
import org.spo.fw.web.ServiceHub;
import org.spo.fw.web.ServiceHubShortCircuit;

import com.esotericsoftware.yamlbeans.YamlReader;





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
	private boolean forcePass;
	private ServiceHub kwEclipsed=null;
	protected long SL = AppConstantsMYP.SLP;
	
	
	protected void init_kw() throws Exception{
		init();
		if(!SessionContext.isBrowserLess)
		kw.create("","");
//		kw.setFailSlow(true);
		
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
					getDefaulModel().getFactory().addValidator("MYPMUQMDashboard", null);
					
			}
		};
		//navContainer.init();
		
		
			kw.impl_nav.setNavContainer(navContainer);
		
	
//		if(!isNotCacheable() && SessionContext.testEnv.equals(AppConstantsMYP.CACHE_RET)){			
//			SessionContext.appConfig.URL_UNIT_TEST_MODE="http://trvwcdqm28/deedee_NIGHTLY/";
//			SessionContext.appConfig.customProperties.put("DCT_DOMAIN_DB","dct_deedee_nightly");
//			kw.setContentProvider(new Lib_Content_Diff3());
//		}
		super.init();//You initialized kw then you changed some internals, now you will have to reinit it. Defaults do not have necessary pre conditions so post init super class
		//TODO
		if(SessionContext.testEnv.equals(AppConstantsMYP.LOCAL)) {//Disabling webdriver invocations and enabling only page compare
			kw = new ServiceHubShortCircuit();
			kw.impl_nav.setNavContainer(navContainer);
			kw.impl_page.setContent_provider(new Lib_Content_LocalRunsRet());
			kw.init();
			
		}
		if(SessionContext.testEnv.equals(AppConstantsMYP.DIFF)) {//Disabling webdriver invocations and enabling only page compare
			kw = new ServiceHubShortCircuit();
			kw.impl_nav.setNavContainer(navContainer);
			kw.impl_page.setContent_provider(new Lib_Content_LocalRunsDiff());
			kw.init();
			
		}
		if(SessionContext.testEnv.startsWith(AppConstantsMYP.CACHE_STORE)){
			log.trace("The testEnv is  "+SessionContext.testEnv+ " so using Diff2 lib");
			kw.impl_page.setContent_provider(new Lib_Content_LocalRunsStore());
			if(this.getScriptType().equals(ScriptType.DOWNLOAD_VERIFY)) {
				this.getKw().impl_page.setContent_provider(new Lib_Content_Downloads_Cacheable());
				this.getKw().impl_nav.getNavContainer().getDefaulModel().getFactory().removeValidator("(.*)");
			}
			kw.init();
			
			
		}
	}


	
	public void evalPageCompare(String pagePath){
		evalPageCmpForm("", pagePath);
	}
	
	public void evalPageCompareNoForm(String pagePath){
		evalPageCompare(pagePath);
	}
	
	
	//
	/**Evaluates a page by comparing it against an expected text file from a server and its form field values when specified 
	 * using suitable syntax. 
	 * First param is page name which (had been navigated to in the sut), second param is the module  in the file server
	 * that serves expected value. In the SEction wise page text syntax the form values are specified with metacharacters
	 * ***Section:formData***
	 * 
	 */
	public void evalPageCmpForm(String pageName, String pagePath){
		if(forcePass){
			log.info(this.getClass().getSimpleName() + " TEST WAS FORCE PASSED IN EVAL PAGE VERIFY GOOD REASON");
			return;
		}
//		if(SessionContext.testEnv.equals(AppConstantsMYP.LOCAL)) {
//			SessionContext.testEnv=AppConstantsMYP.NIGHTLY;
//			kw=new ServiceHub();
//			init();
////			try {			kw=kwEclipsed;	recreateDriver();	} catch (Exception e) {			e.printStackTrace();		}
//		}
		DiffMessage message = kw.checkPageLayoutForm(pageName,pagePath);
		setFailed(message.isFailed(),	message.getDiff()+'\n'+message.getErrorSummary());
	}
	
	
	private static boolean isKnownIssue(String testId, String failureMessage) {
//		YamlReader reader=null;
//		try {
//			//TODO Work in progress 
//			reader = new YamlReader(new FileReader("KNOWN_ISSUES.yml"));
//
//			ObjectMapper mapper = new ObjectMapper(new JsonFactory());
//			try {
//				try {
//					KnownIssues issues = mapper.readValue(new File("KNOWN_ISSUES.yml"), KnownIssues.class);
//					//			            System.out.println(ReflectionToStringBuilder.toString(user,ToStringStyle.MULTI_LINE_STYLE));
//					System.out.println(issues.getIssues().toString());
//					String testId_shorter= testId.substring(testId.lastIndexOf(".")+1, testId.length());
//					String failureMessage_cleaned = failureMessage.replaceAll("\n", "");
//					for(Issue issue:issues.getIssues()) {
//						if(issue.getTestId().equals(testId_shorter) && issue.getMessage().trim().equals(failureMessage_cleaned.trim())) {
//							return true;
//						}
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}


//		} catch (FileNotFoundException e) {
//			System.out.println("Breezing over the KNOWN_ISSUES.yml not being found");
//			//e.printStackTrace();
//			return false;
//		}
		return false;
	}
	
	public void setFailed(boolean failed, String failureMessage){
		this.failed=failed;
		this.failureMessage=failureMessage;
		if(SessionContext.testEnv.equals(AppConstantsMYP.IGNORE_KNOWN_ISSUES)) {
			boolean isknown_issue=isKnownIssue(this.getClass().getName(), failureMessage);
			if(isknown_issue) {
			this.failed=!isknown_issue;
			log.info("################ ::::: ALERT :::::::::::::###################");
			log.info("FAILURE SUPPRESSED SINCE THE ERROR APPEARS TO BE A KNOWN ISSUE LISTED IN THE KNOWNISSUES.YML");
			log.info("::::End of Message::::");
			}
			
		}
	}
	
	public abstract void start_test() throws Exception;

	@Override
	public final void execute() throws Exception {
		start_test();
	}
	
	public boolean isForcePass() {
		return forcePass;
	}
	public void setForcePass(boolean forcePass) {
		this.forcePass = forcePass;
	}



	}



	
	

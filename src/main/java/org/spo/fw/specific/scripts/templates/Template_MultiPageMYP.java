package org.spo.fw.specific.scripts.templates;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.spo.fw.log.Logger1;
import org.spo.fw.selenium.JunitScript;
import org.spo.fw.shared.DiffMessage;

/**
 * 
 * @author Prem
 * The test domain is Reports in ground state of application
 * This is a Non destructive test 
 *
 */
public class Template_MultiPageMYP extends JunitScript{

	protected Logger1 log = new Logger1("Template_MultiPage") ;
	protected Map<String, String> failedMap = new LinkedHashMap<String,String>();	
	protected ArrayList<String> debugCase=new ArrayList<String>(){
		{
			add("Home");
		}
	};
	protected ArrayList<String> excludeCase=new ArrayList<String>(){
		{
			add("Home");
		}
	};
	protected String xpathForPages="";
	protected String groupName="";
	protected int stopper=400;

	public void config(){
		xpathForPages="//page[@name='ReportsOverview']/nav/page";
		groupName="reports";
		
		debugCase=new ArrayList();	;//KEep this blank in all code checked in
		
		stopper=400;
	}
	public void start_test() throws Exception {
		config();
		List<String> reportNames= kw.impl_nav.queryAppDocModel(xpathForPages, "name");
		for(String name:reportNames){
			if(excludeCase.contains(name)){
				continue;
			}
			try{
				
				if(!debugCase.isEmpty() && !debugCase.contains(name)){
						continue;
				}
				log.info("####################################### Test: "+name);
			
				log.debug("Testing :"+name);

				
				//String result = kw.checkPageCollated("Test:"+name, "Reports/"+name);

				if(!failed){
					failed=start_test_isFailed(name);
				}else{
					start_test_isFailed(name);
				}
				
				stopper--;
				if(stopper==0)break;	
			}catch(Throwable t){
				log.error("ERror encountered "+t.getClass().getName());
				if(t instanceof Exception){
					((Exception)t).printStackTrace();
				}
				continue;
			}
		}
		log.info('\n'+"THESE MANY CASES FAILED:"+'\n'+failedMap.keySet().size());
		logReport();
		for(String key:failedMap.keySet()){
			log.error('\n'+key+":"+'\n'+failedMap.get(key));
			System.out.println("============================");
		}

	}
	@Override
	public final void execute() throws Exception {
	//	init();
		start_test();
	     
	}

protected void logReport()throws Exception {
	
}
	
	protected boolean start_test_isFailed(String name){
		kw.navigateByName(name);
		DiffMessage result = kw.checkPageAgainstRemoteFileExpression	(groupName+"/"+name);
		if(result.getErrorLog().length()>0){
			failedMap.put(name, "Section:"+result.getErrorSection() +"::::::DIFF:"+ result.getDiff());
		}
		return (result.getErrorLog().length()>0);
	}
	
	
	
	
}






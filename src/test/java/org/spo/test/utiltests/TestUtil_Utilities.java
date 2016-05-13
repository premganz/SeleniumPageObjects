package org.spo.test.utiltests;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;
import org.spo.fw.config.AppConfig;
import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.util.StateExpressionWrapper;
import org.spo.fw.session.SessionContainer;
import org.spo.fw.utils.pg.util.SPODateUtils;



public class TestUtil_Utilities { 
	Logger1 log = new Logger1("TestUtil_StateExpressionWrapper") ;

	@Test
	//StateExpressionWrapper
	public void validateRegex_StateExpressionWrapper() throws Exception{

		String[] positives= {"a:","A:","A:author=hello","A:author=hello&sub_author=hi 1 - ()+","",
				"A:population=DEC&subPopulation=DEC-10&sub=hi","N:url=https://wikipedia.org/","SET_TO_DEFAULT:"};
		for(String exp:positives){
			try{
				StateExpressionWrapper wrapper = new StateExpressionWrapper(exp);
			}catch(Exception e){
				log.debug("+ve faild for "+exp);
				e.printStackTrace();
				Assert.assertTrue(false);
			}
		}

		String[] negatives= {"&","A:hello"};
		for(String exp:negatives){
			try{
				StateExpressionWrapper wrapper = new StateExpressionWrapper(exp);
			}catch(Exception e){
				//e.printStackTrace();				
				Assert.assertTrue(true);
				continue;
			}
			log.error("negative faild for "+exp);
			Assert.assertTrue(false);
		}

	}
	@Test
	public void validateValueFetch(){
		String exp = "ChangeField:fldName=author&fldValue=asdf42&fldName=author_dob&fldValue=01/01/1961&au_id=res ";
		StateExpressionWrapper wrapper =null;
		try{
			wrapper = new StateExpressionWrapper(exp);
		}catch(Exception e){
			//e.printStackTrace();				
			Assert.assertTrue(false);
		}

		Assert.assertTrue(wrapper.values("fldName").size()>1);
		Assert.assertTrue(wrapper.values("fldValue").size()>1);
	}

	@Test
	public void testLogger(){

		try{
			SessionContainer.logQueue.clear();
			AppConfig config = new AppConfig();
			config.basicAuth_userId="testerId";
			SessionContext.appConfig=config;
			log.debug("http://"+SessionContext.appConfig.basicAuth_userId+"@www.google.com");
			String logValue = SessionContainer.logQueue.poll();
			log.error(logValue);
		}catch(Exception e){
			e.printStackTrace();				
			Assert.assertTrue(false);
		}

	}


	@Test
	public void testSPODateUtils(){

		try{
			String zone = "Asia/Tokyo";
			String dt1 = SPODateUtils.getApproxDateAsRegex(zone, "yyyy_MM_dd_HH:mm ", 2, TimeUnit.MINUTES);
			System.out.println(dt1);
		}catch(Exception e){
			e.printStackTrace();				
			Assert.assertTrue(false);
		}

	}
	
}

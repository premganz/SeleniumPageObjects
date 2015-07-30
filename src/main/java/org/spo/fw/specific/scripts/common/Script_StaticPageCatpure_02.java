package org.spo.fw.specific.scripts.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.spo.fw.config.Constants;
import org.spo.fw.config.SessionContext;
import org.spo.fw.itf.ScaffoldableScript;
import org.spo.fw.navigation.itf.MultiPage;
import org.spo.fw.navigation.itf.NavLink;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.selenium.KeyWordsScript;


/**
 * 
Checks data elements in abstract page against excel file.
 * 
 */
public class Script_StaticPageCatpure_02 extends KeyWordsScript  implements ScaffoldableScript
{



	//CONFIGURATION BY USER

	
	public int  start_eclipseMode() throws Exception{
		kw.create("browser", "testName");
		Thread.sleep(3000);			
		if(SessionContext.appConfig.atMode){
			return Constants.SE_SCRIPT_AT_MODE;	
		}else{
			return Constants.SE_SCRIPT_DEV_MODE;
		}
	}


	public void  initScaffolding() {

		//		inParams.put("measure", "common");
		//		inParams.put("absFieldType", ""+4);
		URL resourceUrl = getClass().getResource("/Script7Test.xml");
		String resourcePath = "";
		try {
			resourcePath = resourceUrl.toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}


	public int start_robotMode() throws Exception{
		return Constants.SE_SCRIPT_DEV_MODE;		
	}

//TODO To cover with test after creating a simple dummy applicationnavmodel.xml

	public  String start_test(int mode) throws Exception{		
		try{
		kw.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(kw.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		container.navigateToUrlByName("Home",kw );
		MultiPage multiPage = container.getMultiPage("Home");
		List<NavLink> navLinks = multiPage.getSubLinks();
		for(NavLink link: navLinks){
			multiPage.followLink(link, kw);
			Thread.sleep(1000);
			String x = kw.printPageAsTextFormatted();
			
			log.info(x);
			String filePath = SessionContext.textFilesPath+"/new/"+link.getName()+".txt";
			FileWriter fw = new FileWriter(new File(filePath));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(x);
			bw.close();
			log.debug("reached "+kw.getCurrentUrl());
			Thread.sleep(1000);
			multiPage.followLink(multiPage.getLoopBackLink(), kw);
			log.debug("reached "+kw.getCurrentUrl());
		}

		log.debug("reached "+kw.getCurrentUrl());
		Thread.sleep(3000);
		log.debug(kw.getCurrentUrl());
		//Assert.isTrue(kw.getCurrentUrl().contains("False"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	//AT





	public void route_delegate_handler(int abs_field_type) throws Exception{


			if(abs_field_type==6) handleCheckBoxes();
			if(abs_field_type==1) handleTextBoxes();
			if(abs_field_type==4) handleListBoxes();
	}


	public boolean handleListBoxes() throws Exception{
		boolean isSuccess= true;

		return isSuccess;
	}



	public boolean handleTextBoxes() throws Exception {
		log.info("Repop TextBoxes");
		boolean isSuccess= true;
		return isSuccess;
	}

	public boolean handleCheckBoxes() throws Exception {	
		log.info("repop cb");	
		boolean isSuccess= true;

		return isSuccess;	
	}

	public boolean delegate_handleLabelValidation(String lstBoxElemId){
		boolean isSuccess=true;

		return isSuccess;
	}



	
}

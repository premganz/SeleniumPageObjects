package org.spo.fw.specific.scripts.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.spo.fw.config.Constants;
import org.spo.fw.config.SessionContext;
import org.spo.fw.itf.ScaffoldableScript;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.selenium.ScriptConstraint;
import org.spo.fw.selenium.KeyWordsScript;


/**
 * 
Checks data elements in abstract page against excel file.
 * 
 */
public class Script_ControlsCatpure_03 extends KeyWordsScript  implements ScaffoldableScript
{



	//CONFIGURATION BY USER
	String pageName;
	String stateExpression;
	String toModifyPageName;

	public int  start_eclipseMode() throws Exception{
		kw.create("browser", "testName");
		pageName=System.getProperty("params.script03.pageName");
		toModifyPageName=System.getProperty("params.script03.toModifyPageName");
		stateExpression=System.getProperty("params.script03.stateExpression");
		//pageName=inParams.get("pageName");
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
			container.getDefaulModel().changePageState(toModifyPageName, stateExpression);
			container.navigateToUrlByName(pageName,kw );

			StringBuffer outputBuffer = new StringBuffer();
			String x = kw.doPrintPageAsTextFormatted();
			outputBuffer.append(x);
			Script_ControlsToMap_04 script = new Script_ControlsToMap_04();
			script.setScriptConstraint(new ScriptConstraint().setWebDriver(kw.getDriver()));
			script.startUp();
			//script.execute();
			Map<String,String> formMap = script.getPageDataStoreMap();
			ArrayList<String> sortedIds = new ArrayList<String>();
			sortedIds.addAll(formMap.keySet());
			Collections.sort(sortedIds);//sorting to make things sensible because different control types are obtained one after another
			outputBuffer.append(""+'\n'+'\n'+"***section:formdata***"+'\n'+'\n');
			for(String key : sortedIds){
				//outputBuffer.append(key+":"+formMap.get(key)+'\n');
				outputBuffer.append(formMap.get(key)+'\n');
			}

			outputBuffer.append(""+'\n'+'\n'+"***end***"+'\n'+'\n');
			log.info(outputBuffer.toString());
			String filePath = SessionContext.textFilesPath+"/new/"+pageName+".txt";
			FileWriter fw = new FileWriter(new File(filePath));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(outputBuffer.toString());
			bw.close();
			log.debug("reached "+kw.getCurrentUrl());

			Thread.sleep(3000);
			//Assert.isTrue(kw.getCurrentUrl().contains("False"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	//AT





	public void route_delegate_handler(int abs_field_type) throws Exception{
		handleCheckBoxes();
		handleTextBoxes();
		handleListBoxes();
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

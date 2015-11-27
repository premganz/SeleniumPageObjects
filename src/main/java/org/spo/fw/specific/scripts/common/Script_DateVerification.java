package org.spo.fw.specific.scripts.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.selenium.KeyWordsScript;
import org.spo.fw.web.ServiceHub;
import org.springframework.beans.factory.annotation.Autowired;


public class Script_DateVerification extends KeyWordsScript {
	@Autowired
	ServiceHub kw ;
	
	List<WebElement> lstDateFields = new ArrayList<WebElement>();
	List<String> lstTextBoxIds = new ArrayList<String>(); 
	
	Pattern  pattern = Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}");
	StringBuffer errorMsg = new StringBuffer();

	String[]  allowedVals = {"01/01/2000","01012011",  "01/01/11", "12/1/12"};
	String[]  deniedVals = {"0456789", "0101" ,"01/01/", "09/29/201", "9/29/2", "109/29/2012"};

		
	public boolean  doTest(){
		listDateFields();
		if(checkRandomDateFieldNegative() && checkRandomDateFieldPositive()){
			return true;
		}
		return false;
	}

	public boolean checkRandomDateFieldPositive(){
		Random rand = new Random();
		int rand_int = rand.nextInt(lstDateFields.size());
		WebElement dateField = lstDateFields.get(rand_int);
		for(int i = 0; i<allowedVals.length;i++){

			String allowed_val = allowedVals[i];
			dateField.clear();
			dateField.sendKeys(allowed_val);
			dateField.sendKeys("\u0009");
			if(SessionContext.isVisibleBrowser){
				try{
					kw.acceptAlert();

				}catch(NoAlertPresentException e){

				}

			}
			Matcher matcher = pattern.matcher(dateField.getAttribute("value"));
			if (matcher.find()) {

			}else{
				errorMsg.append("Failed for "+dateField.getAttribute("id")+":"+allowed_val);
				return false;
			}

		}

		return true;
	}


	public boolean checkRandomDateFieldNegative(){
		Random rand = new Random();
		int rand_int = rand.nextInt(lstDateFields.size());
		WebElement dateField = lstDateFields.get(rand_int);
		for(int i = 0; i<deniedVals.length;i++){

			String denied_val = deniedVals[i];
			dateField.clear();
			dateField.sendKeys(denied_val);
			dateField.sendKeys("\u0009");
		if(SessionContext.isVisibleBrowser){
			try{
				kw.acceptAlert();

			}catch(NoAlertPresentException e){

			}

		}
			
			Matcher matcher = pattern.matcher(dateField.getAttribute("value"));
			if (matcher.find()) {
				errorMsg.append("Failed for "+dateField.getAttribute("id")+":"+denied_val);
				return false;
			}else{

			}

		}

		return true;


	}

	public void listDateFields(){
		
		if(lstTextBoxIds.isEmpty()){
			
			List<WebElement> lstTextBoxes = kw.getDriver().findElements(By.tagName("input"));
			int loopLen = lstTextBoxes.size();
			for(int i = 0; i<loopLen;i++){
				lstTextBoxIds.add(lstTextBoxes.get(i).getAttribute("id"));
			}
		}
			for(int i = 0; i<lstTextBoxIds.size();i++){
				WebElement textBoxWidget = kw.getDriver().findElement(By.id(lstTextBoxIds.get(i)));
				//print1(textBoxWidget.getAttribute("id")+"="+textBoxWidget.getAttribute("type"));
				if(!textBoxWidget.getAttribute("type").equals("text") || !textBoxWidget.isDisplayed()){
					continue;
				}
				if(textBoxWidget.getAttribute("maxLength").equals("10") ){
					lstDateFields.add(textBoxWidget);
				}
			}

	}
	

	@Override
	public int start_eclipseMode() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int start_robotMode() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String start_test(int mode) throws Exception {
		lstTextBoxIds = Arrays.asList(constraint.widgetScopeIds);
		if(!doTest()){
			throw new AssertionError(errorMsg);
		}
		return null;
	}

	@Override
	public RunStrategy customizeStrategy(RunStrategy strategy) {
		// TODO Auto-generated method stub
		return null;
	}
	

}

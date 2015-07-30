package org.spo.fw.specific.scripts.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.spo.fw.config.Constants;
import org.spo.fw.itf.ScaffoldableScript;
import org.spo.fw.selenium.KeyWordsScript;
import org.spo.fw.specific.scripts.utils.Util_WidgetsFilter;
import org.spo.fw.web.Util_WebElementQueryHelper;


/**
 * 
Navigates to the pulllist page and navigates to the records on the page, enters/changes data randomly,
in listboxes, textboxes and checkbox,
stores url
comes back to the abstract page, verifies if entered values have indeed been successfully stored and retrieved back.
 * 
 */
public class Script_ControlsToMap_04  extends KeyWordsScript  implements ScaffoldableScript
{

	private Map<String, String> pageDataStoreMap = new LinkedHashMap<String, String>();
	int customGroupIdx;
	StringBuffer buf = new StringBuffer();
	int scriptId = 0;
	//CONFIGURATION BY USER

	

	private Map<String, List<String>> id_cache = new LinkedHashMap<String, List<String>>();

	private Util_WidgetsFilter.Workflow_scriptGeneric filterWorkflow = new Util_WidgetsFilter().new Workflow_scriptGeneric();
	private boolean makeRandomExclusions; 

	public int start_eclipseMode() throws Exception{
			return Constants.SE_SCRIPT_DEV_MODE;

	}
	public int start_robotMode() throws Exception{
			return Constants.SE_SCRIPT_DEV_MODE;

	}


	public  String start_test(int mode) throws Exception{
		setScriptId();
		if(inParams.containsKey("repop") && inParams.get("repop").equalsIgnoreCase("true")){
			route_delegate_handler(true);
		}else{
		route_delegate_handler(false);
		}
		return "success";
	}

	//AT


	public void route_delegate_handler(boolean repop) throws Exception{

		if(inParams.containsKey("absFieldType") && (Integer.valueOf(inParams.get("absFieldType")) > 0)){
			int abs_field_type = Integer.valueOf(inParams.get("absFieldType"));
			if(!repop){
				if(abs_field_type==6) pickOnCheckBoxes();//in this oroder to facilitate UTD.
				if(abs_field_type==1) pickOnTextBoxes();
				if(abs_field_type==4) pickOnListBoxes();
				if(abs_field_type==2) {
					pickOnListBoxes();
					pickOnTextBoxes();
				}
			}else{
				if(abs_field_type==6) repopCheckBoxes();
				if(abs_field_type==1) repopTextBoxes();
				if(abs_field_type==4) repopListBoxes();
				if(abs_field_type==2) {
					repopListBoxes();
					repopTextBoxes();
				}
			}}else{
				if(!repop){
					pickOnListBoxes();
					pickOnCheckBoxes();//in this oroder to facilitate UTD.
					pickOnTextBoxes();
				}else{
					repopListBoxes();
					repopCheckBoxes();
					repopTextBoxes();
				}

			}
	}

	public void pickOnListBoxes() throws Exception{
		Random rand = new Random();
		List<String> listSbValues = util_getWidgetIdList("select",scriptId);
		List<String> lstSbValuesMdf = new ArrayList<String>();
		String lstBoxElemId = StringUtils.EMPTY;
		String mdfValue = StringUtils.EMPTY;
		log.info("COPYING LISTBOXES");
		log.trace("picked up controls "+listSbValues);
		for(int i = 0; i< listSbValues.size();i++){
			lstBoxElemId = listSbValues.get(i);

			try{
				Select select = new Select(kw.getDriver().findElement(By.id(lstBoxElemId)));				
				mdfValue = select.getFirstSelectedOption().getText();
				lstSbValuesMdf.add(lstBoxElemId);
				handle_storeMdfVals("LISTBOX", lstBoxElemId, mdfValue);
			}catch(Exception e){
				log.error("WARNING:An Error occured in changing checkbox  box : "+lstBoxElemId +" to   "+mdfValue);
				log.error(e);
				continue;
			}
			id_cache.put("listbox", lstSbValuesMdf);
		}
	}

	public void repopListBoxes() throws Exception{
		log.info("Repop Lb");
		List<String> listSbValues = util_getWidgetIdList("select",scriptId);
		for(int i = 0; i< listSbValues.size();i++){
			String lstBoxElemId = listSbValues.get(i);
			try{
				final Select select = new Select(new Util_WebElementQueryHelper( kw.getDriver()).querySilent(lstBoxElemId));				
				String selectedValue = select.getFirstSelectedOption().getText();				
				handle_CheckForSuccess("select", lstBoxElemId, selectedValue);
			}catch(Exception e){
				//print1(e.getMessage());
				continue;
			}
		}
	}

	public void pickOnCheckBoxes() throws Exception{
		log.info("CHANGING CHECKBOX");
		List<String> listCbValues = util_getWidgetIdList("checkbox",scriptId);
		List<String> cbWidgetsModified = new ArrayList<String>();
		String checkBoxWidgetId =StringUtils.EMPTY;
		for(int i = 0; i<listCbValues.size();i++){
			try{
				checkBoxWidgetId = listCbValues.get(i);
				WebElement checkBoxWidget;
					checkBoxWidget = kw.getDriver().findElement(By.xpath("//input[@id='"+checkBoxWidgetId+"']"));
					if(checkBoxWidget.isSelected()){
						log.trace("asserted checkbox selected");
						cbWidgetsModified.add(checkBoxWidgetId);
						handle_storeMdfVals("CHECKBOX", checkBoxWidgetId, "changed");
					}

			}catch(Exception e){
				log.error("WARNING:  An Error occured in changing checkbox  box : "+checkBoxWidgetId +"  ");
				log.error(e);
				continue;
			}

		}
		//id_cache.remove("checkbox");
		id_cache.put("checkbox", cbWidgetsModified);//Special case for checkbox
	}


	public void pickOnTextBoxes() throws Exception {		
		log.info("CHANGING text boxes");
		List<String> tbWidgetsModified = new ArrayList<String>();
		
		final String numbers = "0123456789";
		String existingVal = StringUtils.EMPTY;
		String id = StringUtils.EMPTY;
		List<String> lstTextBoxIds = util_getWidgetIdList("input",scriptId); 
		log.trace("picked controls "+lstTextBoxIds);
		for(int i = 0; i<lstTextBoxIds.size();i++){
			try{
				existingVal = StringUtils.EMPTY;
				id =  lstTextBoxIds.get(i);
				WebElement textBoxWidget = kw.getDriver().findElement(By.id(id));
				existingVal = textBoxWidget.getAttribute("value");
				id = textBoxWidget.getAttribute("id");



			}catch(Exception e){
				log.error("WARNING:An Error occured in  text box : ["+id +"] ");
				log.error(e);
				
				continue;
			}
			tbWidgetsModified.add(id);
			handle_storeMdfVals("TEXT BOX", id, existingVal);
		}
		id_cache.put("input", tbWidgetsModified);
	}

	public void repopTextBoxes() throws Exception {
		log.info("Repop TextBox");
		String val_toVerify = "";
		List<String> lstTextBoxIds = util_getWidgetIdList("input",scriptId); 

		for(int i = 0; i<lstTextBoxIds.size();i++){
			try{
				WebElement textBoxWidget = kw.getDriver().findElement(By.id(lstTextBoxIds.get(i)));
				val_toVerify = textBoxWidget.getAttribute("value");
				String id = textBoxWidget.getAttribute("id");				
				handle_CheckForSuccess("TEXTBOX", id, val_toVerify);
			}catch(Exception e){
				continue;
			}
		}
	}

	public void repopCheckBoxes() throws Exception {	
		log.info("repop cb");	
		List<String> listCbValues = util_getWidgetIdList("checkbox",scriptId);
		for(int i = 0; i<listCbValues.size();i++){
			String checkBoxWidgetId = listCbValues.get(i);
			if(!checkBoxWidgetId.equals(StringUtils.EMPTY)){
				handle_CheckForSuccess("checkbox", checkBoxWidgetId, "changed");
			}

		}}



	public void handle_storeMdfVals(String type, String id, String mdfVal){

			pageDataStoreMap.put(id,  String.valueOf(mdfVal));

		log.debug(type+" : changed :"+id+"/"+mdfVal);
	}

	public void handle_CheckForSuccess(String type,String elemId, String actualVal){
		String selectedValueFromMap = pageDataStoreMap.get(elemId);
		//The null check for checkbox makes sure that checkboxes that were skipped while actually changed only are rechecked 
		if(type.equals("checkbox") && selectedValueFromMap!=null){
			WebElement checkBoxWidget = kw.getDriver().findElement(By.xpath("//input[@id='"+elemId+"']"));
			if(checkBoxWidget.isSelected()){
				log.debug("Success:"+elemId);
			}
			else{
				failed=true;
				log.debug(":FAILED:"+": repopulated"+type+" :"+ elemId+" "+actualVal+" is Actuals");
			}return;
		}
		if(actualVal.equals(selectedValueFromMap)){
			log.debug("Success:"+elemId);
		}else{
			failed=true;
			log.debug(":FAILED:"+": repopulated"+type+" :"+ elemId+" "+actualVal+" is Actuals");
		}
	}


	public Map<String, String> getPageDataStoreMap() {
		return pageDataStoreMap;
	}


	public void setPageDataStoreMap(Map<String, String> pageDataStoreMap) {
		this.pageDataStoreMap = pageDataStoreMap;
	}


	public Map<String, List<String>> getId_cache() {
		return id_cache;
	}


	public void setId_cache(Map<String, List<String>> id_cache) {
		this.id_cache = id_cache;
	}

	public void setScriptId(){
		scriptId=40;
	}
	@Override
	public void initScaffolding() {
		// TODO Auto-generated method stub
		
	}
	
	public List<String> util_getWidgetIdList(String type, int scriptId){
	
		if(id_cache.containsKey(type)){
			log.trace("in getWidgetList id_cache stands as "+id_cache);
			return id_cache.get(type);

		}
		String tagName = type;
		if (type.equals("checkbox")) tagName= "input";		
		List<WebElement> lstElems = kw.getDriver().findElements(By.tagName(tagName));
		List<String> listSbValues = new ArrayList<String>();
		//Freezing context for each call and not for each control
		for(int i = 0; i< lstElems.size();i++){
			WebElement elem = lstElems.get(i);
			try{
					filterWorkflow.step1_setContext(makeRandomExclusions, type);
					boolean widgetExcludable = filterWorkflow.step2_configRunFilter(elem);
					if(!widgetExcludable){
						listSbValues.add(elem.getAttribute("id"));	
					}
			}catch(StaleElementReferenceException e){
				i--;	
			}
		}

		id_cache.put(type, listSbValues);
		return listSbValues;
	}
	


}

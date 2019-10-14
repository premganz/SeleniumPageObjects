/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.selenium.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.web.ServiceHub;


/**
 * 
 * 
 * This class extends the ServiceHub library that is based on Selenium and introduced in 2.0.0. 
 * 
 * THIS SCRIPT RECORDS THE DATA ON A SCREEN FOR A GIVEN URL, STORES TO A FILE OF CHOICE, 
 * optional: IT THEN PERFORMS AN OPTIONAL SAVE OPERATION
 * optional: NAVIGATES TO ANOTHER SCREEN and verifies recorded data expected for that screen. 
 * 
 * This script is configurable with
 * 
 * 1. Target url
 * 2. File format to store
 * 3. Save button operation
 * 4. NExt navigation
 * 5. File to compare
 * 
 * BUT FORGET ALL THE FEATURES, THIS WILL MOSTLY BE USED FOR GENERATING ID AND VALUE KEYWORDS FOR ROBOT FOR A GIVEN URL.
 * 
 * 
 *
 * Works only with ie
 * There are at several places runtime exceptions eaten simply, reason being the unpredictability of ids of html tags and as work
 * around to handle alerts, guess excusable in tool code.
 * There are at many pleases thread introduced to get it working.
 * The list of widgets got by calling By.tagName is not directly looped but rather stored to a new list of ids and then looped 
 * to solve a problem  caused by time taken in looping causing staleness.  
 * 
 */
public class RecordPlayTests extends ServiceHub{ 
	//implements SeleniumScript{

	protected static final String HTTP = "http://";
	protected static final String SLASH = "/";

	private static Map<Character, Character> replacementMap = null;

	private static boolean debug;
	private static boolean debug1;
	private Map<String, String> pageDataStoreMap = new LinkedHashMap<String, String>();
	StringBuffer buf_master = new StringBuffer();
	private static final String TEXTBOX = "Text-Box";
	private static final String LISTBOX = "List-Box";
	private static final String CHECKBOX = "Check-Box";
	private static final String COMMA = ",";
	
	
	
	
	
	private static String ROOT_DIR = "";
	
	
	
	
	/****** USER DEFINED ***********/
	//COMMON KEYS
	private static String TEST_NAME="TEST1";
	private static String MEASURE_NAME="HF";
	
	//RECORD KEYS
	private static String section_tag_name="div_HF";
	private static String url_toTest="";
	
	//RERUN KEY
	private String EOC_ID_TO_TEST;
	
	
	/****** USER DEFINED ***********/
	
	private  static String FILE_DATA_CSV = "C:\\" +MEASURE_NAME+".csv";
	
	
	private static String DEST_DIR=ROOT_DIR+MEASURE_NAME;
	
	public  WebDriver driver ;//TODO make private 

	public void start(){
		try {
			execute();
		} catch (SPOException e) {
			e.printStackTrace();
		}
	}


	public void execute() throws SPOException{
		
	create("", "RecordPlayTest");
	driver = getDriver();
		
		//driver = new PhantomJSDriver();
		driver = new InternetExplorerDriver();
			buf_master =new StringBuffer();
		//	RunStrategy.isRecordMode=true;
			//rowSelect=3;
				try {
				goToPage(url_toTest);
			
				Thread.sleep(1000);
				Thread.sleep(1000);
				print1("==================================================================================");
				print1(getCurrentUrl());
				String url= getCurrentUrl();
				Thread.sleep(100000);
				record_listBoxValues();
				record_Radio();
				record_Checkbox();//in this oroder to facilitate UTD.
				record_TextBox();
				Thread.sleep(3000);
				//record_Skips_toFile();
				Thread.sleep(3000);
				//record_Cats_toFile();
				Thread.sleep(3000);
				
				util_mapToFile();
				
				driver.quit();
				} catch (Exception e) {
					e.printStackTrace();
				}

	}

	

	public String localTester() throws Exception{
		//driver = new PhantomJSDriver();
		driver = new InternetExplorerDriver();

		for( int k=0;k<1;k++){			
			goToPage("");

		}
		return "hi";
	}

	public void record_listBoxValues() throws Exception{
		print1("***LISTBOXES***"+'\n');
		List<WebElement> lstSelectBoxes = driver.findElements(By.tagName("select"));
		List<String> listSbValues = new ArrayList<String>();
		for(int i = 0; i< lstSelectBoxes.size();i++){
			WebElement lstBoxElem = lstSelectBoxes.get(i);
			listSbValues.add(lstBoxElem.getAttribute("id"));
		}

		for(int i = 0; i< listSbValues.size();i++){
			String lstBoxElemId = listSbValues.get(i);
		
			try{
				Select select = new Select(driver.findElement(By.id(lstBoxElemId)));
				pageDataStoreMap.put(LISTBOX+COMMA+lstBoxElemId,  select.getFirstSelectedOption().getText());
				print1("Select Option    "+lstBoxElemId+"    "+select.getFirstSelectedOption().getText());
			}catch(Exception e){
				//print1(e.getMessage());
				continue;
			}
		}
		//print1(buf_master.toString());
		print2("completed List Data save");
	}


	public void record_Checkbox() throws Exception{
		print1("***CHECK BOXES***"+'\n');
		List<WebElement> lstTextBoxes = driver.findElements(By.tagName("input"));
		
		int loopLen = lstTextBoxes.size();
		for(int i = 0; i<loopLen;i++){
			WebElement textBoxWidget = lstTextBoxes.get(i);
			if(textBoxWidget.getAttribute("type").equals("checkbox") && textBoxWidget.getAttribute("id").contains("_")){
				pageDataStoreMap.put(CHECKBOX+COMMA+textBoxWidget.getAttribute("id"),  textBoxWidget.getAttribute("_just_changed"));
				print1("Click    "+textBoxWidget.getAttribute("id")+"    "+textBoxWidget.getAttribute("_just_changed"));
			}
			
		}
		
	}

	
	public void record_Radio() throws Exception{
		print1("***RADIO***"+'\n');
		List<WebElement> lstTextBoxes = driver.findElements(By.tagName("input"));
		
		int loopLen = lstTextBoxes.size();
		for(int i = 0; i<loopLen;i++){
			WebElement textBoxWidget = lstTextBoxes.get(i);
			if(textBoxWidget.getAttribute("type").equals("radio") ){
				print1("Click    "+textBoxWidget.getAttribute("id")+"    "+textBoxWidget.getAttribute("_just_changed"));
			}
			
		}
		
	}

	public void record_TextBox() throws Exception {
		print("***TEXT BOXES***"+'\n');
		List<WebElement> lstTextBoxes = driver.findElements(By.tagName("input"));
		
		int loopLen = lstTextBoxes.size();
		String existingVal = "";
		List<String> lstTextBoxIds = new ArrayList<String>(); 
		for(int i = 0; i<loopLen;i++){
			lstTextBoxIds.add(lstTextBoxes.get(i).getAttribute("id"));
		}
		//String encDate= "04/01/2014";//encounter date
        
		for(int i = 0; i<lstTextBoxIds.size();i++){
			
			try{
				WebElement textBoxWidget = driver.findElement(By.id(lstTextBoxIds.get(i)));
				//print1(textBoxWidget.getAttribute("id")+"="+textBoxWidget.getAttribute("type"));
				if(!textBoxWidget.getAttribute("type").equals("text") || !textBoxWidget.isDisplayed()){
					continue;
				}
				existingVal = textBoxWidget.getAttribute("value");
				String id = textBoxWidget.getAttribute("id");
				pageDataStoreMap.put(TEXTBOX+COMMA+id,  existingVal);
				print1("Enter Text   "+id+"    "+existingVal);
				try{
					//System.out.println("Main Thread calling alert accept");				
					driver.switchTo().alert().accept();	

				}catch(NoAlertPresentException e){
				}
			
		}catch(Exception e){
			//print1(e.getMessage());
			continue;
		}
		}
	}

	public void record_Skips_toFile(){
		
		String xpath = "//div[@id='" + section_tag_name +"']";
		WebElement element = driver.findElement(By.xpath(xpath));
//		String html_section = element.getAttribute("innerHTML");
//		LabelNodeVisitor visitor = new LabelNodeVisitor();
//		Jsoup.parse(html_section ).traverse(visitor);
		String parsedText = element.getText();		
		String clean = parsedText.replaceAll("\\P{Print}", "");
		util_WriteToFile(new StringBuffer(clean), DEST_DIR+"/"+"Skip_"+TEST_NAME+".aspx");
		print2("completed Skip record");
	}
	public void record_Cats_toFile() throws Exception{
		
		
	}
	

	public  void print2(String x ){
		//System.out.println(x);
	}

	
	public void util_mapToFile(){
		String header = ""; 
		StringBuffer  buf_input_file = util_ReadFromFile(FILE_DATA_CSV);
		StringBuffer  buf_output_file =new StringBuffer();
		Iterator<String> mapKeyIter = pageDataStoreMap.keySet().iterator();
		List<String> lines = new ArrayList<String>();
		Collections.addAll(lines, buf_input_file.toString().split("\n"));
		if (StringUtils.deleteWhitespace(buf_input_file.toString()).isEmpty()){
			header = "Elem-type,Elem-id";
		}else{
			header = lines.get(0);
		}
		header=header.concat(COMMA+TEST_NAME)+'\n';
		buf_output_file.append(header);
		
		while(mapKeyIter.hasNext()){
			String key = mapKeyIter.next();
			String value = pageDataStoreMap.get(key);
			if (StringUtils.deleteWhitespace(buf_input_file.toString()).isEmpty()){
				buf_output_file.append(key+COMMA);
				buf_output_file.append(value+'\n');
			}else{
				//loop through the lines and pick up the key
				
				inner:
				for(int i = 0; i<lines.size(); i++){
					String line = lines.get(i);
					if(null!=line && !line.isEmpty()){					
						String element_meta = line.split(",")[0]+COMMA+line.split(",")[1];//first two positions as meta
						if(key.equals(element_meta)){
							line = line.concat(COMMA).concat(value);
							buf_output_file.append(line+'\n');
							break inner;
						}
					}
				}
			}
		
			
			
		}
		util_WriteToFile(buf_output_file, FILE_DATA_CSV);
	}
	
	
	
	public String util_fileToMap(int  idx_test){
		
		StringBuffer  buf_input_file = util_ReadFromFile(FILE_DATA_CSV);
		List<String> lines = new ArrayList<String>();
		Collections.addAll(lines, buf_input_file.toString().split("\n"));
		
		for(int i = 0; i<lines.size(); i++){
			String line = lines.get(i);
			if(null!=line && !line.isEmpty()){					
				String element_meta = line.split(",")[0]+COMMA+line.split(",")[1];//first two positions as meta
				String value_set_forKey = StringUtils.EMPTY;
				if(i>0 && line.split(",").length<=idx_test){//TODO Workaround for an assumptive feature in Split which ignores trailing blanks.
					
				}else{
					value_set_forKey =  line.split(",")[idx_test];
				}
				
				pageDataStoreMap.put(element_meta,value_set_forKey);
			}
		}
		
	return "Map is populated for index "+idx_test;
	}
	
	public void util_WriteToFile(StringBuffer buf, String filePath){
		FileWriter writer2;
		try {
			writer2 = new FileWriter(new File(filePath));
			writer2.write(buf.toString());
			writer2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public StringBuffer util_ReadFromFile(String filePath){
		StringBuffer buf_read_temp = new StringBuffer();
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(filePath));String ss;
			while ((ss = reader.readLine()) != null) {
				buf_read_temp.append(ss+'\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf_read_temp;
	}

	//ALL for running all tests
	public String replay_Start(String testFilePath,  String url , String testId) throws Exception{
		//driver = new PhantomJSDriver();
		driver = new InternetExplorerDriver();
		goToPage(url);
		Thread.sleep(6000);
		print1(getCurrentUrl());
		StringBuffer result = new StringBuffer();
		result.append("Running Test with ID "+testId+'\n');
		StringBuffer  buf_input_file = util_ReadFromFile(FILE_DATA_CSV);
		List<String> lines = new ArrayList<String>();
		Collections.addAll(lines, buf_input_file.toString().split("\n"));
		String testName = testId;
		String header = lines.get(0);
		String[] header_arr = header.split(",");
		if (StringUtils.deleteWhitespace(buf_input_file.toString()).isEmpty()){
			return "";
		}
		if(testId.equals("ALL")){
			for(int k = 0; k < header_arr.length;k++){
					testName = header_arr[k];
					result.append("Running Now "+testName);
					result.append(util_fileToMap(k));
			}
			
		}else{
			for(int k = 0; k < header_arr.length;k++){
				String test_k = header_arr[k];
				if(test_k.equals(testName)){
					result.append(util_fileToMap(k));
					break;
				}
			}
		}
		print2("REPLAY MODE");
		print2(result.toString());
		//result.append(replay_SaveCheck());
		Thread.sleep(2000);
		result.append(replay_Skips_fromFile());
		Thread.sleep(2000);
		result.append(replay_Cats_fromFile());
		Thread.sleep(2000);
		print1(result.toString());
		return "hi";
	}

	
	public String replay_SaveCheck() {
		try {
			replay_enterListBox();
			//replay_enterTextBoxes();
			doExecuteJavaScript("tha.abstraction.Save()");
			Thread.sleep(7000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";	
	}
	
	public void replay_enterListBox() throws Exception{
		List<WebElement> lstSelectBoxes = driver.findElements(By.tagName("select"));
		List<String> listSbValues = new ArrayList<String>();
		print2("Entering replay list boxes");
		for(int i = 0; i< lstSelectBoxes.size();i++){
			WebElement lstBoxElem = lstSelectBoxes.get(i);
			listSbValues.add(lstBoxElem.getAttribute("id"));
		}
		print2("list boxes size "+listSbValues.size());
		for(int i = 0; i< listSbValues.size();i++){
			String lstBoxElemId = listSbValues.get(i);
			String selectedValueMap = pageDataStoreMap.get(LISTBOX+COMMA+lstBoxElemId);
			Select select = new Select(driver.findElement(By.xpath("//select[@id='"+lstBoxElemId+"']")));
			try{
				List<WebElement> options_list = select.getOptions();
				List<String> options_text_list = new ArrayList<String>();
				for(WebElement elem:options_list){
					options_text_list.add(elem.getText());
				}
				for(String option_text : options_text_list){
					if(option_text.equals(selectedValueMap)){
						int optionIdx = options_text_list.indexOf(selectedValueMap);
						select.selectByIndex(optionIdx);
					}
				}
				
				print2("retrived size "+selectedValueMap);
				try{
					driver.switchTo().alert().accept();	
				}catch(NoAlertPresentException e){

				}
				print1("LISTBOX : verified :"+i +":"+lstBoxElemId);
			}catch(Exception e){
				print1("Exception while tyring to select "+selectedValueMap+" in  "+lstBoxElemId);
				//print1(e.getMessage());
				continue;
			}
		}
	}
	
	public void replay_enterTextBoxes() throws Exception {
		print2("Entering text  boxes");
		List<WebElement> lstTextBoxes = driver.findElements(By.tagName("input"));
		int loopLen = lstTextBoxes.size();
		String val_fromMap = "";
		List<String> lstTextBoxIds = new ArrayList<String>(); 
		for(int i = 0; i<loopLen;i++){
			lstTextBoxIds.add(lstTextBoxes.get(i).getAttribute("id"));
		}

		for(int i = 0; i<lstTextBoxIds.size();i++){
			try{
				WebElement textBoxWidget = driver.findElement(By.id(lstTextBoxIds.get(i)));
				
				if(!textBoxWidget.getAttribute("type").equals("text") || !textBoxWidget.isDisplayed()){
					continue;
				}
				String id = textBoxWidget.getAttribute("id");
				val_fromMap = pageDataStoreMap.get(TEXTBOX+COMMA+id);
				print2(pageDataStoreMap.toString());
				print2("Trying with key "+TEXTBOX+COMMA+id);
				print2("Entering text  boxes");
				textBoxWidget.clear();
				textBoxWidget.sendKeys(val_fromMap);
				textBoxWidget.sendKeys("\u0009");
				print1("TEXT Verified "+textBoxWidget.getAttribute("id")+"="+val_fromMap);
				
				Thread.sleep(500);
				try{
					driver.switchTo().alert().accept();	

				}catch(NoAlertPresentException e){
				}
				print1(val_fromMap);
			
		}catch(Exception e){
			//print1(e.getMessage());
			continue;
		}
		}
	}
	
	public String replay_Skips_fromFile(){
		
		String xpath = "//div[@id='" + section_tag_name +"']";
		WebElement element = driver.findElement(By.xpath(xpath));
		filePath_root_screens = DEST_DIR+"\\";
//		String html_section = element.getAttribute("innerHTML");
//		LabelNodeVisitor visitor = new LabelNodeVisitor();
//		Jsoup.parse(html_section ).traverse(visitor);
		String parsedText = element.getText();		
		//String clean = parsedText.replaceAll("\\P{Print}", "");
		//StringBuffer input_fromFile = util_ReadFromFile(DEST_DIR+"/"+"Skip_"+TEST_NAME+".aspx");
		try{
			checkPageAgainstFile("Skip_"+TEST_NAME+".aspx");
		}catch (AssertionError err){
			return err.getMessage();
			
		}
			return "Skips_success";
		}
		
	public String replay_Cats_fromFile() throws Exception{
		
		clickByTagAndAttribute("a", "target", "categorizations"); 
		Thread.sleep(1000);
		doSwitchToNewWindow();
		Thread.sleep(1000);
		if(driver.getTitle().contains("an HCO")){
			click("_ctl0:PageMeat:btnGo");
			
		}
		Thread.sleep(1000);
		String xpath = "//table[@id='" + "mainContent" +"']";
		WebElement element = driver.findElement(By.xpath(xpath));
		String parsedText = element.getText();		
		String clean = parsedText.replaceAll("\\P{Print}", "");
		util_WriteToFile(new StringBuffer(clean), DEST_DIR+"/"+"Measures_"+TEST_NAME+".aspx");
		StringBuffer input_fromFile = util_ReadFromFile(DEST_DIR+"/"+"Measures_"+TEST_NAME+".aspx");
		filePath_root_screens = DEST_DIR+"\\";
		try{
			checkPageAgainstFile("Measures_"+TEST_NAME+".aspx");
		}catch (AssertionError err){
			return err.getMessage();
			
		}
			return "Cats_success";
	}
	
	
	public static void main(String[] args) throws Exception {
		RecordPlayTests abs = new RecordPlayTests();
		

	}


	public void setStartPoint() {
		// TODO Auto-generated method stub
		
	}

	
	//VerifyCheckbox code

//	Random rand = new Random();
//	List<WebElement> lstTextBoxes = driver.findElements(By.tagName("input"));
//	List<String> listCbValues = new ArrayList<String>();
//	int loopLen = lstTextBoxes.size();
//	for(int i = 0; i< loopLen;i++){
//		WebElement lstBoxElem = lstTextBoxes.get(i);
//		listCbValues.add(lstBoxElem.getAttribute("id"));
//	}
//	for(int i = 0; i<listCbValues.size();i++){
//		String checkBoxWidgetId = listCbValues.get(i);
//		WebElement checkBoxWidget = driver.findElement(By.xpath("//input[@id='"+checkBoxWidgetId+"']"));
//		if(checkBoxWidget.getAttribute("type").equals("checkbox")){
//			
//			int a = rand.nextInt(1);
//			if(a==0 && !checkBoxWidget.isSelected()){
//				if(checkBoxWidget.isDisplayed()){
//					checkBoxWidget.sendKeys(Keys.SPACE);
//					checkBoxWidget.sendKeys("\u0009");	
//				}
//				
//				Thread.sleep(500);
//				print1("CHECKBOX"+": changed :"+i +":"+checkBoxWidget.getAttribute("id")+"/");
//				pageDataStoreMap.put(checkBoxWidget.getAttribute("id"),  "selected");
//			}
//				
//			
//			
//			
//			try{
//								
//				driver.switchTo().alert().accept();	
//			}catch(NoAlertPresentException e){
//			}
//		}
//	}

	
	
}

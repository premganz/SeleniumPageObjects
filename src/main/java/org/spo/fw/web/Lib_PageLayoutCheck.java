package org.spo.fw.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.utils.Utils_PageDiff;

import difflib.DiffUtils;
import difflib.Patch;

@Deprecated
public class Lib_PageLayoutCheck extends Lib_KeyWordsCore{
private static final String IGNORABLE_HDR="Help|Support|SignOutSelectanHCO:GeneralHospitalGreatCareHospitalHCOID:2222HomeConcurrentCreateUploadAbstractApproveDataRe-abstractReportsSettingsResources";
private static final String[] IGNORABLE_STRINGS={"[\\n]"};
private static final String[] IGNORABLE_STRINGS_L2={",",".","/",":","//*"};
private List<String>  debug_pageContent_list = new ArrayList<String>();
private List<String>  debug_fileContent_list = new ArrayList<String>();
private StringBuffer collated_log=new StringBuffer();
private PageFactory factory;
ServiceHub kw;

	public Lib_PageLayoutCheck(WebDriver driver) {
		super(driver);
	}

	public String delegate_checkPageAgainstFile(String pageText, List<String> fileLines, Properties properties, boolean ignoreFormData){
		String message;
		//1. Initializations
		StringBuffer buf = new StringBuffer();
		StringBuffer buf_fileText_debug = new StringBuffer();
		Map<String,Integer> debugMapInfo = new LinkedHashMap<String, Integer>();
		List<String> lstTestData = new ArrayList<String>();
		List<String> debugListSectionTitles = new ArrayList<String>();
		Map<String, String> sectionTypeTitles = new LinkedHashMap<String, String>();//sectionName, section Type
		int lineNumber = 1;
		//Getting ready for diffUtils.
		debug_pageContent_list=	Arrays.asList(pageText.split("\\n"));
		List<String> fileLinesDebuggable = new ArrayList<String>();
//		fileLinesDebuggable.add("CareDiscovery Quality Measures - " +
//				"ExpectedPage" +'\n'+
//				"Help | Support | Sign Out " +'\n'+
//				"Select an HCO: * General Hospital* Great Care Hospital HCO ID:2222 Home Concurrent Create Upload Abstract Approve Data Re-abstract Reports Settings Resources " 
//				+'\n');
		fileLinesDebuggable.addAll(fileLines);
		debug_fileContent_list=	 fileLinesDebuggable;

		//BLOCK 2: PreProecssing
		pageText= preProcessString(pageText);
		List<String> fileLines1 = new ArrayList<String>();
		try{
			for(String line1: fileLines) {		
				line1=util_preProcessFileLine(line1);
				debugMapInfo.put(substituteDynaVals(line1, properties),lineNumber);
				fileLines1.add(line1);
				lineNumber++;
			}
			

			//Copying file content into a list and a debug Buffer
			//Preprocessign by substituting dynavals and removing whitespace and meta chars.
				boolean allowedSection=true;
				String sectionTitle="";		
			for(String line1:fileLines1) {
			
				if(line1.startsWith("***") && line1.matches("^(([\\*]{3})([Bb]reak)([\\*]{3}))$")){
					lstTestData.add(StringUtils.deleteWhitespace(buf.toString()));
					buf = new StringBuffer();
					debugListSectionTitles.add("section"+(debugListSectionTitles.size()+1));
					continue;

				}
				else if(line1.startsWith("***") &&  line1.matches("^(([\\*]{3})([Ss]ection\\:[\\w\\W0-9]{0,100})([\\*]{3}))$")){
					String temp= line1.replaceAll("\\*", "");
					String sectionName= temp.substring(line1.indexOf(":")-2);
					if(sectionName.equalsIgnoreCase("formData") && ignoreFormData){
						allowedSection=false;
					}					
					
					System.out.println("section heading is "+sectionName);
					lstTestData.add(StringUtils.deleteWhitespace(buf.toString()));
					debugListSectionTitles.add("section"+(debugListSectionTitles.size()+1));
					sectionTitle=line1;
					buf = new StringBuffer();
					continue;

				}
				else if(line1.startsWith("***") && line1.matches("^*\\*\\*\\*[Ee][Nn][Dd]*\\*\\*\\**$")){					
					if(allowedSection){
						lstTestData.add(StringUtils.deleteWhitespace(buf.toString()));					
						debugListSectionTitles.add(sectionTitle);
						allowedSection=true;
					}
					buf = new StringBuffer();
					continue;

				}
				buf.append(substituteDynaVals(line1, properties));
				buf_fileText_debug.append(substituteDynaVals(line1, properties));

			}
			lstTestData.add(StringUtils.deleteWhitespace(buf.toString()));
			debugListSectionTitles.add(sectionTitle);

		}
		catch(Exception e){
			log.error(e);
			message= "Error while handling Breaks or Injected dynamic keywords";
			return message;

		}
		
		String pageText_debug= pageText;

		//BLOCK 4: actual Processing 
		//Navigating through the fileTextLines and maatching it against page text	
		if(lstTestData.size()<1){
			lstTestData.add(StringUtils.deleteWhitespace(buf.toString()));
			debugListSectionTitles.add("defaultSection");
		}


		for(int i = 0; i<lstTestData.size();i++){
			String fileText = lstTestData.get(i);
			fileText = preProcessString(fileText);
			String errorLog = StringUtils.EMPTY;
			String line1 = debugListSectionTitles.get(i);			
			String sectionName= "";
			boolean pass= false;
			if(line1.contains(":")){
				String temp= line1.replaceAll("\\*", "");
				sectionName= temp.substring(line1.indexOf(":")-2);	
			}			
			
			if(sectionName.startsWith("regex")){
				pass = pageContains_regex(pageText, fileText);
			}else{
				pass = pageContains(pageText, fileText);
			}

			if(!pass){
				fileText = preProcessString(fileText);
				System.out.println('\n'+"ERROR IN SECTION "+debugListSectionTitles.get(i)+
						'\n'+"ACTUALS"+'\n'+pageText+'\n'+"NOT EQUAL"+'\n'+"EXPECTED"+'\n'+fileText);
				Patch patch = DiffUtils.diff(debug_pageContent_list, debug_fileContent_list);
				Utils_PageDiff diff = new Utils_PageDiff();
				//diff.compareTexts(fileText, pageText);
				diff.getDiff(fileText, pageText);
				diff.getDiff(pageText, fileText);
				//print('\n'+"DIFF IS: "+'\n'+patch.getDeltas()+'\n');
				errorLog=handle_errorLogging(pageText_debug, debugMapInfo);
				return "TEXT MATCH ERROR:  " +"IN SECTION "+debugListSectionTitles.get(i)+'\n'+errorLog.toString();


			}
		}

		return "";


	}

	
	public String util_preProcessFileLine(String line1){
		String line_mdf3=line1;
		//Comments
		if(line1.contains("###")){
			int end_idx = 0;
			int actual_idx=line1.lastIndexOf("###");
			if(actual_idx>1){
				end_idx=actual_idx;
			}
			 line_mdf3 = line1.substring(0,end_idx);
		}else{
			line_mdf3=line1.trim();
		}
		return line_mdf3.trim();
		
	}
	
	
	//This keyword allows the capture of the screen using Ctrl+c from IE , store it as a file and 
	// compare it against text representation of actual screen. Notice ***Break*** keyword helps to have breaks in the compared text
	public String  checkPageAgainstFile(String filePath, boolean ignoreFormData){

		//1. Initializations
		filePath = filePath_root_screens+"/"+filePath;
		BufferedReader reader = null;
		String pageText;	
		List<String> fileLines = new ArrayList<String>();
		Properties properties = new Properties();
		//BLOCK 1: REading
		try{
			try{
				//Populating debug map	
				reader = new BufferedReader(new FileReader(new File(filePath)));
				String line;

				while (( line = reader.readLine()) != null) {		
					fileLines.add(line);				
				}

				String propFilePath = filePath_root_screens+"/dynavalues.properties";

				InputStream in = new FileInputStream(new File(propFilePath));

				properties.load(in);

			}catch(FileNotFoundException e){
				log.error(e);
				throw new AssertionError("File Not found");
			}finally{
				reader.close();			 
			}
		}catch (IOException e) {
			log.error(e);
		}
		//BLOCK 3: Getting and preprocessing actuals
		pageText = printPageAsText();				
		String message = delegate_checkPageAgainstFile(pageText, fileLines,properties, ignoreFormData);
		if(message.equals(StringUtils.EMPTY)){
			return "Success";
		}else{
			throw new AssertionError(message);
		}

	}



	private boolean pageContains(String pageText, String fileText){
		pageText = pageText.replaceAll("\\*","").replaceAll("", "");//Ad hoc fix, to structure TODO
		if(!pageText.replaceAll("\\*","").toLowerCase().contains(fileText.replaceAll("\\*","").toLowerCase())){
			return false;
		}
		return true;
	}

	
	private boolean pageContains_regex(String pageText, String fileText){
		//fileText = fileText.replaceAll("(","").replaceAll(")","");
	try{
		if(!pageText.matches("^(.*)"+fileText+"(.*)$")){
			log.error(pageText + '\n'+" pagetext does not match filetext REGEX"+'\n'+ fileText);
			return false;
		}
	}catch(Exception e){		
		e.printStackTrace();
		return false;
	}
		return true;
	}


	private String handle_errorLogging(String fileText, Map<String, Integer> mapDebugInfo){

		StringBuffer errorLog = new StringBuffer();
		//Iterat
		Iterator<String> iter = mapDebugInfo.keySet().iterator();
		String line1 = StringUtils.EMPTY;
		String oneLine_noSpace = StringUtils.EMPTY;
		while(iter.hasNext()){
			line1= iter.next();
			oneLine_noSpace = StringUtils.deleteWhitespace(line1);
			if(line1.startsWith("***") && line1.matches("^*\\*\\*\\*[bB][rR][eE][aA][kK]\\*\\*\\**$")){
				continue;

			}
			if(line1.startsWith("***") && line1.matches("^(([\\*]{3})([Ss]ection\\:[\\w\\W0-9]{0,100})([\\*]{3}))$")){
				continue;

			}
			if(line1.startsWith("***") && line1.matches("^*\\*\\*\\*[Ee][Nn][Dd]*\\*\\*\\**$")){
				continue;
			}
			if(!fileText.contains(oneLine_noSpace)){
				Integer lineNr = mapDebugInfo.get(line1);
				errorLog.append("Error");
				errorLog.append(lineNr);
				errorLog.append(")");
				errorLog.append(line1);
				errorLog.append('\n');
				//printerrorLog.toString();
				//break;//TODO remove this break afterwards, currently memory is not enough
			}

		}

		return errorLog.toString();

	}
	private String substituteDynaVals(String x, Properties properties){
		//substituting dynaValues
		//TODO: Vulnerable code, relies on null value for flow control.
		if(properties==null){
			return x;
		}
		try{
			x.replace("*", StringUtils.EMPTY);//Getting rid of stars
			if(x.contains("$")){
				log.trace("Dynaval Substitution in  : "+x);
				StringBuffer xReformedBuf = new StringBuffer();
				String[] xArray = x.split("$");
				for(String splitted:xArray){
					String keyName = splitted.substring(splitted.indexOf("{")+1,splitted.indexOf("}"));
					String actualValue = properties.getProperty(keyName);
					splitted=splitted.replace("{", StringUtils.EMPTY);
					splitted=splitted.replace("}", StringUtils.EMPTY);
					splitted=splitted.replace("$", StringUtils.EMPTY);
					splitted= splitted.replace(keyName, actualValue);
					xReformedBuf.append(splitted);

				}

				x = xReformedBuf.toString();

			}}catch(Exception e){
				System.out.println("Error encountered in dynaval line "+x);
			}
		return x;
	}

	//Split by path delimiters
	public String checkPageAgainstRemoteFile( String expression) {		
		String pageText = printPageAsText();
		return checkPageAgainstRemoteFile_1(expression, pageText,null);
		
	}
	public String navigateCheckFileLayout( String pageName, String expression) {
		try {
			Page page = factory.getPage(pageName);
			String identifier = page.getIdentifier();
			String formKeyVals = page.getFormData((ServiceHub)getDriver());
			String pageText = util_getPageText(printPageAsText(),  identifier);
			return checkPageAgainstRemoteFile_1(expression, pageText, formKeyVals);
		} catch (SPOException e) {			
			log.error("Page object was not found hence proceding ");
			e.printStackTrace();
			String pageText = printPageAsText();
			return checkPageAgainstRemoteFile_1(expression, pageText,null);
		}
		
	
		
	}
	
	public String util_getPageText(String pageText, String identifier){
		String identifier_sansSpace = identifier.replaceAll("/s", StringUtils.EMPTY).trim();
		if(!identifier_sansSpace.isEmpty() && pageText.contains(identifier_sansSpace)){
			return pageText.split(identifier_sansSpace)[1];	
		}else{
			return pageText;
		}
		
	}
	
	//Split by path delimiters
		public String checkPageAgainstRemoteFile_1( String expression, String pageText, String formData) {
			List<String> expectedPageContent= new ArrayList<String>();
			
			if(!expression.contains("?")){
			expression= expression+"?meta=None";	
			}
			 
			boolean evaluateFormData = expression.contains("includeFormData");
			String formText="";
			if(evaluateFormData ){
				if(formData==null){
					formText = getPageFormData();
					pageText = pageText+'\n'+formText;	
				}else{
					formText=formData;
				}
				
			}
			
			try{
			expectedPageContent= util_checkWithServer(expression);
			}catch(Exception e){
				log.info(e);
				throw new AssertionError(e.getClass().getName()+" was thrown in page layout check");
			}
			String message = delegate_checkPageAgainstFile(pageText, expectedPageContent, null, !evaluateFormData);
			if(message.equals(StringUtils.EMPTY)){
				return "";
			}else{
				throw new AssertionError(message);
			}
		}
		
	@Deprecated	
	public String checkPageAgainstRemoteFile( String moduleName,String pageName) {
		//String message = getPageCompareMessage(moduleName, pageName,true);
		String message = checkPageAgainstRemoteFile(moduleName+"/"+pageName);
		if(message.equals(StringUtils.EMPTY)){
			return "Success";
		}else{
			throw new AssertionError(message);			
		}
	}
	
	private String getPageCompareMessage( String moduleName,String pageName, boolean ignoreFormData) {
		
		String pageText = printPageAsText();
		List<String> expectedPageContent= util_checkWithServer(moduleName, pageName);
		String message = delegate_checkPageAgainstFile(pageText, expectedPageContent, null, ignoreFormData);
			return message;			
	}
	public String checkPageCollated( String testName, String expression) {
		
		String message = checkPageAgainstRemoteFile(expression);
		
			if(message.equals(StringUtils.EMPTY)){
				return "";
			}else{
				collated_log.append("Failed: "+testName+":"+'\n'+message);
				return "Failed: "+testName+":"+'\n';			
			}
		
	}
	
	public String getCollatedLog(){
		return collated_log.toString();
	}


	public List<String> util_checkWithServer(String scenario, String pageName){
		//String result = "";
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			String queryUrl = scenario+"/"+pageName;
			return util_checkWithServer(queryUrl);
		} catch (Exception e) {
			log.info(e);
		}

		return resultList;	

	}
	
	public List<String> util_checkWithServer(String queryUrl) throws Exception{
		//String result = "";
		List<String> resultList = new ArrayList<String>();
					
			
			resultList =			kw.serviceFactory.getDomainSvc().getPage(queryUrl);
		
		return resultList;	

	}
	
	public static String preProcessString(String input){
		String noWhites = StringUtils.deleteWhitespace(input);
		for(String x:IGNORABLE_STRINGS ){
				noWhites = noWhites.replace(x,StringUtils.EMPTY);
		}
		noWhites = noWhites.replaceAll(IGNORABLE_HDR,StringUtils.EMPTY);
		return noWhites;
	}

	public void setPageFactory(PageFactory factory2) {
		this.factory=factory2;
		
	}

	public ServiceHub getKw() {
		return kw;
	}

	public void setKw(ServiceHub kw) {
		this.kw = kw;
	}
	
	
}

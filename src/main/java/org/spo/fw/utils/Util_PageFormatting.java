/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils;

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
import org.spo.fw.exception.SPOException;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.service.TestResourceServerConnector;
import org.spo.fw.web.Lib_KeyWordsCore;


import difflib.DiffUtils;
import difflib.Patch;


public class Util_PageFormatting extends Lib_KeyWordsCore{
private static final String IGNORABLE_HDR="Help|Support|SignOutSelectanHCO:GeneralHospitalGreatCareHospitalHCOID:2222HomeConcurrentCreateUploadAbstractApproveDataRe-abstractReportsSettingsResources";
private static final String[] IGNORABLE_STRINGS={"[\\n]"};
private static final String[] IGNORABLE_STRINGS_L2={",",".","/",":","//*"};
private List<String>  debug_pageContent_list = new ArrayList<String>();
private List<String>  debug_fileContent_list = new ArrayList<String>();
private StringBuffer collated_log=new StringBuffer();
private PageFactory factory;

	public Util_PageFormatting(WebDriver driver) {
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
			log.debug(e);
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


	public  static boolean pageContains(String pageText, String fileText){
		pageText = pageText.replaceAll("\\*","").replaceAll("", "");//Ad hoc fix, to structure TODO
		if(!pageText.replaceAll("\\*","").toLowerCase().contains(fileText.replaceAll("\\*","").toLowerCase())){
			return false;
		}
		return true;
	}

	
	public static boolean pageContains_regex(String pageText, String fileText){
		//fileText = fileText.replaceAll("(","").replaceAll(")","");
	try{
		if(!pageText.matches("^(.*)"+fileText+"(.*)$")){
			log.debug(pageText + '\n'+" pagetext does not match filetext REGEX"+'\n'+ fileText);
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
	public static  String substituteDynaVals(String x, Properties properties){
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

		
	
	
	public String getCollatedLog(){
		return collated_log.toString();
	}

	
	public static String preProcessString(String input){
		String noWhites = StringUtils.deleteWhitespace(input);
		for(String x:IGNORABLE_STRINGS ){
				noWhites = noWhites.replace(x,StringUtils.EMPTY);
		}
		noWhites = noWhites.replaceAll(IGNORABLE_HDR,StringUtils.EMPTY);
		return noWhites;
	}

	
	
}

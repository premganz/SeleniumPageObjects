package org.spo.fw.utils.pg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.log.Logger1;
import org.spo.fw.session.SessionContainer;
import org.spo.fw.session.SessionContainer.SCOPE;
import org.spo.fw.shared.DiffMessage;
import org.spo.fw.utils.Utils_PageDiff;
import org.spo.fw.utils.pg.model.FileContent;
import org.spo.fw.utils.pg.model.PageContent;
import org.spo.fw.utils.pg.model.Section;
import org.spo.fw.utils.pg.model.SectionWiseContentProcessor;
import org.spo.fw.utils.pg.util.IgnorableTextUtils;
import org.spo.fw.web.Lib_KeyWordsCore;
import org.spo.fw.web.ServiceHub;

/**
 * This is where  dsl semantics that describe the content of a page is interpreted and matched against 
 * plain text the caomes from real web pages.
 * You will see the meaning of experssions in this file and {@link SectionWiseContentProcessor} 
 * You may not be able to use colon and stars in the expected data because these have special meanings.
 *   * ***expr***, ***expr1***, ***break***, ***end***, ***section:regex***, Section:***, ### here
 *   
 *   Since release 1.4.6 while using the "regex" dsl expressions, you CANNOT have escaped characters in the expression.
 *   Since release 1.4.7
 *   regex expressions come in two flavors - expr and expr1
 *   you can use either of the tags to seperate sentences in a Section:regex_??? 
 *   If you use regex to seperate teh expressions, 
 *   then you will have to see to removing all sepical characters that have meaning in regex and provide a clean string, 
 *   because the page content will be stripped off all the regex speical characters while trying to match, 
 *   in case if you are using expr1 then you will have to make sure to escape all the special characters in your expression.  
 *   
 *   
 *   An example file might look like 
 *   
 *   
 *   
 *   
 *   Unresolved Authors 
***Section:headings***
The following authors do not have matchign books.(R)
Time Period: ${PeriodListBox.Quarter_style1}  
***end***     

<%model=AuthorActor%>
<%msg = AuthorActor.messages%>
<%qtr =  '[0-9]Q201[0-9] '%>

<% if(AuthorActor.hasRecords) print'
1 
    AUTHOR ID  Error Code 
' %>


***section:regex1*** 
<% if(msg['Arnold']!='') 		print ' Unresolved \: Arnold [0-9]Q201[0-9] 123456''***expr1***'	 %>
<% if(msg['Ramaswamy']!='') 	print ' Unresolved \: Arnold [0-9]Q201[0-9] 123456 ''***expr1***' %>

***end*** 

***section:regex1*** 
<% if(msg['Arnold']!='') 		print ' Unresolved  Arnold [0-9]Q201[0-9] 123456''***expr***'	 %>
<% if(msg['Ramaswamy']!='') 	print ' Unresolved  Arnold [0-9]Q201[0-9] 123456 ''***expr***' %>

***end*** 
CopyRight blah blah
 


 

 *   
 * @author prem
 *
 */

public class Lib_PageLayout_Processor extends Lib_KeyWordsCore implements ExtensibleService{
	private StringBuffer collated_log=new StringBuffer();
	//private PageFactory factory;
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	public static final String REGEX_FLAG_LINE ="regexFlag:";
	StringBuffer errorLog = new StringBuffer();
	private Lib_PageLayout_Content content_provider = new Lib_PageLayout_Content();
	private Lib_PageLayout_Validator validation_provider = new Lib_PageLayout_Validator();
	protected List<String> lstReplacements=new ArrayList<String>();

	@Override
	public void init() {		
		content_provider.init();
		IgnorableTextUtils.IGNORABLE_STRINGS_L2.add("\\*");
	}


	public Lib_PageLayout_Processor(WebDriver driver) {
		super(driver);

	}
	

	public DiffMessage core_getCompareLog(FileContent  content, PageContent pageContent){
		//1. Initializations
		String pageText = pageContent.content;
		StringBuffer buf = new StringBuffer();
		StringBuffer buf_fileText_debug = new StringBuffer();
		Map<String,Integer> debugMapInfo = new LinkedHashMap<String, Integer>();
		//List<String> debugListSectionTitles = new ArrayList<String>();


		//BLOCK 2: PreProecssing


		//BLOCK 4: actual Processing 
		//Navigating through the fileTextLines and maatching it against page text	


		for(int i = 0; i<content.sections.size();i++){
			Section section= content.sections.get(i);
			String sectionName= section.sectionTitle;
			boolean pass= false;
			if(sectionName.startsWith("ignore")){
				continue;
			}
			if(sectionName.startsWith("regex")){
				pass = rule_pageContains_regex(pageText, section.content,true);
			}else{
				pass = rule_pageContains(pageText, section.content);
			}

			if(!pass){
				errorLog = new StringBuffer();
				DiffMessage msg = new DiffMessage();
				Utils_PageDiff diff = new Utils_PageDiff();
				msg.setLogFull('\n'+"ERROR IN SECTION "+section.sectionTitle+
						'\n'+"ACTUALS"+'\n'+pageText+'\n'+"NOT EQUAL"+'\n'+"EXPECTED"+'\n'+section.content);
				msg.setPassed(false);
				errorLog.append(handle_errorLogging( pageContent,  content.debugMapInfo));
				msg.setDiff(diff.getDiff(section.content, pageText));				
				msg.setDiffInverse(diff.getDiff(pageText, section.content));
				msg.setErrorLog("TEXT MATCH ERROR:  " +"IN SECTION "+section.sectionTitle+'\n'+errorLog.toString());
				msg.setErrorSection(section.sectionTitle);
				//diff.compareTexts(fileText, pageText);
				//print('\n'+"DIFF IS: "+'\n'+patch.getDeltas()+'\n');
				System.out.println(msg.getLogFull()+'\n'+msg.getErrorLog());

				return msg;


			}
		}

		return new DiffMessage();


	}

	private String handle_errorLogging(PageContent content, Map<String, Integer> mapDebugInfo){
		String pageText = content.toString();
		StringBuffer errorLog = new StringBuffer();
		//Iterat
		Iterator<String> iter = mapDebugInfo.keySet().iterator();
		String line1 = StringUtils.EMPTY;
		String oneLine_noSpace = StringUtils.EMPTY;

		while(iter.hasNext()){
			line1= iter.next();
			oneLine_noSpace = StringUtils.deleteWhitespace(line1);
			boolean failed=false;
			if(oneLine_noSpace.startsWith("regexFlag:")){
				oneLine_noSpace=oneLine_noSpace.replaceAll("regexFlag:","");
				failed=!rule_pageContains_regex(pageText, oneLine_noSpace,false)	;
			}else{
				failed=!rule_pageContains(pageText, oneLine_noSpace)	;
			}


			if(failed){
				Integer lineNr = mapDebugInfo.get(line1);
				errorLog.append("Error");
				errorLog.append(lineNr);
				errorLog.append(")");
				errorLog.append(line1.replaceAll("regexFlag:",""));
				errorLog.append('\n');
				//printerrorLog.toString();
				//break;//TODO remove this break afterwards, currently memory is not enough
			}

		}

		return errorLog.toString();

	}

	public  boolean rule_pageContains(String pageText, String fileText){
		if(!doReplacements(pageText).toLowerCase().contains(doReplacements(fileText).toLowerCase())){
			return false;
		}
		return true;
	}

	
	
	public void addIgnorableRules(String ignorable){
		lstReplacements.add(ignorable);
	}
	
	private String doReplacements(String input){
		for(String replaceable : IgnorableTextUtils.IGNORABLE_STRINGS_L2){
			input =input.replaceAll(replaceable,"");
		}
		
		return input;
	}

	public boolean rule_pageContains_regex(String pageText, String fileText, boolean toLog){
		//fileText = fileText.replaceAll("(","").replaceAll(")","");
		boolean result=true;
		
		if(fileText.contains("***expr***")){
			try{
				pageText=(IgnorableTextUtils.cleanRegexChars(pageText));
				//This is order sensitive evaluation of expressions
				String[] fileTextExprs = fileText.split("\\*\\*\\*expr\\*\\*\\*");
				if(fileTextExprs.length<=1){}
				for(String expr: fileTextExprs){
					Pattern  pattern = Pattern.compile(expr);
					Matcher matcher = pattern.matcher(pageText);
					if (matcher.find()) {					
						String found = matcher.group();
						pageText=pageText.replace(found, "***temp***");
					}else{
						if(toLog)errorLog.append("Error in regex evaluation for "+expr+'\n');
						log.trace("Error in regex evaluation for "+expr+'\n');
						result= false;
						pageText="***temp***"+pageText;
					}
					pageText=(pageText.split("\\*\\*\\*temp\\*\\*\\*")[1]);	


				}



			}catch(Exception e){		
				e.printStackTrace();
				result= false;
			}
		}else if(fileText.contains("***expr1***")){
			try{
				//This is order sensitive evaluation of expressions
				String[] fileTextExprs = fileText.split("\\*\\*\\*expr1\\*\\*\\*");
				if(fileTextExprs.length<=1){}
				for(String expr: fileTextExprs){
					Pattern  pattern = Pattern.compile(expr);
					Matcher matcher = pattern.matcher(pageText);
					if (matcher.find()) {					
						String found = matcher.group();
						pageText=pageText.replace(found, "***temp***");
					}else{
						log.error("Error in regex evaluation for "+expr+'\n');
						result= false;
						pageText="***temp***"+pageText;
					}
					pageText=(pageText.split("\\*\\*\\*temp\\*\\*\\*")[1]);	


				}



			}catch(Exception e){		
				e.printStackTrace();
				result= false;
			}
		}else{
			Pattern  pattern = Pattern.compile(fileText);
			Matcher matcher = pattern.matcher(pageText);
			if (matcher.find()) {					
				result= true;
			}else{
				//if(!pageText.matches("(.*?)"+fileText+"(.*?)")){
				log.error("PageText:"+'\n'+pageText + '\n'+" pagetext does not match filetext REGEX"+'\n'+ fileText);
				result= false;
			}	}
		
		return result;
	}


	//TODO : no longer works as expected
	@Deprecated
	//This keyword allows the capture of the screen using Ctrl+c from IE , store it as a file and 
	// compare it against text representation of actual screen. Notice ***Break*** keyword helps to have breaks in the compared text
	public String  entry_checkPageAgainstFile(String filePath, boolean ignoreFormData, ServiceHub kw){
		String filePath_root_screens = System.getProperty("textScreens.path");
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
		//pageText = content_provider.entry_getPageText("",  kw );				
		//String message = core_getCompareLog(content, pageContent);
		String message="";
		if(message.equals(StringUtils.EMPTY)){
			return "Success";
		}else{
			throw new AssertionError(message);
		}

	}


	public DiffMessage entry_navigateCheckPageLayout( String pageName, String expression, ServiceHub kw) {
		if(pageName.isEmpty()){
			String[] exprArr = expression.split("/");
			int len = exprArr.length;
			if(len>0)pageName=exprArr[len-1];
		}
		if(!validation_provider.validatePage(pageName, kw)){
			DiffMessage message = new DiffMessage();
			message.setPassed(false);
			message.setErrorLog("VALIDATION FAILED");
			log.error("Validation Failed for a registered Validator in the PAGEfactory for  "+pageName);
			return message;

		}
		FileContent fileContent = content_provider.entry_getFileContent(expression,kw);
		PageContent pageContent = content_provider.entry_getPageContent(pageName, kw);
		logToFile(fileContent, pageContent);
		DiffMessage message = core_getCompareLog(fileContent, pageContent);
		//SessionContainer.store(SCOPE.REQ, "diff",message.getDiff());
		return message;

	}

	public void logToFile(FileContent fileContent, PageContent pageContent){
		StringBuffer buf = new StringBuffer();
		for(String val : fileContent.debugMapInfo.keySet()){
			buf.append(val+'\n');
		}
		String pageContentToPrint = pageContent.contentFormatted.replaceAll(" ", "").replaceAll(":", "");
		log.logToFile("expected.txt", buf.toString());
		log.trace(buf.toString());
		log.trace("does not match actuals");
		log.trace(pageContentToPrint);
		log.logToFile("actual.txt",pageContentToPrint );
	}

	@Deprecated //1.4.3
	public DiffMessage entry_navigateCheckPageLayout( String expression, ServiceHub kw) {
		FileContent fileContent = content_provider.entry_getFileContent(expression,kw);
		PageContent pageContent = content_provider.entry_getPageContent("", kw);
		DiffMessage message = core_getCompareLog(fileContent, pageContent);

		return message;

	}




	public DiffMessage entry_checkPageCollated( String testName, String expression, ServiceHub kw) {

		DiffMessage message = entry_navigateCheckPageLayout(expression,kw);

		if(message.equals(StringUtils.EMPTY)){
			return null;
		}else{
			collated_log.append("#######################################################################"
					+'\n'+"Failed: "+testName+":"+'\n'+message);
			return null;			
		}

	}

	public String getCollatedLog(){
		return collated_log.toString();
	}


	public Lib_PageLayout_Content getContent_provider() {
		return content_provider;
	}
	public void setContent_provider(Lib_PageLayout_Content content_provider) {
		this.content_provider = content_provider;
	}






}

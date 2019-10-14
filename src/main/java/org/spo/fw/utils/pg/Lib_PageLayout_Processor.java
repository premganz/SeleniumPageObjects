/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
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
import org.spo.fw.config.AppConstants;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.log.Logger1;
import org.spo.fw.shared.DiffMessage;
import org.spo.fw.utils.Utils_PageDiff;
import org.spo.fw.utils.pg.model.FileContent;
import org.spo.fw.utils.pg.model.PageContent;
import org.spo.fw.utils.pg.model.Section;
import org.spo.fw.utils.pg.model.SectionWiseContentProcessor;
import org.spo.fw.utils.pg.util.IgnorableTextUtils;
import org.spo.fw.web.KeyWords_Utils;
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
	private Lib_PageLayout_Content content_provider;
	private Lib_PageLayout_Validator validation_provider ;
	protected List<String> lstReplacements=new ArrayList<String>();

	@Override
	public void init() {	
		if(content_provider==null) {
			content_provider=new Lib_PageLayout_Content();
		}
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

		DiffMessage msg = new DiffMessage();
		for(int i = 0; i<content.sections.size();i++){
			//This type of section, needs escaping all regex chars, having it false means that regex chars should be removed instead of escaped.
			boolean isRegex2=false;
			Section section= content.sections.get(i);
			String sectionName= section.sectionTitle;
			boolean pass= false;
			if(sectionName.startsWith("ignore")){
				continue;
			}
			else if(sectionName.startsWith("regex")){
				msg = rule_pageContains_regex(pageText, section.content,true, msg);
			}else{
				msg.setFailed(!rule_pageContains(pageText, section.content));
			}

			if(msg.isFailed()){
				errorLog = new StringBuffer();

				Utils_PageDiff diff = new Utils_PageDiff();
				msg.setLogFull('\n'+"ERROR IN SECTION "+section.sectionTitle+
						'\n'+"ACTUALS"+'\n'+pageText+'\n'+"NOT EQUAL"+'\n'+"EXPECTED"+'\n'+section.content);
				msg.setPassed(false);
				String pageTextTrimmed="";
				//Additional Debugging for long sections

				if(section.content.length()> AppConstants.LONG_SECTION_THRESHOLD) {
					log.debug("Attempting to extract better debugging information for this section");
					int roundedVal = AppConstants.LONG_SECTION_THRESHOLD+(10-AppConstants.LONG_SECTION_THRESHOLD%10);
					int testFragment=roundedVal/10;
					String sectionStart=section.content.substring(0, testFragment);
					String sectionEnd=section.content.substring(section.content.length()-testFragment,section.content.length());
					boolean isFeasibile=pageText.contains(sectionStart)&&pageText.contains(sectionEnd);
					if(isFeasibile) {
						pageTextTrimmed=pageText.substring(pageText.indexOf(sectionStart),pageText.indexOf(sectionEnd));
						double distance = KeyWords_Utils.LevenshteinDistance.similarity(pageTextTrimmed+sectionEnd,section.content);
						log.trace("::::EQUIVALENCE FACTOR = "+distance);
						String betterCompared = '\n'+"BETTER COMPARED SECTIONWISE "+section.sectionTitle+
								'\n'+"ACTUALS"+'\n'+pageTextTrimmed+sectionEnd+" [NOT EQUAL "+"EXPECTED]"+'\n'+section.content+'\n'+"::::DISTANCE "+distance*100; 
						msg.setLogFull(msg.getLogFull()+'\n'+betterCompared);
					}else {
						log.debug("Unable to extract better debugging information for this section");
					}


				}
				errorLog.append(handle_errorLogging( pageContent,  content.debugMapInfo, msg, isRegex2));
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

		return msg;


	}

	private String handle_errorLogging(PageContent content, Map<String, Integer> mapDebugInfo, DiffMessage msg, boolean isRegex2){
		String pageText = content.toString();
		StringBuffer errorLog = new StringBuffer();
		//Iterat
		Iterator<String> iter = mapDebugInfo.keySet().iterator();
		String line1 = StringUtils.EMPTY;
		String oneLine_noSpace = StringUtils.EMPTY;

		boolean failed_atleastOnce=msg.isFailed();
		while(iter.hasNext()){
			boolean failed=false;
			line1= iter.next();
			oneLine_noSpace = StringUtils.deleteWhitespace(line1);

			if(oneLine_noSpace.startsWith("regexFlag:")){
				oneLine_noSpace=oneLine_noSpace.replaceAll("regexFlag:","");
				msg=rule_pageContains_regex(pageText, oneLine_noSpace,false,msg)	;
				failed=msg.isFailed();
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

			if(!failed_atleastOnce && failed) {
				failed_atleastOnce=failed;
			}
		}
		if(msg.isPassed()) {
			msg.setFailed(failed_atleastOnce);
			msg.setPassed(!failed_atleastOnce);
		}

		return errorLog.toString();

	}

	public  boolean rule_pageContains(String pageText, String fileText){
		String pageTextMdf = doReplacements(pageText).toLowerCase();
		String fileTextMdf = doReplacements(fileText).toLowerCase();
		if(pageTextMdf.contains(fileTextMdf)) {
			return true;
		}else {
			log.trace("Match Rule Failed "+'\n'+"ACTUALS "+Utils_PageDiff.longTextConsoleLogHelper(pageTextMdf)+'\n'+"does not CONTAIN "+'\n'+fileTextMdf.trim());
			return false;
		}
		
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
	/*This works on two principles, that is to say, it uses a splitter called ***expr*** wherein the expected 
	 * file text gets cleaned of regex charactres, it also serves the same purpose as ***break*** within regex blocks
	 * Secondly the splitter ***expr1*** takes the file text as such without cleanup. Without the expr splitte rthe default working is 
	 * as if with expr1.
	 */

	public DiffMessage rule_pageContains_regex(String pageText, String fileText, boolean toLog, DiffMessage msg){
		//fileText = fileText.replaceAll("(","").replaceAll(")","");
		boolean result=true;

		if(fileText.contains("***expr1***")){
			try{

				//log.debug("markdown is ***expr1*** where you have to escape regexp spl char in string , opt for expr2 in case you prefer to delete them");

				//This is order sensitive evaluation of expressions
				String[] fileTextExprs = fileText.split("\\*\\*\\*expr1\\*\\*\\*");
				if(fileTextExprs.length<=1){}
				for(String expr: fileTextExprs){
					if(expr.contains("***expr***")) {
						throw new SPOException("Nesting ***expr*** within ***expr1*** not legal in the same section");
					}
					Pattern  pattern = Pattern.compile(expr);
					Matcher matcher = pattern.matcher(pageText);
					if (matcher.find()) {					
						String found = matcher.group();
						pageText=pageText.replace(found, "***temp***");
					}else{
						//if(toLog)
						StringBuffer toDump=new StringBuffer();
						//errorLog.append("Error in regex evaluation for "+expr+'\n');

						int len = pageText.length();
						int segSize=4000;
						if(len>segSize) {

							int seg = len/segSize;
							int segReminder=len%segSize;
							String temppageText="";

							for(int i=1;i<seg+1;i++) {
								temppageText=pageText.substring(segSize*(i-1), segSize*i);
								toDump.append(temppageText+'\n');
								//log.debug(temppageText);
							}
							temppageText=pageText.substring(segSize*(seg), segSize*(seg)+segReminder);
							toDump.append(temppageText+'\n');
							//log.debug(temppageText);
							//log.info(toDump.toString());
						}else {
							toDump.append(pageText);
						}
						//						System.out.println(pageText);
						//						System.out.println(StringUtils.isAsciiPrintable(pageText));
						//						System.out.println("Length of pagetext == "+pageText.length());
						String logMsg="Error in regex evaluation for "+expr;
						log.info(logMsg+'\n'+"for the "+'\n'+toDump.toString());
						msg.getErrorSummary().append(logMsg);
						result=false;	
						pageText="***temp***"+pageText;

					}	if(pageText.endsWith("***temp***")) {
						pageText="";
					}else {
						pageText=(pageText.split("\\*\\*\\*temp\\*\\*\\*")[1]);	

					}
				}



			}catch(Exception e){		
				e.printStackTrace();
				result= false;
				throw e;
			}
		}else if(fileText.contains("***expr***")){
			try{
				//pageText=(IgnorableTextUtils.cleanRegexChars(pageText));
				pageText=(IgnorableTextUtils.cleanRegexChars(pageText));
				//log.debug("markdown is ***expr*** and you need to delete (not escape) regex spl chars");
				//This is order sensitive evaluation of expressions
				String[] fileTextExprs = fileText.split("\\*\\*\\*expr\\*\\*\\*");
				if(fileTextExprs.length<=1){}
				for(String expr: fileTextExprs){
					if(expr.contains("***expr1***")) {
						throw new SPOException("Nesting ***expr1*** within ***expr*** not legal in the same section");
					}
					Pattern  pattern = Pattern.compile(expr);
					Matcher matcher = pattern.matcher(pageText);
					if (matcher.find()) {					
						String found = matcher.group();
						pageText=pageText.replace(found, "***temp***");
					}else{
						//if(toLog)
						StringBuffer toDump=new StringBuffer();
						//errorLog.append("Error in regex evaluation for "+expr+'\n');

						int len = pageText.length();
						int segSize=10000;
						if(len>segSize) {

							int seg = len/segSize;
							int segReminder=len%segSize;
							String temppageText="";

							for(int i=1;i<seg+1;i++) {
								temppageText=pageText.substring(segSize*(i-1), segSize*i);
								toDump.append(temppageText+'\n');
								//log.debug(temppageText);
							}
							temppageText=pageText.substring(segSize*(seg), segSize*(seg)+segReminder);
							toDump.append(temppageText+'\n');
							//log.debug(temppageText);
							//log.info(toDump.toString());
						}else {
							toDump.append(pageText);
						}
						//							System.out.println(pageText);
						//							System.out.println(StringUtils.isAsciiPrintable(pageText));
						//							System.out.println("Length of pagetext == "+pageText.length());
						String logMsg="Error in regex evaluation for "+expr;
						log.info(logMsg+'\n'+"for the "+'\n'+toDump.toString());
						msg.getErrorSummary().append(logMsg);
						result=false;	
						//to provide for trailing expr markdowns 
						pageText="***temp***"+pageText;

					}
//					log.info("Attempting to split pageText "+pageText);
					if(pageText.endsWith("***temp***")) {
						pageText="";
					}else {
						pageText=(pageText.split("\\*\\*\\*temp\\*\\*\\*")[1]);	
					}



				}



			}catch(Exception e){		
				e.printStackTrace();
				result= false;
				throw e;
			}

		}else{
			Pattern  pattern = Pattern.compile(fileText);
			Matcher matcher = pattern.matcher(pageText);
			if (matcher.find()) {					
				result= true;
			}else{
				//if(!pageText.matches("(.*?)"+fileText+"(.*?)")){
				//log.error("PageText:"+'\n'+pageText + '\n'+" pagetext does not match filetext REGEX"+'\n'+ fileText);
				result= false;
			}	}
		msg.setFailed(!result);
		return msg;
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


	public void setValidation_provider(Lib_PageLayout_Validator lib_PageLayout_Validator) {
		this.validation_provider=lib_PageLayout_Validator;

	}






}

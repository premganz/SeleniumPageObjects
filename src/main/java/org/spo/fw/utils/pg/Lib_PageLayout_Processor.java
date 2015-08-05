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

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.shared.DiffMessage;
import org.spo.fw.utils.Utils_PageDiff;
import org.spo.fw.utils.pg.Lib_PageLayout_Content.FileContent;
import org.spo.fw.utils.pg.Lib_PageLayout_Content.PageContent;
import org.spo.fw.utils.pg.Lib_PageLayout_Content.Section;
import org.spo.fw.web.KeyWords;
import org.spo.fw.web.Lib_KeyWordsCore;



public class Lib_PageLayout_Processor extends Lib_KeyWordsCore {
	private StringBuffer collated_log=new StringBuffer();
	private PageFactory factory;
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	public static final String REGEX_FLAG_LINE ="regexFlag:";
	
	private Lib_PageLayout_Content content_provider = new Lib_PageLayout_Content(factory);


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
			String errorLog = StringUtils.EMPTY;
			String sectionName= section.sectionTitle;
			boolean pass= false;

			if(sectionName.startsWith("regex")){
				pass = rule_pageContains_regex(pageText, section.content);
			}else{
				pass = rule_pageContains(pageText, section.content);
			}

			if(!pass){
				DiffMessage msg = new DiffMessage();
				Utils_PageDiff diff = new Utils_PageDiff();
				msg.setLogFull('\n'+"ERROR IN SECTION "+section.sectionTitle+
						'\n'+"ACTUALS"+'\n'+pageText+'\n'+"NOT EQUAL"+'\n'+"EXPECTED"+'\n'+section.content);
				msg.setPassed(false);
				errorLog=handle_errorLogging( pageContent,  content.debugMapInfo);
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
				failed=!rule_pageContains_regex(pageText, oneLine_noSpace)	;
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
		pageText = pageText.replaceAll("\\*","").replaceAll("", "");//Ad hoc fix, to structure TODO
		if(!pageText.replaceAll("\\*","").toLowerCase().contains(fileText.replaceAll("\\*","").toLowerCase())){
			return false;
		}
		return true;
	}


	public boolean rule_pageContains_regex(String pageText, String fileText){
		//fileText = fileText.replaceAll("(","").replaceAll(")","");
		try{
			if(!pageText.matches("^((.*)"+fileText+"(.*))$")){
				log.error(pageText + '\n'+" pagetext does not match filetext REGEX"+'\n'+ fileText);
				return false;
			}
		}catch(Exception e){		
			e.printStackTrace();
			return false;
		}
		return true;
	}


	//TODO : no longer works as expected
	@Deprecated
	//This keyword allows the capture of the screen using Ctrl+c from IE , store it as a file and 
	// compare it against text representation of actual screen. Notice ***Break*** keyword helps to have breaks in the compared text
	public String  entry_checkPageAgainstFile(String filePath, boolean ignoreFormData, KeyWords kw){
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


	public DiffMessage entry_navigateCheckPageLayout( String pageName, String expression, KeyWords kw) {
		FileContent fileContent = content_provider.entry_getFileContent(expression);
		PageContent pageContent = content_provider.entry_getPageContent(pageName, kw);
		DiffMessage message = core_getCompareLog(fileContent, pageContent);

		return message;

	}
	public DiffMessage entry_navigateCheckPageLayout( String expression, KeyWords kw) {
		FileContent fileContent = content_provider.entry_getFileContent(expression);
		PageContent pageContent = content_provider.entry_getPageContent("", kw);
		DiffMessage message = core_getCompareLog(fileContent, pageContent);

		return message;

	}




	public DiffMessage entry_checkPageCollated( String testName, String expression, KeyWords kw) {

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

	public void setPageFactory(PageFactory factory2) {
		this.factory=factory2;
		content_provider = new Lib_PageLayout_Content(factory);

	}
	public Lib_PageLayout_Content getContent_provider() {
		return content_provider;
	}
	public void setContent_provider(Lib_PageLayout_Content content_provider) {
		this.content_provider = content_provider;
	}
	public PageFactory getPageFactory() {
		return this.factory;
	
	}
	


	

}
package org.spo.fw.utils.pg;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.service.TestResourceServerConnector;
import org.spo.fw.web.KeyWords;



public class Lib_PageLayout_Content {
	private static final String IGNORABLE_HDR="Help|Support|SignOutSelectanHCO:GeneralHospitalGreatCareHospitalHCOID:2222HomeConcurrentCreateUploadAbstractApproveDataRe-abstractReportsSettingsResources";
	private static final String[] IGNORABLE_STRINGS_L1={"[\\n]","[\\s]"};//regex cases
	private static final String[] IGNORABLE_STRINGS_L2={"[\\n]",":","//*","[\\s]"};//general
	private PageFactory factory;
	Logger1 log = new Logger1(this.getClass().getSimpleName());


	public Lib_PageLayout_Content(PageFactory factory) {
		this.factory=factory;
	}

	public FileContent core_getFileContent	(List<String> fileLines, Properties properties){
		//1. Initializations
		FileContent pageContent = new FileContent();

		StringBuffer buf = new StringBuffer();
		StringBuffer buf_fileText_debug = new StringBuffer();
		Map<String,Integer> debugMapInfo = new LinkedHashMap<String, Integer>();
		//List<String> debugListSectionTitles = new ArrayList<String>();
		int lineNumber = 1;


		//BLOCK 2: PreProecssing
		List<String> fileLines1 = new ArrayList<String>();
		boolean isRegexFlag=false;
		for(String line1: fileLines) {		
			line1=util_preProcessFileLine(line1);
			if(!isRegexFlag){
				isRegexFlag= line1.startsWith("***") && (line1.contains("regex")||line1.contains("Regex"));
			}else{
				isRegexFlag= !(line1.startsWith("***") && (line1.contains("end")||line1.contains("End")));
			}
			debugMapInfo.put(util_cleanupForDebugMap(line1,isRegexFlag),lineNumber);
			fileLines1.add(line1);
			lineNumber++;
		}

		pageContent.debugMapInfo = debugMapInfo;


		Section section = new Section();
		int sectionCounter=0;
		for(String line1:fileLines1) {
			if(line1.startsWith("***") && line1.matches("^(([\\*]{3})([Bb]reak)([\\*]{3}))$")){					
				section.content=(util_processContent(buf.toString(),IGNORABLE_STRINGS_L2));
				section.sectionTitle="section"+sectionCounter;
				pageContent.sections.add(section);
				sectionCounter++;				
				section=new Section();
				buf = new StringBuffer();

				continue;

			}
			else if(line1.startsWith("***") &&  line1.matches("^(([\\*]{3})([Ss]ection\\:[\\w\\W0-9]{0,100})([\\*]{3}))$")){
				if(!buf.toString().isEmpty()){				
					section.content=(util_processContent(buf.toString(),IGNORABLE_STRINGS_L2));	
					if(section.sectionTitle.isEmpty())section.sectionTitle="section"+sectionCounter;
					sectionCounter++;
					pageContent.sections.add(section);
					buf = new StringBuffer();
				}
				section=new Section();				
				String temp= line1.replaceAll("\\*", "");
				String sectionName= temp.substring(line1.indexOf(":")-2);					
				if(sectionName.isEmpty()){
					section.sectionTitle=("section"+(sectionCounter));	
				}else{
					section.sectionTitle=sectionName;	
				}
				//log.trace("section heading is "+sectionName);			

				continue;

			}
			else if(line1.startsWith("***") && line1.matches("^*\\*\\*\\*[Ee][Nn][Dd]*\\*\\*\\**$")){
				if(section.sectionTitle.contains("regex")||section.sectionTitle.contains("Regex")){
					section.content=(util_processContent(buf.toString(),IGNORABLE_STRINGS_L1));	
				}else{
					section.content=(util_processContent(buf.toString(),IGNORABLE_STRINGS_L2));	
				}
				sectionCounter++;
				pageContent.sections.add(section);
				buf = new StringBuffer();
				section=new Section();
				continue;

			}			
			buf.append(line1);
			buf_fileText_debug.append(line1);

		}
		section.content=(util_processContent(buf.toString(),IGNORABLE_STRINGS_L2));		
		pageContent.sections.add(section);

		//BLOCK 4: actual Processing 
		//Navigating through the fileTextLines and maatching it against page text	
		if(sectionCounter==0){
			section.content=util_processContent(buf.toString(),IGNORABLE_STRINGS_L2);
			section.sectionTitle="defaultSection";
			pageContent.sections.add(section);
		}
		pageContent.pageTextDebug=buf_fileText_debug.toString();

		return pageContent;


	}

	public static String util_processContent(String input, String[] ignorables){
		String noWhites = StringUtils.deleteWhitespace(input);
		for(String x:ignorables ){
			noWhites = noWhites.replace(x,StringUtils.EMPTY);
		}
		noWhites = noWhites.replaceAll(IGNORABLE_HDR,StringUtils.EMPTY);
		return StringUtils.deleteWhitespace(noWhites);
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


	private String util_cleanupForDebugMap(String input, boolean isRegex){
		if(input.trim().startsWith("*")){
			return "";
		}else if(isRegex){
			return "regexFlag:"+input;
		}else{
			return util_processContent(input, IGNORABLE_STRINGS_L2);
		}
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
	public FileContent entry_getFileContent( String expression ) {		
		return util_getFileFromServer(expression);

	}

	public PageContent entry_getPageContent( String pageName,  KeyWords kw) {
		String content;
		PageContent pageContent = new PageContent();
		try {
			Page page = factory.getPage(pageName);
			String identifier = page.getIdentifier();
			String formKeyVals = page.getFormData(kw);
			String fromPage = kw.doPrintPageAsText();
			pageContent.contentDebug=fromPage;
			String pageText = util_getPageText(fromPage,  identifier);
			content = pageText+formKeyVals;

		} catch (SPOException e) {			
			log.debug("Page object was not found hence proceding ");
			e.printStackTrace();
			content = kw.doPrintPageAsText();
		}
		pageContent.content =util_processContent(content,IGNORABLE_STRINGS_L2) ;
		return pageContent;


	}

	public String util_getPageText(String pageText, String identifier){
		String identifier_sansSpace = identifier.trim();
		if(!identifier_sansSpace.isEmpty() && pageText.contains(identifier_sansSpace)){
			String[] splits= pageText.split(identifier_sansSpace);
			splits[0]="";
			StringBuffer buf = new StringBuffer();
			for(String x :splits){
				buf.append(x);
			}
			return identifier_sansSpace+buf.toString();
		}else{
			return pageText;
		}

	}

	//Split by path delimiters
	public FileContent util_getFileFromServer( String expression) {
		List<String> expectedPageContent= new ArrayList<String>();

		if(!expression.contains("?")){
			expression= expression+"?meta=None";	
		}

		try{
			expectedPageContent= util_checkWithServer(expression);
		}catch(Exception e){
			log.info(e);
			throw new AssertionError(e.getClass().getName()+" was thrown");
		}
		FileContent pageContent= core_getFileContent(expectedPageContent, null);
		return pageContent;

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
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			log.debug("calling server on url "+queryUrl);
			TestResourceServerConnector<ArrayList<String>> con = new TestResourceServerConnector<ArrayList<String>>(resultList);
			resultList = con.queryServer("pages", queryUrl);
		} catch (Exception e) {
			log.info(e);
			//SessionContainer.storeLogToSession("WARN : Server Connection for url failed for : "+pageName);
			//log.debug("Recovering silently");
			throw e;
		}
		if(resultList.size()==1 && resultList.get(0).equals("ERROR")){
			throw new SPOException("ERROR received for "+queryUrl);
		}
		return resultList;	

	}


	public void setPageFactory(PageFactory factory2) {
		this.factory=factory2;

	}

	public class FileContent{
		public String pageTextDebug;
		public List<Section> sections=new ArrayList<Section>();
		public Map<String,Integer> debugMapInfo = new LinkedHashMap<String, Integer>();
		public List<String> debugListSectionTitles = new ArrayList<String>();		
		public String toString() {	
			StringBuffer buf =new StringBuffer();
			for(Section section:sections){
				buf.append(section.toString()+'\n');
			}
			return buf.toString();
		}
	}
	public class Section{
		public String sectionTitle="";
		public String type;
		public String content="";

		public String toString() {	
			return "Title:"+sectionTitle+"/////"+"content:"+content;
		}
	}
	public class PageContent{
		public String content;
		public String contentDebug;
		public String toString() {	
			return "PageContent:"+"/////"+"content:"+content;
		}
	}
}




package org.spo.fw.utils.pg.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.itf.StaticContentProcessor;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.utils.pg.util.IgnorableTextUtils;
import org.spo.fw.web.ServiceHub;

public class SectionWiseContentProcessor implements StaticContentProcessor {
/**
 * author prem
 * relies on syntax of ***Section:*** and ***end***
 * Section name ie  the text beyond the colon in teh SEction keyword has two reserved words one is 
 * regex and another lite. these have spl meanings see code below.
 */
	
	private StaticContentProvider staticContentProvider;
	
	Logger1 log = new Logger1("SectionWiseContentProcessor");
	@Override
	public FileContent getFileContent(String expression, ServiceHub kw) {
		List<String> lstContent = staticContentProvider.getContent(expression,kw);
		return core_processFileContent(lstContent, null);
		
	}
	
	@Override
	public void setStaticContentProvider(StaticContentProvider provider) {
		this.staticContentProvider=provider;
	}
	
	
	public FileContent core_processFileContent	(List<String> fileLines, Properties properties){
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
				section.content=(IgnorableTextUtils.util_processContent(buf.toString(),IgnorableTextUtils.IGNORABLE_STRINGS_L2));
				section.sectionTitle="section"+sectionCounter;
				pageContent.sections.add(section);
				sectionCounter++;				
				section=new Section();
				buf = new StringBuffer();

				continue;

			}
			else if(line1.startsWith("***") &&  line1.matches("^(([\\*]{3})([Ss]ection\\:[\\w\\W0-9]{0,100})([\\*]{3}))$")){
				if(!buf.toString().isEmpty()){				
					section.content=(IgnorableTextUtils.util_processContent(buf.toString(),IgnorableTextUtils.IGNORABLE_STRINGS_L2));	
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
					section.content=(IgnorableTextUtils.util_processContent(buf.toString(),IgnorableTextUtils.IGNORABLE_STRINGS_L1));	
				}else{
					section.content=(IgnorableTextUtils.util_processContent(buf.toString(),IgnorableTextUtils.IGNORABLE_STRINGS_L2));	
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
		section.content=(IgnorableTextUtils.util_processContent(buf.toString(),IgnorableTextUtils.IGNORABLE_STRINGS_L2));		
		pageContent.sections.add(section);

		//BLOCK 4: actual Processing 
		//Navigating through the fileTextLines and maatching it against page text	
		if(sectionCounter==0){
			section.content=IgnorableTextUtils.util_processContent(buf.toString(),IgnorableTextUtils.IGNORABLE_STRINGS_L2);
			section.sectionTitle="defaultSection";
			pageContent.sections.add(section);
		}
		pageContent.pageTextDebug=buf_fileText_debug.toString();
		//1.4.4 lite mode added
		for(int i =0;i<pageContent.sections.size();i++){
			Section sec = pageContent.sections.get(i);
			if(sec.sectionTitle.contains("lite") && SessionContext.appConfig.liteMode){
				log.info("WARN WARN WARN : LITE MODE ON, REMOVING  FROM EXPECTED SECTION ->"+sec.sectionTitle);
			pageContent.sections.remove(sec);	
			}
		}
		
		return pageContent;


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
			return IgnorableTextUtils.util_processContent(input, IgnorableTextUtils.IGNORABLE_STRINGS_L2);
		}
	}

	
}

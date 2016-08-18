package org.spo.fw.specific.scripts.templates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.utils.URIBuilder;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.external.ExternalScriptSvc;
import org.spo.fw.utils.Utils_PageDiff;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.web.ServiceHub;

/**
 * 
 * @author Prem
 * The test domain is Upload 
 * This is a Non destructive test 
 *
 */




public class DiffCheckTemplate extends Template_MultiPageMYP {
	String pageName="ApproveData";
	protected boolean storeCache=false;
	List<LogRecord> logRecords = new ArrayList<LogRecord>();

	@Override
	public void config() {
	}

	@Override
	public RunStrategy customizeStrategy(RunStrategy strategy) {
		//strategy.browserName="ie";		
		//strategy.cleanupDrivers=true;		
		//	strategy.logLevel=Constants.LogLevel.INFO;
		return super.customizeStrategy(strategy);
	}

	@Override
	public void init() {
		SessionContext.appConfig.WEBDRIVER_TIMEOUT=360;
		kw.setContentProvider(new Lib_Content_Diff());
		super.init();	
		kw.getNavContainer().getDefaulModel().getFactory().removeValidator("(.*)");

	}

	public DiffCheckTemplate(String page){
		this.pageName=page;
	}
	public DiffCheckTemplate(){
	}
	protected void setPageName(String page){
		this.pageName=page;
	}


	public boolean start_test_isFailed(String pageName){
		LogRecord logRecord = new LogRecord();
		boolean isFailed=false;
		recreateDriver();
		SessionContext.appConfig.URL_UNIT_TEST_MODE="http://host/yesterdayBuild/";
		kw.navigateByName(pageName);		
		log.info(kw.getCurrentUrl());
		try {
			FileUtils.write(new File("temp.txt"),kw.getContentProvider().entry_getPageContent("", kw).contentFormatted);
			String contentOld=cleanup(pageName,kw.getContentProvider().entry_getPageContent(pageName, kw).content);
			//For more debugggin
			if(log.TRACE_ENABLED)log.trace(kw.getContentProvider().entry_getPageContent(pageName, kw).contentFormatted);

			recreateDriver();
			SessionContext.appConfig.URL_UNIT_TEST_MODE="http://host/todayBuild/";
			kw.navigateByName(pageName);

			FileUtils.write(new File("temp1.txt"),kw.getContentProvider().entry_getPageContent("", kw).contentFormatted);
			String contentNew=cleanup(pageName,kw.getContentProvider().entry_getPageContent(pageName, kw).content);
			if(log.TRACE_ENABLED)log.trace(kw.getContentProvider().entry_getPageContent(pageName, kw).contentFormatted);
			log.info(kw.getCurrentUrl());

			isFailed=!contentOld.equals(contentNew);

			logRecord.name=pageName;
			if(isFailed){
				logRecord=handleKnownDiff(pageName, contentOld, contentNew, logRecord);
				isFailed=logRecord.isFailed;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = !isFailed?"PASSED":"FAILED";
		log.info("Comparing for page : "+pageName +" result is "+ result);

		logRecord.status=result;

		logRecords.add(logRecord);
		return isFailed;

	}


	public String cleanup(String pageName, String input){
		String output="";
		output=input.replaceAll("[0-9]{1,2}\\/[0-9]{1,2}\\/2016","")
				.replaceAll("[0-9]{1,2}\\/[0-9]{1,2}\\/16","")
				.replaceAll("[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}","")
				.replaceAll("[0-9]{1,2}.{1}[AP]M","")
				.replaceAll("[0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}","")
				;			

		if(pageName.equals("")){
			output=output.replaceAll("[0-9]{3}", "");
		}

		return output;
	}



	private LogRecord handleKnownDiff(String pageNaem, String contentOld, String contentNew, LogRecord logRecord){
		boolean isFailed=true;
		String contentOldFormatted=cleanup(pageName,contentOld);
		String contentNewFormatted=cleanup(pageName, contentNew);
		//kw.checkPageLayoutForm(pageName, "temp.txt");
		Utils_PageDiff utils = new Utils_PageDiff();
		String diff = utils.getDiff(contentOldFormatted, contentNewFormatted);
		String diff_regex=diff.replaceAll("AREAS OF DIFFERENCE:", "").replaceAll("[0-9]", "9").replaceAll(":", "").replaceAll("\\.", "").replaceAll("-", "").replaceAll(",", "").replaceAll("\n", "");

		URIBuilder uri = new URIBuilder();
		log.debug(diff_regex);
		ExternalScriptSvc<List<String>> externalSvc = kw.serviceFactory.getExternalScriptSvc();
		if(storeCache){
			externalSvc.queryTRSJsonMessage("py/diffRegexInsert?Page=" +pageName+"&Ctrls="+diff_regex);
		}
		List<String> list_diff_regex_expected = externalSvc.queryTRSJsonMessage("py/diffRegexFind?Page=" +pageName+"&Ctrls="+diff_regex);
		String diff_regex_expected="";
		if(!list_diff_regex_expected.isEmpty()){
			diff_regex_expected=list_diff_regex_expected.get(0).replaceAll("/n","");
		}
		if(diff_regex.equals(diff_regex_expected)){
			isFailed=false;
			log.info("Page Different but variation regex is acceptable, hence passing");
		}else{
			log.info(diff_regex);
			log.info("vs");
			log.info(diff_regex_expected);

		}
		logRecord.diff=diff_regex;
		//log.debug(diff_regex);
		diff = utils.getDiff(contentNew, contentOld);
		//log.debug(diff);
		log.info(+'\n'+contentOld+'\n'+"vs"+'\n'+contentNew);

		//				log.debug(contentOldFormatted);
		//				log.error("vs");
		//				log.debug(contentNewFormatted);
		logRecord.isFailed=isFailed;		
		return logRecord;
	}
	
	protected void logReport(){
		int i =0;
		System.out.println("==================================================================================="+'\n');
		for(LogRecord rec:logRecords){
			i++;
			System.out.println('\n');
			System.out.format("%3d %-20s %14s %45s", i, rec.name, rec.status, rec.diff);		}
			System.out.println('\n'+"------------------------------------------------------------------------------------"+'\n');
		
	}
}



class AlternateSiteContentProvider implements StaticContentProvider {
	Logger1 log = new Logger1(this.getClass().getName());

	public List<String> getContent(String expression, ServiceHub kw) {
		List<String> toReturn =new ArrayList<String>();
		try {
			toReturn  = FileUtils.readLines(new File("temp.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toReturn;
	}


}


class Lib_Content_Diff extends Lib_PageLayout_Content{
	@Override
	public void init() {			
		super.init();
		fileContentProvider.setStaticContentProvider(new AlternateSiteContentProvider());

	}


}


class LogRecord{
	public int sno;
	public String name;
	public String status;
	public String diff;
	public boolean isFailed;
}
package org.spo.fw.specific.scripts.templates;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.log.Logger1;
import org.spo.fw.session.SessionContainer;
import org.spo.fw.specific.scripts.setup.SimpleScriptMYP;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.itf.WebContentProvider;
import org.spo.fw.web.ServiceHub;

/**
 * 
 * @author Prem
 * The test domain is Upload 
 * This is a Non destructive test 
 *
 */
public class DownloadCheckTemplate extends SimpleScriptMYP {
	String pageName="";
	
	@Override
	public RunStrategy customizeStrategy(RunStrategy strategy) {
		strategy.browserName="chrome";
		strategy.cleanupDrivers=true;
		return super.customizeStrategy(strategy);
	}

	@Override
	public void init() {
		super.init();	
		kw.setContentProvider(new Lib_Content_Downloads());
		kw.getNavContainer().getDefaulModel().getFactory().removeValidator("(.*)");

	}

	protected void validateDownload(String fileNameRegex, String fileNameExtn){
		SessionContainer.store(SessionContainer.SCOPE.REQ, "fileNameRegex", fileNameRegex);
		SessionContainer.store(SessionContainer.SCOPE.REQ, "fileNameExtn", fileNameExtn);
	}

	@Override
	public void start_test() throws Exception {
		// TODO Auto-generated method stub

	}

}



class DownloadedFileContentProvider implements WebContentProvider{
	Logger1 log = new Logger1(this.getClass().getName());
	public String getPageContent(String pageName, ServiceHub kw) {
		try {
			String[] extns = {(String)SessionContainer.get(SessionContainer.SCOPE.REQ, "fileNameExtn")};
			String fileNameExpr = (String)SessionContainer.get(SessionContainer.SCOPE.REQ, "fileNameRegex");
			Collection<File> fileList= FileUtils.listFiles(new File(System.getProperty("user.home")+"/Downloads"),extns, false);
			for(File file:fileList){
				if(file.getName().substring(0,file.getName().indexOf(".")).matches(fileNameExpr+".*")){
					log.debug("Reading downloaded file "+file.getName());
					return FileUtils.readFileToString(file);
				}
			}
			log.error("File Not found "+fileNameExpr);
			//return FileUtils.readFileToString(new File(System.getProperty("user.home")+"/Downloads/UploadErrors_2016_03_30_093021.csv"));
			return "";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getPageContentFormatted(String pageName, ServiceHub kw) {
		return "";
	}

}


class Lib_Content_Downloads extends Lib_PageLayout_Content{
	@Override
	public void init() {			
		super.init();			
		webContentProvider.setWebContentProvider(new DownloadedFileContentProvider());
	}
}



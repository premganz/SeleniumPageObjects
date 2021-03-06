/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.navigation.svc;


import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.openqa.selenium.WebDriverException;
import org.spo.fw.config.Constants;
import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.ApplicationNavigationModel;
import org.spo.fw.navigation.itf.MultiPage;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.itf.NavLink;
import org.spo.fw.navigation.itf.NavigationServiceProvider;
import org.spo.fw.navigation.itf.NavigationTask;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.web.ProfileLogger;
import org.spo.fw.web.ServiceHub;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 * @author prem
 * achieves transformations on the webdriver based on applicationnavmodel query results.
 * Also stateful.
 * workflows can use this api's getModel() for any in memory manipualation to the  app nav model
 * It can be used statefully to navigate through the pages start point and reset point is init()
 *
 */

public class ApplicationNavContainerImpl implements NavigationServiceProvider{


	protected ApplicationNavigationModel model;
	protected Logger1 log =new Logger1("ApplicationNavContainerImpl");

	public void init() {
		model=new ApplicationNavModelGeneric();		
		model.init();

	}

	@Override
	public void navigateToUrlFromHome(String destUrl, ServiceHub kw) throws NavException{
		try {
			List<NavigationTask> navSteps = model.getDefaultPath("", destUrl);
			log.debug(navSteps.toString());
			for(NavigationTask step: navSteps){
				step.navigate(kw);
			}			
			log.debug("reached "+kw.getCurrentUrl());
		} catch (Exception e) {
			log.info(e);
			throw new NavException(e.getClass().getCanonicalName());
		}

	}

	public void navigateToUrlByName(String name,  ServiceHub kw) throws NavException{
		try {
			List<NavigationTask> navSteps = model.getDefaultPath_name(name);			
			log.debug(navSteps.toString());
			if(navSteps.isEmpty()){
				throw new NavException("The name "+name + " resulted in zero steps.");
			}
			for(int i=0; i<navSteps.size();i++){
				NavigationTask step = navSteps.get(i);
				if(i==navSteps.size()-1) {
					StringBuffer profMsg = new StringBuffer();
					boolean onProfileMode = SessionContext.profileLoggingLevel.equals(Constants.ProfileLevel.PAGE_LOAD);
					if(onProfileMode) {
						kw.setProfileMode(true);
					}
						step.navigate(kw);
					if(onProfileMode) {
						profMsg.append("got to,"+step.getTargetPage().getName()+"," +kw.getCurrentUrl().replaceAll("http://qatest@trvwcdqm28/chakra_", ""));
						kw.setProfileMode(false);
						ProfileLogger.closeAndPushToLog(profMsg.toString());
					}
				}else {
					step.navigate(kw);	
				}



			}
			NavigationTask step= navSteps.get(navSteps.size()-1);			
			if(step.getTargetPage().getPageValidator()!=null && !step.getTargetPage().getPageValidator().validateOnLoad(kw)){
				log.error("Validate on load failed for "+name);
				throw new NavException("VAlidate on load failed");
				
			}
			log.debug("reached "+kw.getCurrentUrl());
		} catch (WebDriverException e) {
			e.printStackTrace();log.info(e);
			log.debug(e.getClass().getSimpleName()+" was thrown for navigating/setting state to page "+name);
			log.trace(e);
			throw new NavException(e.getClass().getCanonicalName());
		}

	}
	
	

	public void changeLastPageState(String name, String stateExpression,  ServiceHub kw) throws NavException{
		try {
			List<NavigationTask> navSteps = model.getDefaultPath_name(name);			
			log.debug(navSteps.toString());
			if(navSteps.isEmpty()){
				throw new NavException("The name "+name + " resulted in zero steps.");
			}
			NavigationTask step= navSteps.get(navSteps.size()-1);
			step.getTargetPage().setState(stateExpression, kw);			

			log.debug("reached "+kw.getCurrentUrl());
		} catch (Exception e) {
			e.printStackTrace();
			throw new NavException(e.getClass().getCanonicalName());
		}

	}

	public void navigateToUrlByName_loop(String baseName,  ServiceHub kw) throws NavException{
		try {
			List<NavigationTask> navSteps = model.getDefaultPath_name(baseName);
			log.debug(navSteps.toString());
			for(NavigationTask step: navSteps){
				step.navigate(kw);
			}
			//kw stands on last Step.
			Page targetPage = navSteps.get(navSteps.size()-1).getTargetPage();
			MultiPage multiPage = model.enrichMultiPage(targetPage);
			List<NavLink> navLinks = multiPage.getSubLinks();
			for(NavLink link: navLinks){
				multiPage.followLink(link, kw);
				log.debug("reached "+kw.getCurrentUrl());
				Thread.sleep(1000);
				multiPage.followLink(multiPage.getLoopBackLink(), kw);
				log.debug("reached "+kw.getCurrentUrl());
			}

			log.debug("reached "+kw.getCurrentUrl());

		} catch (Exception e) {
			log.info(e);
			throw  new NavException(e.getClass().getName());
		}

	}

	//TODO move to model itself? since not involving driver.
	public MultiPage getMultiPage(String baseName) throws NavException{
		try {
			List<NavigationTask> navSteps = model.getDefaultPath_name(baseName);
			log.debug(navSteps.toString());			
			Page targetPage = navSteps.get(navSteps.size()-1).getTargetPage();
			MultiPage multiPage = model.enrichMultiPage(targetPage);
			return multiPage;

		} catch (Exception e) {
			log.info(e);
			throw  new NavException(e.getClass().getName());
		}

	}


	public List<String> queryAppDocModel(String query, String attr) throws NavException {
		List<String> resultLst = new ArrayList<String>();
		try{
			Document doc = model.getAppHeirarchyDoc();
			XPath xpath = XPathFactory.newInstance().newXPath();

			//		String query = "//Row/Record[(@key=\"" +expressionArray[0]+"\" and @value=\"" +expressionArray[1]+"\" )" +
			//				" and  (@key=\"" +expressionArray[2]+"\" and @value=\"" +expressionArray[3]+"\")"+ 
			//				"]/../Record[@key=\"" +requiredField+"\"]";

			log.debug("Query is "+ query);



			XPathExpression expr = xpath.compile(query);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList tableRowNodes = (NodeList) result;
			for(int i = 0;i<tableRowNodes.getLength();i++){
				Node node = tableRowNodes.item(i);
				resultLst.add(node.getAttributes().getNamedItem(attr).getNodeValue());
				log.trace("returning "+node.getAttributes().getNamedItem(attr));
			}

			log.info("returning "+tableRowNodes);

		}catch(Exception e){
			log.info(e);
			throw  new NavException(e.getClass().getName());
		}
		return resultLst;
	}

	//TODO Change signature to getNavModel

	public ApplicationNavModelGeneric getDefaulModel() {
		return (ApplicationNavModelGeneric)model;
	}

	public void setDefaultModel(ApplicationNavModelGeneric model) {
		this.model = model;
	}	


	public ApplicationNavigationModel getModel() {
		return model;
	}

	public void setModel(ApplicationNavigationModel model) {
		this.model = model;
	}	



}



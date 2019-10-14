/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.navigation.svc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.xerces.dom.DeferredDocumentImpl;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.ApplicationNavigationModel;
import org.spo.fw.navigation.itf.MultiPage;
import org.spo.fw.navigation.itf.NavLink;
import org.spo.fw.navigation.itf.NavigationTask;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.navigation.model.NavLinkImpl;
import org.spo.fw.navigation.model.NavTaskImpl;
import org.spo.fw.navigation.util.PageFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;

/**
 * 
 * @author prem
 * A stateful query interface of the application model document. This provides various methods to modify the document and to 
 * extract domain model(s) from the document.
 * state is reset on calling init() method.
 * Has a default access constructor because it is designed to be acessed via the container api methods. 
 *
 */
public class ApplicationNavModelGeneric implements ApplicationNavigationModel {
	protected Document appHeirarchyDoc;
	protected Logger1 log = new Logger1("ApplicationNavModelGeneric");
	protected PageFactory factory;

	protected String resourcePath="";
	public ApplicationNavModelGeneric() {

	}

	public void init() {

		resourcePath="/ApplicationNavTreeModelGeneric.xml";
		factory= new PageFactoryImpl();
		appHeirarchyDoc =parseDoc(resourcePath);

	}

	public Document parseDoc(String resourcePath) {
		InputStream in = getClass().getResourceAsStream(resourcePath);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			try {
				Document appHeirarchyDoc1 = docBuilder.parse(in);
				return appHeirarchyDoc1;
			} catch (SAXException | IOException e) {

				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getHomePageUrl() {
		String xquery= "//homepage" ;
		List<Element> heirarchialElements = new ArrayList<Element>();
		try {
			heirarchialElements = util_queryHelper(xquery);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return heirarchialElements.get(0).getAttribute("url");
	}

	public List<NavigationTask> getDefaultPath_name(String name) {
		List<NavigationTask> taskList = new ArrayList<NavigationTask>();
		try{
			String xquery= "application/homepage//page[@name='" +name +"']" ;
			List<Element> heirarchialElements = util_queryHelper(xquery);
			return util_getDefaultPath(heirarchialElements);}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Page util_getTargetPagePath(List<Element> existingElems) {
		List<NavigationTask> taskList = new ArrayList<NavigationTask>();
		Element lastElem = existingElems.get(existingElems.size()-1);
		String xquery="";
		try{
			if(lastElem.hasAttribute("type")){
				//then the last one is a page with a link and so expect a link beyond
				String lastNavLinkValue = lastElem.getAttribute("name");
				Element lastButOneElem = existingElems.get(existingElems.size()-2);	
				String name_lbo_page = lastButOneElem.getAttribute("name");
				xquery= "application/homepage//page[@name='" +name_lbo_page +"']/nav[@name='" +lastNavLinkValue +"']/page" ;
				Element lastTargetPage = util_queryHelper_flat(xquery).get(0);
				Page targetPage = factory.getPage(lastTargetPage.getAttribute("name"));
				targetPage.setName(lastTargetPage.getAttribute("name"));
				targetPage.setUrl(lastTargetPage.getAttribute("url"));
				targetPage.setStateExpression(lastTargetPage.getAttribute("state"));
				return targetPage;
			}else{
				return null;
			}
		}catch(Exception e){
			log.error("Error encoutnered while working with xquery "+xquery);
			e.printStackTrace();
			return null;
		}
	}

	//TODO : FIXME fromUrl is never used.
	public List<NavigationTask> getDefaultPath(String fromUrl, String toUrl) {
		List<NavigationTask> taskList = new ArrayList<NavigationTask>();
		try{
			String xquery= "application/homepage//page[@url='" +toUrl +"']" ;
			List<Element> heirarchialElements = util_queryHelper(xquery);
			return util_getDefaultPath(heirarchialElements);}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public List<NavigationTask> getFromToPath(String fromUrl, String toUrl) {
		try{
			String xquery= "application/homepage//page[@url='" +fromUrl +"']" ;
			List<Element> heirarchialElements = util_queryHelper(xquery);
			List<NavigationTask> pathTillFromTask = util_getDefaultPath(heirarchialElements);
			xquery= "application/homepage//page[@url='" +toUrl +"']" ;
			heirarchialElements = util_queryHelper(xquery);
			List<NavigationTask> pathTillToTask = util_getDefaultPath(heirarchialElements);
			for(NavigationTask task: pathTillFromTask){
				if(pathTillToTask.contains(task)){
					pathTillToTask.remove(task);
				}
			}

			return pathTillToTask;

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public MultiPage enrichMultiPage(Page lastPage) {
		MultiPage multiPage = (MultiPage)factory.getMultiPage();	
		String toName = lastPage.getName();
		try{
			String xquery= "application/homepage//page[@name='" +toName +"']" ;
			List<Element> flatElements_toPage = util_queryHelper_flat(xquery);
			Element elem_page = flatElements_toPage.get(flatElements_toPage.size()-1);
			if(elem_page.hasAttribute("url")){
				if(!elem_page.getAttribute("loopbacktype").isEmpty()){
					NavLink link = new NavLinkImpl();
					link.setNavId(elem_page.getAttribute("loopbacklink"));
					link.setNavType(elem_page.getAttribute("loopbacktype"));
					multiPage.setLoopBackLink(link);
				}
				multiPage.setUrl(elem_page.getAttribute("url"));
				multiPage.setName(elem_page.getAttribute("name"));
			}

			xquery= "application/homepage//page[@name='" +toName +"']/nav" ;
			List<Element> flatElements = util_queryHelper_flat(xquery);
			List<NavLink> navList = new ArrayList<NavLink>();
			for(Element elem: flatElements){
				NavLink link = new NavLinkImpl() ;
				link.setName(elem.getAttribute("name"));
				link.setNavId(elem.getAttribute("val"));
				link.setNavType(elem.getAttribute("type"));
				navList.add(link);
			}
			multiPage.setSubLinksList(navList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return multiPage;
	}

	public List<NavigationTask> util_getDefaultPath(List<Element> heirarchialElements) {
		List<NavigationTask> taskList = new ArrayList<NavigationTask>();
		try{
			NavTaskImpl navTask = new NavTaskImpl() ;
			Page lastPage = factory.getDefaultPage("");	
			boolean gotPage= false;
			for(int i = 0; i< heirarchialElements.size();i++){
				Element elem = heirarchialElements.get(i);
				if(elem.hasAttribute("url")){
					lastPage = factory.getPage(elem.getAttribute("name"));
					lastPage.setUrl(elem.getAttribute("url"));
					lastPage.setName(elem.getAttribute("name"));
					lastPage.setStateExpression(elem.getAttribute("state"));
					gotPage = true;
				}else if(elem.getTagName().equals("nav")){
					NavLink link = new NavLinkImpl() ;
					link.setName(elem.getAttribute("name"));
					link.setNavId(elem.getAttribute("val"));
					link.setNavType(elem.getAttribute("type"));					
					if(gotPage){
						navTask = new NavTaskImpl();
						navTask.setPage(lastPage);
						navTask.setLink(link);

						//Setting targetPage
						if(i<heirarchialElements.size()-1){
							Element elem_targetPage = heirarchialElements.get(i+1);//Assumes that Page is at the bottom and not nav TODO
							Page targetPage = factory.getPage(elem_targetPage.getAttribute("name"));
							targetPage.setUrl(elem_targetPage.getAttribute("url"));
							targetPage.setName(elem_targetPage.getAttribute("name"));
							targetPage.setStateExpression(elem_targetPage.getAttribute("state"));
							navTask.setTargetPage(targetPage);

						}else{
							//we are probably at the end 
							Page targetPage = util_getTargetPagePath(heirarchialElements);
							targetPage.setLastPage(true);
							navTask.setTargetPage(targetPage);
						}

						taskList.add(navTask);
						gotPage= !gotPage;
					}
				}


			}


		}catch(Exception e){
			e.printStackTrace();
		}
		return taskList;
	}

	public List<Element> util_queryHelper(String xquery) throws Exception{
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		//		xquery= "//page[@name='AbstractHomePage']" ;
		XPathExpression expr = xpath.compile(xquery );
		Object result = expr.evaluate( appHeirarchyDoc, XPathConstants.NODESET );
		NodeList nl = (NodeList) result;
		List<Element> heirarchialElems = new ArrayList<Element>();
		for(int j=0 ; j < nl.getLength() ; j++){
			Node node = nl.item(j);
			if(node instanceof DeferredElementImpl){
				continue;
			}
			Element elem = (Element)node;
			while(elem.getParentNode()!=null && !(elem.getParentNode() instanceof DeferredDocumentImpl )){ 

				Node n = elem.getParentNode();	    	   
				if(n instanceof DeferredElementImpl){
					continue;
				}
				else{
					elem=(Element)n;	
					if(elem.getTagName().equals("application")){
						break;	
					}
					heirarchialElems.add(elem);	

				}


			}

		}
		Collections.reverse(heirarchialElems);
		log.debug(heirarchialElems.toString());
		return heirarchialElems;
	}

	public List<Element> util_queryHelper_flat(String xquery) throws Exception{
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		//		xquery= "//page[@name='AbstractHomePage']" ;
		XPathExpression expr = xpath.compile(xquery );
		Object result = expr.evaluate( appHeirarchyDoc, XPathConstants.NODESET );
		NodeList nl = (NodeList) result;
		List<Element> heirarchialElems = new ArrayList<Element>();
		for(int j=0 ; j < nl.getLength() ; j++){
			Node node = nl.item(j);
			if(node instanceof DeferredElementImpl){
				continue;
			}
			Element elem = (Element)node;

			heirarchialElems.add(elem);	

		}
		log.debug(heirarchialElems.toString());
		return heirarchialElems;
	}


	public void changePageState(String pageName, String pageStateExpression){
		log.trace("changed page state for " +pageName +" expression: "+pageStateExpression);
		appHeirarchyDoc.getElementById(pageName).setAttribute("state", pageStateExpression);

	}

	public void changeLinkVal(String navName, String navStateExpression){
		appHeirarchyDoc.getElementById(navName).setAttribute("val", navStateExpression);

	}

	@Override
	public Document getAppHeirarchyDoc() {
		return appHeirarchyDoc;
	}

	public PageFactory getFactory() {
		return factory;
	}

	public void setFactory(PageFactory factory) {
		this.factory = factory;
	}



}

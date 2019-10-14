/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.setup;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.ApplicationNavigationModel;
import org.spo.fw.navigation.svc.ApplicationNavModelGeneric;
import org.spo.fw.navigation.util.PageFactoryImpl;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author prem
 * A stateful query interface of the application model document. This provides various methods to modify the document and to 
 * extract domain model(s) from the document.
 * state is reset on calling init() method.
 * Has a default access constructor because it is designed to be acessed via the container api methods. 
 *
 */
public class ApplicationNavModelMYP extends ApplicationNavModelGeneric implements ApplicationNavigationModel  {
	protected Logger1 log = new Logger1("ApplicationNavModelMYP");

	
	public void init() {
		
		resourcePath="/ApplicationNavTreeModel.xml";
		factory=new PageFactoryMYP();
		appHeirarchyDoc =parseDoc(resourcePath);
	}


}

/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.meta.fixture.page;

import java.net.URISyntaxException;
import java.net.URL;

import org.spo.fw.config.AppConfig;
import org.spo.fw.web.ServiceHub;

public class HomePage extends StubDefaultPage {

	@Override
	public void setState(String stateExpression) {
		if(exp.getName().equals("Wiki")){
			kw.goToPage("http://en.wikipedia.org");	
		}else if(exp.getName().equals("Stat")){
			URL resourceUrl_page = getClass().getResource("/test_Script_Complex.html");
			String resourcePath_driver="";
			String resourcePath_page="";
			try {
				resourcePath_page = resourceUrl_page.toURI().getPath();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			kw.goToPage("file://"+resourcePath_page);
		}
		
		
	}


}

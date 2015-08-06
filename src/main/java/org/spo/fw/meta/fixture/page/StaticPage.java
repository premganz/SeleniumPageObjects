package org.spo.fw.meta.fixture.page;

import java.net.URISyntaxException;
import java.net.URL;

import org.spo.fw.config.AppConfig;
import org.spo.fw.web.KeyWords;

public class StaticPage extends StubDefaultPage {

	@Override
	public void setState(String stateExpression) {
		if(exp.getName().equals("Wiki")){
			kw.goToPage("http://en.wikipedia.org");	
		}else{
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

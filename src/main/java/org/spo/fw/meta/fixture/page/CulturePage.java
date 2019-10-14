/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.meta.fixture.page;

import org.spo.fw.web.ServiceHub;

public class CulturePage extends StubDefaultPage {

	@Override
	public void setState(String stateExpression) {
		log.info("Setting state to "+stateExpression);
		log.debug("ECHO "+this);
		super.setState(stateExpression);
	}


	@Override
	public String getFormData(ServiceHub kw) {	
		log.debug("ECHO "+this);
		if(state.equals("SomeState:test=local")){
			return kw.getTitle();	
		}
		return "";

	}

}

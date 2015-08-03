package org.spo.fw.meta.fixture.page;

import org.spo.fw.web.KeyWords;

public class CulturePage extends StubDefaultPage {

	@Override
	public void setState(String stateExpression) {
		log.info("Setting state to "+stateExpression);
		log.debug("ECHO "+this);
		super.setState(stateExpression);
	}


	@Override
	public String getFormData(KeyWords kw) {	
		log.debug("ECHO "+this);
		if(state.equals("SomeState:test=local")){
			return kw.getTitle();	
		}
		return "";

	}

}

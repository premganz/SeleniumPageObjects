package org.spo.fw.meta.fixture.page;

import org.spo.fw.navigation.model.BasePage;
import org.spo.fw.web.ServiceHub;

public class StubDefaultPage extends BasePage {

	@Override
	public void setState(String stateExpression) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void init(){
	}
	
	@Override
	public String getFormData(ServiceHub kw) {
		StringBuffer buf = new StringBuffer();		
		return buf.toString();
	}


}

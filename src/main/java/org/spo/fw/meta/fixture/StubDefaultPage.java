package org.spo.fw.meta.fixture;

import org.spo.fw.navigation.model.BasePage;
import org.spo.fw.web.KeyWords;

public class StubDefaultPage extends BasePage {

	@Override
	public void setState(String stateExpression) {
		// TODO Auto-generated method stub
		
	}

	public void init(){
	}
	
	@Override
	public String getFormData(KeyWords kw) {
		StringBuffer buf = new StringBuffer();		
		return buf.toString();
	}


}

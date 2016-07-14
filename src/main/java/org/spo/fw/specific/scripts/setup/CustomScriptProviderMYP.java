package org.spo.fw.specific.scripts.setup;

import org.spo.fw.launch.CustomScriptProvider;

public class CustomScriptProviderMYP extends CustomScriptProvider {

	@Override
	public  void init() {		
		customScriptFactory=new ScriptFactoryMYP();
	}

	
}

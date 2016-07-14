package org.spo.fw.specific.scripts.setup;

import org.spo.fw.launch.robot.CustomScriptEnabledLibrary;


public class CustomScriptLibraryMYP extends CustomScriptEnabledLibrary{

	@Override
	public  void init() {		
		setFactory(new PageFactoryMYP());
		setNavModel(new ApplicationNavModelMYP());
		customScriptFactory=new ScriptFactoryMYP();
		super.init();
	}


}

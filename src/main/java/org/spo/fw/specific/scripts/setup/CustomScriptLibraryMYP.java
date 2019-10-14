/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.setup;

import org.spo.fw.launch.robot.CustomScriptEnabledLibrary;


public class CustomScriptLibraryMYP extends CustomScriptEnabledLibrary{

	@Override
	public  void init() {		
		impl_nav.getNavContainer().getDefaulModel().setFactory(new PageFactoryMYP());
		impl_nav.getNavContainer().setModel(new ApplicationNavModelMYP());
		customScriptFactory=new ScriptFactoryMYP();
		super.init();
	}


}

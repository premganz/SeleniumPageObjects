package org.spo.fw.selenium;

import org.spo.fw.config.SessionContext;
import org.spo.fw.recorder.Script_Robot_Generator;

public class ScriptContraintFactory {

	
	public static ScriptConstraint instance(String scriptName){
	if(scriptName.contains(Script_Robot_Generator.class.getSimpleName())){
		return getDefault().setStartUrl(SessionContext.appConfig.BASE_URL+"cm_home.aspx").setTimer(90);
	}else{
		return getDefault();
	}
	
}
	public static ScriptConstraint getDefault(){
		ScriptConstraint constraint = new ScriptConstraint();
		return constraint;
	}
	

}
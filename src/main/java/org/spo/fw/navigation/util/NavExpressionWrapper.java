package org.spo.fw.navigation.util;


public class NavExpressionWrapper {
	String exp;
	public NavExpressionWrapper(String exp) {
		this.exp= exp;
	}

	public String getConditional(){
		if(exp.contains("_")){
			String[] navItems = exp.split("_");
			return navItems[1];
		}else{
			return "";
		}
	}
	
	public String getStrategy(){
		if(exp.contains("_")){
			String[] navItems = exp.split("_");
			return navItems[0];
		}else{
			return exp;
		}
	}
}

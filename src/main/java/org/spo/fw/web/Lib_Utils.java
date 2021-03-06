/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.web;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.spo.fw.config.SessionContext;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;



public class Lib_Utils {


	public static String alterGotoUrl(String url , String user, String password){
		 if(url.startsWith("https")){
			 return url;
		 }
		String url_frag1 =StringUtils.EMPTY;
		String url_toPrefix= StringUtils.EMPTY;
		String userId = SessionContext.appConfig.basicAuth_userId;
		String passwordUrl = SessionContext.appConfig.basicAuth_pwd;
		
		
       //Allowing user to configure the basic auth from robot
        if(user!=null){
        	userId = user;
        }
        if(password!=null){
        	passwordUrl = password;
        }
        url_toPrefix= url;
        //No basic auth for https
        
        if(url.startsWith("http")){
        	url_frag1 = url.substring(7,url.length());
        	if(SessionContext.requireBasicAuthUrlPrefix){
        		url_toPrefix = "http://"+userId+":"+passwordUrl+"@";	
        	}else{
        		url_toPrefix = "http://";	
        	}

        }
        //Basic auth is not supported for https and file, we will see in the future if this is needed
//        else if(url.startsWith("https")){
//        	url_frag1 = url.substring(8,url.length());
//        	if(SessionContext.requireBasicAuthUrlPrefix){
//        	//	url_toPrefix = "https://"+userId+":"+passwordUrl+"@";	
//        	}else{
//        	//	url_toPrefix = "https://";	
//        	}
//        }
//        else if(url.startsWith("file")){
//        	url_frag1 = url.substring(7,url.length());
//        	if(SessionContext.requireBasicAuthUrlPrefix){
//        		url_toPrefix = "file://"+userId+":"+passwordUrl+"@";	
//        	}else{
//        		url_toPrefix = "file://";	
//        	}
//
//        }
		
		String url_postProcessed=url_toPrefix+url_frag1;
		url_postProcessed=url_postProcessed.replaceAll("[\\s]" ,"%20");
		
		
		return url_postProcessed;
	
	}
	
	public static String printifyUrl(String output){		
		if(SessionContext.appConfig!=null && output.contains(SessionContext.appConfig.basicAuth_userId)){
			output=output.replaceAll("http://"+SessionContext.appConfig.basicAuth_userId+"@", "http://");
		}
		return output;
	}
	

	public String splitString(String input, String delimiter, int idx) {
		return input.split("\\"+delimiter)[idx];
	}

	public String generateRandomAscString(int maxLen) {
		return RandomStringUtils.randomAscii(maxLen);
	}
	
	public String generateRandomString( int maxLen) {
		return RandomStringUtils.randomAlphanumeric(maxLen);
	}
}

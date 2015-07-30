package org.spo.fw.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;


public class RobotCache {
	//For reusing drivers but not presently used due to unconfirmed basic auth issues.
	private static Map<String, WebDriver> driverStoreMap = new LinkedHashMap<String, WebDriver>();
	private static Map<String, String>  cache_ElementValues = new LinkedHashMap<String, String>();
	private static Map<String, Document > xmlCache = new LinkedHashMap<String, Document>();
	

	public static WebDriver getByName(String name){
		return driverStoreMap.get(name);
	}
	
	
	public static void put(String name, WebDriver driver){
		driverStoreMap.put(name, driver);
	}

	public static void putElemInCache(String elemId, String value){
		cache_ElementValues.put(elemId, value);
	}
	
	public static boolean isCacheElemValid(String elemId, String value){
		if(!cache_ElementValues.containsKey(elemId)){
			return false;
		}
		if(cache_ElementValues.get(elemId).equals(value)){
			return true;
		}
		return false;
		
	}
	public static void resetElemCache(){
		cache_ElementValues = new LinkedHashMap<String, String>();
		xmlCache = new LinkedHashMap<String, Document>();
	}


	public static Map<String, Document > getXmlCache() {
		return xmlCache;
	}


	public static void setXmlCache(Map<String, Document > xmlCache) {
		RobotCache.xmlCache = xmlCache;
	}

}

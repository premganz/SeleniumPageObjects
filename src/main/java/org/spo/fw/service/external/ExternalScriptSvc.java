package org.spo.fw.service.external;

import java.util.Map;

import org.spo.fw.config.RestfulExternalServices;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.TestResourceServerConnector;

// TODO FIXME right now only string works
public class ExternalScriptSvc<T> implements RestfulExternalServices  {
	private String serverRoot;
	Class<T> resultType;
	
	Logger1 log= new Logger1("ExternalScriptSvc");

	public String prepareUrl(String url){
		if(url.startsWith(serverRoot)){
			url.replaceAll(serverRoot, "");
			
		}
		return serverRoot+url;
	}
	
	
	public ExternalScriptSvc(String secretKey) {
		if(!secretKey.equals("9999")){
			log.error("Illegal Access of constructor");
			throw new IllegalAccessError();
		}
	}
	public T queryTRSJsonMessage(String url) {
		
		JsonMessage<T> jsonMessage = new JsonMessage<T>();
		TestResourceServerConnector<JsonMessage<T>> con = new TestResourceServerConnector<JsonMessage<T>>(jsonMessage);
		return con.queryServer(prepareUrl(url)).getPayload();
		
	}
public T queryTRS(String url, T object) {
		
		TestResourceServerConnector<T> con = new TestResourceServerConnector<T>(object);
		return con.queryServer(prepareUrl(url));
		
	}

public T postTRS(String url, T object, Object postBody, Map<String,String> uriParams) {
	
	TestResourceServerConnector<T> con = new TestResourceServerConnector<T>(object);
	return con.queryServerPost(prepareUrl(url), postBody, uriParams);
	
}
	public String queryTRSString(String url) {
		
		TestResourceServerConnector<String> con = new TestResourceServerConnector<String>("");
		return con.queryServer(prepareUrl(url));
		
	}
	public String getServerRoot() {
		return serverRoot;
	}
	public void setServerRoot(String serverRoot) {
		this.serverRoot = serverRoot;
	}
	
	
//	public void queryServerString(String queryUrl, TestResourceServerConnector<String> con){
//		try {
//			log.debug("calling server on url "+queryUrl);			
//			result = con.queryServer( queryUrl);
//		} catch (TestResourceServerException e) {		
//			log.trace(e);
//			log.error("Continuing Execution silently after TestResourceServerException on "+queryUrl);
//		}
//	}
//	
//	public T queryTRSCustom(String url) {
//		queryServerString(url, new TestResourceServerConnector<String>(result));
//		try{
//			Gson gson = new Gson();
//			Type typ = new TypeToken<T>(){}.getType();//FIXME right now only string works
//			T toReturn= gson.fromJson(result.toString(),this.getClass().getGenericSuperclass());		
//			return toReturn;
//
//		}catch(Exception e){
//			log.error("Error during messagePayload processing from  TestResourceServerException on "+url);
//			throw e;
//		}
//
//		
//	}
//	
	
//	public T queryTRS(String url) {
//	
//	JsonMessage<T> jsonMessage = new JsonMessage<T>();
//	TestResourceServerConnector<JsonMessage<T>> con = new TestResourceServerConnector<JsonMessage<T>>(jsonMessage);
//	
//	return con.queryServer("http://localhost:8081/python/getLatestDir").getPayload();
//	
//	T messagePayload =null;
//	JsonMessage<T> message = new JsonMessage<T>();	
//	
//	queryServer(url, new TestResourceServerConnector<T>(resultType));
//	try{
//		Gson gson = new Gson();
//		Type typ = new TypeToken<JsonMessage<T>>(){}.getType();//FIXME right now only string works
//		message= gson.fromJson(result,typ);		
//		messagePayload = message.getPayload();
//		log.debug(result);
//		if(message!=null) {log.debug(message.toString());}else{log.error("TRS MEssage null");}
//
//	}catch(Exception e){
//		log.error("Error during messagePayload processing from  TestResourceServerException on "+url);
//		log.trace(e);
//		e.printStackTrace();
//	}
//	return messagePayload;
//}
}

package org.spo.fw.service.external;

import java.lang.reflect.Type;
import java.util.List;

import org.spo.fw.exception.TestResourceServerException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.TestResourceServerConnector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// TODO FIXME right now only string works
public class ExternalScriptSvcOld<T>  {
	protected String result="";
	protected T resultCustom;
	Class<T> resultType;
	
	Logger1 log= new Logger1("ExternalScriptSvc");
	public ExternalScriptSvcOld(Class<T> clazz){
		this.resultType=clazz;
	}
	public ExternalScriptSvcOld(T obj){
		this.resultCustom=obj;
	}
	public T queryTRS(String url) {
		T messagePayload =null;
		JsonMessage<T> message = new JsonMessage<T>();	
		
		queryServer(url, new TestResourceServerConnector<T>(resultType));
		try{
			Gson gson = new Gson();
			Type typ = new TypeToken<JsonMessage<T>>(){}.getType();//FIXME right now only string works
			message= gson.fromJson(result,typ);		
			messagePayload = message.getPayload();
			log.debug(result);
			if(message!=null) {log.debug(message.toString());}else{log.error("TRS MEssage null");}

		}catch(Exception e){
			log.error("Error during messagePayload processing from  TestResourceServerException on "+url);
			log.trace(e);
			e.printStackTrace();
		}
		return messagePayload;
	}

	
	public void queryServer(String queryUrl, TestResourceServerConnector<T> con){
		try {
			log.debug("calling server on url "+queryUrl);			
			resultCustom = con.queryServer( queryUrl);
		} catch (TestResourceServerException e) {		
			log.trace(e);
			log.error("Continuing Execution silently after TestResourceServerException on "+queryUrl);
		}
	}
	public void queryServerString(String queryUrl, TestResourceServerConnector<String> con){
		try {
			log.debug("calling server on url "+queryUrl);			
			result = con.queryServer( queryUrl);
		} catch (TestResourceServerException e) {		
			log.trace(e);
			log.error("Continuing Execution silently after TestResourceServerException on "+queryUrl);
		}
	}
	
	public T queryTRSCustom(String url) {
		queryServerString(url, new TestResourceServerConnector<String>(result));
		try{
			Gson gson = new Gson();
			Type typ = new TypeToken<T>(){}.getType();//FIXME right now only string works
			T toReturn= gson.fromJson(result.toString(),this.getClass().getGenericSuperclass());		
			return toReturn;

		}catch(Exception e){
			log.error("Error during messagePayload processing from  TestResourceServerException on "+url);
			throw e;
		}

		
	}
}

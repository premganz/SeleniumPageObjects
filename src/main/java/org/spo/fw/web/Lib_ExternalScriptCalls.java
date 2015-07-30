package org.spo.fw.web;

import java.lang.reflect.Type;

import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.TestResourceServerException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.TestResourceServerConnector;
import org.spo.fw.shared.JsonMessage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// TODO FIXME right now only string works
public class Lib_ExternalScriptCalls<T>  {
	String result="";
	
	Logger1 log= new Logger1("Lib_External");
	


	public T queryTRS(String url) {
		T messagePayload =null;
		JsonMessage<T> message = new JsonMessage<T>();	
		//log.debug(message.toString());
		try {

			String queryUrl = SessionContext.appConfig.TEST_SERVER_BASE_URL+"python/"+url;
			log.debug("calling server on url "+queryUrl);
			TestResourceServerConnector<String> con = new TestResourceServerConnector<String>(result);
			result = con.queryServer("python", url);


		} catch (TestResourceServerException e) {		
			log.info(e);
			log.debug("Continuing Execution silently");
		}
		try{
			Gson gson = new Gson();
			Type typ = new TypeToken<JsonMessage<String>>(){}.getType();//FIXME right now only string works
			message= gson.fromJson(result,typ);		
			messagePayload = message.getPayload();
			// result= messagePayload.substring(messagePayload.lastIndexOf("/")+1,messagePayload.length());
			log.debug(result);
			if(message!=null) {log.debug(message.toString());}else{log.error("TRS MEssage null");}
			
//			String[] payload = (String[])message.getPayload();
//			for(int i =0;i<payload.length;i++){
//				System.out.println(payload[i]);
//				
//			}
		}catch(Exception e){
			log.error(e);
		}

		return messagePayload;
	}

}

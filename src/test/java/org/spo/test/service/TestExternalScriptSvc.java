package org.spo.test.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.spo.fw.config.SessionContext;
import org.spo.fw.service.TestResourceServerConnector;
import org.spo.fw.service.external.ExternalScriptSvc;
import org.spo.fw.service.external.JsonMessage;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class TestExternalScriptSvc {

	
	
	//@Test
	public void testExtSvc1(){
//		List<String> expected = new ArrayList<String>();
//		List<String> actual= new ArrayList<String>();
//		expected.add("hello");expected.add("hi");
//		JsonMessage<List<String>> jsonMessage = new JsonMessage<List<String>>();
//		ExternalScriptSvcStub<JsonMessage<List<String>>> svc = new ExternalScriptSvcStub<JsonMessage<List<String>>>(jsonMessage);
//		actual=svc.queryTRSCustom("").getPayload();
//		System.out.println(actual);		
	}
	
	@Test
	public void testExtSvc2(){
		List<String> expected = new ArrayList<String>();
		List<String> actual= new ArrayList<String>();
		expected.add("hello");expected.add("hi");
		JsonMessage<String> jsonMessage = new JsonMessage<String>();
		TestResourceServerConnector<JsonMessage<String>> con = new TestResourceServerConnector<JsonMessage<String>>(jsonMessage);
		//con.setRestTemplate(new RestTemplateStub());
		actual.add(con.queryServer("http://localhost:8081/python/getLatestDir").getPayload());
		System.out.println(actual);
		
	}
	
	
	
	@Test
	public void testExtSvc4(){
		List<String> expected = new ArrayList<String>();
		List<String> actual= new ArrayList<String>();
		expected.add("hello");expected.add("hi");
		
		ExternalScriptSvc<String> svc= new ExternalScriptSvc<String>();
		actual.add(svc.queryTRSString("http://localhost:8081/readxl/Demo/item?fld=price&val=6000"));
		System.out.println(actual);
		
	}
	
	
	@Test
	public void testExtSvc5(){
		List<String> expected = new ArrayList<String>();
		List<String> actual= new ArrayList<String>();
		expected.add("hello");expected.add("hi");
		
		TestResourceServerConnector<String> con = new TestResourceServerConnector<String>("");
		actual.add(con.queryServer("http://localhost:8081/readxl/Demo/item?fld=price&val=6000"));
		System.out.println(actual);
		
	}
	
	class RestTemplateStub extends RestTemplate{
//		public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException{
//			return (T)"{\"header\":\"head\",\"payload\":[\"hello\",\"hi\"]}";
//		}
//		
		
		
	}
	
	
	
}

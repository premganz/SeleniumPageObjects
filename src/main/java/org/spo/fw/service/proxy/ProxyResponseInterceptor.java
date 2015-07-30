package org.spo.fw.service.proxy;

import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;

import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import net.lightbody.bmp.proxy.http.ResponseInterceptor;


public class ProxyResponseInterceptor implements ResponseInterceptor{
	protected static Logger1 log = new Logger1("ProxyRequestInterceptor");
	
	public void process(BrowserMobHttpResponse response) {
		//System.err.println(response.getRawResponse().toString());
		//System.err.println(request.getProxyRequest().toString());
		
	}

}

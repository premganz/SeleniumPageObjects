/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.service.proxy;

import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;

import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import net.lightbody.bmp.proxy.http.RequestInterceptor;

public class ProxyRequestInterceptor implements RequestInterceptor{
	protected static Logger1 log = new Logger1("ProxyRequestInterceptor");
	
	public void process(BrowserMobHttpRequest request) {
		System.err.println(request.toString());
		request.addRequestHeader("Authorization", SessionContext.appConfig.BASIC_AUTH_DIGEST);
		System.err.println(request.getProxyRequest().toString());
		
	}

}

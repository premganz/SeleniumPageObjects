package org.spo.fw.service.proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.http.ResponseInterceptor;

import org.openqa.selenium.Proxy;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.service.RestrictedOSCmdRouter;


public class ProxyServerController {

	static ProxyServer proxyServer = null; 
	static Proxy proxy = null;
	static boolean isProxyingAllowed = false;
	/**
	 * https://groups.google.com/forum/#!topic/browsermob-proxy/w-xttP8NO_g
	 */
	private static RunStrategy runStrategy;

	public static  boolean isStarted;



	public static Proxy getProxy(){
		return proxy;
	}

	public static void stop(){
		try{
			if(isStarted){

				proxyServer.stop();
				proxy = null;
				proxyServer=null;
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
		isStarted = false;

	}

	public static void init(RunStrategy strategy) throws Exception{

		runStrategy = strategy;
		proxy = RestrictedOSCmdRouter.startProxyServer(proxyServer, proxy);
		isStarted = true;
		if (runStrategy.isRecordMode) {
			ResponseInterceptor interceptor = new JavaScriptLoggerInjector();
			proxyServer.addResponseInterceptor(interceptor);
		}

	}


	



}

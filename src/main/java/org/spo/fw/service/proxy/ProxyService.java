package org.spo.fw.service.proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.http.ResponseInterceptor;

import org.openqa.selenium.Proxy;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.service.RestrictedOSCmdRouter;

//TODO Future
public interface ProxyService {



	public  Proxy getProxy();

	public  void stop();

	public  void init(RunStrategy strategy) throws Exception;


	



}

package org.spo.fw.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.lightbody.bmp.proxy.ProxyServer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.UnPrivilagedOperationException;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.proxy.ProxyRequestInterceptor;
import org.spo.fw.service.proxy.ProxyResponseInterceptor;


public class RestrictedOSCmdRouter{ 
//TODO: extends SecurityManager{

	/**
	 * Channelizes OS Commands checking for security preconditions to avoid
	 * nuisance on build servers Uses conservative white listing.
	 * 
	 */
	static Logger1 log = new Logger1(
			"org.spo.fw.service.RestrictedOSCmdRouter");

	
	
	public static void securityCheck() throws Exception {

		try {
			osCheck();
			privilageCheck();
		} catch (Exception ex) {
			throw ex;
		}

	}

	private static void osCheck() throws Exception {
		if (!System.getProperty("os.name").startsWith("Windows")) {
			throw new SPOException();
		}

	}

	private static void privilageCheck() throws Exception {
		String hostName = "Unknown";
		boolean isSecure = false;

		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (UnknownHostException ex) {
			System.out.println("Hostname can not be resolved");
			throw new UnknownHostException();
		}
		for (String whiteListhostName : SessionContext.appConfig.whiteListedHosts) {
			if (hostName.equals(whiteListhostName)) {
				isSecure = true;
				break;
			}
		}

		if (!isSecure) {
			throw new UnPrivilagedOperationException();
		}
	}

	public static void taskKill(String taskId) throws Exception {
		try {
			securityCheck();
			Runtime.getRuntime().exec("taskkill /F /IM " + taskId);
		} catch (Exception e) {
			throw e;
		}

	}

	public static String execArbitraryCommand(String os, String cmd, String execDir) throws Exception {
		securityCheck();
		BufferedReader error_buffer = null;
		StringBuffer buf = new StringBuffer();
		String line = StringUtils.EMPTY;
		try {
			//System.setProperty("jdk.lang.Process.allowAmbigousCommands", "true");
			String[] cmdArray = {"cmd.exe","/c",cmd+ " 2>&1" };
			log.debug("Trying to execute in "+" os > "+cmd + " in "+execDir);
			File f = new File(execDir);
			
		//	Process p = new ProcessBuilder(cmdArray).start();

			InputStream is = new ProcessBuilder(cmdArray).directory(f).start().getInputStream();
		
		//	InputStream is = p.getInputStream();
			error_buffer = new BufferedReader(new InputStreamReader(is));
			while((line=error_buffer.readLine())!=null){
			
				log.debug("Reading cmd Stream "+line);
				buf.append(line);
			}
		}
		finally{
			try {
				if(error_buffer!=null){
					error_buffer.close();	
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buf.toString();
	}

	public static void unprivilagedTaskKill(String taskId) throws Exception {
		try {
			osCheck();
			Runtime.getRuntime().exec("taskkill /F /IM " + taskId);
		} catch (Exception e) {
			throw e;
		}

	}

	public static Proxy startProxyServer(ProxyServer proxyServer, Proxy proxy)
			throws Exception {
		String hostName = "Unknown";
		boolean isSecure = false;
		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (UnknownHostException ex) {
			System.out.println("Hostname can not be resolved");
			throw new UnPrivilagedOperationException();
		}
		for (String whiteListhostName : SessionContext.appConfig.whiteListedHosts) {
			if (hostName.equals(whiteListhostName)) {
				isSecure = true;
				break;
			}
		}

		if (!isSecure) {
			throw new UnPrivilagedOperationException();
		}

		try {
			proxyServer = new ProxyServer(9103);
			proxyServer.start();
			// proxyServer.setCaptureContent(true);
			// proxyServer.setCaptureHeaders(true);
			proxyServer.addRequestInterceptor(new ProxyRequestInterceptor());
			proxyServer.addResponseInterceptor(new ProxyResponseInterceptor());
			proxyServer.autoBasicAuthorization("TSH",
					SessionContext.appConfig.basicAuth_userId, SessionContext.appConfig.basicAuth_pwd);
			proxy = proxyServer.seleniumProxy();
			proxy.setHttpProxy("localhost:9103");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return proxy;
	}
	
}

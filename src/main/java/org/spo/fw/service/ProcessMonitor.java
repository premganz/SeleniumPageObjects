package org.spo.fw.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.UnPrivilagedOperationException;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;


public class ProcessMonitor extends Thread{ 
	//TODO: extends SecurityManager{

	/**

	 * 
	 */
	List<String> pids=new ArrayList<String>();
	List<String> target_processNames = new ArrayList<String>();
	// List<String> pids_new=new ArrayList<String>();
	Logger1 log = new Logger1("====PROCESSMONITOR=====");
	String currPid;
	//String processName;
	String deLimiter;//The column next to the ImageName 
	int min;//pollmin
	{
		//		processName="calc.exe";
		//		nextColumnValue="Console";//The column next to the ImageName	
		//		min=15;
	}

	public ProcessMonitor() {
		if(SessionContext.appConfig!=null){
			String unstablePcs = SessionContext.appConfig.UNSTABLE_PCS;

			String[] unstablePcsArr = unstablePcs.split(",");
			for(int i=0;i<unstablePcsArr.length;i++){
				target_processNames.add(unstablePcsArr[i]);
			}

			this.deLimiter="/t";//TODO Try to parametrize
			this.min=SessionContext.appConfig.WEBDRIVER_TIMEOUT;
		}
	}

	private  void securityCheck() throws Exception {

		try {
			osCheck();
			privilageCheck();
		} catch (Exception ex) {
			throw ex;
		}

	}

	private  void osCheck() throws Exception {
		if (!System.getProperty("os.name").startsWith("Windows")) {
			throw new SPOException();
		}

	}

	private  void privilageCheck() throws Exception {
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
		for (String whiteListhostName :SessionContext.appConfig.whiteListedHosts) {
			if (hostName.equals(whiteListhostName)) {
				isSecure = true;
				break;
			}
		}

		if (!isSecure) {
			throw new UnPrivilagedOperationException();
		}
	}

	public  void taskKill(String taskId) throws Exception {
		try {
			securityCheck();
			Runtime.getRuntime().exec("taskkill /F /IM " + taskId);
		} catch (Exception e) {
			throw e;
		}

	}

	private  String execArbitraryCommand(String os, String cmd, String execDir) throws Exception {
		securityCheck();
		BufferedReader error_buffer = null;
		StringBuffer buf = new StringBuffer();
		String line = StringUtils.EMPTY;
		try {
			//System.setProperty("jdk.lang.Process.allowAmbigousCommands", "true");
			String[] cmdArray = {"cmd.exe","/c",cmd+ " 2>&1" };
			log.trace("Trying to execute in "+" os > "+cmd + " in "+execDir);
			File f = new File(execDir);

			//	Process p = new ProcessBuilder(cmdArray).start();

			InputStream is = new ProcessBuilder(cmdArray).directory(f).start().getInputStream();

			//	InputStream is = p.getInputStream();
			error_buffer = new BufferedReader(new InputStreamReader(is));
			while((line=error_buffer.readLine())!=null){

				//log.debug("Reading cmd Stream "+line);
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


	private  String pollPids(String processName) {
		String x="";
		try{
			x = execArbitraryCommand("Windows","tasklist /FI \"IMAGENAME eq "+processName+"\"","c:\\");
			log.trace(x);
		}catch(Exception e){

		}
		return x;

	}
	//TODO FIXME
	//public  void main(String[] args) throws Exception{
	public void run(){
		String pid="";
		try{
			for(String target_process_image_name:target_processNames){
				String taskList_full_str = pollPids(target_process_image_name);
				String[] pcs_instance_arr = taskList_full_str.split(target_process_image_name);
				//List<String> pids= new ArrayList<String>();
				log.trace("whole string is  "+taskList_full_str+'\n'+"for process: "+target_process_image_name);

				for(int j = 0;j<pcs_instance_arr.length;j++){
					String pcs_instance = pcs_instance_arr[j];
					//	log.info("pcs line after splitting by process name is "+pcs_instance);
					if(pcs_instance.contains("=")  || pcs_instance.contains("INFO")   || pcs_instance.contains("Info") ||  pcs_instance.contains("info") ) {log.trace("ok,skipping thi sline");continue;}//header row
					log.trace("Splitting this one by spaces one gets ");					
					String pcs_instance_processed = pcs_instance.trim().replaceAll("[\\s]{1,5}", " ");
					String[] pid_frag_arr =pcs_instance_processed.split("[\\s]");
					for(int k =0;k<pid_frag_arr.length;k++){
						//log.info(pid_frag_arr[k]);
					}
					pid=pid_frag_arr[0];
					log.trace("Initial run Found pid at zero index "+pid);
				}

				pids.add(pid);
			}
			while(true){
				Thread.sleep(1000*60*min);
				for(String target_process_image_name:target_processNames){
					String taskList_full_str = pollPids(target_process_image_name);
					String[] pcs_instance_arr = taskList_full_str.split(target_process_image_name);
					//List<String> pids= new ArrayList<String>();
					log.trace("whole string is  "+taskList_full_str+'\n'+"for process: "+target_process_image_name);

					for(int j = 0;j<pcs_instance_arr.length;j++){
						String pcs_instance = pcs_instance_arr[j];
						//log.trace("pcs line after splitting by process name is "+pcs_instance);
						if(pcs_instance.contains("=")  || pcs_instance.contains("INFO")   || pcs_instance.contains("Info") ||  pcs_instance.contains("info") ) {log.info("ok,skipping thi sline");continue;}//header row
						log.trace("Splitting this one by spaces one gets "+'n');					
						String pcs_instance_processed = pcs_instance.trim().replaceAll("[\\s]{1,5}", " ");
						String[] pid_frag_arr =pcs_instance_processed.split("[\\s]");
						for(int k =0;k<pid_frag_arr.length;k++){
							//log.info(pid_frag_arr[k]);
						}
						pid=pid_frag_arr[0];//TODO to parametrize this zero.
						log.trace("Initial run Found pid at zero index "+pid);
					}

					pids.add(pid);


					if(pids.contains(pid)){					
						log.error("Found new long process pid "+pid);
						try {
							execArbitraryCommand("Windows", "taskkill /F /PID " + pid.trim(), "C:\\");									
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}

					}else{
						log.trace("Allowing process to run "+pid);
						pids.add(pid);
					}

				}


				log.trace(pids.toString());
			}
		}
		catch(InterruptedException e){
			log.trace("killing thread");
			return;
		}

	}



}

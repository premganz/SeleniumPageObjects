package org.spo.fw.recorder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.selenium.ScriptConstraint;
import org.spo.fw.web.KeyWords;


/**
 * 
 * Recorder of Browser Action into Robot Script
 * Requires Jquery support on the page
 * The keywords are hardcoded as it stands
 * 
 */
public class Script_Robot_Generator extends KeyWords implements SeleniumScript {
	String url_to_Start;
	private int timer;
	private boolean interrupt;
	private static List<String> filterSet = new ArrayList<String>();

	public int getTimer() {
		return timer;
	}

	
	
	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int start_recordSession(final int timeInSec, String launch_url) throws Exception{
		create("ie","test");

		goToPage(launch_url);
		print("Reached Page : You got  "+timer+" sec to record, Well actually ");
		//Thread.sleep(15000);
		new Thread("T"+1){
			public void run() {
				try {
					
					for(int i = 0; i<timeInSec;i++){
						Thread.sleep(1000);
						
							System.err.println(timeInSec-i*1);	
						
						
						String x = (String)executeJavaScript(generateReplaceString());
						if(interrupt)
							break;
						//print1(x);
					}
					print("***TestCases***");
					print("GeneratedTestCase "+Calendar.getInstance().getTime().toLocaleString());
					
					LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
					//Clean logentry and remove consecutive duplicates
					for (int i = 0; i< logEntries.getAll().size();i++){
						LogEntry eachEntry = logEntries.getAll().get(i);
						String logMessage =  eachEntry.getMessage().replaceAll("([0-9]{0,3}):([0-9]{0,3})","").replaceAll("console-api", "");
						if(i>0){
							if(!(filterSet.get(filterSet.size()-1).trim().equals(logMessage.trim()))){
								filterSet.add('\t'+logMessage);	
							}	
						}else{
							filterSet.add('\t'+logMessage);	
						}
						
						
						
						//System.out.println(eachEntry.toString());
					}
					//print1(filterSet.toString());
					for(String msg : filterSet){
						print(msg);
					}
					sleep(100);
					//join();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Exception caught in "+Thread.currentThread().getName());
					e.printStackTrace();
				}					

			};
		}.start();
		
		Thread.sleep(100000);
		return 0;
	}

	public void startUp(){
		execute();
		for(String msg : filterSet){
			print(msg);
		}
	}

	public String stop(){
		interrupt=true;
		StringBuffer buf = new StringBuffer();
		
		//buf.append("GeneratedTestCase "+Calendar.getInstance().getTime().toLocaleString()+'\n');
		for(String msg : filterSet){
			buf.append(msg);
			buf.append(StringUtils.EMPTY+'\n');
		}
		return "***TestCases***"+'\n'+
				"GeneratedTestCase "+Calendar.getInstance().getTime().toLocaleString()+'\n'+buf.toString();
	}


	public void execute()  {
		try{
			start_recordSession(timer,url_to_Start);
		}catch(Exception e){
			print1(e.getMessage());
		}finally{
			//DriverFactory.cleanUp();
		}

	}


	public String generateReplaceString(){
		StringBuffer buf = new StringBuffer();

		buf.append("	$(document).ready(function(){");
		buf.append("	  $('select').change(function(){");
		buf.append("	    console.log('Select  '+ $(this).attr('id')+'   '+ $(this).val());");
		buf.append("	  });");
		buf.append("	});");
		buf.append("	$(document).ready(function(){");
		buf.append("	  $('a').click(function(){");
		buf.append("	  if($(this).attr('id')== ''){							");	
		buf.append("	    console.log('Click  '+ $(this).attr('id'))");
		buf.append("	  }else{							");
		buf.append("	    console.log('clickByTagAndAttribute    a    href    '+ $(this).attr('href'))");
		buf.append("	  }});");
		buf.append("	});");
		buf.append("	$(document).ready(function(){");
		buf.append("	  $('input').change(function(){");
		buf.append("	  if($(this).attr('type') =='text'){							");
		buf.append("	   console.log('Enter Text  '+ $(this).attr('id')+'   ');						");
		buf.append("	  }else{							");
		buf.append("	    console.log('Click  '+ $(this).attr('id')+'   ');");
		buf.append("	  }});");
		buf.append("	});");
		buf.append("	$(document).ready(function(){");
		buf.append("	  $('input').click(function(){");
		buf.append("	  if($(this).attr('type') =='text'){							");
		buf.append("	  }else{							");
		buf.append("	    console.log('Click  '+ $(this).attr('id')+'   ');");
		buf.append("	  }});");
		buf.append("	});");
		buf.append("	$(document).ready(function(){");
		buf.append("	  $('span').dblclick(function(){");
		buf.append("	    console.log('Page Should Contain   '+ $(this).text());");
		buf.append("	  });");
		buf.append("	});");
		return buf.toString()
				;

	}

	public void setScriptConstraint(ScriptConstraint constraint) {
		timer = constraint.timer;
		url_to_Start= constraint.startUrl;
		
	}

	@Override
	public String getFailureMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFailed() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	

}

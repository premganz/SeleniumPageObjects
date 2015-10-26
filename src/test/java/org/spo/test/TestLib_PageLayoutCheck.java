package org.spo.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.Lib_PageLayout_Processor;
import org.spo.fw.web.ServiceHub;



public class TestLib_PageLayoutCheck {
	ServiceHub robot = new ServiceHub();		
Logger1 log = new Logger1("TestLib_PageLayoutCheck");

	public void setUp() throws Exception{}

	public void tearDown(){}

	@Test
	public void testExceptionReporting() throws Exception{
		try{
			throw new NullPointerException();
		}catch(Exception e){
			log.error(e);
		}

	}
	
	@Test
	public void testDiff() throws Exception{
		try{
			  // read in lines of each file
	   
		}catch(Exception e){
			log.error(e);
		}

	}
	
	
	@Test
	//FIXME //TODO 
	public void testRegex() throws Exception{
		String line1="***section:sss***";
		if(line1.startsWith("***") && line1.matches("^(([\\*]{3})([Ss]ection\\:[\\w\\W0-9]{0,100})([\\*]{3}))$")){

		}else{
			Assert.assertTrue(false);
		}
		line1="***Break***";
		if(line1.startsWith("***") && line1.matches("^(([\\*]{3})([Bb]reak)([\\*]{3}))$")){

		}else{
			Assert.assertTrue(false);
		}
		line1="  sayHello #Comment  ";
		Lib_PageLayout_Processor check = new Lib_PageLayout_Processor(null);
		
		//Assert.assertTrue(check.((line1)).equals("sayHello"));
		line1="CQ:04/01/2015-06/30/2015";
		String tr3 = "tr[3]";
		tr3=tr3.replaceAll("tr\\[[0-9]\\]", "tr");
		 Assert.assertEquals(tr3,"tr");
		Assert.assertTrue(line1.matches("^((.*)CQ:[0-9]{2}/01/2015-[0-9]{2}/3[01]/2015(.*))$"));
		
		String pcs ="Image Name                     PID Session Name        Session#    Mem Usage========================= ======== ================ =========== ============IEDriverServer.exe            3892                            1     13,052 KIEDriverServer.exe           29756                            1     11,928 KIEDriverServer.exe           17080                            1     13,324 KIEDriverServer.exe            7600                            1     11,848 KIEDriverServer.exe           20672                            1     13,232 KIEDriverServer.exe           21012                            1     11,944 KIEDriverServer.exe           24608                            1     13,272 KIEDriverServer.exe           25372                            1     12,000 KIEDriverServer.exe            9856                            1     13,296 KIEDriverServer.exe           18968                            1     13,220 KIEDriverServer.exe           25888                            1     13,560 KIEDriverServer.exe            7156                            1     13,496 KIEDriverServer.exe           26016                            1     13,492 KIEDriverServer.exe           22044                            1     13,628 KIEDriverServer.exe           18216                            1     12,328 K";
		
		Pattern p = Pattern.compile("[\\s]IEDriverServer.*[\\s]");
		Matcher m = p.matcher(pcs);
		String[] pids =pcs.split("IEDriverServer.exe");
		for(int i =0;i<pids.length;i++){
			String pid_frag = pids[i].trim();
			String[] pid_frag_arr = pid_frag.split("[\\s]");
			System.out.println(pid_frag_arr[0]);
			for(int j=0;j<pid_frag_arr.length;j++){
				//System.out.println(pid_frag_arr[0]);
			}
		}
		System.out.println("PID:"+pids);
		
		//System.out.println("PID:"+pids[0]);
		
		
		
//		if(m.find()){
//			pid=m.group(1).replaceAll("[a-zA-Z\\.]","");
//			System.out.println("PID:"+pid);
//		}
		
	}
	
	
	@Test
	public void testRegex1() throws Exception{
		String a = "9 UnPublishedAuthor_23Drafts (402as) is not valid  01   A  1 ";
		String b = "^(.*)(.*)UnPublishedAuthor_23Drafts\\([0-9]{1,3}as\\)isnotvalid01A1(.*)$";
		
		a = "xxx";
		b = "([!H][!o][][])";
		boolean isMatches = false;
		try{
			isMatches = a.matches(b);
		}
		catch(Exception e){e.printStackTrace();}
		System.out.println("Matches : "+isMatches);
	}
	
	


	@Test
	public void checkLogCompare() throws Exception{
		//TODO
		List<String>  a = new ArrayList<String>();
		a.add("Form has been submitted		Author:   hello");
		a.add("ISBN: 123456555555   four five 		Reason for using Author(check all that apply)");
		a.add("***Section:1***");
		a.add("1 - Chriton(Jurassic Park) allergy		 ");
		a.add("***end***");
		
	}

}

package org.spo.test.runner;

import org.apache.commons.lang.StringUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
/*
 * AS OF NOW APPLICATION IS SINGLE THREADED, CONSIDERING IE DREIVERFACTORY IS SINGLETHREADED
 */

public class JunitProxyRunner {
	private static boolean ignoreCustomStrategy=false;
	public static String lock=StringUtils.EMPTY;


	public static void main(String[] args) throws Exception {
		System.getProperties().put("webdriver.ie.driver", "C:/IEDriverServer.exe");
		System.getProperties().put("message-robot", "dev");
		JUnitCore core = new JUnitCore();
		try {
		Class test = Class.forName(TestToRunInEclipse.class.getName());
		Result result = core.run(test);
		System.out.println(result.toString());
		}catch ( Exception  e) {
			e.printStackTrace();
		}

	}
}

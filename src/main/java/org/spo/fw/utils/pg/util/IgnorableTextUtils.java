/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.log.Log;
import org.spo.fw.log.Logger1;
import org.springframework.context.support.StaticApplicationContext;

import sun.util.logging.resources.logging;

public class IgnorableTextUtils {
	public static  String IGNORABLE_HDR="Help";
	
	public static  List<String> IGNORABLE_STRINGS_L1= new ArrayList<String>();
	public static  List<String>  IGNORABLE_STRINGS_L2=new ArrayList<String>();//general
	static Logger1 log = new Logger1("IgnorableTextUtils");
	static{
		reset();
	}
	//IGNORING TRIVIAL CHARACTERS
	public static void reset(){
		IGNORABLE_STRINGS_L1= new ArrayList<String>();
		IGNORABLE_STRINGS_L2=new ArrayList<String>();
		//for regex
		IGNORABLE_STRINGS_L1.add("[\\n]");
		IGNORABLE_STRINGS_L1.add("[\\s]");
//		IGNORABLE_STRINGS_L1.add("\\,");
		
		//CAlling reset would add these defaults, 
		IGNORABLE_STRINGS_L2.add(":");
		IGNORABLE_STRINGS_L2.add("\\*");
		IGNORABLE_STRINGS_L2.add("[\\s]");
		IGNORABLE_STRINGS_L2.add("[\\n]");
		IGNORABLE_STRINGS_L2.add("\\,");
	}
	
	
	public static void prependToIgnorableStrings2(List<String> customIgnorables){
		List<String> temp = new ArrayList<String>();
		temp.addAll(IGNORABLE_STRINGS_L2);
		IGNORABLE_STRINGS_L2.clear();
		IGNORABLE_STRINGS_L2.addAll(customIgnorables);
		IGNORABLE_STRINGS_L2.addAll(temp);
	
	}
	
	public static String util_processContent(String input, List<String> ignorables){
		
		String noWhites = StringUtils.deleteWhitespace(input);
		StringBuffer buf = new StringBuffer();
		for(String x:ignorables ){
			noWhites = noWhites.replaceAll(x,StringUtils.EMPTY);
			buf.append(x);
		}
		noWhites = noWhites.replaceAll(IGNORABLE_HDR,StringUtils.EMPTY);
//		log.trace("Trivial Characters are Ignored namely "+buf.toString());
		return StringUtils.deleteWhitespace(noWhites);
	}
	
	
	public static String cleanRegexChars(String input){		
		String specialChars2 = "[`~\\!\\$%^\\&\\*\\-(\\)\\+\\[\\]\\\\;\',\\./{}|\\:\\\"\\?\\>\\<]";
		return input.replaceAll(specialChars2, "");
	}
	
	
	
}

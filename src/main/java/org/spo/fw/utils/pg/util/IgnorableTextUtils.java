package org.spo.fw.utils.pg.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.StaticApplicationContext;

public class IgnorableTextUtils {
	public static  String IGNORABLE_HDR="Help";
	
	public static  List<String> IGNORABLE_STRINGS_L1= new ArrayList<String>();
	public static  List<String>  IGNORABLE_STRINGS_L2=new ArrayList<String>();//general
	
	static{
		reset();
	}
	
	public static void reset(){
		IGNORABLE_STRINGS_L1.add("[\\n]");
		IGNORABLE_STRINGS_L1.add("[\\s]");
		
		IGNORABLE_STRINGS_L2.add("[\\n]");
		IGNORABLE_STRINGS_L2.add(":");
		IGNORABLE_STRINGS_L2.add("\\*");
		IGNORABLE_STRINGS_L2.add("[\\s]");
		
	}
	
	public static String util_processContent(String input, List<String> ignorables){
		
		String noWhites = StringUtils.deleteWhitespace(input);
		for(String x:ignorables ){
			noWhites = noWhites.replace(x,StringUtils.EMPTY);
		}
		noWhites = noWhites.replaceAll(IGNORABLE_HDR,StringUtils.EMPTY);
		return StringUtils.deleteWhitespace(noWhites);
	}
	
	
	public static String cleanRegexChars(String input){		
		String specialChars2 = "[`~\\!\\$%^\\&\\*\\-(\\)\\+\\[\\]\\\\;\',\\./{}|\\:\\\"\\?]";
		return input.replaceAll(specialChars2, "");
	}
	
	
	
}

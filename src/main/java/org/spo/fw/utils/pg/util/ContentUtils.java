package org.spo.fw.utils.pg.util;

import org.apache.commons.lang3.StringUtils;

public class ContentUtils {
	private static final String IGNORABLE_HDR="Help|Support|SignOutSelectanHCO:GeneralHospitalGreatCareHospitalHCOID:2222HomeConcurrentCreateUploadAbstractApproveDataRe-abstractReportsSettingsResources";
	
	public static final String[] IGNORABLE_STRINGS_L1={"[\\n]","[\\s]"};//regex cases
	public static final String[] IGNORABLE_STRINGS_L2={"[\\n]",":","//*","[\\s]"};//general
	public static String util_processContent(String input, String[] ignorables){
		String noWhites = StringUtils.deleteWhitespace(input);
		for(String x:ignorables ){
			noWhites = noWhites.replace(x,StringUtils.EMPTY);
		}
		noWhites = noWhites.replaceAll(IGNORABLE_HDR,StringUtils.EMPTY);
		return StringUtils.deleteWhitespace(noWhites);
	}
	
	
	public static String cleanRegexChars(String input){		
		String specialChars2 = "[`~\\!\\$%^\\&\\*\\(\\)\\+\\[\\]\\\\;\',\\./{}|\\:\\\"\\?]";
		return input.replaceAll(specialChars2, "");
	}
}

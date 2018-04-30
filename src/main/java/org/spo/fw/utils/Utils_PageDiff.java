package org.spo.fw.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.spo.fw.log.Logger1;


public class Utils_PageDiff {
	//Comparing texts in contains mode
	/**
	 * @param theLesser
	 * @param theMore
	 * @return
	 */

	Logger1 log = new Logger1(getClass().getSimpleName());
	public String compareTexts(String theLesser, String theMore){
		try{
			String escapedMinimalEndSeq = "";
			if(theMore.contains(theLesser)){
				return "";
			}else{

				int len = theLesser.length();
				String minimalStartSeq = "";

				for(int i =2;i<100;i++){
					if(len>i){
						minimalStartSeq = theLesser.substring(0,i);	
					}else{
						break;
					}
					if(theMore.split(minimalStartSeq).length ==2){
						break;
					}
				}
				String inv_theMore = StringUtils.reverse(theMore);
				String inv_theLesser = StringUtils.reverse(theLesser);
				String minimalEndSeq= "";
				for(int i =2;i<inv_theLesser.length();i++){
					if(len>i){
						minimalEndSeq = inv_theLesser.substring(0,i);	
					}else{
						break;
					}
					escapedMinimalEndSeq = new String(); 
					if(StringUtils.split(inv_theMore, minimalEndSeq).length ==2){
						break;
					}
				}


				int start = theMore.indexOf(minimalStartSeq);
				int end = inv_theMore.indexOf(escapedMinimalEndSeq);
				int real_end = theMore.length()-end;

				String section = theMore.substring(start, real_end);
				String diff1 =StringUtils.difference(theLesser, section );
				String diff2 = StringUtils.difference(section, theLesser);
				String message = StringUtils.difference(diff1, diff2 )+'\n'+"NOT EQUAL"+ '\n'+StringUtils.difference(diff2, diff1);

				System.out.println(message);

				return StringUtils.difference(section, theLesser );

			}
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}

	}

	public static String longTextConsoleLogHelper(String pageText) {
		int len = pageText.length();
		int segSize=4000;
		StringBuffer toDump = new StringBuffer();
		int seg = len/segSize;
		int segReminder=len%segSize;
		String temppageText="";
		if(len>segSize) {
			for(int i=1;i<seg+1;i++) {
				temppageText=pageText.substring(segSize*(i-1), segSize*i);
				toDump.append(temppageText+'\n');
				//log.debug(temppageText);
			}
			temppageText=pageText.substring(segSize*(seg), segSize*(seg)+segReminder);
			toDump.append(temppageText+'\n');
			//log.debug(temppageText);
			//log.info(toDump.toString());
			//						System.out.println(pageText);
			//						System.out.println(StringUtils.isAsciiPrintable(pageText));
			//						System.out.println("Length of pagetext == "+pageText.length());
			//log.info('\n'+"for the "+'\n'+toDump.toString());
			//new Utils_PageDiff().compareTexts(fileTextMdf, pageTextMdf);
			return toDump.toString();
		}else {
			return pageText;
		}

	}

	public String getDiff(String theLess, String theMore){
		try{
			//pickup 5 characters from the lesser and try to ease common areas
			final int CONST_LEN=15;
			int lenOfLesser = theLess.length();
			if(lenOfLesser<CONST_LEN){
				return "ERROR: Input invalid";
			}
			String key = theLess.substring(0,CONST_LEN);
			int iMax = theLess.length()-CONST_LEN;
			int lenToChop = CONST_LEN;

			String commonString ="";

			for(int i = 0; i<lenOfLesser-lenToChop;i++){
				key = theLess.substring(i,i+lenToChop);
				if(theMore.contains(key)){
					//try increasing lenToChop								
					while(lenToChop<lenOfLesser){
						String temp="";
						if(lenOfLesser>i+lenToChop){
							temp= theLess.substring(i,i+lenToChop);
						}else{
							break;
						}
						if(!theMore.contains(temp)){
							break;
						}else{
							commonString = temp;
							lenToChop++;
						}
					}
					theMore = theMore.replace(commonString, "---");
					theLess = theLess.replace(commonString, "---");
					//System.out.println("replacing "+commonString);					
					lenOfLesser = theLess.length();					
					i=0;
					lenToChop=CONST_LEN;				
					continue;

				}else{

					continue;
				}
			}
			//String result = compareTexts(theLess, theMore);

			log.info("AREAS OF DIFFERENCE:"+'\n'+ theLess.replace("(.*)", "").replace("//",""));
			//getDiff(theLess, theMore);

			return "AREAS OF DIFFERENCE:"+'\n'+ theLess.replace("(.*)", "").replace("//","");


		}catch(Exception e){
			e.printStackTrace();return "";
		}


	}

}

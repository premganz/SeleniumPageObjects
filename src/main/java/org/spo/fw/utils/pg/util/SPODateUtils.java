package org.spo.fw.utils.pg.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SPODateUtils {
	
	
	public static String getDateAsString(String zone, String format){
		DateTimeZone zone1 = DateTimeZone.forID(zone);
		DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
		java.util.Date juDate = new Date();
		DateTime dt = new DateTime(juDate, zone1);	
		return fmt.print(dt);
		
	}
	
	
	//approxExpression = 1/Minute
	public static String getApproxDateAsRegex(String zone, String format, int val, TimeUnit unit){
		DateTimeZone zone1 = DateTimeZone.forID(zone);
		DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
		java.util.Date juDate = new Date();		
		DateTime dt = new DateTime(juDate, zone1);
		DateTime dt1 = new DateTime(juDate, zone1);
		StringBuffer buf = new StringBuffer();
		buf.append("(");	
		buf.append(ContentUtils.cleanRegexChars(fmt.print(dt))+"|");	
//		if(unit.equals(TimeUnit.MINUTES)){
//			buf.append(ContentUtils.cleanRegexChars(fmt.print(dt.plusMinutes(val)))+"|"+ContentUtils.cleanRegexChars(fmt.print(dt.minusMinutes(val)))+"|");	
//		}
		if(unit.equals(TimeUnit.DAYS)){
			
			for(int i=0;i<val;i++){
				buf.append(ContentUtils.cleanRegexChars(fmt.print(dt.plusDays(i)))+"|");
			}
			for(int i=0;i<val;i++){
				buf.append(ContentUtils.cleanRegexChars(fmt.print(dt.minusDays(i)))+"|");
			}
				
		}
	if(unit.equals(TimeUnit.MINUTES)){
			
			for(int i=1;i<val+1;i++){
				buf.append(ContentUtils.cleanRegexChars(fmt.print(dt.plusMinutes(i)))+"|");
			}
			for(int i=1;i<val+1;i++){
				buf.append(ContentUtils.cleanRegexChars(fmt.print(dt1.minusMinutes(i)))+"|");
			}
				
		}
		buf.append(ContentUtils.cleanRegexChars(fmt.print(dt)));	
		buf.append(")");	
		return buf.toString();
		
	}

	

}



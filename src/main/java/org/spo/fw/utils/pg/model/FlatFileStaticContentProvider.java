package org.spo.fw.utils.pg.model;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.itf.StaticContentProvider;

public class FlatFileStaticContentProvider implements StaticContentProvider {
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	@Override
	public List<String> getContent(String expression) {
	//TODO to implement
		return null;
	}

	
	private String substituteDynaVals(String x, Properties properties){
		//substituting dynaValues
		//TODO: Vulnerable code, relies on null value for flow control.
		if(properties==null){
			return x;
		}
		try{
			x.replace("*", StringUtils.EMPTY);//Getting rid of stars
			if(x.contains("$")){
				log.trace("Dynaval Substitution in  : "+x);
				StringBuffer xReformedBuf = new StringBuffer();
				String[] xArray = x.split("$");
				for(String splitted:xArray){
					String keyName = splitted.substring(splitted.indexOf("{")+1,splitted.indexOf("}"));
					String actualValue = properties.getProperty(keyName);
					splitted=splitted.replace("{", StringUtils.EMPTY);
					splitted=splitted.replace("}", StringUtils.EMPTY);
					splitted=splitted.replace("$", StringUtils.EMPTY);
					splitted= splitted.replace(keyName, actualValue);
					xReformedBuf.append(splitted);

				}

				x = xReformedBuf.toString();

			}}catch(Exception e){
				System.out.println("Error encountered in dynaval line "+x);
			}
		return x;
	}

}

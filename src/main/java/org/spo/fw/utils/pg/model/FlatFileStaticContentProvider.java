/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.web.ServiceHub;

public class FlatFileStaticContentProvider implements StaticContentProvider {
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	@Override
	public List<String> getContent(String expression, ServiceHub kw) {
		String filePath =expression;
		String result="";
		try {
			result=FileUtils.readFileToString(new File(filePath), "UTF-8");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String folder = expression.substring(0,expression.lastIndexOf("/") );
		String folderName = folder.substring(folder.lastIndexOf("/") , folder.length());
		Properties p = new Properties();
		File f = new File(folder+"/"+folderName+".properties");
		if(f.exists()){
			try {

				p.load(new BufferedReader(new FileReader(f)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				log.error("The file system.properties need to be present in your working directory defined by your strategy.textFilesPath");

			}catch (IOException e1) {
				//e1.printStackTrace();
				log.error("The file system.properties there but  not read");

			} 
			for (String name : p.stringPropertyNames()) {
				String value = p.getProperty(name);
				System.setProperty(name, value);
			}
			result = substituteDynaVals(result, p);
		}
		ArrayList<String> resultList= new ArrayList<String>();
		resultList.addAll(Arrays.asList(result.split("\n")));
		return resultList;

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

package org.spo.fw.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

import org.spo.fw.web.KeyWords;


/**
 * 
 * 
 * @author prem
 * Can use this for major changes find replace, method list fetches, 
 * for simple replace use 
 * grep and  perl -p -i -e "s/DX_and_PX_DisplayControl_Show/Dx + Px/g" Check_Save_Functionality.txt
 * grep and  perl -p -i -e "s/org.spo.fw.web.KeyWords/org.spo.fw.web.KeyWords/g" *.txt
 
 *
 */

public class FileManipulationScripts {
	public static StringBuffer walkin(File dir,StringBuffer buf) {
		File listFile[] = dir.listFiles();
			if (listFile != null) {
				for (int i=0; i<listFile.length; i++) {
					if (listFile[i].isDirectory() ) {
						walkin(listFile[i],buf);
					} else if(listFile[i].getName().contains("txt")){
						BufferedReader reader = null;
						try{
							reader = new BufferedReader(new FileReader(listFile[i]));
							while ((reader.readLine()) != null) {
								buf.append(reader.readLine()+'\n');
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						finally{
							try {
								reader.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}
				
			}
			return buf;
		}
	
	public static void switchLineValue(File dir, String findValue ,String replaceValue) {
		File listFileOuter[] = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		}
);
		for(int m=0;m<listFileOuter.length;m++){
		File listFile[] = listFileOuter[m].listFiles();

		BufferedReader reader = null;BufferedWriter writer = null;
		try{
			if (listFile != null) {
				for (int i=0; i<listFile.length; i++) {
					StringBuffer buf = new StringBuffer();
					if (listFile[i].isDirectory() ) {
						//switchLineValue(listFile[i],findValue,replaceValue);
					} else if(listFile[i].getName().contains("txt")){
						reader = new BufferedReader(new FileReader(listFile[i]));
						String fileName = listFile[i].getName();
						System.out.println("File to replace "+fileName);
						String line = "";
						while ((line = reader.readLine()) != null ) {
							
							//System.out.println("Line to replace "+line);
							if(line.contains(findValue)){
								String altered = line.replace(findValue, replaceValue);
								buf.append(altered+'\n');
							}else{
								buf.append(line+'\n');
							}

						}
						reader.close();
					}
					FileWriter writer1 = new FileWriter(new File(listFile[i].getAbsolutePath().replace("txt", "txt")));
					writer1.write(buf.toString());
					writer1.close();
				}

			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}
	}
	static void printMethods(StringBuffer buf){
		Method[] m = KeyWords.class.getMethods();
		for (int k =0;k<m.length;k++){
			String x = m[k].getName();
		 if(buf.toString().replaceAll(" ", "").toLowerCase().contains(x.toLowerCase())){
 	    	System.out.println("Method: "+x+":::::::::::"+" ++++++++++++ is used");
 	    	
 	    }else{
 	    	//System.out.println("Method: "+x+":::::::::::"+" ------------- is NOT used");
 	    }
	}
	}
	
	
	
	public static void main(String[] args) throws Exception {
	
		

		
	}
	

}

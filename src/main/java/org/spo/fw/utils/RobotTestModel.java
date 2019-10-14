/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import org.dom4j.Element;
//import org.dom4j.Node;
//import org.dom4j.Document;

public class RobotTestModel {
	
	
public static final String FOLDER_KW_TXT="__Folder_keywords__.txt"	;
	
static List<Folder> folders;
static boolean test1; 
public static String baseDir;//IN
public static String destDir;//OUT
private static int masterIdReg;
static String rootDir = "C:\\Scripts\\";
	/**
	 * <tests>
	 * 
	 * <folder name="fldr">
	 * 	<testsuite name="tc1">
	 * 		<keyword name="">
	 * 			<step/>
	 * 		<Keyword>
	 * 		<test name="">
	 * 			<step/>
	 * 		<test>
	 * </folder>
	 * </tests>
	 *
	 * OBSOLETE AS OF SPO 1.1.0
	 * 
	 * Iterate and reach a testsuite  file
	 * Things todo there
	 * 	a. Load keywords from xml first
	 * 	b. If first line of a test file is a keyword call, then put it in a map, testNumber=keywordname=line, add a new keyword
	 * 		
	 * 	a. Create a map of keyword calls in a test 
	 *  b. Check if the tests call the same keyword  
	 * 
	 * Use Cases:
	 * Pull out common keywords into common directory. 
	 * Pull out repeated test cases into a common directory.
	 * If keyword calls intersect exactly in the same lines, then merge the tests, move keyword calls to new setStage testcase. 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static void execute(){
		try {
			RunRefactor();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void walkFolder() {
		File dir = new File( rootDir.concat("Web1\\"));
		File listFile[] = dir.listFiles();
			if (listFile != null) {
				for (int i=0; i<listFile.length; i++) {
					if (listFile[i].isDirectory() ||! listFile[i].getName().contains(".") ) {
					
						RobotTestModel.baseDir = rootDir.concat("Web1\\").concat(listFile[i].getName());
						RobotTestModel.destDir = rootDir.concat("Web2\\").concat(listFile[i].getName()).concat("\\");
						print1("Working with "+baseDir);	
						execute();
					}else{
						print1("not a dir "+listFile[i].getName());
					}
				}
			}
		
						
		}
	
	public static void RunRefactor() throws Exception{

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("folder");
		doc.appendChild(rootElement);
		File dir = new File(baseDir);
		File listFile[] = dir.listFiles();
		if(test1)
			doc = fileToXml(new File(baseDir),doc);

		else{
			// root elements

			for (int i=0; i<listFile.length; i++) {
				if (!listFile[i].isDirectory() ) {
					TestSuite ts =  new TestSuite();
					ts.name = listFile[i].getName();
					doc = fileToXml(listFile[i],doc);
					//dataFolder.testSuites.add(ts);
				}

			}
		}
		printXML1(doc);
		//Document doc2 =  extractCommonKeywords(doc,new File(destDir));//TODO
		Document doc2 = mergeTestCases(doc);
		doc2 = mergeTestCases2(doc2);
		doc2 = mergeTestCases(doc2);
		doc2 = mergeTestCases2(doc2);
		printXML1(doc2);
		xmlToFile(doc2, new File(destDir));
	}
	
	public static Document getTestFileAsXml(File f) throws  Exception{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("folder");
		doc.appendChild(rootElement);
		doc = fileToXml(f,doc);
		
		doc.getElementsByTagName("BUTTON");
		
		return doc;
	}
	
	
	
	
	public static Document fileToXml(File f,Document doc){
		Map<String,List<TestCase>> m = new LinkedHashMap<String,List<TestCase>>();
		StringBuffer buf = new StringBuffer();
		
		try{
		final BufferedReader reader = new BufferedReader(new FileReader(f));String ss;
			while ((ss = reader.readLine()) != null) {
				buf.append(ss+'\n');
			}
			List<String> lines = new ArrayList<String>();
			Collections.addAll(lines, buf.toString().split("\n"));

			
			Node rootElementBase = doc.getLastChild();
			Element rootElement = doc.createElement("testsuite");
			rootElement.setAttribute("value", f.getName());
			rootElementBase.appendChild(rootElement);
			
			for(int i =0;i<lines.size();i++){
				String x = lines.get(i);
				if("".equals(x))
					continue;
				
				if(x.contains("**")){
					Element category = doc.createElement("category");
					category.setAttribute("value", x);
					String value = "testcase";
					if(x.contains("Keyword")){
						value="keyword";
					}						
					else if (x.contains("Settings")){
						value ="settings";
					}						 
					category.setAttribute("type", value);
					category.setAttribute("id", String.valueOf(masterIdReg+i));
					category.setIdAttribute("id",true);
					rootElement.appendChild(category);
					//printXML(rootElement,"");
				}
				else if(!x.startsWith("  ") || !doc.getLastChild().getLastChild().getLastChild().hasChildNodes()){
					//int size= doc.getElementsByTagName("category").getLength();
					//Node node = doc.getElementsByTagName("category").item(size-1);
					Node node = doc.getLastChild().getLastChild().getLastChild();	
					Element testcase = doc.createElement("testcase");
					testcase.setAttribute("value", x);
					//testcase.setAttribute("type", x);	
					testcase.setAttribute("id", String.valueOf(masterIdReg+i));
					testcase.setIdAttribute("id",true);
					node.appendChild(testcase);
				}
				else if(x.startsWith("  ")){					
					Node node = doc.getLastChild().getLastChild().getLastChild().getLastChild();
					Element stmt = doc.createElement("statement");
					if(x.trim()!=StringUtils.EMPTY){
						stmt.setAttribute("value", x.trim());						
					}
					//elem.setAttribute("type", x);	
					stmt.setAttribute("id", String.valueOf(masterIdReg+i));
					stmt.setIdAttribute("id",true);
					
					node.appendChild(stmt);
				}
				
			}
			masterIdReg = masterIdReg+lines.size()-1;
			//printXML(rootElement,"");
			printXML1(doc);
			//String xml = new SimpleXml(doc).toString();
			

		}catch (Exception e){
				e.printStackTrace();
		}
		
		return doc;
	}
	
	 static class Folder{
		String name;
		static List<TestSuite> testSuites = new ArrayList<TestSuite>();
		
		public String toString(){
			return name+": "+'\t'+testSuites.toString()+'\n';
		}
	}
	
	
	 static class TestSuite{
		String name;
		Map<String,List<TestCase>> blockMap;
		public String toString(){
			return "Suite "+name+": "+'\t'+blockMap.toString()+'\n';
		}
	}
	
	 static class TestCase{
		String name;
		List<TestStep> steps= new ArrayList<TestStep>();
		public String toString(){
			return "TestCase  "+name+": "+'\t'+steps.toString()+'\n';
		}
		
	}
	 static class TestStep{
		String type="";
		String statement;	
		public String toString(){
			return "Stmt  "+type+": "+'\t'+statement+'\n';
		}
	}
	
	 
	 public static void main(String[] args) {
		 test1 = true;boolean test2 = false;
		
		 
		 
		 if(test2){
			//baseDir = baseDir1
			 walkFolder();
			 
		 }
		 
		 
		 else if(test1){
			 RobotTestModel.baseDir = "";
			 RobotTestModel.destDir = "";
			 
		 }else{
			 RobotTestModel.baseDir = "";
			 RobotTestModel.destDir = "";
			 
		 }
			RobotTestModel.execute();
			
	}
	 
	public static void print(String x ){
		System.out.println(x);
	}
	
	public static void print1(String x ){
		System.err.println(x);
	}
	
	
	private static void printXML1(Document doc ) throws Exception{
		OutputFormat format = new OutputFormat(doc);
		format.setIndenting(true);
		XMLSerializer serializer = new XMLSerializer(System.out, format);
		serializer.serialize(doc);
	}
	
	
	//Refactor by extracting repeated keywords to common file.
	public static Document extractCommonKeywords(Document doc, File rootDir) throws Exception{
		//extracts the keywords used commonly across testsuites.
		NodeList suiteLst = doc.getElementsByTagName("testsuite");
		Map<String, String>  mapKwInScope = new LinkedHashMap<String,String>();
		List<String> lstKeywrodsInScope = new ArrayList<String>();
		//Iterating thru test suites.
		for(int i  = 0;i <suiteLst.getLength();i++){
			Node testSuiteNode = suiteLst.item(i);
			NodeList categoryPerSuiteLst = testSuiteNode.getChildNodes();

			for(int j  = 0;j <categoryPerSuiteLst.getLength();j++){
				Element categoryNode = (Element)categoryPerSuiteLst.item(j);
				if(categoryNode.getAttribute("type").equals("keyword")
						//||categoryNode.getAttribute("type").equals("testcase")TODO: Not needed at present, need to investigate
						){
					NodeList keywordTCLst = categoryNode.getChildNodes();

					//Iterating thru each tst suites keywords	
					for(int k  = 0;k <keywordTCLst.getLength();k++){
						Element testSuiteNode2 = (Element)keywordTCLst.item(k);
						lstKeywrodsInScope.add(testSuiteNode2.getAttribute("value").toString().trim());
						mapKwInScope.put(testSuiteNode2.getAttribute("id"),testSuiteNode2.getAttribute("value").toString().trim());
					}
				}

			}


		}
		print(mapKwInScope.toString());
		
		
		//PART2 : To pull out the comon Keywords by Id and adda  new testSuite contatining it 
		
		
		Iterator<String> iter = mapKwInScope.keySet().iterator();		
		Map<String, List<String>> mapInverseKw = new LinkedHashMap<String, List<String>>();
		while (iter.hasNext()){
			String key = iter.next();
			String keywordName = mapKwInScope.get(key);			
			if(mapInverseKw.containsKey(keywordName)){
				mapInverseKw.get(keywordName).add(key); 
			
			}else{
				List<String> lstVal = new ArrayList<String>();
				lstVal.add(key);
				mapInverseKw.put(keywordName, lstVal);
			}
		
		}
		//printXML(rootElement,"");
		
		//Iterate through inverse map and populate the new testSuite
		
		Element testSuite = doc.createElement("testsuite");
		testSuite.setAttribute("value",FOLDER_KW_TXT);

		Element category = doc.createElement("category");
		category.setAttribute("value", "***keywords***");
		category.setAttribute("type", "keywords");
		print(mapInverseKw.toString());
		Iterator<String> iterInv = mapInverseKw.keySet().iterator();
		while(iterInv.hasNext()){
			String key = iterInv.next();
			List lstNode = mapInverseKw.get(key);
			if(lstNode.size()>1){
				for(int n = 0;n<lstNode.size();n++ ){
					String id = mapInverseKw.get(key).get(n);
					XPath xpath = XPathFactory.newInstance().newXPath();
					// XPath Query for showing all nodes value
					XPathExpression expr = xpath.compile("//testcase[@id='"+id+"']");
					Object result = expr.evaluate(doc, XPathConstants.NODESET);
					NodeList nodes = (NodeList) result;
					Node toRem = (Node)nodes.item(0);

					if(n==0){
						category.appendChild(toRem.cloneNode(true));
					}

					toRem.getParentNode().removeChild(toRem);
					//Pulling common test cases in the same manner as keywords.
//					Element commonTestCaseCallSlot = (Element)toRem.getParentNode();
//					Element commonCall = doc.createElement("testcase");
//					commonCall.setAttribute("value", "Common Test Cases");
//					Element commonCallStmt = doc.createElement("statement");
//					commonCallStmt.setAttribute("value", "execute common testcases");
//					commonCall.appendChild(commonCallStmt);
//					commonTestCaseCallSlot.appendChild(commonCall);
					

				}
			}
		}
		
		testSuite.appendChild(category);

		doc.getFirstChild().appendChild(testSuite);
		
		
		//PART3 : Inside each testsuite, check if any testcase contains keyword calls that form the first line.  
		//delegated to mergetTestcases
		
		
		
		return doc;
		
	}
	
	
	public static Document mergeTestCases(Document doc) throws Exception{
		NodeList suiteLst = doc.getElementsByTagName("testsuite");

		//Iterating thru test suites.
		for(int i  = 0;i <suiteLst.getLength();i++){
			Node testSuiteNode = suiteLst.item(i);
			NodeList categoryPerSuiteLst = testSuiteNode.getChildNodes();

			for(int j  = 0;j <categoryPerSuiteLst.getLength();j++){
				Element categoryNode = (Element)categoryPerSuiteLst.item(j);
				if(categoryNode.getAttribute("type").equals("testcase")){
					NodeList TCLst = categoryNode.getChildNodes();

					//Iterating thru each tst suites keywords	
					for(int k  = 0;k <TCLst.getLength();k++){
						Element testCaseNode = (Element)TCLst.item(k);
						NodeList lstStmtNode =  testCaseNode.getChildNodes();
						Element stmtNode= null;
						String stmtTrimmed = "";
						if(testCaseNode.getAttribute("type").contains("Merge")){//TODO extract keyword
							continue;
						}
						if(testCaseNode.hasChildNodes()){
							stmtNode= (Element)lstStmtNode.item(0);
							stmtTrimmed = stmtNode.getAttribute("value").trim();	
						}						
						Element testCaseNodeNext = testCaseNode;
						//get first line and search forward
						for(int n  = k+1;n <TCLst.getLength();n++){
							if(!testCaseNodeNext.hasChildNodes()){
								break;
							}
							testCaseNodeNext = (Element)TCLst.item(n);
							NodeList lstStmtNodeNeigh =  testCaseNodeNext.getChildNodes();	
							Element stmtNodeNeigh= (Element)lstStmtNodeNeigh.item(0);
							//TODO Fix for strange issues due to some testcases being created without stmt.
							if(stmtNodeNeigh==null){
								print("Found a null"+testCaseNodeNext.getAttribute("id"));
								continue;
								
							}
							
							String stmtTrimmedNeigh = stmtNodeNeigh.getAttribute("value").trim();
							if(stmtTrimmed.equals(stmtTrimmedNeigh)){
								int holder=1;

								//on getting lead dive deeper and prospect
								for (int m=1;m<lstStmtNode.getLength();m++){
									Element stmtNode1= (Element)lstStmtNode.item(m);
									String stmtTrimmed1 = stmtNode1.getAttribute("value").trim();
									if(lstStmtNode.getLength()<m){
										break;
									}								
									try{
										//Element stmtNodeNeigh1 = (Element)lstStmtNode.item(m);
										//String stmtTrimmedNeigh1 = stmtNodeNeigh1.getAttribute("value").trim();		
									}catch(Exception e){
										System.out.println("HIHIHI"+stmtNodeNeigh);
										printXML1((Document)stmtNodeNeigh);
										throw e;
									}
									
									
									if(stmtTrimmed1.equals(stmtTrimmedNeigh)){
										holder++;
									}else{
										break;}
								}
								String value = "Merge With TC __"+testCaseNode.getAttribute("value")+"__"+testCaseNode.getAttribute("id")+"__remove top__"+holder+"__lines";
								if(lstStmtNodeNeigh.getLength()<9){ //Limits temp TODO
									print1("Merge Candidate "+testCaseNodeNext.getAttribute("id"));
									testCaseNodeNext.setAttribute("type", value);
								}
								

							}


						}
					}

				}
			}
		}
		return doc;
	}
	
	
	public static Document mergeTestCases2(Document doc) throws Exception{
		NodeList suiteLst = doc.getElementsByTagName("testsuite");

		//Iterating thru test suites.
		for(int i  = 0;i <suiteLst.getLength();i++){
			Node testSuiteNode = suiteLst.item(i);
			NodeList categoryPerSuiteLst = testSuiteNode.getChildNodes();

			for(int j  = 0;j <categoryPerSuiteLst.getLength();j++){
				Element categoryNode = (Element)categoryPerSuiteLst.item(j);
				if(categoryNode.getAttribute("type").equals("testcase")){
					NodeList TCLst = categoryNode.getChildNodes();

					//Iterating thru each tst suites keywords	
					for(int k  = TCLst.getLength()-1;k>1;k--){
						Element testCaseNode = (Element)TCLst.item(k);
						NodeList lstStmtNode =  testCaseNode.getChildNodes();
						Element stmtNode= null;
						String strMergeMetaData = testCaseNode.getAttribute("type");
						String[] mergeMetaData = strMergeMetaData.split("__");
						if(mergeMetaData.length>0){
							if(mergeMetaData[0].contains("Merge")){
								String toMergeNodeId = testCaseNode.getAttribute("id");
								String mergeToId = mergeMetaData[2];
								int linesToDelete = Integer.valueOf(mergeMetaData[4]);
								XPath xpath = XPathFactory.newInstance().newXPath();
								// XPath Query for showing all nodes value
								XPathExpression expr = xpath.compile("//testcase[@id='"+mergeToId+"']");
								Object result = expr.evaluate(doc, XPathConstants.NODESET);
								NodeList nodes = (NodeList) result;
								Node targetNode = (Node)nodes.item(0);
								
								for(int m= lstStmtNode.getLength()-linesToDelete ; m< lstStmtNode.getLength() ; m++){
									 stmtNode = (Element)lstStmtNode.item(m);
									 targetNode.appendChild(stmtNode.cloneNode(true));
									 
									 //doc= mergeTestCases2( doc);
								}
								
								XPathExpression expr1 = xpath.compile("//testcase[@id='"+toMergeNodeId+"']");
								Object result1 = expr1.evaluate(doc, XPathConstants.NODESET);
								NodeList nodes1 = (NodeList) result1;
								Node toDeleteNode = (Node)nodes1.item(0);
								print("toDeleteNode "+((Element)toDeleteNode).getAttribute("id"));
								print("from Cat "+((Element)categoryNode).getAttribute("id"));
								categoryNode.removeChild(toDeleteNode);
								
								
							}
						}
						
					}

				}
			}
		}
		return doc;
	}
	
	
	public static void xmlToFile(Document doc, File rootDir) throws Exception{
		//writes the xml to fileSystem.
		NodeList suiteLst = doc.getElementsByTagName("testsuite");
		
		//Add a file for each testSuite in the baserootDir
		StringBuffer buf = new StringBuffer() ;
		//Iterating thru test suites.
		for(int i  = 0;i <suiteLst.getLength();i++){
			Node testSuiteNode = suiteLst.item(i);
			File f1 = new File(rootDir+"/"+((Element)testSuiteNode).getAttribute("value"));
			NodeList categoryPerSuiteLst = testSuiteNode.getChildNodes();
			for(int j  = 0;j <categoryPerSuiteLst.getLength();j++){							
				Element categoryNode = (Element)categoryPerSuiteLst.item(j);
				buf.append(categoryNode.getAttribute("value"));				
				buf.append('\n');	
				buf.append('\n');
				NodeList testCaseLst = categoryNode.getChildNodes();

				//Iterating thru each tst suites keywords	
				for(int k  = 0;k <testCaseLst.getLength();k++){
					Element testCaseNode = (Element)testCaseLst.item(k);
					buf.append('\n');
					buf.append(testCaseNode.getAttribute("value"));				
					buf.append('\n');					
					if(testCaseNode.hasChildNodes()){
						NodeList stmtLst = testCaseNode.getChildNodes();
						for(int m  = 0;m <stmtLst.getLength();m++){							
							Element stmtNode = (Element)stmtLst.item(m);
							//if(!stmtNode.getAttribute("value").startsWith("#"))
								buf.append("\t"+stmtNode.getAttribute("value"));
							buf.append('\n');
						}
						
					}
					
					
					//lstKeywrodsInScope.add(testSuiteNode2.getAttribute("value").toString().trim());
				}

			}

			FileWriter writer2 = new FileWriter(f1);
			writer2.write(buf.toString());
			writer2.close();
			buf = new StringBuffer();
			
		}
		
		//print(lstKeywrodsInScope.toString());
	}
	
}

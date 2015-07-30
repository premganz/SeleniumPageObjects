package org.spo.fw.specific.scripts.dbcon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.util.PackageHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.xerces.dom.DeferredTextImpl;
import org.spo.fw.config.Constants;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.RobotCache;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XlsReader {


	private File file;
	
	private String sheetName;
	private String cacheId;
	private Document docNew;


	private static final Logger1 log =new Logger1("xlsreader");


	public void  getXlsAsString() throws Exception{}


	public XlsReader(){
		URL resourceUrl = getClass().getResource("/DraftOne.xml");
		String resourcePath;
		try {
			resourcePath = resourceUrl.toURI().getPath();
			 file= new File(resourcePath);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 

	public XlsReader(File f){
		this.file = f;	
		cacheId = f.getName();
		docNew = RobotCache.getXmlCache().get(cacheId);
	}

	//Constructor for unit testing
	public XlsReader(File f, String sheetName){
		this.file = f;
		this.sheetName= sheetName;
	}

	//TODO: Implementation suspended
	public void readXlsAsXml() throws Exception{
		FileInputStream in = new FileInputStream(file);
		//	FileOutputStream out = new FileOutputStream(new File("temp.xml"));
		ByteArrayOutputStream out = new ByteArrayOutputStream ();

		OPCPackage pack = PackageHelper.open(in);
		XSSFReader reader = new XSSFReader(pack);
		StringWriter writer = new StringWriter();

		POIXMLDocument doc = new POIXMLDocument(pack) {

			@Override
			public List<PackagePart> getAllEmbedds() throws OpenXML4JException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		//IOUtils.copy(reader.getSheet("rId1"), writer, "UTF-8");
		doc.write(out);
		String x = new String(out.toByteArray(), "UTF-8");
		//	while(reader.getSheetsData().hasNext()){
		//	IOUtils.copy(reader.getWorkbookData(), writer, "UTF-8");	
		String theString = writer.toString();
		//System.out.println(x);
		//	}



	}


	public Document getSimpleXmlFromXls( )  throws Exception{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		docNew = docBuilder.newDocument();
		Element rootElement = docNew.createElement("Worksheet");

		NodeList listHeaders = getHeaderRows();
		NodeList listRecords = getRecordRows();


		Element row = null;
		Element record=null;
		Node cell = null;


		for(int rowIdx=1;rowIdx<listRecords.getLength();rowIdx++){
			int trueIdx =-1;
			row = docNew.createElement("Row");
			Node rows = listRecords.item(rowIdx);
			log.debug("working with row "+listRecords.item(rowIdx).getTextContent());


			NodeList cellNodeList = rows.getChildNodes();//Cell

			for(int cellIdxAsRecd = 0; cellIdxAsRecd<cellNodeList.getLength()-1;cellIdxAsRecd++){
				cell = cellNodeList.item(cellIdxAsRecd);
				trueIdx++;
				if(cell instanceof DeferredTextImpl){
					cell = cell.getNextSibling();
					cellIdxAsRecd++;
					//continue;
				}


				log.trace("Working with cell "+cell.getTextContent());

				Element elem = (Element)cell;					
				if(elem.hasAttribute("ss:Index")){
					//this means some previous cells have been skipped.
					int idxActual = Integer.parseInt(elem.getAttribute("ss:Index"));
					//do some empty runs before resuming the process.
					//run only for the preceeding empty ones, the current one needs to proceed
					int skippedIdxs = idxActual-trueIdx-1;

					//catchup the skipped idx, which automatically brings trueIdx up to date.(whihc is one lies than the actual)

					int emtpyTodo= idxActual-trueIdx-1; 
					for( int i = 0;i<skippedIdxs;i++){
						record = docNew.createElement(listHeaders.item(trueIdx).getTextContent());
						log.trace("Empty Run"+":::::"+"key cellIdx( "+(trueIdx)+") "+listHeaders.item(trueIdx).getTextContent());
						//record.setAttribute("key", listHeaders.item(trueIdx).getTextContent());
						record.setTextContent("");
						//setAttribute("value", "");
						row.appendChild(record);
						trueIdx++;

					}
				}

				record = docNew.createElement("Record");
				Node data = cell.getFirstChild();

				if(trueIdx<listHeaders.getLength()){									
					record = docNew.createElement(listHeaders.item(trueIdx).getTextContent());
					log.trace("Data now is "+data.getTextContent()+"//"+"key from header cellIdx( "+trueIdx+") "+listHeaders.item(trueIdx).getTextContent());
					//record.setAttribute("key", listHeaders.item(trueIdx).getTextContent());
					//record.setAttribute("value", data.getTextContent());
					record.setTextContent(data.getTextContent());
					row.appendChild(record);					
				}else{
					break;
				}


			}
			rootElement.appendChild(row);
		}
		docNew.appendChild(rootElement);
		log.debug("Number of rows are "+rootElement.getChildNodes().getLength());
		xmlToFile(docNew, "C:");
		return docNew;
	}

	public NodeList getHeaderRows() throws Exception{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		//docFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(file);

		NodeList sheetLst = doc.getElementsByTagName("ss:Name");


		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("//Worksheet[@Name=\"" +sheetName+"\"]/Table/Row[1]/Cell");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList headerNodes = (NodeList) result;

		Node toRem = (Node)headerNodes.item(0);
		for(int i  = 0;i <sheetLst.getLength();i++){
			List  lstFields = new ArrayList<Node>();
			List lstFields_common = new ArrayList<Node>();
			log.debug("======================start=======================");
			Node testSuiteNode = sheetLst.item(i);
			NamedNodeMap commonAttributesList = testSuiteNode.getAttributes();
		}

		return headerNodes;
	}



	public NodeList getRecordRows() throws Exception{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		//docFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(file);


		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("//Worksheet[@Name=\"" +sheetName+"\"]/Table/Row");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList tableRowNodes = (NodeList) result;

		//	Node toRem = tableRowNodes.item(1);
		//	toRem.getParentNode().removeChild(toRem);
		//	Node toRem1 = tableRowNodes.item(2);
		//	toRem1.getParentNode().removeChild(toRem1);
		//	tableRowNodes.

		return tableRowNodes;
	}

	public  void xmlToFile(Document doc, String rootDir) throws Exception{
		//writes the xml to fileSystem.
		NodeList rowLst = doc.getElementsByTagName("Row");

		//Add a file for each testSuite in the baserootDir
		StringBuffer buf = new StringBuffer() ;
		//Iterating thru test suites.
		for(int i  = 0;i <rowLst.getLength();i++){
			Node row = rowLst.item(i);
			NodeList recordsPerRow = row.getChildNodes();
			for(int j  = 0;j <1;j++){							
				Element recordNode = (Element)recordsPerRow.item(j);

			}	

			FileWriter writer2 = new FileWriter("test.xml");
			writer2.write(buf.toString());
			writer2.close();

			buf = new StringBuffer();
			// Use a Transformer for output
			TransformerFactory tFactory =	    TransformerFactory.newInstance();
			Transformer transformer = 
					tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			//StreamResult result = new StreamResult(System.out);
			//transformer.transform(source, result);	


		}

		//print(lstKeywrodsInScope.toString());
	}


	public NodeList queryAbstractElementDocElem(String sheetName, String queryExpression, String requiredField  ) throws Exception{
		if(!queryExpression.contains(Constants.QUERY_EQUALS_EXP)){
			throw new IllegalArgumentException("expression should contain =", null);
		}
		ArrayList<String> keyValSequence =new ArrayList<String>();
		if(queryExpression.contains(Constants.QUERY_CONCAT_EXP)){
			String[] expressionArray_joined  = queryExpression.split(Constants.QUERY_CONCAT_EXP);
			for(String equalsExpression: expressionArray_joined){
				String[] expressionArray_equals  = equalsExpression.split(Constants.QUERY_EQUALS_EXP);
				keyValSequence.add(expressionArray_equals[0]);
				keyValSequence.add(expressionArray_equals[1]);
			}
		}else{
			String[] expressionArray_equals  = queryExpression.split(Constants.QUERY_EQUALS_EXP);
			keyValSequence.add(expressionArray_equals[0]);
			keyValSequence.add(expressionArray_equals[1]);
		}
		String[] expressionArray1 = {};
		String[] e = keyValSequence.toArray(expressionArray1);
		this.sheetName = sheetName;
		String toRet = "";
		if(docNew==null){
			docNew = getSimpleXmlFromXls();	
			RobotCache.getXmlCache().put(cacheId, docNew);
			docNew = RobotCache.getXmlCache().get(cacheId);
		}else{
			log.debug("Working with docNew from cache");
		}

		XPath xpath = XPathFactory.newInstance().newXPath();

		//		String query = "//Row/Record[(@key=\"" +expressionArray[0]+"\" and @value=\"" +expressionArray[1]+"\" )" +
		//				" and  (@key=\"" +expressionArray[2]+"\" and @value=\"" +expressionArray[3]+"\")"+ 
		//				"]/../Record[@key=\"" +requiredField+"\"]";
		String query = "";
		if(e.length>2){
			query = "//Row["+e[0]+"='"+e[1]+"' and "+e[2]+"='"+e[3]+"']";	
		}else{
			query = "//Row["+e[0]+"='"+e[1]+"']";	
		}

		log.debug("Query is "+ query);



		XPathExpression expr = xpath.compile(query);
		Object result = expr.evaluate(docNew, XPathConstants.NODESET);
		NodeList tableRowNodes = (NodeList) result;


		log.info("returning "+tableRowNodes);


		return tableRowNodes;

	}
	public String queryAbstractElementDoc(String sheetName, String queryExpression, String requiredField  ) throws Exception{
		String toRet="";
		NodeList tableRowNodes= queryAbstractElementDocElem(sheetName, queryExpression, requiredField);
		log.debug("Result count is " +tableRowNodes.getLength());
		Node recordNode = tableRowNodes.item(0);
		for(int resultSetIdx = 0; resultSetIdx<tableRowNodes.getLength();resultSetIdx++){
			Element record = (Element)tableRowNodes.item(resultSetIdx);			
			NodeList childNodes = record.getChildNodes();
			for(int recordIdx = 0; recordIdx<childNodes.getLength();recordIdx++){
				Element recordField = (Element)childNodes.item(recordIdx);
				if(recordField.getTagName().equals(requiredField)){
					if(!toRet.isEmpty()){
						toRet = toRet+",";	
					}
					toRet=toRet+recordField.getTextContent();
				}


			}
		}
		return toRet;
	}

	public Document queryAbstractElementDocDocument(String sheetName, String queryExpression, String requiredField  ) throws Exception{

		NodeList tableRowNodes = queryAbstractElementDocElem(sheetName, queryExpression, requiredField);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = docNew.createElement("Result");


		log.info("returning "+tableRowNodes);
		for(int resultSetIdx = 0; resultSetIdx<tableRowNodes.getLength();resultSetIdx++){
			Element record = (Element)tableRowNodes.item(resultSetIdx);			

			rootElement.appendChild(record);	
		}

		//doc.appendChild(rootElement);
		return docNew;

	}

		public List<Map<String, String>>  queryAbstractElementDoc_List(String sheetName, String queryExpression, String requiredField  ) throws Exception{
			List<Map<String, String>> lstResultMap = new ArrayList<Map<String, String>>();
			Map<String, String> entryMap = new LinkedHashMap<String, String>();
			NodeList tableRowNodes= queryAbstractElementDocElem(sheetName, queryExpression, requiredField);
			log.debug("Result count is " +tableRowNodes.getLength());
			Node recordNode = tableRowNodes.item(0);
			for(int resultSetIdx = 0; resultSetIdx<tableRowNodes.getLength();resultSetIdx++){
				Element record = (Element)tableRowNodes.item(resultSetIdx);			
				NodeList recordFldNodes = record.getChildNodes();
				entryMap = new LinkedHashMap<String, String>();
				for(int fldIdx = 0; fldIdx<recordFldNodes.getLength();fldIdx++){
					Element recordField = (Element)recordFldNodes.item(fldIdx);
					entryMap.put(recordField.getTagName(), recordField.getTextContent());
				}
				lstResultMap.add(entryMap);
			}
			
			return lstResultMap;
		}

		
	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
	}


	public String getSheetName() {
		return sheetName;
	}


	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	
}

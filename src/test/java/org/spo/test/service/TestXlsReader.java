/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.test.service;

import java.io.File;
import java.net.URL;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spo.fw.config.Constants;
import org.spo.fw.config.SessionContext;
import org.spo.fw.specific.scripts.dbcon.XlsReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TestXlsReader {

	File f = null;
	@Before
	public void setUp() throws Exception{
		SessionContext.logLevel=Constants.LogLevel.TRACE;
		URL resourceUrl = getClass().getResource("/script7Test.xml");
		String resourcePath = resourceUrl.toURI().getPath();
		f= new File(resourcePath);
	}
	

	@After
	public void tearDown(){
		
		
	}
	
	
	@Test //TODO impl of original method suspended
	public void testReadXlsAsXml() {
		try {
		URL resourceUrl = getClass().getResource("/script7Test.xml");
		String resourcePath = resourceUrl.toURI().getPath();
		f= new File(resourcePath);
		XlsReader reader = new XlsReader(f, "data");
		
			//reader.readXlsAsXml();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	@Test
	public void testGetHeaderRows() throws Exception {
		XlsReader reader = new XlsReader(f, "data");
		NodeList list = reader.getHeaderRows();
		for(int i=0;i<list.getLength();i++){
			Node n = list.item(i);
			System.out.println(n.getTextContent());
		}
			Assert.assertTrue(list.getLength()==3);
		
	}
	
	@Test
	public void getRecordRows() throws Exception {
		XlsReader reader = new XlsReader(f, "data");
		NodeList list = reader.getRecordRows();
		for(int i=0;i<list.getLength();i++){
			Node n = list.item(i);
			System.out.println(n.getTextContent());
		}
			Assert.assertTrue(list.getLength()==30);
		
	}
	
	@Test
	public void testGetSimpleXmlFromXls() {
		XlsReader reader = new XlsReader(f, "data");
		Document doc=null;
		try {
			doc = reader.getSimpleXmlFromXls();	
		
			NodeList list = (NodeList) doc.getChildNodes();
			for(int i=0;i<list.getLength();i++){
				Node n = list.item(i);
				System.out.println(n.getTextContent());
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		Assert.assertTrue(doc!=null);
	

	}
	
	@Test
	public void testQueryAbstractElementDoc() {
		XlsReader reader = new XlsReader(f);
		String result="";
		try {
			result = reader.queryAbstractElementDoc("data","vendor=Michelin","price" );
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Assert.assertTrue(result.equals("6000"));


	}
	

	@Test
	public void testQueryAbstractElementDocComplex() {
		XlsReader reader = new XlsReader(f);
		String result="";
		try {
			result = reader.queryAbstractElementDoc("data","","" );
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Assert.assertTrue(result.equals(""));


	}

}

package com.tutorialspot.demo.hr.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


public class CommonUtils {

	private XPath xPath;
    
    /**
     * Evaluates an XPath expression on the specified XML code.
     * 
     * @param node
     *            Source Document XMLDocument document
     * @param xPath
     *            XPath expression to evaluate
     * @return The result of the evaluation, as a {@link Node}
     * @throws Exception
     */
    public Node evaluateAsNode(Node node, String xPath) throws XPathExpressionException {
        XPathExpression expr = getXPath().compile(xPath);
        Object value = expr.evaluate(node, XPathConstants.NODE);

        return (Node) value;
    }
    
    /**
     * Creates an XML document from an XML input.
     * 
     * @param inputXML
     * @return The XML document
     * @throws Exception
     */
    public Document getXmlDocument(String inputXML) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(inputXML)));

        return document;
    }
    
    /**
     * Injects text values inside XML nodes.
     * 
     * @param inputXML
     *            The XML to modify, provided as a String
     * @param values
     *            A map containing the XPath of the nodes to modify as a key,
     *            and the texts to set as values
     * @return The modified XML, as a String
     * @throws Exception
     */
    public String fillXml(String inputXML, Map<String, String> values) throws Exception {
        // Reads XML code
        Document document = getXmlDocument(inputXML);

        // Does injections
        if (values != null) {
            for (String value : values.keySet()) {
                Node node = evaluateAsNode(document, value);
                node.setTextContent(values.get(value));
            }
        }
       
        // Writes XML code
        return transformXmlDocumentToString(document);
    }
    
    /**
     * Transforms the XML string into a document.
     * 
     * @param xmlString
     * @return a document
     * @throws Exception
     */
    public Document transformXmlStringToDocument(String xmlString) throws Exception{
    	
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder docBuilder;
        
        docBuilder = factory.newDocumentBuilder();
		Document document = docBuilder.parse(new InputSource(new StringReader(xmlString))); 
        return document;
    }
    
    /**
     * Creates an XML file in the file system from a document..
     * 
     * @param doc
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public void createFileFromDocument(Document doc, String filePath, String fileName) throws Exception {
    	
    	File file = new File(filePath, fileName);
    	   	
    	file.createNewFile();
    	
    	FileOutputStream outputStream = new FileOutputStream(file);
    	
    	TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        
        DOMSource domSource = new DOMSource(doc);
        StreamResult result = new StreamResult(outputStream);
        transformer.transform(domSource, result);
    }
        
    /**
     * Transforms the document content into a string.
     * 
     * @param document
     * @return A string
     * @throws Exception
     */
    public String transformXmlDocumentToString(Document document) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        
        return writer.getBuffer().toString();
    }
    
    /**
     * Reads the XML file from the specified path.
     * 
     * @param querySpecPath
     *            The path to the query specification file
     * @return The XML file as an XML flow
     * @throws Exception
     */
    public String retrieveXmlFromFile(String xmlFilePath) throws Exception {

        String querySpec = getResourceAsXml(xmlFilePath);

        return querySpec;
    }    
    
    /**
     * Retrieves the resource in XML format.
     * 
     * @param resourcePath
     * @return The XML resource as a {@link String}
     * @throws Exception
     */
    private String getResourceAsXml(String resourcePath) throws Exception {
        InputStream inputStream = createInputStream(resourcePath);

        try {
            return getXmlFromInputStream(inputStream);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Creates the input stream from a resource path.
     * 
     * @param resourcePath
     * @return The input stream
     * @throws IOException
     */
    private InputStream createInputStream(String resourcePath) throws IOException {
    	InputStream inputStream = new FileInputStream(resourcePath);
        
        return inputStream;
    }
    
    /**
     * Retrieves the XML resource from an input stream.
     * 
     * @param inputStream
     * @return The XML resource as a {@link String}
     */
    private String getXmlFromInputStream(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String xmlResource = scanner.hasNext() ? scanner.next() : "";

        return xmlResource;
    }
    
    /**
     * Gets the new xpath instance if needed.
     * 
     * @return xpath
     */
    private XPath getXPath() {
        if (xPath == null) {
            xPath = XPathFactory.newInstance().newXPath();
        }
        return xPath;
    }
}

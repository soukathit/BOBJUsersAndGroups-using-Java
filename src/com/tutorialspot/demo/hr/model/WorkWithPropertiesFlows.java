package com.tutorialspot.demo.hr.model;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
public class WorkWithPropertiesFlows {

	private Request request;
	private Authentication authentication;
	private CommonUtils commonUtils;
	
	public WorkWithPropertiesFlows(Authentication authentication) {

		this.authentication = authentication;
		
		// Create a new request object.
		this.request = new Request(authentication.getLogonToken());
		
		// Create a new CommonUtils object.
		this.commonUtils = new CommonUtils();
	}
	
	public WorkWithPropertiesFlows() {
		
		// Create a new CommonUtils object.
		this.commonUtils = new CommonUtils();
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
		
		this.request = new Request(authentication.getLogonToken());
	}

	public String getDocumentProperties(String documentId, String resourcesFolder) throws Exception {
		
		// Get the properties of a Webi document referenced by its documentId parameter.
		// Request --> GET <webiURL>/documents/{documentId}/properties
		// Where: 
		// {documentId} is the document identifier retrieved from the list of documents 
		// No Request body.
		// Response body: a list of Web Intelligence documents sorted by name. For each document: 
		//	<properties>
		//		<property key="...">
		
		// There is no request body so we don't need to create the XML content from
		// an XML file.
		
		// Set up the request URL.
		String requestURL = authentication.getBipURL() + "/raylight/v1" + "/documents/" + documentId + "/properties";
				
		// To consolidate all of the traces together.
		StringBuilder consolidatedTrace = new StringBuilder();
		
		// Used to store the request string and the response string.
		String requestContent;
		String responseContent;
		
		// Used to store the request and response document objects.
		Document requestDoc;
		Document responseDoc;
		
		// Display a message indicating what this trace is for.
		consolidatedTrace.append("<-- The following shows the trace that is meant for"
				+ " retrieving the properties for the Webi document with ID '" + documentId
				+ "'.-->\n");
		
		// Send the request.
		String trace = request.send(requestURL, "GET", null, "application/xml");
				
		// Store the request XML and response XML in  the directory specified by
	    // resourcesFolder appended with "traceflows/".
	    // If there is no request XML, then the requestContent will be null,  This
	    // will be the case if there was no request body from an XML file
	    // created with the request message.

	    // Retrieve the request.
	    requestContent =  request.getRequestContent();
	    
	    if (requestContent != null) {
	    	
	    	// Transform the request String to a Document.
		    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
		    
		    // Create the request message as a file.
		    commonUtils.createFileFromDocument(requestDoc, resourcesFolder , "Id_" + documentId + "_getDocumentProperties_request.xml");
	    }
	    
	    // Retrieve the response.
	    responseContent = request.getResponseContent();
	    
	    // Transform the response String to a Document.
	    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
	    
	    // Create the response message as a file.
	    //commonUtils.createFileFromDocument(responseDoc, resourcesFolder, "Id_" + documentId + "_getDocumentProperties_response.xml");

	    consolidatedTrace.append(trace);
	    
		return consolidatedTrace.toString();
	}
	
public String getDocumentDataProviders(String documentId, String resourcesFolder,PreparedStatement statement_webi_rpt_dp , PreparedStatement statement_webi_rpt_dp_details,Connection connection,PreparedStatement statement_webi_rpt_dp_query,String current_Date_parameter,String CMServerURL) throws Exception {
		
		// Get the properties of a Webi document referenced by its documentId parameter.
		// Request --> GET <webiURL>/documents/{documentId}/properties
		// Where: 
		// {documentId} is the document identifier retrieved from the list of documents 
		// No Request body.
		// Response body: a list of Web Intelligence documents sorted by name. For each document: 
		//	<properties>
		//		<property key="...">
		
		// There is no request body so we don't need to create the XML content from
		// an XML file.
		
		// Set up the request URL.
		String requestURL = authentication.getBipURL() + "/raylight/v1" + "/documents/" + documentId + "/dataproviders";
				
		// To consolidate all of the traces together.
		StringBuilder consolidatedTrace = new StringBuilder();
		
		// Used to store the request string and the response string.
		String requestContent;
		String responseContent;
		
		// Used to store the request and response document objects.
		Document requestDoc;
		Document responseDoc;
		
		// Display a message indicating what this trace is for.
		consolidatedTrace.append("<-- The following shows the trace that is meant for"
				+ " retrieving the properties for the Webi document with ID '" + documentId
				+ "'.-->\n");
		
		// Send the request.
		String trace = request.send(requestURL, "GET", null, "application/xml");
				
		// Store the request XML and response XML in  the directory specified by
	    // resourcesFolder appended with "traceflows/".
	    // If there is no request XML, then the requestContent will be null,  This
	    // will be the case if there was no request body from an XML file
	    // created with the request message.

	    // Retrieve the request.
	    requestContent =  request.getRequestContent();
	    
	    if (requestContent != null) {
	    	
	    	// Transform the request String to a Document.
		    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
		    
		    //NodeList dp_tags = requestDoc.getElementsByTagName("dataprovider");
		    //int dp_tags_length = dp_tags.getLength();
		    //System.out.println("DP Tags Length : " + dp_tags_length);
		    //for (int i=0;i<dp_tags.getLength();i++)
		    //{
		    //System.out.println("DP Tags Length : " + dp_tags.item(i).getNodeName());
		    //}
		    //dp_tags.get(0);
		    
		    // Create the request message as a file.
		    //commonUtils.createFileFromDocument(requestDoc, resourcesFolder , "Id_" + documentId + "_getDocumentDataProviders_request.xml");
	    }
	    
	    // Retrieve the response.
	    responseContent = request.getResponseContent();
	    
	    // Transform the response String to a Document.
	    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
	    //responseDoc = commonUtils.transformXmlStringToDocument(requestContent);
	    responseDoc.getDocumentElement().normalize();
	    //System.out.println("Root element :"  + responseDoc.getDocumentElement().getNodeName());
	    //NodeList nList = doc.getElementsByTagName("student");
	    NodeList dp_tags = responseDoc.getElementsByTagName("dataprovider");
	    int dp_tags_length = dp_tags.getLength();
	    //System.out.println("DP Tags Length : " + dp_tags_length);
	    String datsourceid_error = null;
	    //commonUtils.createFileFromDocument(responseDoc, resourcesFolder, "Id_" + documentId + "_getDocumentDataProviders_request.xml");
	    for (int i=0;i<dp_tags.getLength();i++)
	    {
	    	Node nNode = dp_tags.item(i);
	    	 //System.out.println("\nCurrent Element :" + nNode.getNodeName());
	    	 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               //System.out.println("Data Provider Id : " + eElement.getAttribute("id"));
	               String dp_id = eElement.getElementsByTagName("id").item(0).getTextContent();
	               //System.out.println("Data Provider Id : "  + eElement.getElementsByTagName("id").item(0).getTextContent());
	               //System.out.println("Data Provider Name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
	               
	               try
	               {
	            	  datsourceid_error = eElement.getElementsByTagName("dataSourceId").item(0).getTextContent();
	            	  statement_webi_rpt_dp.setString(1, documentId);
		               statement_webi_rpt_dp.setString(2, eElement.getElementsByTagName("id").item(0).getTextContent());
		               statement_webi_rpt_dp.setString(3, eElement.getElementsByTagName("name").item(0).getTextContent());
		               statement_webi_rpt_dp.setString(4, eElement.getElementsByTagName("dataSourceId").item(0).getTextContent());
		               statement_webi_rpt_dp.setString(5, eElement.getElementsByTagName("dataSourceType") .item(0).getTextContent());
		               statement_webi_rpt_dp.setString(6, current_Date_parameter);
		               statement_webi_rpt_dp.setString(7, CMServerURL);
		               statement_webi_rpt_dp.executeUpdate();
	               }catch (NullPointerException npe) {
	            	    // It's fine if findUser throws a NPE
	               }
	               if (datsourceid_error != null)
	               {
	               //System.out.println("Universe_Id : " + eElement.getElementsByTagName("dataSourceId").item(0).getTextContent());
	               //System.out.println("Universe Type : "  + eElement.getElementsByTagName("dataSourceType") .item(0).getTextContent());
	               
	               }
	               getDocumentDPDetails(documentId, resourcesFolder, dp_id,statement_webi_rpt_dp_details,connection,statement_webi_rpt_dp_query,current_Date_parameter,CMServerURL);
	               //getDocumentFilterDetails(documentId, resourcesFolder, dp_id,statement_webi_rpt_dp_details);
	    	 
	    	 }
	    }
	    
	    // Create the response message as a file.
	    //commonUtils.createFileFromDocument(responseDoc, resourcesFolder, "Id_" + documentId + "_getDocumentDataProviders_request.xml");

	    consolidatedTrace.append(trace);
	    
		return consolidatedTrace.toString();
	}
	
public String getDocumentDPDetails(String documentId, String resourcesFolder, String dpid,PreparedStatement statement_webi_rpt_dp_details,Connection connection,PreparedStatement statement_webi_rpt_dp_query,String current_Date_parameter,String CMServerURL) throws Exception {
	
	// Get the properties of a Webi document referenced by its documentId parameter.
	// Request --> GET <webiURL>/documents/{documentId}/properties
	// Where: 
	// {documentId} is the document identifier retrieved from the list of documents 
	// No Request body.
	// Response body: a list of Web Intelligence documents sorted by name. For each document: 
	//	<properties>
	//		<property key="...">
	
	// There is no request body so we don't need to create the XML content from
	// an XML file.
	
	// Set up the request URL.
	String requestURL = authentication.getBipURL() + "/raylight/v1" + "/documents/" + documentId + "/dataproviders/" + dpid;
			
	// To consolidate all of the traces together.
	StringBuilder consolidatedTrace = new StringBuilder();
	
	// Used to store the request string and the response string.
	String requestContent;
	String responseContent;
	
	// Used to store the request and response document objects.
	Document requestDoc;
	Document responseDoc;
	
	// Display a message indicating what this trace is for.
	consolidatedTrace.append("<-- The following shows the trace that is meant for"
			+ " retrieving the properties for the Webi document with ID '" + documentId
			+ "'.-->\n");
	
	// Send the request.
	String trace = request.send(requestURL, "GET", null, "application/xml");
			
	// Store the request XML and response XML in  the directory specified by
    // resourcesFolder appended with "traceflows/".
    // If there is no request XML, then the requestContent will be null,  This
    // will be the case if there was no request body from an XML file
    // created with the request message.

    // Retrieve the request.
    requestContent =  request.getRequestContent();
    
    if (requestContent != null) {
    	
    	// Transform the request String to a Document.
	    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
	    
	    // Create the request message as a file.
	    //commonUtils.createFileFromDocument(requestDoc, resourcesFolder , "Id_" + documentId + "_getDocumentDPDetails" + dpid +"_request.xml");
    }
    
    // Retrieve the response.
    responseContent = request.getResponseContent();
    
    // Transform the response String to a Document.
    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
    responseDoc.getDocumentElement().normalize();
    //System.out.println("Root element :"  + responseDoc.getDocumentElement().getNodeName());
    //NodeList nList = doc.getElementsByTagName("student");
    NodeList dp_tags = responseDoc.getElementsByTagName("expression");
    int dp_tags_length = dp_tags.getLength();
    Clob query_expression = connection.createClob();
    try
    {
    	String query_tag_name = responseDoc.getElementsByTagName("query").item(0).getTextContent();
    	
    	query_expression.setString( 1,query_tag_name);
        statement_webi_rpt_dp_query.setString(1, documentId);
        statement_webi_rpt_dp_query.setString(2, dpid);
        statement_webi_rpt_dp_query.setClob(3, query_expression);
        statement_webi_rpt_dp_query.setString(4, current_Date_parameter);
        statement_webi_rpt_dp_query.setString(5, CMServerURL);
        statement_webi_rpt_dp_query.executeUpdate();
    //System.out.println("String Data Provider Query : "  + query_tag_name );
    } catch(NullPointerException npe){
    	//System.out.println("String Data Provider Query : "  + "Null");
    }
    
    
    
    //System.out.println("DP Tags Length : " + dp_tags_length);
    for (int i=0;i<dp_tags.getLength();i++)
    {
    	Node nNode = dp_tags.item(i);
    	 //System.out.println("\nCurrent Element :" + nNode.getNodeName());
    	 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               //System.out.println("Data Provider Id : " + eElement.getAttribute("id"));
               //System.out.println("Data Provider Object Id : "  + eElement.getElementsByTagName("id").item(0).getTextContent());
               //System.out.println("Universe Object Name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
               //System.out.println("Universe_Object Identifier : " + eElement.getElementsByTagName("dataSourceObjectId").item(0).getTextContent());
               //System.out.println("formulaLanguageId : "  + eElement.getElementsByTagName("formulaLanguageId") .item(0).getTextContent());
               try
               {
               statement_webi_rpt_dp_details.setString(1, documentId);
               statement_webi_rpt_dp_details.setString(2, dpid);
               statement_webi_rpt_dp_details.setString(3, eElement.getElementsByTagName("id").item(0).getTextContent());
               statement_webi_rpt_dp_details.setString(4, eElement.getElementsByTagName("name").item(0).getTextContent());
               statement_webi_rpt_dp_details.setString(5, eElement.getElementsByTagName("dataSourceObjectId").item(0).getTextContent());
               statement_webi_rpt_dp_details.setString(6, eElement.getElementsByTagName("formulaLanguageId") .item(0).getTextContent());
               statement_webi_rpt_dp_details.setString(7, current_Date_parameter);
               statement_webi_rpt_dp_details.setString(8, CMServerURL);
               statement_webi_rpt_dp_details.executeUpdate();
               } catch (NullPointerException npe){
            	   
               }
    	 
    	 }
    }
    
    // Create the response message as a file.
    //commonUtils.createFileFromDocument(responseDoc, resourcesFolder, "Id_" + documentId + "_getDocumentDPDetails" + dpid +"_request.xml");

    consolidatedTrace.append(trace);
    
	return consolidatedTrace.toString();
}

public String getDocumentFilterDetails(String documentId, String resourcesFolder, String dpid,PreparedStatement statement_webi_rpt_dp_details) throws Exception {
	
	// Get the properties of a Webi document referenced by its documentId parameter.
	// Request --> GET <webiURL>/documents/{documentId}/properties
	// Where: 
	// {documentId} is the document identifier retrieved from the list of documents 
	// No Request body.
	// Response body: a list of Web Intelligence documents sorted by name. For each document: 
	//	<properties>
	//		<property key="...">
	
	// There is no request body so we don't need to create the XML content from
	// an XML file.
	
	// Set up the request URL.
	String requestURL = authentication.getBipURL() + "/raylight/v1" + "/documents/" + documentId + "/parameters/" ;
			
	// To consolidate all of the traces together.
	StringBuilder consolidatedTrace = new StringBuilder();
	
	// Used to store the request string and the response string.
	String requestContent;
	String responseContent;
	
	// Used to store the request and response document objects.
	Document requestDoc;
	Document responseDoc;
	
	// Display a message indicating what this trace is for.
	consolidatedTrace.append("<-- The following shows the trace that is meant for"
			+ " retrieving the properties for the Webi document with ID '" + documentId
			+ "'.-->\n");
	
	// Send the request.
	String trace = request.send(requestURL, "GET", null, "application/xml");
			
	// Store the request XML and response XML in  the directory specified by
    // resourcesFolder appended with "traceflows/".
    // If there is no request XML, then the requestContent will be null,  This
    // will be the case if there was no request body from an XML file
    // created with the request message.

    // Retrieve the request.
    requestContent =  request.getRequestContent();
    
    if (requestContent != null) {
    	
    	// Transform the request String to a Document.
	    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
	    
	    // Create the request message as a file.
	    //commonUtils.createFileFromDocument(requestDoc, resourcesFolder , "Id_" + documentId + "_getDocumentDPDetails" + dpid +"_request.xml");
    }
    
    // Retrieve the response.
    responseContent = request.getResponseContent();
    
    // Transform the response String to a Document.
    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
    responseDoc.getDocumentElement().normalize();
    //System.out.println("Root element :"  + responseDoc.getDocumentElement().getNodeName());
    //NodeList nList = doc.getElementsByTagName("student");
    
    
    // Create the response message as a file.
    commonUtils.createFileFromDocument(responseDoc, resourcesFolder, "Id_" + documentId + "_getDocumentFilterDetails" + "_request.xml");

    consolidatedTrace.append(trace);
    
	return consolidatedTrace.toString();
}
public String getAllUniverses(String resourcesFolder,String current_Date_parameter,String CMServerURL) throws Exception {
	
	// Get the properties of a Webi document referenced by its documentId parameter.
	// Request --> GET <webiURL>/documents/{documentId}/properties
	// Where: 
	// {documentId} is the document identifier retrieved from the list of documents 
	// No Request body.
	// Response body: a list of Web Intelligence documents sorted by name. For each document: 
	//	<properties>
	//		<property key="...">
	
	// There is no request body so we don't need to create the XML content from
	// an XML file.
	
	// Set up the request URL.
	String requestURL = authentication.getBipURL() + "/raylight/v1" + "/universes/";
			
	// To consolidate all of the traces together.
	StringBuilder consolidatedTrace = new StringBuilder();
	
	// Used to store the request string and the response string.
	String requestContent;
	String responseContent;
	
	// Used to store the request and response document objects.
	Document requestDoc;
	Document responseDoc;
	
	// Display a message indicating what this trace is for.
	consolidatedTrace.append("<-- The following shows the trace that is meant for"
			+ " retrieving the properties for the Webi document with ID '" 
			+ "'.-->\n");
	
	// Send the request.
	String trace = request.send(requestURL, "GET", null, "application/xml");
			
	// Store the request XML and response XML in  the directory specified by
    // resourcesFolder appended with "traceflows/".
    // If there is no request XML, then the requestContent will be null,  This
    // will be the case if there was no request body from an XML file
    // created with the request message.

    // Retrieve the request.
    requestContent =  request.getRequestContent();
    
    if (requestContent != null) {
    	
    	// Transform the request String to a Document.
	    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
	    
	    // Create the request message as a file.
	    //commonUtils.createFileFromDocument(requestDoc, resourcesFolder , "Id_" + "getAllUniverses"  +"_request.xml");
    }
    
    // Retrieve the response.
    responseContent = request.getResponseContent();
    
    // Transform the response String to a Document.
    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
    
    // Create the response message as a file.
    //commonUtils.createFileFromDocument(responseDoc, resourcesFolder, "Id_" + "getAllUniverses" +"_request.xml");

    consolidatedTrace.append(trace);
    
	return consolidatedTrace.toString();
}

/*public String getAllUserGroups(String resourcesFolder) throws Exception {
	
	// Get the properties of a Webi document referenced by its documentId parameter.
	// Request --> GET <webiURL>/documents/{documentId}/properties
	// Where: 
	// {documentId} is the document identifier retrieved from the list of documents 
	// No Request body.
	// Response body: a list of Web Intelligence documents sorted by name. For each document: 
	//	<properties>
	//		<property key="...">
	
	// There is no request body so we don't need to create the XML content from
	// an XML file.
	
	// Set up the request URL.
	String requestURL = authentication.getBipURL() +  "/v1/usergroups";
	
	
	System.out.println("Request URL : "  + requestURL);
			
	// To consolidate all of the traces together.
	StringBuilder consolidatedTrace = new StringBuilder();
	
	// Used to store the request string and the response string.
	String requestContent;
	String responseContent;
	
	// Used to store the request and response document objects.
	Document requestDoc;
	Document responseDoc;
	
	// Display a message indicating what this trace is for.
	consolidatedTrace.append("<-- The following shows the trace that is meant for"
			+ " retrieving the properties for the Webi document with ID '" 
			+ "'.-->\n");
	
	// Send the request.
	String trace = request.send(requestURL, "GET", null);
	
	System.out.println("Trace Print :  " + trace);
			
	// Store the request XML and response XML in  the directory specified by
    // resourcesFolder appended with "traceflows/".
    // If there is no request XML, then the requestContent will be null,  This
    // will be the case if there was no request body from an XML file
    // created with the request message.

    // Retrieve the request.
    requestContent =  request.getRequestContent();
    
    if (requestContent != null) {
    	
    	// Transform the request String to a Document.
	    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
	    
	    // Create the request message as a file.
	    commonUtils.createFileFromDocument(requestDoc, resourcesFolder , "Id_" + "getAllUserGroups"  +"_request.xml");
    }
    
    // Retrieve the response.
    responseContent = request.getResponseContent();
    
    // Transform the response String to a Document.
    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
    
    // Create the response message as a file.
    commonUtils.createFileFromDocument(responseDoc, resourcesFolder, "Id_" + "getAllUserGroups" +"_request.xml");

    consolidatedTrace.append(trace);
    
	return consolidatedTrace.toString();
}*/

public String getDocumentSharedElements(String documentId, String resourcesFolder,PreparedStatement SQL_INSERT_WEBI_RPT_SHRD_ELMTS ,Connection connection,String current_Date_parameter,String CMServerURL) throws Exception {
	
	// Get the properties of a Webi document referenced by its documentId parameter.
	// Request --> GET <webiURL>/documents/{documentId}/properties
	// Where: 
	// {documentId} is the document identifier retrieved from the list of documents 
	// No Request body.
	// Response body: a list of Web Intelligence documents sorted by name. For each document: 
	//	<properties>
	//		<property key="...">
	
	// There is no request body so we don't need to create the XML content from
	// an XML file.
	
	// Set up the request URL.
	String requestURL = authentication.getBipURL() + "/raylight/v1" + "/documents/" + documentId + "/sharedelements";
			
	// To consolidate all of the traces together.
	StringBuilder consolidatedTrace = new StringBuilder();
	
	// Used to store the request string and the response string.
	String requestContent;
	String responseContent;
	
	// Used to store the request and response document objects.
	Document requestDoc;
	Document responseDoc;
	
	// Display a message indicating what this trace is for.
	consolidatedTrace.append("<-- The following shows the trace that is meant for"
			+ " retrieving the properties for the Webi document with ID '" + documentId
			+ "'.-->\n");
	
	// Send the request.
	String trace = request.send(requestURL, "GET", null, "application/xml");
			
	// Store the request XML and response XML in  the directory specified by
    // resourcesFolder appended with "traceflows/".
    // If there is no request XML, then the requestContent will be null,  This
    // will be the case if there was no request body from an XML file
    // created with the request message.

    // Retrieve the request.
    requestContent =  request.getRequestContent();
    
    if (requestContent != null) {
    	
    	// Transform the request String to a Document.
	    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
	    
	    //NodeList dp_tags = requestDoc.getElementsByTagName("dataprovider");
	    //int dp_tags_length = dp_tags.getLength();
	    //System.out.println("DP Tags Length : " + dp_tags_length);
	    //for (int i=0;i<dp_tags.getLength();i++)
	    //{
	    //System.out.println("DP Tags Length : " + dp_tags.item(i).getNodeName());
	    //}
	    //dp_tags.get(0);
	    
	    // Create the request message as a file.
	    //commonUtils.createFileFromDocument(requestDoc, resourcesFolder , "Id_" + documentId + "_getDocumentDataProviders_request.xml");
    }
    
    // Retrieve the response.
    responseContent = request.getResponseContent();
    
    // Transform the response String to a Document.
    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
    //responseDoc = commonUtils.transformXmlStringToDocument(requestContent);
    responseDoc.getDocumentElement().normalize();
    //System.out.println("Root element :"  + responseDoc.getDocumentElement().getNodeName());
    //NodeList nList = doc.getElementsByTagName("student");
    NodeList dp_tags = responseDoc.getElementsByTagName("sharedelement");
    int dp_tags_length = dp_tags.getLength();
    //System.out.println("DP Tags Length : " + dp_tags_length);
    String datsourceid_error = null;
    commonUtils.createFileFromDocument(responseDoc, resourcesFolder, "Id_" + documentId + "_getDocumentSharedElements.xml");
    for (int i=0;i<dp_tags.getLength();i++)
    {
    	Node nNode = dp_tags.item(i);
    	 //System.out.println("\nCurrent Element :" + nNode.getNodeName());
    	 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               //System.out.println("Data Provider Id : " + eElement.getAttribute("id"));
               String dp_id = eElement.getElementsByTagName("id").item(0).getTextContent();
               //System.out.println("Data Provider Id : "  + eElement.getElementsByTagName("id").item(0).getTextContent());
               //System.out.println("Data Provider Name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
               
               try
               {
            	  datsourceid_error = eElement.getElementsByTagName("id").item(0).getTextContent();
            	  SQL_INSERT_WEBI_RPT_SHRD_ELMTS.setString(1, documentId);
            	  SQL_INSERT_WEBI_RPT_SHRD_ELMTS.setString(2, eElement.getElementsByTagName("id").item(0).getTextContent());
            	  SQL_INSERT_WEBI_RPT_SHRD_ELMTS.setString(3, eElement.getElementsByTagName("cuid").item(0).getTextContent());
            	  SQL_INSERT_WEBI_RPT_SHRD_ELMTS.setString(4, eElement.getElementsByTagName("name").item(0).getTextContent());
            	  SQL_INSERT_WEBI_RPT_SHRD_ELMTS.setString(5, eElement.getElementsByTagName("folderid").item(0).getTextContent());
            	  SQL_INSERT_WEBI_RPT_SHRD_ELMTS.setString(6, eElement.getElementsByTagName("revision") .item(0).getTextContent());
            	  SQL_INSERT_WEBI_RPT_SHRD_ELMTS.setString(7, current_Date_parameter);
            	  SQL_INSERT_WEBI_RPT_SHRD_ELMTS.setString(8, CMServerURL);
            	  SQL_INSERT_WEBI_RPT_SHRD_ELMTS.executeUpdate();
               }catch (NullPointerException npe) {
            	    // It's fine if findUser throws a NPE
               }
               if (datsourceid_error != null)
               {
               //System.out.println("Universe_Id : " + eElement.getElementsByTagName("dataSourceId").item(0).getTextContent());
               //System.out.println("Universe Type : "  + eElement.getElementsByTagName("dataSourceType") .item(0).getTextContent());
               
               }
               //getDocumentDPDetails(documentId, resourcesFolder, dp_id,statement_webi_rpt_dp_details,connection,statement_webi_rpt_dp_query,current_Date_parameter,CMServerURL);
               //getDocumentFilterDetails(documentId, resourcesFolder, dp_id,statement_webi_rpt_dp_details);
    	 
    	 }
    }
    
    // Create the response message as a file.
    //commonUtils.createFileFromDocument(responseDoc, resourcesFolder, "Id_" + documentId + "_getDocumentDataProviders_request.xml");

    consolidatedTrace.append(trace);
    
	return consolidatedTrace.toString();
}

public String refreshDocument(String documentId, String lovInfo, String	 bodyType, String resourcesFolder,String report_name) throws Exception {
	
	// Refreshes a Webi document by filling the refresh parameters if needed and
	// running the query.
	// You can ask for the refresh without providing any parameters (no request
	// body). In this case, the web service returns the context or prompt that needs
	// to be filled. If no parameter has to be filled, the document is refreshed. 

	// Request --> PUT <webiURL>/documents/{documentId}/parameters?lovInfo={true|false}
	// Where:
	// 	{documentId} is the document identifier retrieved from the list of documents 
	//	lovInfo is an optional, Boolean parameter. Default value is true. If set to
	//	false, the lists of values are not computed, nor displayed. 
	// Response: When all parameters have been answered, the last PUT call returns a
	// message stating the success of the request.

	// Set up the request URL.
	String requestURL = authentication.getBipURL() + "/raylight/v1" + "/documents/" + documentId + "/parameters?lovInfo=" + lovInfo;
	
	System.out.println("Reguest URL : " + requestURL);
	
	// The request XML body that needs to be loaded depends on the value that was specified
	// for bodyType.		
	// The XML file is stored in the .../resources/ folder.
	String xmlPath = "";
	
	String xmlContent = "";
	
	String trace;
		
	StringBuilder consolidatedTrace = new StringBuilder();
	
	// Used to store the request string and the response string.
	String requestContent;
	String responseContent;
	
	// Used to store the request and response document objects.
	Document requestDoc;
	Document responseDoc;
	String Report_status = "";
	if (bodyType.startsWith("1")) {
		
		// This is for an example that has no prompts.
		
		// Display a message indicating what this trace is for.
		consolidatedTrace.append("<-- The following shows the trace that is meant for"
				+ " a Webi document that has no prompts. -->\n");
		
		consolidatedTrace.append("<-- The Webi report that is being traced has report ID = '"
				+ documentId + "' -->\n");
		
		consolidatedTrace.append("<-- The following PUT call is intended to refresh "
				+ "the report. -->\n");
		
		// When no request body is specified, the context or prompt that needs to be filled
		// will be returned.  If no parameter has to be filled, the document is refreshed.
		
		// Send the request.
	    trace = request.send(requestURL, "PUT", null, "application/xml");
	    
	    // Store the request XML and response XML in  the directory specified by
	    // resourcesFolder appended with "traceflows/".
	    // If there is no request XML, then the requestContent will be null,  This
	    // will be the case if there was no request body from an XML file
	    // created with the request message.

	    // Retrieve the request.
	    requestContent =  request.getRequestContent();
	    
	    if (requestContent != null) {
	    	
	    	// Transform the request String to a Document.
		    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
		    
		    // Create the request message as a file.
		    commonUtils.createFileFromDocument(requestDoc, resourcesFolder + "/traceflows/", "Id_" + documentId + "_noprompts_request.xml");
	    }
	    
	    // Retrieve the response.
	    responseContent = request.getResponseContent();
	    
	    // Transform the response String to a Document.
	    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
	    
	    // Create the response message as a file.
	    commonUtils.createFileFromDocument(responseDoc, resourcesFolder + "/traceflows/", "Id_" + documentId + "_noprompts_response.xml");
	    
	    responseDoc.getDocumentElement().normalize();
	   // NodeList dp_tags = responseDoc.getElementsByTagName("message");
	    String Message_str = responseDoc.getElementsByTagName("message").item(0).getTextContent();
	    if (Message_str.contains("success"))
	    {
	    	Report_status = "The Report with Id : " + documentId + " and report name : " + report_name +" ran successfully";
	    	System.out.println("Report_status :  "+ Report_status);
	    	System.out.println("The Report ran successfully");
	    }
	    else
	    {
	    	Report_status = "<font size=\"3\" color=\"red\">" +  "The Report with Id : " + documentId + " and report name : " + report_name +" did not run successfully. Please check the issue - Error Message : " + Message_str + "</font>";
	    	System.out.println("Report_status :  "+ Report_status);
	    	System.out.println("The Report did not ran successfully");
	    }
	    
	    consolidatedTrace.append(trace);
	}
	else if (bodyType.startsWith("2")) {
		
		// This is for an example that has one normal prompt.
		
		// Display a message indicating what this trace is for.
		consolidatedTrace.append("<-- The following shows the trace that is meant for"
				+ " a Webi document that has one normal prompt. -->\n");
		
		consolidatedTrace.append("<-- The Webi report that is being traced has report ID = '"
				+ documentId + "' -->\n");
		
		consolidatedTrace.append("<-- The following GET call is intended to retrieve "
				+ "the list of possible values to the prompt. -->\n");
		
		String getRequestURL = authentication.getBipURL() + "/raylight/v1" + "/documents/" + documentId + "/parameters?lovInfo=" + lovInfo;
		
		// Step 1
		// Make a GET .../parameters call to return:
		// 	- The list of possible values for the answer to the "Year" prompt.
		trace = request.send(getRequestURL, "GET", null, "application/xml");
		
		// Store the request XML and response XML in  the directory specified by
	    // resourcesFolder appended with "traceflows/".
	    // If there is no request XML, then the requestContent will be null,  This
	    // will be the case if there was no request body from an XML file
	    // created with the request message.

	    // Retrieve the request.
	    requestContent =  request.getRequestContent();
	    
	    if (requestContent != null) {
	    	
	    	// Transform the request String to a Document.
		    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
		    
		    // Create the request message as a file.
		    commonUtils.createFileFromDocument(requestDoc, resourcesFolder + "/traceflows/", "Id_" + documentId + "_onenormalprompt_step1__request.xml");
	    }
	    
	    // Retrieve the response.
	    responseContent = request.getResponseContent();
	    
	    // Transform the response String to a Document.
	    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
	    
	    // Create the response message as a file.
	    commonUtils.createFileFromDocument(responseDoc, resourcesFolder + "/traceflows/", "Id_" + documentId + "_onenormalprompt_step1_response.xml");

		consolidatedTrace.append(trace);
					
		xmlPath = resourcesFolder + "/requests/RefreshDocumentNormalPrompt_XML.xml";
				
		// Retrieves the specified refreshdocument XML body as an XML flow.
	    xmlContent = commonUtils.retrieveXmlFromFile(xmlPath);
	    
	    consolidatedTrace.append("\n\n<-- The following PUT call is intended to send "
				+ "the answer to the prompt. -->\n");
	    
	    // Step 2 - Send a PUT request to refresh the document with the answer to the prompt.
	    trace = request.send(requestURL, "PUT", xmlContent, "application/xml");
	    
	    // Store the request XML and response XML in  the directory specified by
	    // resourcesFolder appended with "traceflows/".
	    // If there is no request XML, then the requestContent will be null,  This
	    // will be the case if there was no request body from an XML file
	    // created with the request message.

	    // Retrieve the request.
	    requestContent =  request.getRequestContent();
	    
	    if (requestContent != null) {
	    	
	    	// Transform the request String to a Document.
		    requestDoc = commonUtils.transformXmlStringToDocument(requestContent);
		    
		    // Create the request message as a file.
		    commonUtils.createFileFromDocument(requestDoc, resourcesFolder + "/traceflows/", "Id_" + documentId + "_onenormalprompt_step2_request.xml");
	    }
	    
	    // Retrieve the response.
	    responseContent = request.getResponseContent();
	    
	    // Transform the response String to a Document.
	    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
	    
	    // Create the response message as a file.
	    commonUtils.createFileFromDocument(responseDoc, resourcesFolder + "/traceflows/", "Id_" + documentId + "_onenormalprompt_step2_response.xml");
	    
	    responseDoc.getDocumentElement().normalize();
		   // NodeList dp_tags = responseDoc.getElementsByTagName("message");
		    String Message_str = responseDoc.getElementsByTagName("message").item(0).getTextContent();
		    
		    if (Message_str.contains("success"))
		    {
		    	Report_status = "The Report with Id : " + documentId + " and report name : " + report_name +" ran successfully";
		    	System.out.println("Report_status :  "+ Report_status);
		    	System.out.println("The Report ran successfully");
		    }
		    else
		    {
		    	Report_status ="<font size=\"3\" color=\"red\">" +  "The Report with Id : " + documentId + " and report name : " + report_name +" did not run successfully. Please check the issue - Error Message : " + Message_str + "</font>";
		    	System.out.println("Report_status :  "+ Report_status);
		    	System.out.println("The Report did not ran successfully");
		    }
	    
	    consolidatedTrace.append(trace);
	}
	return Report_status.toString();
}
public String getRefreshParameters(String documentId, String lovInfo, String resourcesFolder) throws Exception {
	// Returns the parameters to be filled before running a document refresh. 
	// Request --> GET <webiURL>/documents/{documentId}/parameters?lovInfo={true|false}
	// Where:
	// 	{documentId} is the document identifier retrieved from the list of documents 
	//	lovInfo is an optional, Boolean parameter. Default value is true. If set to
	//	false, the lists of values are not computed, nor displayed. 
	// The response provides the parameters with their expected answers, previous values
	// if applicable, otherwise default values.
	
	// There is no request body so we don't need to create the XML content from
	// an XML file.
 	
    // Set up the request URL
    String requestURL = authentication.getBipURL() + "/raylight/v1" + "/documents/" + documentId + "/parameters";
    		//+ "?lovInfo=" + //lovInfo
    
    String trace;
    
    // Used to store the response string.
		String responseContent;
		
		// Used to store the response document object.
		Document responseDoc;
		
    // Send the request.
    trace = request.send(requestURL, "GET", null, "application/xml");
    
    // Retrieve the response.
    responseContent = request.getResponseContent();
    
    // Transform the response String to a Document.
    responseDoc = commonUtils.transformXmlStringToDocument(responseContent);
    
    // Create the response message as a file.
    commonUtils.createFileFromDocument(responseDoc, resourcesFolder + "\\traceflows\\", "Id_" + documentId + "_getRefreshParameters_response.xml");

    return trace;
}

}

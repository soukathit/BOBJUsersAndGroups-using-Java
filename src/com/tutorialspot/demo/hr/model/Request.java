package com.tutorialspot.demo.hr.model;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Request {

	private static boolean isTrace = true;
    
    private String requestUrl, requestMethod, requestContent, responseContent, responseMessage;
    private int responseCode;
    private Map<String, List<String>> responseHeaders;
    private String logonToken;

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
 
    public String getRequestContent() {
		return requestContent;
	}

	public String getResponseContent() {
        return responseContent;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public static boolean getIsTrace() {
        return isTrace;
    }

    public static void setIsTrace(boolean trace) {
        Request.isTrace = trace;
    }

    // Constructor
    public Request() {
    }

    // Constructor
    public Request(String logonToken) {
        this.logonToken = logonToken;
    }
    
    /**
     * @see #send(String, String, String, String)
     */
    public String send(String url, String method, String xmlContent) throws Exception {
    	String trace = send(url, method, xmlContent, "application/xml");
    	System.out.println("Trace1:==" + trace);
    	return trace;        
    }
    
    /**
     * Utility method to send HTTP requests.
     * <p>
     * It provides the following features:
     * <ul>
     * <li>Handling SAP-specific headers, such as X-SAP-LogonToken</li>
     * <li>Allowing to add XML content to the request</li>
     * <li>Reading the server response, available via the members
     * <i>responseContent</i>, <i>responseCode</i> and <i>responseMessage</i></li>
     * <li>Showing the request and response in the console by setting the static
     * member <i>trace</i> value to <i>true</i></li>
     * </ul>
     * </p>
     * 
     * @param url
     *            The URL to send
     * @param method
     *            The HTTP request method (GET, POST, PUT or DELETE)
     * @param xmlContent
     *            The XML content to send along with the request, or <i>null</i>
     *            if no content has to be sent
     * @param accept
     *            Accept header value, corresponding to the requested format for
     *            the response
     * @throws Exception
     */
    public String send(String url, String method, String xmlContent, String accept) throws Exception {
    
    	this.reset();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);

        if (accept != null) {
            connection.setRequestProperty("Accept", accept);
        }

        if (this.logonToken != null) {
            connection.setRequestProperty("X-SAP-LogonToken", this.logonToken);
        }

        if (xmlContent != null) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setRequestProperty("Content-Length", String.valueOf(xmlContent.getBytes().length));
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(xmlContent);
            out.flush();
            out.close();
        }

        // Reads response
        InputStream in;
        try {
            in = (InputStream) connection.getContent();
        } catch (IOException e) {
            in = connection.getErrorStream();
        }
        if (in == null)
            throw new Exception("Connection to " + url + " failed");

        Scanner scanner = new Scanner(in).useDelimiter("\\A");

        requestUrl = url;
        requestMethod = method;
        requestContent = xmlContent;
        responseContent = scanner.hasNext() ? scanner.next() : "";
        responseCode = connection.getResponseCode();
        responseMessage = connection.getResponseMessage();
        responseHeaders = connection.getHeaderFields();

        String trace = "";
        if (isTrace)
            trace = trace(connection);

        in.close();
        connection.disconnect();
        
        return trace;
    }
    
    public void send(String url, String method, String contentXml, String contentType, String accept) 
			throws Exception {
		this.reset();
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod(method);

		if (accept != null) {
			connection.setRequestProperty("Accept", accept);
		}

		if (this.logonToken != null) {
			connection.setRequestProperty("X-SAP-LogonToken", this.logonToken);
		}

		if (contentXml != null) {
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", contentType);
			connection.setRequestProperty("Content-Length", String.valueOf(contentXml.getBytes().length));
			//connection.setRequestProperty("Accept-Language", "en_US");
			// minus : −     tiret 6 : -    underscore : _
			
			//connection.setRequestProperty("X-SAP-PVL", "en_US");
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(contentXml);
			out.flush();
			out.close();
		}

		// Reads response
		InputStream in;
		try {
			in = connection.getInputStream();
		} catch (IOException e) {
			in = connection.getErrorStream();
		}
		if (in == null) {
			throw new Exception("Connection to " + url + " failed");
		}

		Scanner scanner = new Scanner(in).useDelimiter("\\A");

		this.requestUrl = url;
		this.requestMethod = method;
		this.requestContent = contentXml;
		this.responseContent = scanner.hasNext() ? scanner.next() : "";
		this.responseCode = connection.getResponseCode();
		this.responseMessage = connection.getResponseMessage();
		this.responseHeaders = connection.getHeaderFields();

		
			trace(connection);

		in.close();
		connection.disconnect();
	}
    
    public String sendBinaryOutput(String url, String method, String xmlContent, String accept, String filePath, String documentId) throws Exception {
        this.reset();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);

        if (accept != null) {
            connection.setRequestProperty("Accept", accept);
        }

        if (this.logonToken != null) {
            connection.setRequestProperty("X-SAP-LogonToken", this.logonToken);
        }

        if (xmlContent != null) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setRequestProperty("Content-Length", String.valueOf(xmlContent.getBytes().length));
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(xmlContent);
            out.flush();
            out.close();
        }

        // Extract the file name from the Content Disposition header tag.
        String contentDisposition = connection.getHeaderField("Content-Disposition");
        
        String fileName = "";
        
        // If the file name hasn't been specified, then the Content Disposition may
        // be null.
        if (contentDisposition != null) {
        	int index = contentDisposition.indexOf("filename=");
        	
        	if (index > 0) {
                fileName = contentDisposition.substring(index + 10,
                		contentDisposition.length() - 1);
            } else {
            	// Specify a default file name.
            	fileName = setupFileName("", accept, documentId);
            }
        } else {
        	// If the file name was not able to be extracted from the Content Disposition,
            // then check to see if the file name was specified in the url.  If the file name
        	// was not specified in the url either, then specify a default file name.
        	int urlIndex = url.indexOf("fileName=");
        	
        	if (urlIndex > 0) {
                fileName = url.substring(urlIndex + 9,
                		url.length());
                
                fileName = setupFileName(fileName, accept, documentId);
            } else {
            	// Specify a default file name that uses the report ID.
            	fileName = setupFileName("", accept, documentId);
            }
        }
                
        InputStream inputStream = connection.getInputStream();
        
        FileOutputStream outputStream = new FileOutputStream(filePath + fileName);
        
        int bytesRead = -1;
        byte[] buffer = new byte[4096];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        requestUrl = url;
        requestMethod = method;
        requestContent = xmlContent;
        
        // Don't print out the response content because it is not readable and it is being
        // saved to a file.  Specify that the response content has been saved to a file.
        responseContent = "Response content is binary so it has been outputted to a file " +
        		"to the location '" + filePath + "' with the following file name '" + fileName + "'";
        responseCode = connection.getResponseCode();
        responseMessage = connection.getResponseMessage();
        responseHeaders = connection.getHeaderFields();

        String trace = "";
        
        if (isTrace)
        	trace = trace(connection);

        connection.disconnect();
        
        return trace;
    }
    
    // Private

    /**
     * Clears the request information, response content and SAP-specific
     * headers.
     */
    private void reset() {
        requestUrl = null;
        requestMethod = null;
        requestContent = null;
        responseContent = null;
        responseMessage = null;
        responseHeaders = null;
        responseCode = 0;
    }

    /**
     * Traces the HTTP URL connection.
     * 
     * @param connection
     * @throws Exception
     */
    private String trace(HttpURLConnection connection) throws Exception {
        String trace = "\n" + toString() + "\n";
        trace += "=== Headers ===\n";
        for (String key : connection.getHeaderFields().keySet()) {
            trace += key + ": " + connection.getHeaderFields().get(key) + "\n";
        }
        // Adds a leading | for console readability
        trace = trace.replaceAll("\r\n", "\n");
        trace = trace.replaceAll("\n", "\n| ");
        trace = trace.replaceAll("\n\\| $", "");

        return trace;
    }

    /**
     * Displays the request and the response information.
     */
    @Override
    public String toString() {
        String message = "[%s] %s\n" + "=== Request content ===\n" + "%s\n" + "=== Response code ===\n" + "%d\n"
                + "=== Response message ===\n" + "%s\n" + "=== Response content ===\n" + "%s";
        return String.format(message, requestMethod, requestUrl, requestContent, responseCode, responseMessage, responseContent);
    }
    
    private String setupFileName(String fileName, String accept, String documentId) {
    	
    	if (accept.equals("text/xml")) {
    		if (fileName.equals("")) {
    			fileName = documentId + "_outputxmlfile.xml";
    		} else {
    			fileName += ".xml";
    		}    			
    	}
    	else if (accept.equals("application/zip")) {
    		if (fileName.equals("")) {
    			fileName = documentId + "_outputzipfile.zip";
    		} else {
    			fileName += ".zip";
    		}    			
    	} else if (accept.equals("application/pdf")) {    		
    		if (fileName.equals("")) {
    			fileName = documentId + "_outputpdffile.pdf";
    		} else {
    			fileName += ".pdf";
    		}  
    	} else if(accept.equals("application/vnd.ms-excel")) {
    		if (fileName.equals("")) {
    			fileName = documentId + "_outputexcelfile.xls";
    		} else {
    			fileName += ".xls";
    		} 
    	} else if(accept.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
    		if (fileName.equals("")) {
    			fileName = documentId + "_outputexcelfile.xlsx";
    		} else {
    			fileName += ".xlsx";
    		} 
    	}
    	
    	return fileName;
    }
}

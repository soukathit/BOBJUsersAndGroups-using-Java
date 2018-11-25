package com.tutorialspot.demo.hr.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


//import com.crystaldecisions.enterprise.ocaframework.IProperty;
//import com.businessobjects.mds.universe.business.Attribute;
//import com.businessobjects.connectionserver.Measure;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.properties.IProperties;
import com.crystaldecisions.sdk.properties.IProperty;
import com.sap.sl.sdk.authoring.samples.util.AuthenticationMode;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

public class BOBJUsersAndGroupsMain {
	
//private static final String CMS_LOG_HOST = "sbj-prod-a5.vmware.com:6400";
	
	private final String USER_AGENT = "Mozilla/5.0";

	/** User name used to log in to the CMS */

	/** User Password */

	/** Authentication mode used to log in to the CMS. Here: Enterprise */
    private static final String CMS_AUTH_MODE = AuthenticationMode.ENTERPRISE;
    
    /** Full path of the connection in the CMS repository */
    //private static final String CNX_PATH = CmsResourceService.CONNECTIONS_ROOT + "/BO to Greenplum (JDBC)";
    
    /** Full path of the connection in the CMS repository */
    //private static final String UNX_PATH = CmsResourceService.UNIVERSES_ROOT + "/Backup/ByWeek";
    
    /** Universe name */
    private static final String UNX_NAME = "Campaign Influence.unx";
    
    static String dbName = "BINRTUT2";
    
    
    static Connection connection = null;
    
    List<Object> listFolder = new ArrayList<Object>();
    List<Object> listDimension = new ArrayList<Object>();
    List<Object> listAttribute = new ArrayList<Object>();
    List<Object> listMeasure = new ArrayList<Object>();
    List<Object> listFilter = new ArrayList<Object>();
	
	/** Local folder used to save all resources locally */
    //private static final String LOCAL_FOLDER = System.getProperty("java.io.tmpdir");
    private static final String LOCAL_FOLDER = "D://Soukath Folder//BO Shared Folder//SL SDK Project Test";
    
    String Universe_Name = UNX_NAME;
	/** New user name to set in the connection */


	/** New password to set in the connection */


    private static SlContext context;
    
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    static Date date = new Date();
    static String current_Date_parameter = sdf.format(date);
    static Properties prop=null;
    //private IEnterpriseSession enterpriseSession;
    
	public static void main(String[] args) throws SQLException, SDKException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		 //System.out.println("LOCAL_FOLDER : " + LOCAL_FOLDER);
			// Connects to the CMS and creates a session
	    	//System.out.println("@ Before Execution");
			prop = new Properties();
			prop.load(new FileInputStream(args[0]));
	        context = SlContext.create();
	        //System.out.println("@ Before Execution - Context Creation");
	        
	        String to = "XXXXX@XXXXX.com";

	        // Sender's email ID needs to be mentioned
	        String from = "XXXXX@XXXXX.com";

	        // Assuming you are sending email from localhost
	        String host = "XXXXXXX.com";

	        // Get system properties
	        Properties properties = System.getProperties();

	        // Setup mail server
	        properties.setProperty("mail.smtp.host", host);

	        // Get the default Session object.
	        Session session = Session.getDefaultInstance(properties);
	    	

	     Connection connection = null;
	     Statement stmt = null;
	     String SQL_INSERT_BOBJ_USERS = "INSERT INTO VCORE.BOBJ_USERS" + " VALUES(?, ?, ?,?,?,?)";
	     String SQL_INSERT_BOBJ_ROLES = "INSERT INTO VCORE.BOBJ_ROLES" + " VALUES(?, ?, ?,?,?)";
	     String SQL_INSERT_BOBJ_USER_ROLES = "INSERT INTO VCORE.BOBJ_USER_ROLES" + " VALUES(?, ?,?,?)";
	     String SQL_DELETE_BOBJ_USERS = "DELETE FROM VCORE.BOBJ_USERS";
	     String SQL_DELETE_BOBJ_ROLES = "DELETE FROM VCORE.BOBJ_ROLES";
	     String SQL_DELETE_BOBJ_USER_ROLES = "DELETE FROM VCORE.BOBJ_USER_ROLES";
	     
	     //String CMServerURL = prop.getProperty("CMServerURL");
	     String userName =prop.getProperty("userName");
	     
	     String password =prop.getProperty("password");
	     String HANA_DB = prop.getProperty("HANA_DB");	     
	     try {
	    	 
	    	 connection = DriverManager.getConnection(
	    			 HANA_DB,userName,password);
 		         //System.out.println("SAP HANA DB Connected Successfully");
				//System.out.println("Connected :" + connection.getSchema() );

			} catch (SQLException e) {

				e.printStackTrace();
			}
	  
	  if (connection != null) {
				//System.out.println("nSuccessfullly connected to HANA DB");
				stmt = connection.createStatement();
				
			} else {
				//System.out.println("nFailed to connect to HANA DB");
				 
		        try {
		            // Create a default MimeMessage object.
		            MimeMessage message1 = new MimeMessage(session);

		            // Set From: header field of the header.
		            message1.setFrom(new InternetAddress(from));

		            // Set To: header field of the header.
		            message1.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		            // Set Subject: header field
		            message1.setSubject("Alert : BOBJ Users and Groups Schedule Task Update");

		            // Send the actual HTML message, as big as you like
		            message1.setContent("<h3>Alert : Could not connect to HANA Prod Database</h3>", "text/html");

		            // Send message
		            Transport.send(message1);
		         //   System.out.println("Sent message successfully....");
		         }catch (Exception mex) {
		            mex.printStackTrace();
		         }
			}
	  	PreparedStatement statement_bobj_users = connection.prepareStatement(SQL_INSERT_BOBJ_USERS);
	  	PreparedStatement statement_bobj_roles = connection.prepareStatement(SQL_INSERT_BOBJ_ROLES);
	  	PreparedStatement statement_bobj_user_roles = connection.prepareStatement(SQL_INSERT_BOBJ_USER_ROLES);
	  	
	  	stmt.executeUpdate(SQL_DELETE_BOBJ_USERS);
	  	stmt.executeUpdate(SQL_DELETE_BOBJ_ROLES);
	  	stmt.executeUpdate(SQL_DELETE_BOBJ_USER_ROLES);
	  	
	  	//System.out.println("Code Execution Started");
	  	
	  	String bipURL = prop.getProperty("bipURL");  
	  	//System.out.println(bipURL);
	   
		String CMServerURL = prop.getProperty("CMServerURL"); 
	    String documentId = "190149";
	    String CMSUser = prop.getProperty("CMSUser"); 
	    String CMSPassword = prop.getProperty("CMSPassword"); 
	    String CMSAuthType =CMS_AUTH_MODE;// "secEnterprise";
	    //System.exit(0);
	   // IEnterpriseSession enterpriseSession = CrystalEnterprise.getSessionMgr().logon(CMS_LOG_USER, CMS_LOG_PASS, CMServerURL, CMS_AUTH_MODE);
	    IEnterpriseSession enterpriseSession = CrystalEnterprise.getSessionMgr().logon(CMSUser, CMSPassword, CMServerURL, CMSAuthType);
	    context.getService(CmsSessionService.class).setSession(enterpriseSession);
	    IInfoStore infoStore = (IInfoStore) enterpriseSession.getService("InfoStore");
	    
	    // Code to get all the Users in the Enterprise System 
	    String query = "Select TOP 100000 SI_NAME, SI_USERFULLNAME, SI_ID, SI_NAMEDUSER, SI_LASTLOGONTIME, SI_DESCRIPTION, SI_ALIASES, SI_EMAIL_ADDRESS From CI_SYSTEMOBJECTS Where SI_KIND='User'";
	    IInfoObjects iObjects = (IInfoObjects) infoStore.query(query);
	    IInfoObject iObject = null;
	    for(int i=0;i<iObjects.size();i++)
	    {	
	    	 iObject = (IInfoObject)iObjects.get(i);
	    	 //System.out.println("User Name Kind: " + iObject.getKind());
	    	 IProperties prop = iObject.properties();
	    	 IProperty getProp_userfullname = prop.getProperty("SI_USERFULLNAME");
	    	 IProperty getProp_NAMEDUSER = prop.getProperty("SI_NAMEDUSER");
	    	 //IProperty getProp_SI_LASTLOGONTIME = prop.getProperty("SI_LASTLOGONTIME");
	    	 IProperty getProp_SI_DESCRIPTION = prop.getProperty("SI_DESCRIPTION");
	    	 IProperty getProp_SI_ALIASES = prop.getProperty("SI_ALIASES");
	    	 IProperty getProp_SI_EMAIL_ADDRESS = prop.getProperty("SI_EMAIL_ADDRESS");
	    	 System.out.println(iObjects.get(i).toString());
	    	System.out.println("User Name Identifier: " + iObject.getID());
	    	  System.out.println("User Name : " + iObject.getTitle() );
	    	  System.out.println("User Name Description: " + iObject.getDescription());
	    	  System.out.println("User Full Name: " + getProp_userfullname.toString());
	    //	  System.out.println("getProp_NAMEDUSER: " + getProp_NAMEDUSER.toString());
	    	  //System.out.println("getProp_SI_LASTLOGONTIME: " + getProp_SI_LASTLOGONTIME.toString());
	    	//  System.out.println("getProp_SI_DESCRIPTION: " + getProp_SI_DESCRIPTION.toString());
	    	  //System.out.println("getProp_SI_ALIASES: " + getProp_SI_ALIASES.toString());
	    	  //System.out.println("getProp_SI_EMAIL_ADDRESS: " + getProp_SI_EMAIL_ADDRESS.toString());
	    	  statement_bobj_users.setString(1, String.valueOf(iObject.getID()));
	    	  statement_bobj_users.setString(2, iObject.getTitle());
	    	  statement_bobj_users.setString(3, getProp_userfullname.toString());
	    	  statement_bobj_users.setString(4, getProp_SI_EMAIL_ADDRESS.toString());
	    	  statement_bobj_users.setString(5, CMServerURL);
	    	  statement_bobj_users.setString(6, current_Date_parameter);
	    	  statement_bobj_users.executeUpdate();
	    	  
	    	  }
	 // Code to get all the Users & Groups in the Enterprise System 
	    String query_usergroups = "Select TOP 100000 SI_ID, SI_NAME, SI_REL_USERGROUPS, SI_USERGROUPS, SI_GROUP_MEMBERS,  SI_SUBGROUPS from CI_SYSTEMOBJECTS where SI_KIND = 'UserGroup'";
	    IInfoObjects iObjects_user_groups = (IInfoObjects) infoStore.query(query_usergroups);
	    IInfoObject iObject_user_groups = null;
	    for(int i=0;i<iObjects_user_groups.size();i++)
	    {	
	    	iObject_user_groups = (IInfoObject)iObjects_user_groups.get(i);
	    	 //System.out.println(iObjects.get(i).toString());
	    	IProperties prop = iObject_user_groups.properties();
	    	IProperty getProp_SI_ID1 = prop.getProperty("SI_ID");
	    	IProperty getProp_SI_NAME1 = prop.getProperty("SI_NAME");
	    	IProperty getProp_SI_REL_USERGROUPS1;
	    	IProperty getProp_SI_SUBGROUPS1 = null;;
	    	IProperty getProp_SI_GROUP_MEMBERS1;
	    	//IUserGroup user_groups;
	    	//user_groups.getUsers();
	    	 System.out.println("Group Name Identifier: " + iObject_user_groups.getID());
	    	  System.out.println("Group Name : " + iObject_user_groups.getTitle() );
	    	  System.out.println("Group Name Id: " + getProp_SI_ID1.toString());
	    	  System.out.println("Group Name Description: " + getProp_SI_NAME1.toString());
	    	  //System.out.println("Group Name Description: " + getProp_SI_REL_USERGROUPS1.toString());
	    	  //System.out.println("Group Name Description: " + getProp_SI_GROUP_MEMBERS1.toString());
	    	 // System.out.println("Group Name Description: " + getProp_SI_SUBGROUPS1.toString());
	    	  statement_bobj_roles.setString(1, String.valueOf(iObject_user_groups.getID()));
	    	  statement_bobj_roles.setString(2, iObject_user_groups.getTitle());
	    	  statement_bobj_roles.setString(3, getProp_SI_NAME1.toString());
	    	  statement_bobj_roles.setString(4, current_Date_parameter);
	    	  statement_bobj_roles.setString(5, CMServerURL);
	    	  statement_bobj_roles.executeUpdate();
	    	  }
	    
	    System.out.println("User and Group Information Starts");
	    System.out.println("-----------------------------------");
	    
	    String sQueryUsers = "SELECT TOP 100000 SI_ID, SI_NAME, SI_USERGROUPS FROM CI_SYSTEMOBJECTS WHERE SI_KIND = 'User'"; 
	    IInfoObjects users = infoStore.query(sQueryUsers); 
	    for (int i=0; i<users.size(); i++) { 
	        
	    	   // Get user and display properties 
	    	   IInfoObject user = (IInfoObject)users.get(i); 
	    	   //System.out.println("user SI_NAME = " + user.getTitle() + " - user SI_ID = " + user.getID()); 
	    	   //System.out.println("<br />Belongs to the following user group(s):<br />"); 
	    	    
	    	   // Get user group membership from SI_USERGROUPS property bag 
	    	   IProperties userProps = (IProperties)user.properties().getProperty("SI_USERGROUPS").getValue(); 
	    	   int usergroupCount = ((Integer)userProps.getProperty("SI_TOTAL").getValue()).intValue(); 

	    	   // Loop through each user group 
	    	   for (int j = 1; j <= usergroupCount; j++) { 
	    	      // Get user group SI_ID 
	    	      String groupID = userProps.getProperty(Integer.toString(j)).getValue().toString(); 
	    	       
	    	      // Get user group SI_NAME 
	    	      String sQueryGroupName = "SELECT TOP 1000000 SI_NAME FROM CI_SYSTEMOBJECTS WHERE SI_ID = '" + groupID + "' AND SI_KIND = 'UserGroup'"; 
	    	      IInfoObjects groups = infoStore.query(sQueryGroupName); 
	    	      String groupName = ((IInfoObject)groups.get(0)).getTitle(); 
	    	     
	    	      // Display user group properties 
	    	      System.out.println( String.valueOf(user.getID()));
	    	      System.out.println(j + " - group SI_ID = " + groupID + " - group SI_NAME = " + groupName + "<br />"); 
	    	      statement_bobj_user_roles.setString(1, String.valueOf(user.getID()));
	    	      statement_bobj_user_roles.setString(2, groupID);
	    	      statement_bobj_user_roles.setString(3, CMServerURL);
	    	      statement_bobj_user_roles.setString(4, current_Date_parameter);
	    	      statement_bobj_user_roles.executeUpdate();
	    	   } 

	    	   //System.out.println("<hr />"); 
	    	} 


	    connection.commit();
	    	//System.out.println("@ After Execution");
	    enterpriseSession.logoff();
	    connection.close();
			// Closes the CMS session
	        context.close();
	        //System.out.println("@ After Execution -  Context Close and Program Ended Successfully");
	        //enterpriseSession.logoff();
	        
	        
	        
	        try {
	            // Create a default MimeMessage object.
	            MimeMessage message = new MimeMessage(session);

	            // Set From: header field of the header.
	            message.setFrom(new InternetAddress(from));

	            // Set To: header field of the header.
	            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	            // Set Subject: header field
	            message.setSubject("BOBJ Users and Groups Schedule Task Update");

	            // Send the actual HTML message, as big as you like
	            message.setContent("<h3>The BOBJ Users and Groups Job Completed Successfully</h3>", "text/html");

	            // Send message
	            Transport.send(message);
	         //   System.out.println("Sent message successfully....");
	         }catch (Exception mex) {
	            mex.printStackTrace();
	         }

	}

}

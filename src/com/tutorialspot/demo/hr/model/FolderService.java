package com.tutorialspot.demo.hr.model;

import java.io.File;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

















//import com.businessobjects.connectionserver.Measure;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.sap.sl.sdk.authoring.businesslayer.Attribute;
import com.sap.sl.sdk.authoring.businesslayer.BlContainer;
import com.sap.sl.sdk.authoring.businesslayer.BlItem;
import com.sap.sl.sdk.authoring.businesslayer.BusinessFilter;
import com.sap.sl.sdk.authoring.businesslayer.BusinessLayer;
import com.sap.sl.sdk.authoring.businesslayer.BusinessLayerService;
import com.sap.sl.sdk.authoring.businesslayer.Dimension;
import com.sap.sl.sdk.authoring.businesslayer.Filter;
import com.sap.sl.sdk.authoring.businesslayer.Folder;
import com.sap.sl.sdk.authoring.businesslayer.Measure;
import com.sap.sl.sdk.authoring.businesslayer.NativeRelationalFilter;
import com.sap.sl.sdk.authoring.businesslayer.RelationalBinding;
import com.sap.sl.sdk.authoring.businesslayer.RelationalBusinessLayer;
import com.sap.sl.sdk.authoring.businesslayer.RootFolder;
import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.authoring.connection.DatabaseConnection;
import com.sap.sl.sdk.authoring.connection.RelationalConnection;
import com.sap.sl.sdk.authoring.datafoundation.LovColumn;
import com.sap.sl.sdk.authoring.datafoundation.MonoSourceDataFoundation;
import com.sap.sl.sdk.authoring.local.LocalResourceService;
import com.sap.sl.sdk.authoring.samples.util.AuthenticationMode;
import com.sap.sl.sdk.authoring.samples.util.SamplesUtilities;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

import oracle.sql.CLOB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FolderService {
	List folderList = new ArrayList<Folder>();
	List folderList1 = new ArrayList<Folder>();
	String dbName = "BINRTUT2";
    
    String userName = "XXXXX";
    
    String password = "XXXXXX";
	
    public List<Folder> getChildren(List<BlItem> blItems) {
    	
    	if(blItems!= null) {
    		for (BlItem blItem : blItems) {
    			if(blItem instanceof Folder){
    				//System.out.println("Folder Name : " + blItem.getName() );
    				folderList1.add(blItem.getName());
    				//System.out.println("Folder Size : " + folderList1.size() );
    		          //List<BlItem> kids = ((Folder) blItem).getChildren();
    				Folder f =(Folder)blItem ;
    				List<BlItem> kids = f.getChildren();

    		          if(kids != null) 
    		          {
    		        	  
    		        	  for (int i = 0; i < kids.size(); i++) 
    						{
    		        			//System.out.println(kids.get(i));
    						}
    		        	  getChildren(kids);
    		          }
    			} 
    			//else return null;
    		}
    	}
    	return folderList1;
    }
    
 public List<String> getAllFolders(List<BlItem> blItems ,BusinessLayerService blservice,String Universe_Name) throws SQLException {
    	
	 try {
       	 
			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
     Connection connection = null;
     Statement stmt = null;
     String SQL_INSERT_FOLDER = "INSERT INTO DSD_BI_INFO.BOBJ_UNV_FOLDERS" + " VALUES(?, ?, ?, ?, ?, ?, ? , ? ,?)";
     String SQL_INSERT_DIM = "INSERT INTO DSD_BI_INFO.BOBJ_UNV_DIMENSIONS" + " VALUES(?, ?, ?, ?, ?, ?, ?,? , ?,? )";
     String SQL_INT_DIM_FOL_PATH = "INSERT INTO DSD_BI_INFO.BOBJ_UNV_DIM_FOL_PATH" + " VALUES(?, ?, ?, ?, ?, ?, ? ,?,?,?,?)";
	 String SQL_INT_DIM_MEAS_FOL_PATH = "INSERT INTO DSD_BI_INFO.BOBJ_UNV_MEAS_DIM_FOL_PATH" + " VALUES(?, ?, ?, ?, ?, ?, ? ,?,?,?,?,?)";
     String SQL_INSERT_MEASURES = "INSERT INTO DSD_BI_INFO.BOBJ_UNV_MEASURES" + " VALUES(?, ?, ?, ?, ?, ?, ?,? ,?,? )";
	 String SQL_INSERT_MEASURES_DIM = "INSERT INTO DSD_BI_INFO.BOBJ_UNV_MEAS_DIMENSIONS" + " VALUES(?, ?, ?, ?, ?, ?, ?,? ,?,? ,?)";
     String SQL_INT_MEASURE_FOL_PATH = "INSERT INTO DSD_BI_INFO.BOBJ_UNV_MEAS_FOL_PATH" + " VALUES(?, ?, ?, ?, ?, ?, ? ,?,?,?,?)";
     String SQL_INT_FILTER_FOL_PATH = "INSERT INTO DSD_BI_INFO.BOBJ_UNV_FILTERS_FOL_PATH" + " VALUES(?, ?, ?, ?, ?, ?, ? ,?,?,?,?)";
     String dim_object_type = "DIMENSION";
	 String measure_object_type = "MEASURE";
     
     try {
    	 
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@ora-uat-binrt-d2.vmware.com:1521:" + dbName, userName,
					password);
			//System.out.println("Connected :" + connection.getSchema() );

		} catch (SQLException e) {

			e.printStackTrace();
		}
     
     if (connection != null) {
			System.out.println("nSuccessfullly connected to Oracle DB");
			stmt = connection.createStatement();
			
		} else {
			System.out.println("nFailed to connect to Oracle DB");
		}
     	PreparedStatement statement_folder = connection.prepareStatement(SQL_INSERT_FOLDER);
     	PreparedStatement statement_dimensions = connection.prepareStatement(SQL_INSERT_DIM);
		PreparedStatement statement_dim_path = connection.prepareStatement(SQL_INT_DIM_FOL_PATH);
		PreparedStatement statement_measures = connection.prepareStatement(SQL_INSERT_MEASURES);
		PreparedStatement statement_measure_path = connection.prepareStatement(SQL_INT_MEASURE_FOL_PATH);
		PreparedStatement statement_filter_path = connection.prepareStatement(SQL_INT_FILTER_FOL_PATH);
		PreparedStatement statement_dim_meas_path1 = connection.prepareStatement(SQL_INT_DIM_MEAS_FOL_PATH);
		PreparedStatement statement_dim_meas_path2 = connection.prepareStatement(SQL_INT_DIM_MEAS_FOL_PATH);
		PreparedStatement statement_dim_meas1 = connection.prepareStatement(SQL_INSERT_MEASURES_DIM);
		PreparedStatement statement_dim_meas2 = connection.prepareStatement(SQL_INSERT_MEASURES_DIM);
		
    	if(blItems!= null) {
    		for (BlItem blItem : blItems) {
    			if(blItem instanceof Folder){
    				String folder_identifier = blItem.getIdentifier();
    				String folder_name = blItem.getName();
    				String folder_desc = blItem.getDescription();
    				String folder_name_parent = blItem.getParent().getName();
    				Integer folder_hash_code1 = blItem.hashCode();
    				String folder_hash_code = folder_hash_code1.toString();
    				String folder_state = blItem.getState().getName();
    				//System.out.println("Folder Name : " + blItem.getName() );
    				//System.out.println("Folder Name Class: " + blItem.getClass() );
    				//System.out.println("Parent Folder Name: "+ blItem.getParent().getName());
    				//System.out.println("Folder Path: "+ blItem.getPath());
    				//System.out.println("Folder Path Identifier: "+ blItem.getIdentifier());
    				//PreparedStatement statement = connection.prepareStatement(SQL_INSERT_FOLDER);
    				//BlContainer blcontainer = blItem.getParent();
    				folderList.add(blItem.getName());
    				//System.out.println("Folder Size : " + folderList.size() );
    				Folder f =(Folder)blItem ;
    				List<BlItem> kids = f.getChildren();
    				String rootFolderName = f.getName();
    				String parentrootFolderName = f.getParent().getName();
    				if (rootFolderName != "") {
    					BlContainer parent = f.getParent();
    					String parentName = "";
    					if (parent != null)
    						parentName = parent.getName();
    					StringBuilder path = new StringBuilder();
    					path = getPath(parent, rootFolderName, path);
    					BlContainer parent_folder = f.getParent().getParent();
    					StringBuilder parent_path = new StringBuilder();
    					parent_path = getPath(parent_folder, parentrootFolderName, parent_path);
    					
    					//System.out.println("Full Modified Path : = " + path);
    					//System.out.println("Full Dimension Modified Parent Path : = " + parent_path);
    					String folder_path = path.toString();
    					String folder_parent_path = parent_path.toString();
    					statement_folder.setString(1, folder_identifier);
    					statement_folder.setString(2, folder_name);
    					statement_folder.setString(3, folder_desc);
    					statement_folder.setString(4, folder_name_parent);
    					statement_folder.setString(5, folder_path);
    					statement_folder.setString(6, folder_parent_path);
    					statement_folder.setString(7, folder_state);
    					statement_folder.setString(8, folder_hash_code);
    					statement_folder.setString(9, Universe_Name);
    					statement_folder.executeUpdate();
    					//stmt.executeUpdate("INSERT INTO DSD_BI_INFO.BOBJ_UNV_FOLDERS (FOLDER_IDENTIFER,  FOLDER_NAME,  FOLDER_DESC,  PARENT_FOLDER_NAME,  FOLDER_PATH,  FOLDER_STATE,  FOLDER_HASH_CODE)" + "VALUES ( " + folder_identifier + "," + folder_name + "," + folder_desc + "," + folder_name_parent + "," + path + "," + folder_state + "," + folder_hash_code + ")"  );
    				}
    		          if(kids != null) 
    		          {
    		        	  getAllFolders(kids,blservice,Universe_Name);
    		          }
    			} 
    			
    			else if(blItem instanceof Dimension){
    				String dim_identifier = blItem.getIdentifier();
    				String dim_name = blItem.getName();
    				String dim_desc = blItem.getDescription();
    				String dim_folder_name = blItem.getParent().getName();
    				
    				Integer dim_hash_code1 = blItem.hashCode();
    				String dim_hash_code = dim_hash_code1.toString();
    				String dim_state = blItem.getState().getName();
    				//PreparedStatement statement = connection.prepareStatement(SQL_INSERT_DIM);
    				//PreparedStatement statement_dim = connection.prepareStatement(SQL_INT_DIM_FOL_PATH);
    	        	Dimension d = (Dimension)blItem;
    	        	//d.getParent().getParent().getPath();
    	        	//System.out.println ("Dimension :" + d.getName());    	        	
    	        	//System.out.println ("Dimension Parent Folder Name :" + d.getParent().getName());
    	        	//System.out.println ("Dimension Parent/Parent Folder Name :" + d.getParent().getParent().getPath());
    	        	//System.out.println("Dimension Name Path: "+ blItem.getParent().getName());
    	        	//System.out.println("Dimension Name Identifier: "+ blItem.getIdentifier());
    	        	RelationalBinding binding = (RelationalBinding) d.getBinding();
    	        	System.out.println("Object " + d.getName() + " using SQL Definition: " + binding.getSelect());
    	        	Clob dim_expression = connection.createClob();
    	        	dim_expression.setString( 1, binding.getSelect());
    	        	//long dim_expression = (long)binding.getSelect();
    	        	//System.out.println("Object " + d.getName() + " using LOV: " + d.getAssociatedLov());
    	        	//System.out.println("Object " + d.getName() + " using SQL Definition: " + binding.getExtraTables());
    	        	//System.out.println ("Dimension :" + d.getSelect());
    	        	List tables_list =  blservice.getImplicitTables(d);
    	        	//System.out.println("Implicit Tables List Path" + blservice.getBlItemPath(d));
    	        	String rootFolderName = d.getName();
    	        	String parentrootFolderName = d.getParent().getName();
    				if (rootFolderName != "") {
    					BlContainer parent = d.getParent();
    					String parentName = "";
    					if (parent != null)
    						parentName = parent.getName();
    					StringBuilder path = new StringBuilder();
    					path = getPath(parent, rootFolderName, path);
    					BlContainer parent_folder = d.getParent().getParent();
    					StringBuilder parent_path = new StringBuilder();
    					parent_path = getPath(parent_folder, parentrootFolderName, parent_path);
    					//System.out.println("Full Dimension Modified Path : = " + path);
    					//System.out.println("Full Dimension Modified Parent Path : = " + parent_path);
    					String dim_folder_path = path.toString();
    					String dim_parent_folder_path = parent_path.toString();
    					String dim_folder_full_path = path.toString() + "\\" + dim_name;
    					statement_dim_path.setString(1, dim_identifier);
    					statement_dim_path.setString(2, dim_name);
    					statement_dim_path.setString(3, dim_desc);
    					statement_dim_path.setClob(4, dim_expression);
    					statement_dim_path.setString(5, dim_folder_name);
    					statement_dim_path.setString(6, dim_folder_path);
    					statement_dim_path.setString(7, dim_parent_folder_path);
    					statement_dim_path.setString(8, dim_state);
    					statement_dim_path.setString(9, dim_hash_code);
    					statement_dim_path.setString(10, Universe_Name);
    					statement_dim_path.setString(11, dim_folder_full_path);
    					statement_dim_path.executeUpdate();
						statement_dim_meas_path1.setString(1, dim_identifier);
    					statement_dim_meas_path1.setString(2, dim_name);
    					statement_dim_meas_path1.setString(3, dim_desc);
    					statement_dim_meas_path1.setClob(4, dim_expression);
    					statement_dim_meas_path1.setString(5, dim_folder_name);
    					statement_dim_meas_path1.setString(6, dim_folder_path);
    					statement_dim_meas_path1.setString(7, dim_parent_folder_path);
    					statement_dim_meas_path1.setString(8, dim_state);
    					statement_dim_meas_path1.setString(9, dim_hash_code);
    					statement_dim_meas_path1.setString(10, Universe_Name);
    					statement_dim_meas_path1.setString(11, dim_folder_full_path);
						statement_dim_meas_path1.setString(12, dim_object_type);
						statement_dim_meas_path1.executeUpdate();
    				}
    				 int measure_expression_length =(int) dim_expression.length();
    				 char measure_search_pattern ='"';
    				 int cnt = 0;
    				 String measure_exp_string = (String) binding.getSelect();
    				System.out.println("Number of Characters in the Measures SQL Expression :  " + measure_expression_length);
    				for(int i=0;i<measure_exp_string.length();i++)
    			        if(measure_exp_string.charAt(i)==measure_search_pattern)
    			        {
    			        	//measure_exp_string.ch
    			            cnt++;
    			        }
    			    System.out.println("No of Occurences of character "+ measure_search_pattern + "is"+cnt);
    			    List<Integer> list = new ArrayList<Integer>();
    			    List<String> list_string = new ArrayList<String>();
    			    int cnt_new =0;
    			    for(int i=0;i<measure_exp_string.length();i++)
    			    {
    			        if(measure_exp_string.charAt(i)==measure_search_pattern)
    			        {
    			        	cnt_new++;
    			        	list.add(i);
    			        	
    			        }
    			        
    			    }
    			    //List<String> list_string = new ArrayList<String>();
    			    String measure_pres_schema_check = "_SYS_BIC";
    			    System.out.println (measure_pres_schema_check);
    			    for (int list_it =0;list_it<list.size();list_it=list_it+2)
		        	{
		        	System.out.println (list.get(list_it));
		        	int len_start =list.get(list_it) + 1;
		        	int len_end = list.get(list_it+1);
		        	String measure_exp_table_col = measure_exp_string.substring(len_start, len_end);
		        	 	System.out.println (measure_exp_string.substring(len_start, len_end));
		        	 	if (measure_exp_table_col.equals(measure_pres_schema_check.toString()))
		        	 	{
		        	 		//list_string.add(measure_exp_table_col);
		        	 		//System.out.println ("The Table and Column Information :  " + measure_exp_table_col);
		        	 	}
		        	 	else {
		        	 		list_string.add(measure_exp_table_col);
		        	 		System.out.println ("The Table and Column Information :  " + measure_exp_table_col);//
		        	 	}
		        	 
		        	}
    			    
    			    System.out.println("  Number of Values in List:" +  list_string.size());
    			    
    	        	for (int i = 0; i < list_string.size(); i=i+2) {
    	        		String presentation_table_name = list_string.get(i).toString();
    	        		String presentation_table_column = list_string.get(i+1).toString();
    	    			//System.out.println("Tables Used in SQL Expression (Dimensions)  " + tables_list.get(i));
    	    			statement_dimensions.setString(1, dim_identifier);
    	    			statement_dimensions.setString(2, dim_name);
    	    			statement_dimensions.setString(3, dim_desc);
    	    			statement_dimensions.setString(4, dim_folder_name);
    	    			statement_dimensions.setClob(5, dim_expression);
    	    			statement_dimensions.setString(6, dim_state);
    	    			statement_dimensions.setString(7, dim_hash_code);
    	    			statement_dimensions.setString(8, presentation_table_name);
    	    			statement_dimensions.setString(9, Universe_Name);
    	    			statement_dimensions.setString(10, presentation_table_column);
    	    			statement_dimensions.executeUpdate();
						statement_dim_meas1.setString(1, dim_identifier);
    	    			statement_dim_meas1.setString(2, dim_name);
    	    			statement_dim_meas1.setString(3, dim_desc);
    	    			statement_dim_meas1.setString(4, dim_folder_name);
    	    			statement_dim_meas1.setClob(5, dim_expression);
    	    			statement_dim_meas1.setString(6, dim_state);
    	    			statement_dim_meas1.setString(7, dim_hash_code);
    	    			statement_dim_meas1.setString(8, presentation_table_name);
    	    			statement_dim_meas1.setString(9, Universe_Name);
    	    			statement_dim_meas1.setString(10, presentation_table_column);
						statement_dim_meas1.setString(11, dim_object_type);
    	    			statement_dim_meas1.executeUpdate();
    	    		}
    	        	List<BlItem> attributes = d.getChildren();
					for (BlItem biAttribute : attributes) {
						Attribute attribute = (Attribute) biAttribute;
						//System.out.println ("Attribute :" + attribute.getName());
	    	        	//System.out.println ("Attribute Parent Folder Name :" + attribute.getParent().getName());
	    	        	//System.out.println("Attribute Name Path: "+ attribute.getParent().getName());
	    	        	//System.out.println("Attribute Name Identifier: "+ blItem.getIdentifier());
	    	        	RelationalBinding bindingattribute = (RelationalBinding) attribute.getBinding();
	    	        	//System.out.println("Object " + attribute.getName() + " using SQL Definition: " + bindingattribute.getSelect());
	    	        	//System.out.println("Object " + attribute.getName() + " using SQL Definition: " + binding.getExtraTables());
	    	        	//System.out.println ("Dimension :" + d.getSelect());
	    	        	//System.out.println("Object " + d.getName() + " using LOV: " + d.getAssociatedLov());
	    	        	List tables_list1 =  blservice.getImplicitTables(attribute);
	    	        	//System.out.println("Implicit Tables List Path" + blservice.getBlItemPath(attribute));
	    	        	for (int i = 0; i < tables_list1.size(); i++) {
	    	    			System.out.println("Tables Used in SQL Expression" + tables_list1.get(i));
	    	    		}
					}
    	        	}
    			else if(blItem instanceof Measure){

            		Measure m = (Measure)blItem;
            		String measure_identifier = blItem.getIdentifier();
    				String measure_name = blItem.getName();
    				String measure_desc = blItem.getDescription();
    				String measure_folder_name = blItem.getParent().getName();
    				Integer measure_hash_code1 = blItem.hashCode();
    				String measure_hash_code = measure_hash_code1.toString();
    				String measure_state = blItem.getState().getName();
                	System.out.println ("Measures :" + m.getName());
                	//System.out.println ("Measure Parent Folder Name :" + m.getParent().getName());
                	//System.out.println("Measure Name Path "+ blItem.getParent().getName());
                	//System.out.println("Measure Name Identifier: "+ blItem.getIdentifier());
                	RelationalBinding binding = (RelationalBinding) m.getBinding();
                	//String measure_expression = binding.getSelect();
                	Clob measure_expression = connection.createClob();
                	measure_expression.setString( 1, binding.getSelect());
                	System.out.println("Object " + m.getName() + " using SQL Definition: " + binding.getSelect()); 
                	//System.out.println("Object " + m.getName() + " using LOV: " + m.getAssociatedLov());
                	List tables_list =  blservice.getImplicitTables(m);
                	//System.out.println("Implicit Tables List Path" + blservice.getBlItemPath(m));
                	String rootFolderName = m.getName();
                	String parentrootFolderName = m.getParent().getName();
    				if (rootFolderName != "") {
    					BlContainer parent = m.getParent();
    					String parentName = "";
    					if (parent != null)
    						parentName = parent.getName();
    					StringBuilder path = new StringBuilder();
    					path = getPath(parent, rootFolderName, path);
    					BlContainer parent_folder = m.getParent().getParent();
    					StringBuilder parent_path = new StringBuilder();
    					parent_path = getPath(parent_folder, parentrootFolderName, parent_path);
    					//System.out.println("Full Measure Modified Path : = " + path);
    					//System.out.println("Full Dimension Modified Parent Path : = " + parent_path);
    					String measure_folder_full_path = path.toString() + "\\" + measure_name;
    					String measure_folder_path = path.toString();
    					String measure_parent_folder_path = parent_path.toString();
    					statement_measure_path.setString(1, measure_identifier);
    					statement_measure_path.setString(2, measure_name);
    					statement_measure_path.setString(3, measure_desc);
    					statement_measure_path.setClob(4, measure_expression);
    					statement_measure_path.setString(5, measure_folder_name);
    					statement_measure_path.setString(6, measure_folder_path);
    					statement_measure_path.setString(7, measure_parent_folder_path);
    					statement_measure_path.setString(8, measure_state);
    					statement_measure_path.setString(9, measure_hash_code);
    					statement_measure_path.setString(10, Universe_Name);
    					statement_measure_path.setString(11, measure_folder_full_path);
    			        //statement_m.setString(8, presentation_table_name);
    					statement_measure_path.executeUpdate();
    					statement_dim_meas_path2.setString(1, measure_identifier);
    					statement_dim_meas_path2.setString(2, measure_name);
    					statement_dim_meas_path2.setString(3, measure_desc);
    					statement_dim_meas_path2.setClob(4, measure_expression);
    					statement_dim_meas_path2.setString(5, measure_folder_name);
    					statement_dim_meas_path2.setString(6, measure_folder_path);
    					statement_dim_meas_path2.setString(7, measure_parent_folder_path);
    					statement_dim_meas_path2.setString(8, measure_state);
    					statement_dim_meas_path2.setString(9, measure_hash_code);
    					statement_dim_meas_path2.setString(10, Universe_Name);
    					statement_dim_meas_path2.setString(11, measure_folder_full_path);
						statement_dim_meas_path2.setString(12, measure_object_type);
    			        //statement_m.setString(8, presentation_table_name);
    					statement_dim_meas_path2.executeUpdate();
    				}
    				 int measure_expression_length =(int) measure_expression.length();
    				 char measure_search_pattern ='"';
    				 int cnt = 0;
    				 String measure_exp_string = (String) binding.getSelect();
    				System.out.println("Number of Characters in the Measures SQL Expression :  " + measure_expression_length);
    				for(int i=0;i<measure_exp_string.length();i++)
    			        if(measure_exp_string.charAt(i)==measure_search_pattern)
    			        {
    			        	//measure_exp_string.ch
    			            cnt++;
    			        }
    			    System.out.println("No of Occurences of character "+ measure_search_pattern + "is"+cnt);
    			    List<Integer> list = new ArrayList<Integer>();
    			    List<String> list_string = new ArrayList<String>();
    			    int cnt_new =0;
    			    for(int i=0;i<measure_exp_string.length();i++)
    			    {
    			        if(measure_exp_string.charAt(i)==measure_search_pattern)
    			        {
    			        	cnt_new++;
    			        	list.add(i);
    			        	
    			        }
    			        
    			    }
    			    //List<String> list_string = new ArrayList<String>();
    			    String measure_pres_schema_check = "_SYS_BIC";
    			    System.out.println (measure_pres_schema_check);
    			    for (int list_it =0;list_it<list.size();list_it=list_it+2)
		        	{
		        	System.out.println (list.get(list_it));
		        	int len_start =list.get(list_it) + 1;
		        	int len_end = list.get(list_it+1);
		        	String measure_exp_table_col = measure_exp_string.substring(len_start, len_end);
		        	 	System.out.println (measure_exp_string.substring(len_start, len_end));
		        	 	if (measure_exp_table_col.equals(measure_pres_schema_check.toString()))
		        	 	{
		        	 		//list_string.add(measure_exp_table_col);
		        	 		//System.out.println ("The Table and Column Information :  " + measure_exp_table_col);
		        	 	}
		        	 	else {
		        	 		list_string.add(measure_exp_table_col);
		        	 		System.out.println ("The Table and Column Information :  " + measure_exp_table_col);//
		        	 	}
		        	 
		        	}
    			    
                	for (int i = 0; i < list_string.size(); i=i+2) {
                		String presentation_table_name = list_string.get(i).toString();
                		String presentation_table_column = list_string.get(i+1).toString();
            			//System.out.println("(Measures)Tables Used in SQL Expression (Measures)  " + tables_list.get(i));
            			statement_measures.setString(1, measure_identifier);
    	    			statement_measures.setString(2, measure_name);
    	    			statement_measures.setString(3, measure_desc);
    	    			statement_measures.setString(4, measure_folder_name);
    	    			statement_measures.setClob(5, measure_expression);
    	    			statement_measures.setString(6, measure_state);
    	    			statement_measures.setString(7, measure_hash_code);
    	    			statement_measures.setString(8, presentation_table_name);
    	    			statement_measures.setString(9, Universe_Name);
    	    			statement_measures.setString(10, presentation_table_column);
    	    			statement_measures.executeUpdate();
						statement_dim_meas2.setString(1, measure_identifier);
    	    			statement_dim_meas2.setString(2, measure_name);
    	    			statement_dim_meas2.setString(3, measure_desc);
    	    			statement_dim_meas2.setString(4, measure_folder_name);
    	    			statement_dim_meas2.setClob(5, measure_expression);
    	    			statement_dim_meas2.setString(6, measure_state);
    	    			statement_dim_meas2.setString(7, measure_hash_code);
    	    			statement_dim_meas2.setString(8, presentation_table_name);
    	    			statement_dim_meas2.setString(9, Universe_Name);
    	    			statement_dim_meas2.setString(10, presentation_table_column);
						statement_dim_meas2.setString(11, measure_object_type);
    	    			statement_dim_meas2.executeUpdate();
            		}
                	}
    			
    			// Filters Part is commented out as there is no way to identify the type of Filter(Native/Business)
    			 else if(blItem instanceof Filter){
    				if (blItem instanceof NativeRelationalFilter) { 
    				NativeRelationalFilter filter = (NativeRelationalFilter) blItem;
    				String filter_identifier = blItem.getIdentifier();
    				String filter_name = blItem.getName();
    				String filter_desc = blItem.getDescription();
    				String filter_type = "Native";
    				String filter_folder_name = blItem.getParent().getName();
    				Integer filter_hash_code1 = blItem.hashCode();
    				String filter_hash_code =filter_hash_code1.toString();
    				String filter_state = blItem.getState().getName();
                	//System.out.println ("Filters :" + filter.getName());
                	//System.out.println ("Filters Parent Folder Name :" + filter.getParent().getName());
                	//System.out.println("Filter Name Path "+ blItem.getParent().getName());
                	//System.out.println("Filter Name Identifier: "+ blItem.getIdentifier());
                	RelationalBinding binding = (RelationalBinding) filter.getBinding();
                	System.out.println("Object " + filter.getName() + " using SQL Definition: " + binding.getSelect()); 
                	Clob filter_expression = connection.createClob();
                	filter_expression.setString( 1, binding.getSelect());
                	List tables_list =  blservice.getImplicitTables(filter);
                	//System.out.println("Implicit Tables List Path" + blservice.getBlItemPath(filter));
                	String rootFolderName = filter.getName();
                	//String rootFolderName = m.getName();
                	String parentrootFolderName = filter.getParent().getName();
    				if (rootFolderName != "") {
    					BlContainer parent = filter.getParent();
    					String parentName = "";
    					if (parent != null)
    						parentName = parent.getName();
    					StringBuilder path = new StringBuilder();
    					path = getPath(parent, rootFolderName, path);
    					BlContainer parent_folder = filter.getParent().getParent();
    					StringBuilder parent_path = new StringBuilder();
    					parent_path = getPath(parent_folder, parentrootFolderName, parent_path);
    					//System.out.println("Full Measure Modified Path : = " + path);
    					String filter_folder_path = path.toString();
    					String filter_parent_folder_path = parent_path.toString();
    					statement_filter_path.setString(1, filter_name);
    					statement_filter_path.setString(2, filter_identifier);
    					statement_filter_path.setString(3, filter_desc);
    					statement_filter_path.setString(4, filter_type);
    					statement_filter_path.setClob(5, filter_expression);
    					statement_filter_path.setString(6, filter_folder_name);
    					statement_filter_path.setString(7, filter_folder_path);
    					statement_filter_path.setString(8, filter_parent_folder_path);
    					statement_filter_path.setString(9, filter_state);
    					statement_filter_path.setString(10, filter_hash_code);
    					statement_filter_path.setString(11, Universe_Name);
    			        //statement_m.setString(8, presentation_table_name);
    					statement_filter_path.executeUpdate();
    				}
                	for (int i = 0; i < tables_list.size(); i++) {
            			System.out.println("Tables Used in SQL Expression" + tables_list.get(i));
            		}
    				} else if (blItem instanceof BusinessFilter) {
    					BusinessFilter filter = (BusinessFilter) blItem;
        				String filter_identifier = blItem.getIdentifier();
        				String filter_name = blItem.getName();
        				String filter_desc = blItem.getDescription();
        				String filter_type = "Business";
        				String filter_folder_name = blItem.getParent().getName();
        				Integer filter_hash_code1 = blItem.hashCode();
        				String filter_hash_code =filter_hash_code1.toString();
        				String filter_state = blItem.getState().getName();
                    	//System.out.println ("Filters :" + filter.getName());
                    	//System.out.println ("Filters Parent Folder Name :" + filter.getParent().getName());
                    	//System.out.println("Filter Name Path "+ blItem.getParent().getName());
                    	//System.out.println("Filter Name Identifier: "+ blItem.getIdentifier());
                    	//RelationalBinding binding = (RelationalBinding) filter.getBinding();
                    	//System.out.println("Object " + filter.getName() + " using SQL Definition: " + binding.getSelect()); 
                    	Clob filter_expression = connection.createClob();
                    	filter_expression.setString( 1, null);
                    	List tables_list =  blservice.getImplicitTables(filter);
                    	System.out.println("Implicit Tables List Path" + blservice.getBlItemPath(filter));
                    	String rootFolderName = filter.getName();
                    	//String rootFolderName = m.getName();
                    	String parentrootFolderName = filter.getParent().getName();
        				if (rootFolderName != "") {
        					BlContainer parent = filter.getParent();
        					String parentName = "";
        					if (parent != null)
        						parentName = parent.getName();
        					StringBuilder path = new StringBuilder();
        					path = getPath(parent, rootFolderName, path);
        					BlContainer parent_folder = filter.getParent().getParent();
        					StringBuilder parent_path = new StringBuilder();
        					parent_path = getPath(parent_folder, parentrootFolderName, parent_path);
        					//System.out.println("Full Measure Modified Path : = " + path);
        					String filter_folder_path = path.toString();
        					String filter_parent_folder_path = parent_path.toString();
        					statement_filter_path.setString(1, filter_name);
        					statement_filter_path.setString(2, filter_identifier);
        					statement_filter_path.setString(3, filter_desc);
        					statement_filter_path.setString(4, filter_type);
        					statement_filter_path.setClob(5, filter_expression);
        					statement_filter_path.setString(6, filter_folder_name);
        					statement_filter_path.setString(7, filter_folder_path);
        					statement_filter_path.setString(8, filter_parent_folder_path);
        					statement_filter_path.setString(9, filter_state);
        					statement_filter_path.setString(10, filter_hash_code);
        					statement_filter_path.setString(11, Universe_Name);
        			        //statement_m.setString(8, presentation_table_name);
        					statement_filter_path.executeUpdate();
        				}
                    	for (int i = 0; i < tables_list.size(); i++) {
                			System.out.println("Tables Used in SQL Expression" + tables_list.get(i));
                		}	
    				}
                	}
    			
    			//else return null;
    		}
    	}
    	return folderList;
    }
    
	private StringBuilder getPath(BlContainer parent, String rootFolderName,
			StringBuilder path) {
		if (parent != null
				//&& !parent.getName().trim().equalsIgnoreCase(rootFolderName)
				) {

			if (parent != null) {
				path = getPath(parent.getParent(), rootFolderName, path);
				if (path.toString().equalsIgnoreCase("")) {
					path.append(parent.getName());
				} else {
					path.append("\\" + parent.getName());
				}
			}

		}

		return path;
	}
	
	void getLineage(String Universe_Name) throws SQLException {
		try {
	       	 
			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
     Connection connection = null;
     Statement stmt = null;
     String SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH3 = "select  dim_identifer,dim_name,dim_desc,dim_expression,parent_folder_name,dim_folder_path,dim_parent_folder_path,dim_state,dim_folder_full_path from BOBJ_UNV_DIM_FOL_PATH where UNIVERSE_NAME ='" + Universe_Name + "'";
     String SQL_INSERT_DIM = "INSERT INTO DSD_BI_INFO.BOBJ_UNV_DIMENSIONS" + " VALUES(?, ?, ?, ?, ?, ?, ?,? , ?,? )";
     String SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH2 = "select  dim_identifer,dim_name,dim_desc,dim_expression,parent_folder_name,dim_folder_path,dim_parent_folder_path,dim_state,dim_folder_full_path from BOBJ_UNV_DIM_FOL_PATH where UNIVERSE_NAME ='" + Universe_Name + "'" + "and lower(dim_expression) like '%@select%'" ;
     String SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH4 = "select dim_identifer,dim_name,presentation_table_name,presentation_column from BOBJ_UNV_DIMENSIONS where UNIVERSE_NAME='" + Universe_Name + "'";
     System.out.println(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH3);
     System.out.println(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH2);
     System.out.println(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH4);
     try {
    	 
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@ora-uat-binrt-d2.vmware.com:1521:" + dbName, userName,
					password);
			//System.out.println("Connected :" + connection.getSchema() );

		} catch (SQLException e) {

			e.printStackTrace();
		}
     
     if (connection != null) {
			System.out.println("nSuccessfullly connected to Oracle DB");
			stmt = connection.createStatement();
			
		} else {
			System.out.println("nFailed to connect to Oracle DB");
		}
     ResultSet rs_select_dim1 = stmt.executeQuery(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH3);
     int resultset_count_select1 =0;
     
     while(rs_select_dim1.next()){
         //Retrieve by column name
         String dim_identifer1  = rs_select_dim1.getString("dim_identifer");
         String dim_name1 = rs_select_dim1.getString("dim_name");
         String dim_desc1 = rs_select_dim1.getString("dim_desc");
         String dim_expression1 = rs_select_dim1.getString("dim_expression");
         String parent_folder_name1 = rs_select_dim1.getString("parent_folder_name");
         String dim_folder_path1 = rs_select_dim1.getString("dim_folder_path");
         String dim_parent_folder_path1 =rs_select_dim1.getString("dim_parent_folder_path");
         String dim_state1 =rs_select_dim1.getString("dim_state");
         String dim_folder_full_path1 =rs_select_dim1.getString("dim_folder_full_path");
         resultset_count_select1 = resultset_count_select1+ 1;
     }
     System.out.println("Third Result Count: " + resultset_count_select1);
     rs_select_dim1.close();
     
     ResultSet rs_select_dim2 = stmt.executeQuery(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH2);
     int resultset_count_select2 =0;
     while(rs_select_dim2.next()){
         //Retrieve by column name
         String dim_identifer2  = rs_select_dim2.getString("dim_identifer");
         String dim_name2 = rs_select_dim2.getString("dim_name");
         String dim_desc2 = rs_select_dim2.getString("dim_desc");
         String dim_expression2 = rs_select_dim2.getString("dim_expression");
         String parent_folder_name2 = rs_select_dim2.getString("parent_folder_name");
         String dim_folder_path2 = rs_select_dim2.getString("dim_folder_path");
         String dim_parent_folder_path2 =rs_select_dim2.getString("dim_parent_folder_path");
         String dim_state2 =rs_select_dim2.getString("dim_state");
         String dim_folder_full_path2 =rs_select_dim2.getString("dim_folder_full_path");
         resultset_count_select2 = resultset_count_select2+ 1;
     }
     System.out.println("Fourth Result Count: " + resultset_count_select2);
     
     ResultSet rs_select_dim4 = stmt.executeQuery(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH4);
     int resultset_count_select4 =0;
     while(rs_select_dim4.next()){
         //Retrieve by column name
         String dim_identifer4  = rs_select_dim4.getString("dim_identifer");
         String dim_name4 = rs_select_dim4.getString("dim_name");
         String dim_desc4 = rs_select_dim4.getString("presentation_table_name");
         String dim_expression4 = rs_select_dim4.getString("presentation_column");
         resultset_count_select4 = resultset_count_select4+ 1;
     }
     System.out.println("Fifth Result Count: " + resultset_count_select4);
     
     ResultSet rs_select_dim22 = stmt.executeQuery(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH2);
     int resultset_count_select22 =0;
     
    do {
         //Retrieve by column name
    	rs_select_dim22.next();
         String dim_identifer22  = rs_select_dim22.getString("dim_identifer");
         String dim_name22 = rs_select_dim22.getString("dim_name");
         String dim_desc22 = rs_select_dim22.getString("dim_desc");
         String dim_expression22 = rs_select_dim22.getString("dim_expression");
         String parent_folder_name22 = rs_select_dim22.getString("parent_folder_name");
         String dim_folder_path22 = rs_select_dim22.getString("dim_folder_path");
         String dim_parent_folder_path22 =rs_select_dim22.getString("dim_parent_folder_path");
         String dim_state22 =rs_select_dim22.getString("dim_state");
         String dim_folder_full_path22 =rs_select_dim22.getString("dim_folder_full_path");
         resultset_count_select22 = resultset_count_select22+ 1;
        
         ResultSet rs_select_dim11 = stmt.executeQuery(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH3);
         int resultset_count_select11 =0;
         while(rs_select_dim11.next()){
             //Retrieve by column name
        	 String dim_identifer11  = rs_select_dim11.getString("dim_identifer");
             String dim_name11 = rs_select_dim11.getString("dim_name");
             String dim_desc11 = rs_select_dim11.getString("dim_desc");
             String dim_expression11 = rs_select_dim11.getString("dim_expression");
             String parent_folder_name11 = rs_select_dim11.getString("parent_folder_name");
             String dim_folder_path11 = rs_select_dim11.getString("dim_folder_path");
             String dim_parent_folder_path11 =rs_select_dim11.getString("dim_parent_folder_path");
             String dim_state11 =rs_select_dim11.getString("dim_state");
             String dim_folder_full_path11 =rs_select_dim11.getString("dim_folder_full_path");
             resultset_count_select11 = resultset_count_select11+ 1;
         }
         
         System.out.println("Sixth Inside Result Count: " + resultset_count_select11);
         rs_select_dim11.close();
     }  while(rs_select_dim22.next());
     System.out.println("Sevent Outer Result Count: " + resultset_count_select22);
     //rs_select_dim1.close();
     /*while(rs.next()){
         //Retrieve by column name
         String dim_identifer  = rs.getString("dim_identifer");
         String dim_name = rs.getString("dim_name");
         String dim_desc = rs.getString("dim_desc");
         Clob dim_expression = rs.getClob("dim_expression");
         String parent_folder_name = rs.getString("parent_folder_name");
         String dim_folder_path = rs.getString("dim_folder_path");
         String dim_parent_folder_path =rs.getString("dim_parent_folder_path");
         String dim_state =rs.getString("dim_state");
         String dim_folder_full_path =rs.getString("dim_folder_full_path");
         ResultSet rs1 = stmt.executeQuery(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH2);
         ResultSet rs_dim_folder_path = stmt.executeQuery(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH2);
         int rs_count =0;
         int rs_count_new =0;
         while(rs1.next()){
        	 rs_count = rs_count +1;
         }
         rs1.close();
         System.out.println("RS_COUNT  " + rs_count);
         while(rs_dim_folder_path.next()){
        	 rs_count_new = rs_count_new+ 1;
        	 System.out.println("RS_COUNT_NEW  " + rs_count_new);
        	 System.out.println("statement_dimensions executed" );
        	 String dim_identifer1  = rs_dim_folder_path.getString("dim_identifer");
             String dim_name1 = rs_dim_folder_path.getString("dim_name");
             String dim_desc1 = rs_dim_folder_path.getString("dim_desc");
             String dim_expression1 = rs_dim_folder_path.getString("dim_expression");
             String parent_folder_name1 = rs_dim_folder_path.getString("parent_folder_name");
             String dim_folder_path1 = rs_dim_folder_path.getString("dim_folder_path");
             String dim_parent_folder_path1 =rs_dim_folder_path.getString("dim_parent_folder_path");
             String dim_state1 =rs_dim_folder_path.getString("dim_state");
             String dim_folder_full_path1 =rs_dim_folder_path.getString("dim_folder_full_path");
             System.out.println(dim_expression1.toString());
             System.out.println(dim_folder_full_path.toString());
             boolean dim_exp_compare =dim_expression1.contains(dim_folder_full_path.toString());
             System.out.println("Dim Exp Compare : "+ dim_exp_compare);
             String SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH_EXP = "select  dim_identifer,dim_name,dim_desc,dim_expression,parent_folder_name,dim_folder_path,dim_parent_folder_path,dim_state,dim_folder_full_path from BOBJ_UNV_DIM_FOL_PATH where UNIVERSE_NAME ='" + Universe_Name + "'" + " and dim_expression like'%" +  dim_folder_full_path + "%'";
             System.out.println(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH_EXP);
             ResultSet rs_exp = stmt.executeQuery(SQL_SELECT_BOBJ_UNV_DIM_FOL_PATH_EXP);
             //if (dim_expression1.contains(dim_folder_full_path.toString()))
             System.out.println("RS_EXP: " + rs_exp.getFetchSize());
             int rs_exp_count =0;
             while(rs_exp.next()){
            	 rs_exp_count = rs_exp_count +1;
             }
             rs_exp.close();
             System.out.println("RS_EXP_COUNT  " + rs_exp_count);
             if(rs_exp_count>0)
             {
            	 System.out.println("The Expression is:" + dim_expression1 );
            	 System.out.println("The Dim Folder Path is:" + dim_folder_full_path );
            	 String SQL_SELECT_BOBJ_UNV_DIMENSIONS = "select distinct dim_identifer,dim_name,presentation_table_name,presentation_column from BOBJ_UNV_DIMENSIONS where UNIVERSE_NAME ='" + Universe_Name+ "' and" + " dim_identifer='" + dim_identifer +"'";
            	 System.out.println(SQL_SELECT_BOBJ_UNV_DIMENSIONS);
            	 ResultSet rs_OBJ_UNV_DIMENSIONS = stmt.executeQuery(SQL_SELECT_BOBJ_UNV_DIMENSIONS); 
            	 System.out.println("rs_OBJ_UNV_DIMENSIONS:  " +  rs_OBJ_UNV_DIMENSIONS.getFetchSize());
            	 int rs_exp_count1 =0;
                 while(rs_OBJ_UNV_DIMENSIONS.next()){
                	 rs_exp_count1 = rs_exp_count1 +1;
                 }
                 rs_OBJ_UNV_DIMENSIONS.close();
            	 if (rs_exp_count1>=0)
            	 {
            		while (rs_OBJ_UNV_DIMENSIONS.next())
            		{
            		 //Clob dim_expression2 = connection.createClob();
                 	//dim_expression1.setClob( 1, dim_expression1);
            		 statement_dimensions.setString(1, dim_identifer1);
            		 statement_dimensions.setString(2, dim_name1);
            		 statement_dimensions.setString(3, dim_desc1);
            		 statement_dimensions.setString(4, parent_folder_name1);
            		 statement_dimensions.setString(5, dim_expression1);
            		 statement_dimensions.setString(6, dim_state1);
            		 statement_dimensions.setString(7, "");
            		 statement_dimensions.setString(8, rs_OBJ_UNV_DIMENSIONS.getString("presentation_table_name"));
            		 statement_dimensions.setString(9, Universe_Name);
            		 statement_dimensions.setString(10, rs_OBJ_UNV_DIMENSIONS.getString("presentation_column"));
            		}
            		rs_OBJ_UNV_DIMENSIONS.close();
            	 }
             }
         }
         rs_dim_folder_path.close();
      }
     rs.close();*/
	}

}

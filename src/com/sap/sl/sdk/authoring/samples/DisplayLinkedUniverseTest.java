package com.sap.sl.sdk.authoring.samples;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.sl.sdk.authoring.commons.UniverseReference;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.sap.sl.sdk.authoring.businesslayer.BlContainer;
import com.sap.sl.sdk.authoring.businesslayer.BlItem;
import com.sap.sl.sdk.authoring.businesslayer.RelationalBusinessLayer;
import com.sap.sl.sdk.authoring.businesslayer.RootFolder;
import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.authoring.datafoundation.Column;
import com.sap.sl.sdk.authoring.datafoundation.DataFoundation;
import com.sap.sl.sdk.authoring.datafoundation.Join;
import com.sap.sl.sdk.authoring.datafoundation.SQLJoin;
import com.sap.sl.sdk.authoring.datafoundation.Table;
import com.sap.sl.sdk.authoring.local.LocalResourceService;
import com.sap.sl.sdk.authoring.samples.util.AuthenticationMode;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

public class DisplayLinkedUniverseTest {

	//
	// This sample is used to retrieve a universe from a CMS and check if it has a core universe
	// (== the universe is a linked universe).
	//
	// If yes, this sample shows :
	//			- the referenced core universe(s) details
	// 			- a list of classes and objects coming from the core universe(s) business layer
	// 			- a list of tables and joins coming from the core universe(s) data foundation
	// 
	
    //
    // !! Edit the test parameters here !!
    //
	
    /** CMS System */
	private static final String CMS_LOG_HOST = "<CMS_HOST_NAME>:<CMS_HOST_PORT>";

	/** User name used to log in to the CMS */
    private static final String CMS_LOG_USER = "<CMS_USER_NAME>";
	
	/** User password */
	private static final String CMS_LOG_PASS = "<CMS_PASSWORD>";
	
	/** Authentication mode used to log in to the CMS. Here: Enterprise */
    private static final String CMS_AUTH_MODE = AuthenticationMode.ENTERPRISE;
    
	/** Full path of the universe in the CMS repository */
    private static final String UNX_PATH = CmsResourceService.UNIVERSES_ROOT + "/<UNIVERSE_PATH>";
    
    /** Local folder where universes (Linked and Core(s)) will be locally saved */
    private static final String LOCAL_FOLDER = System.getProperty("java.io.tmpdir");
    
    //
    // !! End of parameters definition !!
    //
    
    private SlContext context;
    private IEnterpriseSession enterpriseSession;
    private CmsResourceService cmsResourceService;
    private LocalResourceService localResourceService;
	
    @Before
    public void setUp() throws Exception {
    	// Connects to CMS and creates a session
    	context = SlContext.create();
    	enterpriseSession = CrystalEnterprise.getSessionMgr().logon(CMS_LOG_USER, CMS_LOG_PASS, CMS_LOG_HOST, CMS_AUTH_MODE);
        context.getService(CmsSessionService.class).setSession(enterpriseSession);
        cmsResourceService = context.getService(CmsResourceService.class);
        localResourceService = context.getService(LocalResourceService.class);
    }
    
    @After
    public void tearDown() {
    	// Closes the session
    	enterpriseSession.logoff();
    	context.close();
    }
    
    @Test
    public void displayLinkedUniverse() throws SDKException {
    	
    	// Retrieves the universe from the CMS
    	System.out.println("\n- Retrieves the universe from CMS (" + UNX_PATH + ") to local folder : " + LOCAL_FOLDER);
    	String universeLocalPath = cmsResourceService.retrieveUniverse(UNX_PATH, LOCAL_FOLDER, false);
    	
    	// Loads the business layer and the data foundation
    	System.out.println("\n- Load the business layer");
    	RelationalBusinessLayer businessLayer = (RelationalBusinessLayer) localResourceService.load(universeLocalPath);
    	List<UniverseReference> universeReferenceList = null;
    	
    	//
    	// Displays Core universe(s) information
    	//
    	
    	System.out.println("\n- Display Core universe(s) information...");
    	universeReferenceList = businessLayer.getCoreUniverseReferences();
		displayCoreUniverse(universeReferenceList);
    	
    }
    
	private void displayCoreUniverse(List<UniverseReference> universeReferenceList) {
		if (!universeReferenceList.isEmpty()) {
			int coreUniverseCount = universeReferenceList.size();
			System.out.println("\n\t- " + coreUniverseCount + " core universe(s) found");
			
			String coreBusinessLayerPath = null;
			RelationalBusinessLayer coreBusinessLayer = null;
			DataFoundation coreDataFoundation = null;
		
			for (int i = 0; i < coreUniverseCount; i++) {
				UniverseReference coreUniverse = universeReferenceList.get(i);
				
				// Displays the Core universes details
				System.out.println("\n\t- Display details of Core Universe " + (i+1));
				System.out.println("\t\t- Core universe CUID : " + coreUniverse.getCuid());
				System.out.println("\t\t- Core universe path : " + coreUniverse.getPath());
				System.out.println("\t\t- Core universe revision number : " + coreUniverse.getRevisionNumber());
				
				// Retrieves the Core universe
				coreBusinessLayerPath = cmsResourceService.retrieveUniverse(coreUniverse.getPath(), LOCAL_FOLDER, false);
				coreBusinessLayer = (RelationalBusinessLayer) localResourceService.load(coreBusinessLayerPath);
				coreDataFoundation = (DataFoundation) localResourceService.load(coreBusinessLayer.getDataFoundationPath());
				
				// Displays the Core business layer
				displayCoreBusinessLayer(coreBusinessLayer, i);
				
				// Displays the Core data foundation
				displayCoreDataFoundation(coreDataFoundation, i);
			}
			
		} else {
    		System.out.println("\n\t- No universe reference(s) found.");		
    	}
		
	}
	
	private void displayCoreBusinessLayer(RelationalBusinessLayer coreBusinessLayer, int i) {
		
		// Displays the classes and objects of the core business layer
		System.out.println("\n\t- Display business layer classes and objects of Core universe " + (i+1));
		
		RootFolder rootFolder = coreBusinessLayer.getRootFolder();
		List<BlItem> itemList = rootFolder.getChildren();
		
		displayCoreBusinessLayerItemList(itemList, 0);
		
	}

	private void displayCoreBusinessLayerItemList(List<BlItem> itemList, int offset) {
		for (BlItem item : itemList) {
			displayCoreBusinessLayerItem(item, offset);
			
			if (item instanceof BlContainer) {
				displayCoreBusinessLayerItemList(((BlContainer) item).getChildren(), offset+1);
			}
		}
	}
	
	private void displayCoreBusinessLayerItem(BlItem item, int offset) {
		String tabString = "\t\t";
		
		for (int i = 0; i < offset; i++) {
			tabString += "\t";
		}
		
		// Displays item information
		System.out.println(tabString + "- Item class : " + item.getClass().getSimpleName());
		System.out.println(tabString + "- Item name : " + item.getName());
		System.out.println(tabString + "- Item description : " + item.getDescription());
		System.out.println(tabString + "- Item state : " + item.getState());
		System.out.println();
	}

	private void displayCoreDataFoundation(DataFoundation dataFoundation, int i) {
				
		// Displays  the tables coming from the core universe
		System.out.println("\t- Display tables of Core universe " + (i+1));
		List<Table> tableList = dataFoundation.getTables();
		displayTables(tableList);
		
		// Displays the joins coming from the core universe
		System.out.println("\n\t- Display joins of Core universe " + (i+1));
		List<Join> joinList = dataFoundation.getJoins();
		displayJoins(joinList);
		
	}
	
	private void displayTables(List<Table> tableList) {
		for (Table table : tableList) {
			System.out.println("\n\t\t- Table name : " + table.getName());
			System.out.println("\t\t- Table description : " + table.getDescription());
			System.out.println("\t\t- Table ID : " + table.getIdentifier());
			System.out.println("\n\t\t- Table columns :");
			
			List<Column> columnList = table.getColumns();
			
			for (Column column : columnList) {
				System.out.println("\n\t\t\t- Column name : " + column.getName());
				System.out.println("\t\t\t- Column description : " + column.getDescription());
				System.out.println("\t\t\t- Column ID : " + column.getIdentifier());
			}
		}
	}
	
	private void displayJoins(List<Join> joinList) {
		for (Join join : joinList) {
			SQLJoin sqlJoin = (SQLJoin) join;
			System.out.println("\n\t\t- Join expression : " + sqlJoin.getExpression());
			System.out.println("\t\t- Join cardinality : " + sqlJoin.getCardinality());
			System.out.println("\t\t- Join ID : " + sqlJoin.getIdentifier());
			System.out.println("\t\t- Join left table : " + sqlJoin.getLeftTable().getName());
			System.out.println("\t\t- Join right table : " + sqlJoin.getRightTable().getName());
		}
	}
	
}

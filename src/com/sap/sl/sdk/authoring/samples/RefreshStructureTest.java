package com.sap.sl.sdk.authoring.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;
import com.sap.sl.sdk.authoring.datafoundation.DataFoundationService;
import com.sap.sl.sdk.framework.IStatus;
import com.sap.sl.sdk.framework.SlContext;


public class RefreshStructureTest {

    //
    // This sample is used to refresh the data foundation structure
    //

    //
    // !! Edit the test parameters here !!
    //

    /** Data foundation name */
    private static final String DATA_FOUNDATION_NAME = "<DATA_FOUNDATION_NAME>";
	
	/** Local folder used to save resources locally */
    private static final String LOCAL_FOLDER = System.getProperty("java.io.tmpdir");

    //
    // !! End of parameters definition !!
    //

    private SlContext context;

    @Before
    public void setUp() throws Exception {

        // Connects to the CMS and creates a session
        context = SlContext.create();
    }

    @After
    public void tearDown() throws Exception {

        // Closes the CMS session
        context.close();
    }

    @Test
    public void refreshStructure() throws SDKException {

        DataFoundationService dataFoundationService = context.getService(DataFoundationService.class);

        // Refreshes the data foundation structure without applying changes and gets the resulting status 
        System.out.println(String.format("\n- Refresh the data foundation \"%s\" structure without applying changes", LOCAL_FOLDER + "/" + DATA_FOUNDATION_NAME));
        IStatus status = dataFoundationService.refreshStructure(LOCAL_FOLDER + "/" + DATA_FOUNDATION_NAME, false);
        
        // Checks if some changes have been detected in the data foundation
        if (status.hasChildren()) {
            // Lists the changes 
            IStatus[] children = status.getChildren();
            for (IStatus child : children) {
                System.out.println(child.getMessage());
            }
        }

        // Refreshes the data foundation structure, applies changes and saves
        System.out.println(String.format("\n- Refresh the data foundation \"%s\" structure, apply changes and save", LOCAL_FOLDER + "/" + DATA_FOUNDATION_NAME));
        dataFoundationService.refreshStructure(LOCAL_FOLDER + "/" + DATA_FOUNDATION_NAME, true);
    }
}

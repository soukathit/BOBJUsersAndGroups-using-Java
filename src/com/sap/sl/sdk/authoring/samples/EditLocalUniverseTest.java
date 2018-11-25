package com.sap.sl.sdk.authoring.samples;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;

import com.sap.sl.sdk.authoring.businesslayer.RelationalBusinessLayer;
import com.sap.sl.sdk.authoring.connection.RelationalConnection;
import com.sap.sl.sdk.authoring.datafoundation.MonoSourceDataFoundation;
import com.sap.sl.sdk.authoring.local.LocalResourceService;
import com.sap.sl.sdk.authoring.samples.util.SamplesUtilities;
import com.sap.sl.sdk.framework.SlContext;


public class EditLocalUniverseTest {

    //
    // This sample is used to edit a local universe: 
    //    - retrieve it, 
    //    - modify its business layer and data foundation names 
    //    - change its connection
    //    - republish the universe locally
    //

    //
    // !! Edit the sample parameters here !!
    //

    /** Universe name */
    private static final String UNX_NAME = "<UNIVERSE_NAME>";
	
	/** Local folder used to save all resources locally */
    private static final String LOCAL_FOLDER = System.getProperty("java.io.tmpdir");

    // 
    // !! End of parameters definition !!
    // 
	
    private SlContext context;

    @Before
    public void setUp() throws Exception {

        context = SlContext.create();

    }

    @After
    public void tearDown() throws Exception {

        context.close();

    }

    @Test
    public void editLocalUniverseTest() throws SDKException {

        // Gets the LocalResourceService.class service
        LocalResourceService service = context.getService(LocalResourceService.class);

        // Uses a temporary folder
        File tempFolder = SamplesUtilities.createTempFolder(LOCAL_FOLDER);

        // Retrieves the universe locally
        String businessLayerPath = service.retrieve(LOCAL_FOLDER + "/" + UNX_NAME, tempFolder.getPath());

        // Loads all resources
        RelationalBusinessLayer businessLayer = (RelationalBusinessLayer) service.load(businessLayerPath);
        String dataFoundationPath = businessLayer.getDataFoundationPath();
        MonoSourceDataFoundation dataFoundation = (MonoSourceDataFoundation) service.load(dataFoundationPath);
        String connectionPath = dataFoundation.getConnectionPath();
        RelationalConnection connection = (RelationalConnection) service.load(connectionPath);

        // Edits and saves the connection
        connection.setName("ConnectionNewName");
        File updatedConnectionPath = new File(tempFolder, "ConnectionNewName.cnx");
        service.save(connection, updatedConnectionPath.getPath(), false);

        // Edits and saves the data foundation
        dataFoundation.setName("DataFoundationNewName");
        dataFoundation.setConnectionPath(updatedConnectionPath.getPath());
        File updatedDataFoundationPath = new File(tempFolder, "DataFoundationNewName.dfx");
        service.save(dataFoundation, updatedDataFoundationPath.getPath(), false);

        // Edits and saves the business layer
        businessLayer.setName("BusinessLayerNewName");
        businessLayer.setDataFoundationPath(updatedDataFoundationPath.getPath());
        File updatedBusinessLayerPath = new File(tempFolder, "BusinessLayerNewName.blx");
        service.save(businessLayer, updatedBusinessLayerPath.getPath(), false);

        // Publishes the updated business layer locally
        service.publish(updatedBusinessLayerPath.getPath(), tempFolder.getPath(), false);

        System.out.println("The Local UNX universe has been locally edited and published successfully ");
        System.out.println("in the path: \"" + tempFolder.getPath() + "\"");
        
        // Releases the loaded resources after usage to avoid memory leak
        service.close(connection);
        service.close(dataFoundation);
        service.close(businessLayer);
    }

}

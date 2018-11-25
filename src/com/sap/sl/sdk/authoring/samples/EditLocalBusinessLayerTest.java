package com.sap.sl.sdk.authoring.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;

import com.sap.sl.sdk.authoring.businesslayer.BlContainer;
import com.sap.sl.sdk.authoring.businesslayer.BlItem;
import com.sap.sl.sdk.authoring.businesslayer.ItemState;
import com.sap.sl.sdk.authoring.businesslayer.RelationalBusinessLayer;
import com.sap.sl.sdk.authoring.local.LocalResourceService;
import com.sap.sl.sdk.framework.SlContext;

public class EditLocalBusinessLayerTest {

    // 
    // This sample is used to edit a business layer stored in a local project 
    // and modify one business item (name, description, state)
    // 

    //
    // !! Edit the sample parameters here !!
    //

	/** Business layer name */
    private static final String BLX_NAME = "<BUSINESSLAYER_NAME>";

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
    public void editLocalBusinessLayerTest() throws SDKException {

        // Gets the LocalResourceService.class service
        LocalResourceService service = context.getService(LocalResourceService.class);

        // Loads the business layer resource
        RelationalBusinessLayer businessLayer = (RelationalBusinessLayer) service.load(LOCAL_FOLDER + "/" + BLX_NAME);

        // Finds the item to edit
        BlItem item = findBlItem(businessLayer.getRootFolder(), "City Name");

        // Updates the BlItem
        item.setName("City Name2");
        item.setDescription("City Name2");
        item.setState(ItemState.valueOf("HIDDEN"));

        // Saves the business layer
        service.save(businessLayer, LOCAL_FOLDER + "/" + BLX_NAME, true);

        // Releases the loaded resources after usage to avoid memory leak
        service.close(businessLayer);

		System.out.println("\n The Business Object has been updated.");
		
	}

    // Looks for a business item with the given name in the given container
    private BlItem findBlItem(BlItem container, String name) {

        if (name.equals(container.getName()))
            return container;

        if (container instanceof BlContainer) {
            for (BlItem item : ((BlContainer) container).getChildren()) {
                BlItem searchResult = findBlItem(item, name);
                if (searchResult != null)
                    return searchResult;
            }
        }

        return null;
    }
}

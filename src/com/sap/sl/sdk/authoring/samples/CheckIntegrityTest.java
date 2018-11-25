package com.sap.sl.sdk.authoring.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.sl.sdk.authoring.checkintegrity.CheckIntegrityRunner;
import com.sap.sl.sdk.authoring.local.LocalResourceService;
import com.sap.sl.sdk.framework.IStatus;
import com.sap.sl.sdk.framework.SlContext;


public class CheckIntegrityTest {

    //
    // This sample explains how to use the check integrity on a resource saved locally on the disk.
	// The resource to be checked can be either a business layer or a data foundation or a connection.
    //

    //
    // !! Edit the test parameters here !!
    //

	/** Connection name */
	private static final String CNX_NAME = "<CONNECTION_NAME>";
    
    /** Data foundation name */
    private static final String DFX_NAME = "<DATAFOUNDATION_NAME>";
    
	/** Business layer name */
    private static final String BLX_NAME = "<BUSINESSLAYER_NAME>";

	/** Local folder used to save all resources locally */
	private static final String LOCAL_FOLDER = System.getProperty("java.io.tmpdir");

    //
    // !! End of parameters definition !!
    //

    private SlContext context;
    private LocalResourceService localResourceService;

    @Before
    public void setUp() throws Exception {
        context = SlContext.create();
        localResourceService = context.getService(LocalResourceService.class);
    }

    @After
    public void tearDown() throws Exception {
        context.close();
    }

    @Test
    public void checkIntegrity_Connection() throws Exception {
    	CheckIntegrityRunner checkIntegrityRunner = localResourceService.createCheckIntegrityRunner(LOCAL_FOLDER + "/" + CNX_NAME);
        IStatus status = checkIntegrityRunner.start();
        System.out.println(dumpStatus(status));
    } 
    
    @Test
    public void checkIntegrity_Datafoundation() throws Exception {
    	CheckIntegrityRunner checkIntegrityRunner = localResourceService.createCheckIntegrityRunner(LOCAL_FOLDER + "/" + DFX_NAME);
        IStatus status = checkIntegrityRunner.start();
        System.out.println(dumpStatus(status));
    } 
    
    @Test
    public void checkIntegrity_Businesslayer() throws Exception {
    	CheckIntegrityRunner checkIntegrityRunner = localResourceService.createCheckIntegrityRunner(LOCAL_FOLDER + "/" + BLX_NAME);
        IStatus status = checkIntegrityRunner.start();
        System.out.println(dumpStatus(status));
    } 
    
   /**
    * Dump the status result into a string flow.
    * @param status
    * @return
    * @throws Exception
    */
    private static String dumpStatus(IStatus status) throws Exception {
		if(status==null) throw new Exception("Status is null");
		
		StringBuilder buffer = new StringBuilder();
		dumpStatus(status, buffer, 0);
		return buffer.toString();
	}

	private static void dumpStatus(IStatus status, StringBuilder buffer, int depth) {
		String newLine = System.getProperty("line.separator");

		if(buffer.length() > 0 ) buffer.append(newLine);
		for(int i = depth; i >= 0; i--) buffer.append("-");
		buffer.append("> " + status.getSeverity().name());
		if(!status.getMessage().equals("")) buffer.append(": " + status.getMessage());
		if(status.getCause()!=null) buffer.append(" " + status.getCause().getMessage());
		if(status.getCode()!=null && !status.getCode().equals("")) buffer.append(" (" + status.getCode() + ")");
		
		if(status.hasChildren()) {
			for(IStatus child : status.getChildren()) {
				dumpStatus(child, buffer, depth + 1);
			}
		}
	}
}

package com.sap.sl.sdk.authoring.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;

import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.authoring.connection.DatabaseConnection;
import com.sap.sl.sdk.authoring.samples.util.AuthenticationMode;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

public class UpdateConnectionTest {

    //
    // This sample is used to update a connection
    // and set a new user and a new password
    //

    //
    // !! Edit the sample parameters here !!
    //

    /** CMS System */
	private static final String CMS_LOG_HOST = "<CMS_HOST_NAME>:<CMS_HOST_PORT>";

	/** User name used to log in to the CMS */
	private static final String CMS_LOG_USER = "<CMS_USER_NAME>";

	/** User password */
    private static final String CMS_LOG_PASS = "<CMS_PASSWORD>";

	/** Authentication mode used to log in to the CMS. Here: Enterprise */
    private static final String CMS_AUTH_MODE = AuthenticationMode.ENTERPRISE;

	/** Full path of the connection in the CMS repository */
    private static final String CNX_PATH = CmsResourceService.CONNECTIONS_ROOT + "/<CONNECTION_PATH>";

	/** New user name to set in the connection */
    private static final String NEW_USER = "<NEW_DB_USER>";

	/** New password to set in the connection */
    private static final String NEW_PASS = "<NEW_DB_PASS>";

	//
    // !! End of parameters definition !!
    //
	
    private SlContext context;
    private IEnterpriseSession enterpriseSession;

    @Before
    public void setUp() throws Exception {

        // Connects to the CMS and creates a session
		context = SlContext.create();
        enterpriseSession = CrystalEnterprise.getSessionMgr().logon(CMS_LOG_USER, CMS_LOG_PASS, CMS_LOG_HOST, CMS_AUTH_MODE);
        context.getService(CmsSessionService.class).setSession(enterpriseSession);

    }

    @After
    public void tearDown() throws Exception {

        // Closes the CMS session
		context.close();
        enterpriseSession.logoff();

    }
    
    @Test
    public void updateConnectionTest() throws SDKException {

        // Gets the CmsResourceService service
        CmsResourceService service = context.getService(CmsResourceService.class);

        // Loads the connection to edit
        DatabaseConnection connection = service.loadConnection(CNX_PATH);

        // Changes the connection user name and password
        connection.getParameter(DatabaseConnection.USER_NAME).setValue(NEW_USER);
        connection.getParameter(DatabaseConnection.PASSWORD).setValue(NEW_PASS);

        // Saves the connection
        service.saveConnection(connection);
        System.out.println("The universe connection has been updated successfully.");

        // Releases the connection
        service.close(connection);

    }

}
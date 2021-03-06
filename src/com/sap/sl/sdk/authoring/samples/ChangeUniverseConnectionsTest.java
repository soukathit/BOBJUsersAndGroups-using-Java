package com.sap.sl.sdk.authoring.samples;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;

import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.authoring.samples.util.AuthenticationMode;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

public class ChangeUniverseConnectionsTest {

    //
    // This sample is used to change a connection used by a multisource universe published in the 
    // CMS repository
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

	/** Full path of the source connection in the CMS repository. The universe must use this connection */
	private static final String CNS_OLD = CmsResourceService.CONNECTIONS_ROOT + "/<OLD_CONNECTION_PATH>";

	/** Full path of the target connection in the CMS repository */ 
    private static final String CNS_NEW = CmsResourceService.CONNECTIONS_ROOT + "/<NEW_CONNECTION_PATH>";

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
    public void changeUniverseConnectionsTest() throws SDKException {

        // Creates a map of the connections used by the universe
        // Such map is used in the case of a multisource universe
        Map<String, String> connectionPathsToReplace = new HashMap<String, String>();

		// Replaces the source connection with the target connection
        connectionPathsToReplace.put(CNS_OLD, CNS_NEW);

        // Changes Connections
		CmsResourceService service = context.getService(CmsResourceService.class);
        service.changeUniverseConnections(UNX_PATH, connectionPathsToReplace);
        System.out.println("The universe connection has been changed successfully.");

    }

}
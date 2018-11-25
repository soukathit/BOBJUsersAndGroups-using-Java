package com.sap.sl.sdk.authoring.samples;

import java.util.List;

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

public class GetUniverseConnectionPathsTest {

    //
    // This sample is used to retrieve the list of connections used by a universe
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

	/** Full path of the universe in the CMS repository */
    private static final String UNX_PATH = CmsResourceService.UNIVERSES_ROOT + "/<UNIVERSE_PATH>";	

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
    public void getUniverseConnectionPathsTest() throws SDKException {
    	
        // Creates the CmsResourceService service
        CmsResourceService service = context.getService(CmsResourceService.class);

        // Retrieves the list of connection paths of the universe
        List<String> paths = service.getUniverseConnections(UNX_PATH);

        System.out.println("The universe has " + paths.size() + " connection(s):");
        for (String path : paths) {
            System.out.println("  - \"" + path + "\"");
        }

    }

}
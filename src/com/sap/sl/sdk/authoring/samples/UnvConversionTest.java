package com.sap.sl.sdk.authoring.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession
;
import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.authoring.samples.util.AuthenticationMode;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

public class UnvConversionTest {

    //
    // This sample is used to convert a universe created with the universe design tool into a UNX universe 
    // that can be authored with the information design tool. The universe source is located in the CMS 
    // repository
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

	/** Full path of the universe to convert in the CMS repository */
    private static final String UNV_PATH = CmsResourceService.UNIVERSES_ROOT + "/<CONVERTED_UNIVERSE_PATH>";

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
    public void unvConversionTest() throws SDKException {

        // Gets the CmsResourceService.class service
        CmsResourceService service = context.getService(CmsResourceService.class);

        // Converts the .unv universe into a .unx universe
        service.convertUniverse(UNV_PATH, CmsResourceService.UNIVERSES_ROOT, null, true);
        System.out.println("The UNV universe has been converted.");

    }

}
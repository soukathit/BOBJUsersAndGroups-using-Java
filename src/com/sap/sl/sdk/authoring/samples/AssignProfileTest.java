package com.sap.sl.sdk.authoring.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;

import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.authoring.cms.CmsSecurityService;
import com.sap.sl.sdk.authoring.samples.util.AuthenticationMode;
import com.sap.sl.sdk.authoring.security.SecurityProfileType;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

public class AssignProfileTest {

    //
    // This test is used to assign or unassign a specified security profile 
    // to a specified user or group
    //

    //
    // !! Edit the test parameters here !!
    //

    /** CMS System */
	private static final String CMS_LOG_HOST = "<CMS_HOST_NAME>:<CMS_HOST_PORT>";

	/** User name used to log in to the CMS */
	private static final String CMS_LOG_USER = "<CMS_USER_NAME>";

	/** User Password */
    private static final String CMS_LOG_PASS = "<CMS_PASSWORD>";

	/** Authentication mode used to log in to the CMS. Here: Enterprise */
    private static final String CMS_AUTH_MODE = AuthenticationMode.ENTERPRISE;

	/** Full path of the universe in the CMS repository */
    private static final String UNX_PATH = CmsResourceService.UNIVERSES_ROOT + "/<UNIVERSE_PATH>";

	/** Name of security profile to assign or unassign */
    private static final String SEC_SP_NAME = "<SP_NAME>";

	/** Name of the user or group to assign/unassign the security profile */
    private static final String SEC_USER_GROUP_NAME = "<GROUP_NAME>";

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
    public void assignProfileTest() throws SDKException {

        // Gets the repository security service
        SecurityProfileType securityProfileType = SecurityProfileType.DATA_SECURITY_PROFILE; // SecurityProfileType.BUSINESS_SECURITY_PROFILE
        CmsSecurityService service = context.getService(CmsSecurityService.class);

        // Assigns the security profile
        service.assignSecurityProfile(UNX_PATH, SEC_SP_NAME, securityProfileType, SEC_USER_GROUP_NAME);
        System.out.println("The security profile has been assigned successfully.");

    }

    @Test
    public void unassignProfileTest() throws SDKException {

        // Creates the repository security service
        SecurityProfileType securityProfileType = SecurityProfileType.DATA_SECURITY_PROFILE; // SecurityProfileType.BUSINESS_SECURITY_PROFILE
        CmsSecurityService service = context.getService(CmsSecurityService.class);

        // Unassigns the security profile
        service.unassignSecurityProfile(UNX_PATH, SEC_SP_NAME, securityProfileType, SEC_USER_GROUP_NAME);
        System.out.println("The security profile has been unassigned successfully.");

    }

}

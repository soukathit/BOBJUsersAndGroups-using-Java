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
import com.sap.sl.sdk.authoring.security.BusinessSecurityProfile;
import com.sap.sl.sdk.authoring.security.SecurityFactory;
import com.sap.sl.sdk.authoring.security.SecurityProfileType;
import com.sap.sl.sdk.authoring.security.SecurityStatus;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

public class BusinessSecurityProfileTest {

    //
    // This sample is used to create a business security profile
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

    /** Name of the business security profile */
    private static final String SEC_BSP_NAME = "<BSP_NAME>";

    /** Name of the view to add as granted in Create Query business security profile*/
    private static final String SEC_CREATE_QUERY_GRANTED_VIEW_NAME = "<VIEW_NAME>";

    /** Name of the object to add as denied in Create Query business security profile */
    // OBJECT_TYPE can be "folder", "dimension", "measure", "filter","attribute"
    private static final String SEC_CREATE_QUERY_DENIED_OBJECT_NAME = "<OBJECT_NAME>|<OBJECT_TYPE>";

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
    public void createBusinessSecurityProfileTest() throws SDKException {

        // Gets services
        CmsSecurityService cmsSecurityService = context.getService(CmsSecurityService.class);
        SecurityFactory securityFactory = context.getService(SecurityFactory.class);

        // Creates the business security profile
        System.out.println("\n- Create new business security profile");
        BusinessSecurityProfile businessSecurityProfile = securityFactory.createBusinessSecurityProfile();
        businessSecurityProfile.setName(SEC_BSP_NAME);

        // Grants and denies view and objects in the business security profile
        System.out.println("\n- Grant view in \"Create Query\" business security profile");
        businessSecurityProfile.getCreateQuerySecuredViews().getGrantedElements().add(SEC_CREATE_QUERY_GRANTED_VIEW_NAME);

        System.out.println("\n- Deny object in \"Create Query\" business security profile");
        businessSecurityProfile.getCreateQuerySecuredObjects().getDeniedElements().add(SEC_CREATE_QUERY_DENIED_OBJECT_NAME);
        
        System.out.println("\n- Grant all objects in \"Display Data\" business security profile");
        businessSecurityProfile.getDisplayDataSecuredObjects().setAllElementsStatus(SecurityStatus.GRANTED);

        // Attaches the business security profile to a universe
        cmsSecurityService.attachSecurityProfile(UNX_PATH, businessSecurityProfile);

        // Assignes the business security profile to the Administrators group
        cmsSecurityService.assignSecurityProfile(UNX_PATH, SEC_BSP_NAME, SecurityProfileType.BUSINESS_SECURITY_PROFILE, "Administrators");

        System.out.println("The business security profile has been created.");

    }

}
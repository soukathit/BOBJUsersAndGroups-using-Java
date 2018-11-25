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
import com.sap.sl.sdk.authoring.security.DataSecurityProfile;
import com.sap.sl.sdk.authoring.security.SecurityFactory;
import com.sap.sl.sdk.authoring.security.SecurityProfileType;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

public class DataSecurityProfileTest {

    //
    // This sample is used to create a data security profile
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

    /** Name of the data security profile */
    private static final String SEC_DSP_NAME = "<DSP_NAME>";

    /** Name of the table that triggers the row restriction */
    private static final String SEC_ROW_RESTRICTION_TABLE_NAME = "<RESTRICTION_TABLE_NAME>";

    /** Name of the WHERE clause */
    private static final String SEC_ROW_RESTRICTION_WHERE_CLAUSE = "<RESTRICTION_WHERE_CLAUSE>";

    /** Name of the original table to replace */
    private static final String SEC_TABLE_MAPPING_ORIGINAL_TABLE_NAME = "<OLD_TABLE_NAME>";

    /** Name of the replacement table */
    private static final String SEC_TABLE_MAPPING_REPLACEMENT_TABLE_NAME = "<NEW_TABLE_NAME>";
    
    /** Path of the original connection attached to the universe */
    private static final String SEC_CONNECTION_MAPPING_ORIGINAL_CONNECTION_PATH = "<ORIGINAL_CONNECTION_PATH>";

    /** Path of the replacement connection */
    private static final String SEC_CONNECTION_MAPPING_REPLACEMENT_CONNECTION_PATH = "<REPLACEMENT_CONNECTION_PATH>";

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
    public void createDataSecurityProfileTest() throws SDKException {

        // Gets services
        CmsSecurityService cmsSecurityService = context.getService(CmsSecurityService.class);
        SecurityFactory securityFactory = context.getService(SecurityFactory.class);

        // Creates the data security profile 
        System.out.println("\n- Create new data security profile");
        DataSecurityProfile dataSecurityProfile = securityFactory.createDataSecurityProfile();
        dataSecurityProfile.setName(SEC_DSP_NAME);

        // Adds a row restriction to the data security profile
        System.out.println("\n- Add new row restriction in the data security profile");
        dataSecurityProfile.getRowRestrictions().add(securityFactory.createRowRestriction(SEC_ROW_RESTRICTION_TABLE_NAME, SEC_ROW_RESTRICTION_WHERE_CLAUSE));

        // Adds a table mapping to the data security profile
        System.out.println("\n- Add new table mapping in the data security profile");
        dataSecurityProfile.getTableMappings().add(securityFactory.createTableMapping(SEC_TABLE_MAPPING_ORIGINAL_TABLE_NAME, SEC_TABLE_MAPPING_REPLACEMENT_TABLE_NAME));
        
        // Adds a connection mapping to the data security profile
        System.out.println("\n- Add new connection mapping in the data security profile");
        dataSecurityProfile.getConnectionMappings().add(securityFactory.createConnectionMapping(SEC_CONNECTION_MAPPING_ORIGINAL_CONNECTION_PATH, SEC_CONNECTION_MAPPING_REPLACEMENT_CONNECTION_PATH));
        
        // Attaches the data security profile to a universe
        cmsSecurityService.attachSecurityProfile(UNX_PATH, dataSecurityProfile);

        // Assigns the data security profile to the Administrators group
        cmsSecurityService.assignSecurityProfile(UNX_PATH, SEC_DSP_NAME, SecurityProfileType.DATA_SECURITY_PROFILE, "Administrators");

        System.out.println("The data security profile has been created.");

    }
}
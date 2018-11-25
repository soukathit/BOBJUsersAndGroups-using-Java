package com.sap.sl.sdk.authoring.samples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;

import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.authoring.local.LocalResourceService;
import com.sap.sl.sdk.authoring.samples.util.AuthenticationMode;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;

public class PublishResourceTest {

	//
	// This sample is used to publish a business layer to the CMS repository in order to create a universe
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

	/** Business layer name */
	private static final String BLX_NAME = "<BUSINESSLAYER_NAME>";

	/** Local folder used to save all resources locally */
	private static final String LOCAL_FOLDER = System.getProperty("java.io.tmpdir");

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
	public void publishCmsResourceTest() throws SDKException {

		// Retrieves the CmsResourceService.class service regarding the session type
		CmsResourceService service = context.getService(CmsResourceService.class);

		// Publishes the universe
		String path = service.publish(LOCAL_FOLDER + "/" + BLX_NAME, CmsResourceService.UNIVERSES_ROOT, true);

		System.out.println("The universe has been published in the repository in: \"" + path + "\"");

	}

	@Test
	public void publishLocalResourceTest() throws SDKException {

		// Retrieves the CmsResourceService.class service regarding the session type
		LocalResourceService service = context.getService(LocalResourceService.class);

		// Publishes the universe
		String path = service.publish(LOCAL_FOLDER + "/" + BLX_NAME, LOCAL_FOLDER, true);

		System.out.println("The universe has been published locally in: \"" + path + "\"");

	}
}
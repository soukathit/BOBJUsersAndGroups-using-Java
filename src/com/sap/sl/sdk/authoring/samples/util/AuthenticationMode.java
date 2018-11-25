package com.sap.sl.sdk.authoring.samples.util;

import com.crystaldecisions.sdk.plugin.authentication.enterprise.IsecEnterpriseBase;
import com.crystaldecisions.sdk.plugin.authentication.ldap.IsecLDAPBase;
import com.crystaldecisions.sdk.plugin.authentication.sap.IsecSAPR3;
import com.crystaldecisions.sdk.plugin.authentication.secwinad.IsecWinADBase;

public class AuthenticationMode {

	//The available parameters matched as an authentication mode are the following constants :
	public final static String ENTERPRISE = IsecEnterpriseBase.PROGID;
	public final static String LDAP = IsecLDAPBase.PROGID;
	public final static String WindowsAD = IsecWinADBase.PROGID;
	public final static String SAP = IsecSAPR3.PROGID;

}

package com.sap.sl.sdk.authoring.conversion;

import static com.sap.sl.sdk.authoring.conversion.ConversionException.credentialsErrorMessage;
import static com.sap.sl.sdk.authoring.conversion.ConversionException.propertiesErrorMessage;
import static com.sap.sl.sdk.authoring.conversion.ConversionException.usageErrorMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.authoring.local.LocalResourceService;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;


public class ConversionApplication {

    // true if the CMS is not useful (optional, default value = true)
    private static final String IS_LOCAL_KEY = "conversion.local";

    // Credentials for the CMS hosting the universe to convert. (required only if conversion.local==false, useless
    // otherwise)
    private static final String CMS_HOST_KEY = "cms.host";
    private static final String CMS_USER_KEY = "cms.username";
    private static final String CMS_PASSWORD_KEY = "cms.password";
    // authentication mode (optional, default value = secEnterprise)
    // value == secEnterprise || secLDAP || secWinAD || secSAPR3
    private static final String CMS_AUTHMODE_KEY = "cms.authmode";

    // The path to the universe to convert.
    private static final String UNV_PATH_KEY = "conversion.unv.path";
    // The target directory for the converted universe.
    private static final String DESTINATIONN_PATH_KEY = "conversion.destination.path";
    private static final String NEW_NAME = "conversion.new.name";
    // true if prompt must be converted (optional, default value = true)
    private static final String CONVERT_PROMPT_KEY = "conversion.prompt";

    private static final String CONVERSION_FileName = "conversion.properties";
    private static final String CONVERSION_ExtensionFile = "properties";
    
    private static final Properties defaultParameters = new Properties();
    static {
        // Sets default parameters
        defaultParameters.setProperty(IS_LOCAL_KEY, "true");
        defaultParameters.setProperty(CMS_AUTHMODE_KEY, "secEnterprise");
        defaultParameters.setProperty(CONVERT_PROMPT_KEY, "true");
    }

    private String host;
    private String user;
    private String password;
    private String authMode;

    private boolean local;
    private String unvPath;
    private String destinationPath;
    private String newName;
    private boolean promptConverted;

    private SlContext context;
    private IEnterpriseSession session;

    public ConversionApplication() {
        context = SlContext.create();
    }

    public boolean isLocal() {
        return local;
    }

    public void loadParameters(final String propertiesPath) throws IOException {
        
        // Loads Properties file.
        Properties properties = new Properties(defaultParameters);

        /*
         * Fortify: Attackers can control the filesystem path argument to FileInputStream() 
         *          Attackers can control the filesystem path argument to File() 
         *          
         * old code :File propertiesFile = new File(propertiesPath);
         */
        File propertiesFile = checkIsFile(propertiesPath);  //$NON-NLS-1$
        
        String fileName = propertiesFile.getName();
        if (!fileName.equals(CONVERSION_FileName)) {
            throw new IllegalArgumentException("conversion File Name is not supported"); //$NON-NLS-1$
        }
        
        String extensionFile = getExtension(propertiesPath, false);
        if (!extensionFile.equals(CONVERSION_ExtensionFile)) {
            throw new IllegalArgumentException("Conversion Extension File is not supported"); //$NON-NLS-1$
        }
       
        FileInputStream propertiesInputStream = new FileInputStream(propertiesFile);
        try {
            properties.load(propertiesInputStream);
        }
        finally {
            propertiesInputStream.close();
        }

        // Looks for parameters.
        local = !properties.getProperty("conversion.local").equalsIgnoreCase("FALSE");
        if (!local) {
            // Looks for credential to log on into a CMS.
            host = properties.getProperty(CMS_HOST_KEY);
            user = properties.getProperty(CMS_USER_KEY);
            password = properties.getProperty(CMS_PASSWORD_KEY);
            authMode = properties.getProperty(CMS_AUTHMODE_KEY);
            if (isEmpty(host) || isEmpty(user) || isEmpty(password)) {
                throw new ConversionException(credentialsErrorMessage);
            }
        }

        // Looks for conversion parameters
        unvPath = properties.getProperty(UNV_PATH_KEY);
        destinationPath = properties.getProperty(DESTINATIONN_PATH_KEY);
        newName = properties.getProperty(NEW_NAME);
        promptConverted = !properties.getProperty(CONVERT_PROMPT_KEY).equalsIgnoreCase("FALSE");

        if (isEmpty(unvPath) || isEmpty(destinationPath)) {
            throw new ConversionException(propertiesErrorMessage);
        }

    }

    public void logonToCMS() throws SDKException {
        // logs on with BOE SDK.
        session = CrystalEnterprise.getSessionMgr().logon(user, password, host, authMode);
        // registers the session into the context.
        context.getService(CmsSessionService.class).setSession(session);
    }

    public void doLocalConversion() {
        LocalResourceService service = context.getService(LocalResourceService.class);
        service.convertUniverse(unvPath, destinationPath, newName, promptConverted);
    }

    public void doRemoteConversion() {
        CmsResourceService service = context.getService(CmsResourceService.class);
        service.convertUniverse(unvPath, destinationPath, null, promptConverted);
    }

    public void dispose() {
        if (session != null)
            session.logoff();
        context.close();
        session = null;
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    private File getAbsolutePathFile(String path) {
        if (isEmpty(path))
            throw new IllegalArgumentException("Path cannot be null or empty."); //$NON-NLS-1$

        return new File(path).getAbsoluteFile();
    }

    private File checkIsFile(String path) {
        return checkIsFile(path, true);
    }

    private File checkIsFile(String path, boolean failed) {
        File file = getAbsolutePathFile(path);
        if (!file.isFile()) {
            if (failed)
                throw new IllegalArgumentException("File not found on file system."); //$NON-NLS-1$

            file = null;
        }

        return file;
    }
    
    private String getExtension(String name, boolean withDot) {
        int index = name.lastIndexOf('.');
        if (index == -1)
            return name;
        return name.substring(index + (withDot ? 0 : 1));
    }

    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getAnonymousLogger();
        if (args.length != 1) {
            logger.log(Level.SEVERE, usageErrorMessage);
            return;
        }

        logger.log(Level.INFO, "Initializing application ...");
        ConversionApplication app = new ConversionApplication();
        try {
            logger.log(Level.INFO, "Loading parameters ...");
            app.loadParameters(args[0]);
            if (app.isLocal()) {
                logger.log(Level.INFO, "Converting local unv ...");
                app.doLocalConversion();
                logger.log(Level.INFO, "Local conversion succeeded.");
            }
            else {
                logger.log(Level.INFO, "Logging into the CMS ...");
                app.logonToCMS();
                logger.log(Level.INFO, "Converting remote unv ...");
                app.doRemoteConversion();
                logger.log(Level.INFO, "Remote conversion succeeded.");
            }
        }
        finally {
            logger.log(Level.INFO, "Releasing resources ...");
            app.dispose();
            logger.log(Level.INFO, "Resources released ...");
        }
    }
}

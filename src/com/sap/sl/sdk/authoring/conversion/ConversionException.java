package com.sap.sl.sdk.authoring.conversion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class ConversionException extends RuntimeException {

    private static final long serialVersionUID = 8644336375231212950L;

    private static String parametersTemplateFile = "conversion.properties";

    // Error messages.
    public static String usageErrorMessage = "Usage error: The application requires 1 argument (The path to a property file).";
    static {
        try {
            URL url = ConversionException.class.getResource(parametersTemplateFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = bufferedReader.readLine();
            if (line != null) {
                usageErrorMessage += "\n\nTemplate for a properties file :\n\n" + line;
            }
            while ((line = bufferedReader.readLine()) != null) {
                usageErrorMessage += "\n" + line;
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static final String credentialsErrorMessage =
            "Unless the property \"conversion.local\" is set with the value \"FALSE\", " +
                    "the properties \"cms.host\",\"cms.user\" and \"cms.password\" are required.";

    public static final String propertiesErrorMessage =
            "the properties \"conversion.unv.path\" and \"conversion.unx.path\" are required.";

    public ConversionException(String message) {
        super(message);
    }

}

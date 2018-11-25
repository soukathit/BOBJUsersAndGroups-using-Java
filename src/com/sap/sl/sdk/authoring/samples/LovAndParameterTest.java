package com.sap.sl.sdk.authoring.samples;

import java.sql.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;
import com.sap.sl.sdk.authoring.businesslayer.BlItem;
import com.sap.sl.sdk.authoring.businesslayer.BusinessHierarchicalLov;
import com.sap.sl.sdk.authoring.businesslayer.BusinessLayer;
import com.sap.sl.sdk.authoring.businesslayer.BusinessLayerFactory;
import com.sap.sl.sdk.authoring.businesslayer.BusinessLayerService;
import com.sap.sl.sdk.authoring.businesslayer.Dimension;
import com.sap.sl.sdk.authoring.businesslayer.Folder;
import com.sap.sl.sdk.authoring.datafoundation.LovParameterDataType;
import com.sap.sl.sdk.authoring.datafoundation.Parameter;
import com.sap.sl.sdk.authoring.datafoundation.SQLQueryLov;
import com.sap.sl.sdk.authoring.datafoundation.StaticLov;
import com.sap.sl.sdk.authoring.datafoundation.StaticLovColumn;
import com.sap.sl.sdk.authoring.datafoundation.StaticLovRow;
import com.sap.sl.sdk.authoring.local.LocalResourceService;
import com.sap.sl.sdk.framework.SlContext;

public class LovAndParameterTest {

    //
    // This sample is used to create and set various lists of values and parameters in a business layer.
    // The universe and its sample database SPL_Warehouse.bak that can be downloaded as an attached file 
    // from this article: http://scn.sap.com/docs/DOC-22145
    // Save the universe resources (connection, data foundation and business layer) in a local folder. 
    // Edit the LOCAL_FOLDER variable below to pass this local folder path and run the sample to create
    // the parameters and list of values in the business layer
    // 
 
    //
    // !! Edit the test parameters here !!
    //
   
    /** Business layer name */
    private static final String BLX_NAME = "Warehouse.blx";
    
    /** Local folder where the business layer has been saved */
    private static final String LOCAL_FOLDER = System.getProperty("java.io.tmpdir");

    //
    // !! End of parameters definition !!
    //

    private SlContext context;
    private LocalResourceService localResourceService;
    private BusinessLayerFactory businessLayerFactory;

    @Before
    public void setUp() throws Exception {

        // Connects to the CMS and creates a session
        context = SlContext.create();
        localResourceService = context.getService(LocalResourceService.class);
        businessLayerFactory = context.getService(BusinessLayerFactory.class);
    }

    @After
    public void tearDown() throws Exception {

        // Closes the CMS session
        context.close();
    }

    @Test
    public void createLov() throws SDKException {
        
        System.out.println(String.format("\n\t- Load local business locally from \"%sWarehouse.blx\"", LOCAL_FOLDER));
        BusinessLayer businessLayer = (BusinessLayer) localResourceService.load(LOCAL_FOLDER + BLX_NAME);

        // ------ Creating a Static LOV

        System.out.println(String.format("\n\t- Create a static LOV \"MyYearLov\" in \"%sWarehouse.blx\"", LOCAL_FOLDER));
        StaticLov staticLovYear = businessLayerFactory.createStaticLov("MyYearLov", businessLayer);

        System.out.println(String.format("\n\t\t- Create column #0 as Numeric --> \"NumericColumn\""));
        StaticLovColumn yearLovColumn = businessLayerFactory.createStaticLovColumn("NumericColumn", staticLovYear);
        yearLovColumn.setHidden(false);
        yearLovColumn.setDataType(LovParameterDataType.NUMERIC);

        System.out.println(String.format("\n\t\t- Add several values in \"MyYearLov\""));
        
        System.out.println(String.format("\n\t\t\t- 1985"));
        StaticLovRow yearLovRow = businessLayerFactory.createStaticLovRow(staticLovYear);
        yearLovRow.getValues().add(businessLayerFactory.createNumericValue(1985D));
        
        System.out.println(String.format("\n\t\t\t- 1998"));
        yearLovRow = businessLayerFactory.createStaticLovRow(staticLovYear);
        yearLovRow.getValues().add(businessLayerFactory.createNumericValue(1998D));
        
        System.out.println(String.format("\n\t\t\t- 2006"));
        yearLovRow = businessLayerFactory.createStaticLovRow(staticLovYear);
        yearLovRow.getValues().add(businessLayerFactory.createNumericValue(2006D));
        
        System.out.println(String.format("\n\t\t\t- 2007"));
        yearLovRow = businessLayerFactory.createStaticLovRow(staticLovYear);
        yearLovRow.getValues().add(businessLayerFactory.createNumericValue(2007D));

        // ------ Creating a Parameter 

        System.out.println(String.format("\n\t- Create a Parameter \"MyParameter\""));
        Parameter parameter = businessLayerFactory.createParameter("MyParameter", businessLayer);
        parameter.setUserPrompted(true);
        parameter.setPromptText("Select a year");
        parameter.setDataType(LovParameterDataType.NUMERIC);
        parameter.setMultipleValuesAllowed(true);
        parameter.setKeepLastValuesEnabled(true);
        parameter.setIndexAwarePrompt(true);

        System.out.println(String.format("\n\t\t- Associate the numeric column of \"MyYearLov\" to \"MyParameter\""));
        parameter.setAssociatedLov(staticLovYear.getColumns().get(0));
        parameter.setSelectedOnlyFromList(true);

        // ------ Creating a Static Multi-Column LOV

        System.out.println(String.format("\n\t- Create a Static Multi-Column LOV \"MyStaticLov\" in \"%sWarehouse.blx\"", LOCAL_FOLDER));
        StaticLov staticLovMultiColumn = businessLayerFactory.createStaticLov("MyStaticLov", businessLayer);
        staticLovMultiColumn.setDescription("This is a static List of values containing several columns");

        System.out.println(String.format("\n\t\t- Create column #0 as Numeric --> \"NumericColumn\""));
        StaticLovColumn numericLovColumn = businessLayerFactory.createStaticLovColumn("NumericColumn", staticLovMultiColumn);
        numericLovColumn.setHidden(false);
        numericLovColumn.setDataType(LovParameterDataType.NUMERIC);

        System.out.println(String.format("\n\t\t- Create column #1 as String with \"NumericColumn\" as key --> \"StringColumn\""));
        StaticLovColumn stringColumn = businessLayerFactory.createStaticLovColumn("StringColumn", staticLovMultiColumn);
        stringColumn.setHidden(true);
        stringColumn.setDataType(LovParameterDataType.STRING);
        stringColumn.setKeyColumn(numericLovColumn);

        System.out.println(String.format("\n\t\t- Create column #2 as Date --> \"DateColumn\""));
        StaticLovColumn dateColumn = businessLayerFactory.createStaticLovColumn("DateColumn", staticLovMultiColumn);
        dateColumn.setHidden(true);
        dateColumn.setDataType(LovParameterDataType.DATE);

        System.out.println(String.format("\n\t\t- Create row"));
        StaticLovRow staticLovRow = businessLayerFactory.createStaticLovRow(staticLovMultiColumn);
        
        System.out.println(String.format("\n\t\t\t- NumericColumn = 18.9"));
        staticLovRow.getValues().add(businessLayerFactory.createNumericValue(18.9));
        
        System.out.println(String.format("\n\t\t\t- StringColumn = a string in [C2:R1]"));
        staticLovRow.getValues().add(businessLayerFactory.createStringValue("a string in [C2:R1]"));
        
        System.out.println(String.format("\n\t\t\t- DateColumn = 14/07/2013"));
        staticLovRow.getValues().add(businessLayerFactory.createDateValue(Date.valueOf("2013-07-15")));        

        // ------ Creating a SQL Query LOV

        System.out.println(String.format("\n\t- Create a SQL Query LOV \"MySQLQueryLov\" in \"%sWarehouse.blx\"", LOCAL_FOLDER));
        SQLQueryLov customerLov = businessLayerFactory.createSQLQueryLov("MySQLQueryLov", businessLayer);
        customerLov.setDescription("This is a SQL Query List of values");
        customerLov.setHidden(false);
        customerLov.setMaxRowCountEnabled(true);
        customerLov.setMaxRowCount(666);
        customerLov.setQueryExecutionTimeoutEnabled(true);
        customerLov.setQueryExecutionTimeout(1732050);
        customerLov.setSQLExpression("SELECT CUSTOMER.COMPANY_NAME FROM CUSTOMER");

        System.out.println(String.format("\n\t\t- Detect columns depending on SQL expression \"%s\"", customerLov.getSQLExpression()));
        BusinessLayerService businessLayerService = context.getService(BusinessLayerService.class);
        businessLayerService.detectLovColumns(customerLov);

        // ------ Creating a Hierarchical LOV

        System.out.println(String.format("\n\t- Create a Hierarchical LOV \"MyBusinessHierarchicalLov\" in \"%sWarehouse.blx\"", LOCAL_FOLDER));
        BusinessHierarchicalLov orderCustLov = businessLayerFactory.createBusinessHierarchicalLov("MyBusinessHierarchicalLov", businessLayer);
        orderCustLov.setDescription("This is a Hierarchical List of values");
        orderCustLov.setHidden(false);
        orderCustLov.setMaxRowCount(666);
        orderCustLov.setQueryExecutionTimeoutEnabled(true);
        orderCustLov.setQueryExecutionTimeout(1732050);

        System.out.println(String.format("\n\t\t- Add dimensions in hierarchical LOV \"MyBusinessHierarchicalLov\""));
        List<BlItem> dimensions = ((Folder) businessLayer.getRootFolder().getChildren().get(0)).getChildren();

        System.out.println(String.format("\n\t\t\t- Dimension #0 : \"Customer Id\""));
        businessLayerFactory.createBusinessHierarchicalLovColumn(orderCustLov, (Dimension) dimensions.get(0));
        
        System.out.println(String.format("\n\t\t\t- Dimension #1 : \"Customer\""));
        businessLayerFactory.createBusinessHierarchicalLovColumn(orderCustLov, (Dimension) dimensions.get(1));

        // ------ Saving the Business Layer

        System.out.println(String.format("\n\t- Save the Business Layer \"%sWarehouse.blx\"", LOCAL_FOLDER));
        localResourceService.save(businessLayer, LOCAL_FOLDER + BLX_NAME, true);
    }
}
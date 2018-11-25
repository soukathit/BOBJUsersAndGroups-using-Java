package com.sap.sl.sdk.authoring.samples;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.sap.sl.sdk.authoring.businesslayer.AccessLevel;
import com.sap.sl.sdk.authoring.businesslayer.AggregateIncompatibility;
import com.sap.sl.sdk.authoring.businesslayer.Attribute;
import com.sap.sl.sdk.authoring.businesslayer.BusinessLayerFactory;
import com.sap.sl.sdk.authoring.businesslayer.BusinessLayerView;
import com.sap.sl.sdk.authoring.businesslayer.CustomDateTimeFormat;
import com.sap.sl.sdk.authoring.businesslayer.DataType;
import com.sap.sl.sdk.authoring.businesslayer.Dimension;
import com.sap.sl.sdk.authoring.businesslayer.Folder;
import com.sap.sl.sdk.authoring.businesslayer.FormatColor;
import com.sap.sl.sdk.authoring.businesslayer.ItemState;
import com.sap.sl.sdk.authoring.businesslayer.Measure;
import com.sap.sl.sdk.authoring.businesslayer.NativeRelationalFilter;
import com.sap.sl.sdk.authoring.businesslayer.NavigationPath;
import com.sap.sl.sdk.authoring.businesslayer.PredefinedDateTimeFormat;
import com.sap.sl.sdk.authoring.businesslayer.PredefinedDateTimeFormatType;
import com.sap.sl.sdk.authoring.businesslayer.RelationalBinding;
import com.sap.sl.sdk.authoring.businesslayer.RelationalBusinessLayer;
import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.authoring.cms.DataFederatorService;
import com.sap.sl.sdk.authoring.connection.ConnectionFactory;
import com.sap.sl.sdk.authoring.connection.ConnectionShortcut;
import com.sap.sl.sdk.authoring.connection.RelationalConnection;
import com.sap.sl.sdk.authoring.datafoundation.CalculatedColumn;
import com.sap.sl.sdk.authoring.datafoundation.Cardinality;
import com.sap.sl.sdk.authoring.datafoundation.Column;
import com.sap.sl.sdk.authoring.datafoundation.DataFoundation;
import com.sap.sl.sdk.authoring.datafoundation.DataFoundationFactory;
import com.sap.sl.sdk.authoring.datafoundation.DataFoundationView;
import com.sap.sl.sdk.authoring.datafoundation.DatabaseTable;
import com.sap.sl.sdk.authoring.datafoundation.MonoSourceDataFoundation;
import com.sap.sl.sdk.authoring.datafoundation.MultiSourceDataFoundation;
import com.sap.sl.sdk.authoring.datafoundation.PrimaryKey;
import com.sap.sl.sdk.authoring.datafoundation.SQLJoin;
import com.sap.sl.sdk.authoring.datafoundation.Table;
import com.sap.sl.sdk.authoring.datafoundation.TableState;
import com.sap.sl.sdk.authoring.datafoundation.TableView;
import com.sap.sl.sdk.authoring.local.LocalResourceService;
import com.sap.sl.sdk.authoring.samples.util.AuthenticationMode;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;


public class CreateUniverseTest {

    //
    // This sample is used to create a universe and publish it in the CMS repository
    // The universe is created on top of the sample database SPL_Warehouse.bak that can be downloaded
    // as an attached file from this article: http://scn.sap.com/docs/DOC-22145
    // The database must be uploaded to MS SQL Server
    //

    //
    // !! Edit the test parameters here !!
    //
    
    /** CMS System */
    private static final String CMS_LOG_HOST = "<CMS_HOST_NAME>:<CMS_HOST_PORT>";

    /** User name used to log in to the CMS */
    private static final String CMS_LOG_USER = "<CMS_USER_NAME>";

    /** User password */
    private static final String CMS_LOG_PASS = "<CMS_PASSWORD>";

    /** Authentication mode used to log in to the CMS. Here: Enterprise */
    private static final String CMS_AUTH_MODE = AuthenticationMode.ENTERPRISE;

    /** Database Host name */
    private static final String CNX_DATASOURCE = "<DB_HOST_NAME>";

    /** Database Owner */
    private static final String CNX_OWNER = "<DB_OWNER>";

    /** Database Qualifier */
    private static final String CNX_QUALIFIER = "<DB_QUALIFIER>";

    /** Database Database */
    private static final String CNX_DATABASE = "<DB_NAME>";

    /** Database User name */
    private static final String CNX_USER = "<DB_USER>";

    /** Database User password */
    private static final String CNX_PASS = "<DB_PASS>";

    /** Local folder used to save all resources locally */
    private static final String LOCAL_FOLDER = System.getProperty("java.io.tmpdir");

    //
    // !! End of parameters definition !!
    //
    
    private SlContext context;
    private IEnterpriseSession enterpriseSession;
    private DataFoundationFactory dataFoundationFactory;
    private DataFederatorService dataFederatorService;
    private LocalResourceService localResourceService;
    
    @Before
    public void setUp() throws Exception {

        // Connects to the CMS and creates a session
        context = SlContext.create();
        enterpriseSession = CrystalEnterprise.getSessionMgr().logon(CMS_LOG_USER, CMS_LOG_PASS, CMS_LOG_HOST, CMS_AUTH_MODE);
        context.getService(CmsSessionService.class).setSession(enterpriseSession);
        dataFoundationFactory = context.getService(DataFoundationFactory.class);
        dataFederatorService = context.getService(DataFederatorService.class);
        localResourceService = context.getService(LocalResourceService.class);
    }

    @After
    public void tearDown() throws Exception {

        // Closes the CMS session
        context.close();
        enterpriseSession.logoff();

    }

    @Test
    public void createMonoSourceUniverse() throws SDKException {

        //
        // ** Connection creation
        //

        System.out.println("\n- Create new connection");
        ConnectionFactory connectionFactory = context.getService(ConnectionFactory.class);
        RelationalConnection connection = connectionFactory.createRelationalConnection("SPL_Warehouse", "MS SQL Server 2008", "JDBC Drivers");
        connection.getParameter("DATASOURCE").setValue(CNX_DATASOURCE);
        connection.getParameter("DATABASE").setValue(CNX_DATABASE);
        connection.getParameter("USER_NAME").setValue(CNX_USER);
        connection.getParameter("PASSWORD").setValue(CNX_PASS);

        // Saves the connection locally
        System.out.println(String.format("\n\t- Save connection locally into \"%sSPL_Warehouse.cnx\"", LOCAL_FOLDER));
        LocalResourceService localResourceService = context.getService(LocalResourceService.class);
        localResourceService.save(connection, LOCAL_FOLDER + "SPL_Warehouse.cnx", true);

        // Publishes the connection to a CMS and retrieves a shortcut
        System.out.println(String.format("\n\t- Publish connection into \"%s/SPL_Warehouse.cnx\" and retrieve a shortcut", CmsResourceService.CONNECTIONS_ROOT));
        CmsResourceService cmsResourceService = context.getService(CmsResourceService.class);
        String cnxCmsPath = cmsResourceService.publish(LOCAL_FOLDER + "SPL_Warehouse.cnx", CmsResourceService.CONNECTIONS_ROOT, true);
        String shortcutPath = cmsResourceService.createShortcut(cnxCmsPath, LOCAL_FOLDER);

        //
        // ** Single-source Data Foundation creation
        //

        System.out.println("\n- Create new data foundation");
        DataFoundationFactory dataFoundationFactory = context.getService(DataFoundationFactory.class);
        MonoSourceDataFoundation dataFoundation = dataFoundationFactory.createMonoSourceDataFoundation("SPL_Warehouse", shortcutPath);

        // Adds tables to the data foundation
        System.out.println("\n\t- Add tables PURCHASE, PRODUCT, CUSTOMER_CALL, CUSTOMER, SO_HEADER");
        DatabaseTable purchaseTable = dataFoundationFactory.createDatabaseTable(CNX_QUALIFIER, CNX_OWNER, "PURCHASE", dataFoundation);
        DatabaseTable productTable = dataFoundationFactory.createDatabaseTable(CNX_QUALIFIER, CNX_OWNER, "PRODUCT", dataFoundation);
        dataFoundationFactory.createDatabaseTable(CNX_QUALIFIER, CNX_OWNER, "CUSTOMER_CALL", dataFoundation);
        dataFoundationFactory.createDatabaseTable(CNX_QUALIFIER, CNX_OWNER, "CUSTOMER", dataFoundation);
        dataFoundationFactory.createDatabaseTable(CNX_QUALIFIER, CNX_OWNER, "SO_HEADER", dataFoundation);

        // Creates joins between the tables and sets cardinalities
        System.out.println("\n\t- Create joins");
        SQLJoin join = createJoin(dataFoundation, "PURCHASE.PRODUCT_ID=PRODUCT.PRODUCT_ID", Cardinality.CN_1);
        createJoin(dataFoundation, "CUSTOMER_CALL.PRODUCT_ID=PRODUCT.PRODUCT_ID", Cardinality.CN_1);
        createJoin(dataFoundation, "CUSTOMER_CALL.CUSTOMER_ID=CUSTOMER.CUSTOMER_ID", Cardinality.CN_1);
        createJoin(dataFoundation, "CUSTOMER.CUSTOMER_ID=SO_HEADER.CUSTOMER_ID", Cardinality.C1_N);
        
        // Gets joins identifier
        System.out.println("\n\t\t- Join ID : " + join.getIdentifier());

        // Creates primary keys
        System.out.println("\n\t- Create primary keys for column #0 of \"PURCHASE\" table and for column \"PRODUCT\".\"CATEGORY_ID\"");
        PrimaryKey purchaseTablePrimaryKey = dataFoundationFactory.createPrimaryKey(purchaseTable);
        Column orderIdColumn = purchaseTable.getColumns().get(0);
        purchaseTablePrimaryKey.getColumns().add(orderIdColumn);

        PrimaryKey productTablePrimaryKey = dataFoundationFactory.createPrimaryKey(productTable);
        for (Column productTableColumn : productTable.getColumns()) {
            if (productTableColumn.getName().equals("CATEGORY_ID")) {
                productTablePrimaryKey.getColumns().add(productTableColumn);
                break;
            }
        }

        // Creates column properties
        System.out.println("\n\t- Create column properties Nullable=false, Signed=false and Visible=true for \"PURCHASE\".\"ORDER_ID\" column");
        orderIdColumn.setNullable(false);
        orderIdColumn.setSigned(false);
        orderIdColumn.setVisible(true);
        
        // Creates calculated columns.
        CalculatedColumn calculatedColumn_DiffQty = dataFoundationFactory.createCalculatedColumn("DIFF_QTY", purchaseTable);
        CalculatedColumn calculatedColumn_AvgCost = dataFoundationFactory.createCalculatedColumn("AVG_COST", purchaseTable);
        calculatedColumn_DiffQty.setExpression("PURCHASE.RECEIVED_QTY - PURCHASE.ORDERED_QTY");
        calculatedColumn_AvgCost.setExpression("PURCHASE.COST / PURCHASE.ORDERED_QTY");
        
        // Retrieves data foundation master view and arranges table views position
        System.out.println("\n\t- Arranges tables on the Master view");
        DataFoundationView masterView = dataFoundation.getMasterView();
        Table table = null;
        TableView tableView = null;
        
		for(int i = 0; i < dataFoundation.getTables().size(); i++) {
			table = dataFoundation.getTables().get(i);
			tableView = dataFoundationFactory.createTableView(table, masterView);
			tableView.setX((i * 200) % 1000);
			tableView.setY((i / 5) * 50);
			tableView.setWidth(150);
			tableView.setTableState(TableState.COLLAPSED);
		}
        
        // Adds a data foundation view and table views
        System.out.println("\n\t- Adds a data foundation view \"My data foundation view\" and table views for \"PRODUCT\" and \"PURCHASE\" tables");
        DataFoundationView dataFoundationView = dataFoundationFactory.createDataFoundationView("My data foundation view", dataFoundation);
        dataFoundationView.setDescription("This is my data foundation view");
        TableView tableView1 = dataFoundationFactory.createTableView(productTable, dataFoundationView);
        TableView tableView2 = dataFoundationFactory.createTableView(purchaseTable, dataFoundationView);

        // Sets table views properties
        System.out.println("\n\t- Set table views properties on \"My data foundation view\"");
        System.out.println("\n\t\t- Set TableState property \"COLLAPSED\" to \"PRODUCT\" table view");
        tableView1.setTableState(TableState.COLLAPSED);
        tableView1.setX(500);
        tableView1.setY(200);
        System.out.println("\n\t\t- Set TableState property \"EXPANDED\" to \"PURCHASE\" table view");
        tableView2.setTableState(TableState.EXPANDED);
        tableView2.setWidth(300);
        
        // Saves the data foundation
        System.out.println(String.format("\n\t- Save data foundation locally into \"%sSPL_Warehouse.dfx\"", LOCAL_FOLDER));
        localResourceService.save(dataFoundation, LOCAL_FOLDER + "SPL_Warehouse.dfx", true);

        //
        // ** Business Layer creation
        //

        BusinessLayerFactory businessLayerFactory = context.getService(BusinessLayerFactory.class);

        // Creates the business layer
        System.out.println("\n- Create business layer");
        RelationalBusinessLayer businessLayer = businessLayerFactory.createRelationalBusinessLayer("SPL_Warehouse", LOCAL_FOLDER + "SPL_Warehouse.dfx");

        // Creates the folder that contains the business layer
        System.out.println("\n\t- Create folder \"Products\"");
        Folder blxFolder1 = businessLayerFactory.createBlItem(Folder.class, "Products", businessLayer.getRootFolder());
        blxFolder1.setDescription("Products folder");
        blxFolder1.setState(ItemState.ACTIVE);

        // Creates a dimension in the business layer
        System.out.println("\n\t- Create dimension \"Sport Products\"");
        Dimension blxDimension1 = businessLayerFactory.createBlItem(Dimension.class, "Sport Products", blxFolder1);
        blxDimension1.setDescription("All Sport products");
        blxDimension1.setAccessLevel(AccessLevel.PUBLIC);
        blxDimension1.setDataType(DataType.STRING);
        blxDimension1.setState(ItemState.ACTIVE);
        RelationalBinding binding = (RelationalBinding) blxDimension1.getBinding();
        binding.setSelect("PRODUCT.PRODUCT_NAME");
        binding.setWhere("PRODUCT.CATEGORY_NAME IN ('Bikes', 'Golf', 'Ski', 'Tennis')");
        
        // Sets source information to the dimension
        System.out.println("\n\t- Set source information to the dimension \"Sport Products\"");
        blxDimension1.setTechnicalInformation("My technical information");
        blxDimension1.setMapping("My mapping");
        blxDimension1.setLineage("My lineage");
        
        // Creates a dimension attribute
        System.out.println("\n\t- Create attribute \"Category\" for \"Sport Products\"");
        Attribute blxDimensionAttribute1 = businessLayerFactory.createBlItem(Attribute.class, "Category", blxDimension1);
        blxDimensionAttribute1.setDescription("Product Category");
        blxDimensionAttribute1.setAccessLevel(AccessLevel.PUBLIC);
        blxDimensionAttribute1.setDataType(DataType.DATE_TIME);
        blxDimensionAttribute1.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimensionAttribute1.getBinding();
        binding.setSelect("PRODUCT.CATEGORY_NAME");

        // Creates a dimension in the business layer
        System.out.println("\n\t- Create dimension \"Products\"");
        Dimension blxDimension2 = businessLayerFactory.createBlItem(Dimension.class, "Products", blxFolder1);
        blxDimension2.setDescription("All products");
        blxDimension2.setAccessLevel(AccessLevel.PUBLIC);
        blxDimension2.setDataType(DataType.STRING);
        blxDimension2.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimension2.getBinding();
        binding.setSelect("PRODUCT.PRODUCT_NAME");

        // Creates a dimension attribute
        System.out.println("\n\t- Create attribute \"Shipped Date in July\" for \"Products\"");
        Attribute blxDimensionAttribute2 = businessLayerFactory.createBlItem(Attribute.class, "Shipped Date in July", blxDimension2);
        blxDimensionAttribute2.setDescription("Select all Shipped Date for July month");
        blxDimensionAttribute2.setAccessLevel(AccessLevel.PUBLIC);
        blxDimensionAttribute2.setDataType(DataType.DATE_TIME);
        blxDimensionAttribute2.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimensionAttribute2.getBinding();
        binding.setSelect("SO_HEADER.SHIPPED_DATE");
        binding.setWhere("SO_HEADER.SHIPPED_DATE BETWEEN '06/30/2005 00:00:00' AND '07/31/2005 23:59:59'");
        
        // Sets predefined date time format to the dimension attribute
        System.out.println("\n\t- Set predefined date time format \"SHORT_DATE_TIME\" to the attribute \"Shipped Date in July\"");
        PredefinedDateTimeFormat predefinedDateTimeFormat = businessLayerFactory.createPredefinedDateTimeFormat(blxDimensionAttribute2);
        predefinedDateTimeFormat.setFormat(PredefinedDateTimeFormatType.SHORT_DATE_TIME);
        
        // Creates a dimension attribute
        System.out.println("\n\t- Create attribute \"Shipped Date\" for \"Products\"");
        Attribute blxDimensionAttribute3 = businessLayerFactory.createBlItem(Attribute.class, "Shipped Date", blxDimension2);
        blxDimensionAttribute3.setDescription("Select all Shipped Date");
        blxDimensionAttribute3.setAccessLevel(AccessLevel.PUBLIC);
        blxDimensionAttribute3.setDataType(DataType.DATE_TIME);
        blxDimensionAttribute3.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimensionAttribute3.getBinding();
        binding.setSelect("SO_HEADER.SHIPPED_DATE");
        
        // Sets custom date time format to the dimension attribute
        System.out.println("\n\t- Set custom date time format \"[Blue]yyyy-MM-dd hh:mm:ss\" to the attribute \"Shipped Date\"");
        CustomDateTimeFormat customDateTimeFormat = businessLayerFactory.createCustomDateTimeFormat(blxDimensionAttribute3);
        customDateTimeFormat.getDateTimeFormat().setFormat("yyyy-MM-dd hh:mm:ss");
        customDateTimeFormat.getDateTimeFormat().setColor(FormatColor.BLUE);

        // Creates a dimension in the business layer
        System.out.println("\n\t- Create dimension \"Products Id\"");
        Dimension blxDimension3 = businessLayerFactory.createBlItem(Dimension.class, "Products Id", blxFolder1);
        blxDimension3.setDescription("List all Products Id");
        blxDimension3.setAccessLevel(AccessLevel.PUBLIC);
        blxDimension3.setDataType(DataType.STRING);
        blxDimension3.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimension3.getBinding();
        binding.setSelect("PRODUCT.PRODUCT_ID");

        // Creates a folder in the business layer
        System.out.println("\n\t- Create folder \"Purchase\"");
        Folder blxFolder2 = businessLayerFactory.createBlItem(Folder.class, "Purchase", businessLayer.getRootFolder());
        blxFolder2.setDescription("Purchase folder");
        blxFolder2.setState(ItemState.ACTIVE);

        // Creates a measure in the business layer
        System.out.println("\n\t- Create measure \"Total Cost\"");
        Measure blxMeasure1 = businessLayerFactory.createBlItem(Measure.class, "Total Cost", blxFolder2);
        blxMeasure1.setDescription("Total cost");
        blxMeasure1.setAccessLevel(AccessLevel.PUBLIC);
        blxMeasure1.setDataType(DataType.NUMERIC);
        blxMeasure1.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxMeasure1.getBinding();
        binding.setSelect("Sum(PURCHASE.COST)");
        
        // Creates City & Country dimension.
        Dimension blxDimension4 = businessLayerFactory.createBlItem(Dimension.class, "City & Country", businessLayer.getRootFolder());
		((RelationalBinding) blxDimension4.getBinding()).setSelect("\"Federated Table\".\"City & Country\"");
		
		// Creates attribute continent attached to City & Country dimension
		Attribute blxDimensionAttribute4 = businessLayerFactory.createBlItem(Attribute.class, "Continent", blxDimension4);
		((RelationalBinding) blxDimensionAttribute4.getBinding()).setSelect("@catalog('SPL_WAREHOUSE').\"SPL_Warehouse.dbo\".\"CONTINENT\".\"CONTINENT_NAME\"");

		// Creates Number Of Customers measure
		Measure blxMeasure2 = businessLayerFactory.createBlItem(Measure.class, "Number Of Customers", businessLayer.getRootFolder());
		((RelationalBinding) blxMeasure2.getBinding()).setSelect("sum(\"Federated Table\".\"Number of Customers\")");
		
		// Creates Low Number of Customers native filter 
		NativeRelationalFilter nativeFilter = businessLayerFactory.createBlItem(NativeRelationalFilter.class, "Low Number of Customers", businessLayer.getRootFolder());
		((RelationalBinding) nativeFilter.getBinding()).setWhere("\"Federated Table\".\"Number of Customers\" <= 3");

        // Creates a predefined filter in the business layer
        System.out.println("\n\t- Create predefined filter \"Nokia 7360\"");
        NativeRelationalFilter blxFilter = businessLayerFactory.createBlItem(NativeRelationalFilter.class, "Nokia 7360", blxFolder2);
        blxFilter.setDescription("Nokia 7360");
        blxFilter.getBinding().setWhere("\"PRODUCT\".\"PRODUCT_NAME\"='Nokia 7360'");
        blxFilter.setState(ItemState.ACTIVE);

        // Creates custom properties in the business layer
        System.out.println("\n\t- Create custom property for the business layer");
        businessLayerFactory.createCustomProperty("Property on business layer", "Value of business layer property", businessLayer);

        System.out.println("\n\t- Create custom property for the filter \"Nokia 7360\"");
        businessLayerFactory.createCustomProperty("Property on filter", "Value of filter property", blxFilter);

        // Creates a business layer view in the business layer
        System.out.println("\n\t- Create business layer view");
        BusinessLayerView businessLayerView = businessLayerFactory.createBusinessLayerView("My personal view", businessLayer);
        businessLayerView.getObjects().add(blxMeasure1);
        businessLayerView.getObjects().add(blxFilter);

        System.out.println("\n\t- Hide the master view");
        businessLayer.setMasterViewHidden(true);
        
        // Creates aggregate incompatibility
        AggregateIncompatibility aggregateIncompatibility = businessLayerFactory.createAggregateIncompatibility(businessLayer);
        aggregateIncompatibility.setTableName("SPL_WAREHOUSE.\"SPL_Warehouse.dbo\".CONTINENT");
        aggregateIncompatibility.getIncompatibleObjects().add(blxDimension4);
		aggregateIncompatibility.getIncompatibleObjects().add(blxDimensionAttribute4);
		aggregateIncompatibility.getIncompatibleObjects().add(blxMeasure2);
		aggregateIncompatibility.getIncompatibleObjects().add(nativeFilter);
		
		// Creates navigation paths	for Products
		System.out.println("\n\t- Create custom navigation path");
		NavigationPath navigationPathProducts = businessLayerFactory.createNavigationPath("Custom navigation by geography", businessLayer);
		navigationPathProducts.setDescription("My custom navigation");
		ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
		dimensions.add(blxDimension4);
		dimensions.add(blxDimension2);
		navigationPathProducts.getDimensions().addAll(dimensions);
		dimensions.clear();
        
        // Saves the business layer
        System.out.println(String.format("\n\t- Save business layer locally into \"%sSPL_Warehouse.blx\"", LOCAL_FOLDER));
        localResourceService.save(businessLayer, LOCAL_FOLDER + "SPL_Warehouse.blx", true);

        //
        // ** Business Layer publication
        //

        System.out.println(String.format("\n\t- Publish business layer into \"%s/SPL_Warehouse.unx\"", CmsResourceService.UNIVERSES_ROOT));
        cmsResourceService.publish(LOCAL_FOLDER + "SPL_Warehouse.blx", CmsResourceService.UNIVERSES_ROOT, true);

        // Releases the loaded resources after usage to avoid memory leak
        localResourceService.close(businessLayer);
        localResourceService.close(dataFoundation);
        localResourceService.close(connection);
    }

    @Test
    public void createMultiSourceUniverse() throws SDKException {

        //
        // ** Connection creation
        //

        System.out.println("\n- Create 2 new connections");
        ConnectionFactory connectionFactory = context.getService(ConnectionFactory.class);
        RelationalConnection connection1 = connectionFactory.createRelationalConnection("SPL_Warehouse1", "MS SQL Server 2008", "JDBC Drivers");
        connection1.getParameter("DATASOURCE").setValue(CNX_DATASOURCE);
        connection1.getParameter("DATABASE").setValue(CNX_DATABASE);
        connection1.getParameter("USER_NAME").setValue(CNX_USER);
        connection1.getParameter("PASSWORD").setValue(CNX_PASS);

        RelationalConnection connection2 = connectionFactory.createRelationalConnection("SPL_Warehouse2", "MS SQL Server 2008", "JDBC Drivers");
        connection2.getParameter("DATASOURCE").setValue(CNX_DATASOURCE);
        connection2.getParameter("DATABASE").setValue(CNX_DATABASE);
        connection2.getParameter("USER_NAME").setValue(CNX_USER);
        connection2.getParameter("PASSWORD").setValue(CNX_PASS);

        // Saves the connections locally
        System.out.println(String.format("\n\t- Save connection #1 locally into \"%sSPL_Warehouse1.cnx\"", LOCAL_FOLDER));
        System.out.println(String.format("\n\t- Save connection #2 locally into \"%sSPL_Warehouse2.cnx\"", LOCAL_FOLDER));
        localResourceService.save(connection1, LOCAL_FOLDER + "SPL_Warehouse1.cnx", true);
        localResourceService.save(connection2, LOCAL_FOLDER + "SPL_Warehouse2.cnx", true);

        // Publishes the connections to a CMS and retrieves shortcuts
        System.out.println(String.format("\n\t- Publish connection #1 into \"%s/SPL_Warehouse1.cnx\" and retrieve a shortcut", CmsResourceService.CONNECTIONS_ROOT));
        System.out.println(String.format("\n\t- Publish connection #2 into \"%s/SPL_Warehouse2.cnx\" and retrieve a shortcut", CmsResourceService.CONNECTIONS_ROOT));
        CmsResourceService cmsResourceService = context.getService(CmsResourceService.class);
        String cnxCmsPath1 = cmsResourceService.publish(LOCAL_FOLDER + "SPL_Warehouse1.cnx", CmsResourceService.CONNECTIONS_ROOT, true);
        String shortcutPath1 = cmsResourceService.createShortcut(cnxCmsPath1, LOCAL_FOLDER);
        String cnxCmsPath2 = cmsResourceService.publish(LOCAL_FOLDER + "SPL_Warehouse2.cnx", CmsResourceService.CONNECTIONS_ROOT, true);
        String shortcutPath2 = cmsResourceService.createShortcut(cnxCmsPath2, LOCAL_FOLDER);

        checkCatalog(shortcutPath1, "SPL_WAREHOUSE1");
        checkCatalog(shortcutPath2, "SPL_WAREHOUSE2");

        //
        // ** Multisource-enabled Data Foundation creation
        //

        System.out.println("\n- Create new data foundation");
        DataFoundationFactory dataFoundationFactory = context.getService(DataFoundationFactory.class);
        MultiSourceDataFoundation dataFoundation = dataFoundationFactory.createMultiSourceDataFoundation("MSU_SPL_Warehouse");
        dataFoundationFactory.createDataFederatorSourceInfo(shortcutPath1, "SPL_WAREHOUSE1", dataFoundation);
        dataFoundationFactory.createDataFederatorSourceInfo(shortcutPath2, "SPL_WAREHOUSE2", dataFoundation);

        // Adds tables to the data foundation
        System.out.println("\n\t- Add tables PURCHASE, PRODUCT from SPL_WAREHOUSE1");
        System.out.println("\n\t- Add tables CUSTOMER_CALL, CUSTOMER, SO_HEADER from SPL_WAREHOUSE2");
        DatabaseTable purchaseTable = dataFoundationFactory.createDatabaseTable("SPL_WAREHOUSE1", "SPL_Warehouse.dbo", "PURCHASE", dataFoundation);
        DatabaseTable productTable = dataFoundationFactory.createDatabaseTable("SPL_WAREHOUSE1", "SPL_Warehouse.dbo", "PRODUCT", dataFoundation);
        DatabaseTable customerCallTable = dataFoundationFactory.createDatabaseTable("SPL_WAREHOUSE2", "SPL_Warehouse.dbo", "CUSTOMER_CALL", dataFoundation);
        dataFoundationFactory.createDatabaseTable("SPL_WAREHOUSE2", "SPL_Warehouse.dbo", "CUSTOMER", dataFoundation);
        dataFoundationFactory.createDatabaseTable("SPL_WAREHOUSE2", "SPL_Warehouse.dbo", "SO_HEADER", dataFoundation);

        // Creates joins between the tables and sets cardinalities
        System.out.println("\n\t- Create joins");
        SQLJoin join = createJoin(dataFoundation, getFullName("SPL_WAREHOUSE1", "\"PURCHASE\".\"PRODUCT_ID\"") + "=" + getFullName("SPL_WAREHOUSE1", "\"PRODUCT\".\"PRODUCT_ID\""), Cardinality.CN_1);
        createJoin(dataFoundation, getFullName("SPL_WAREHOUSE2", "\"CUSTOMER_CALL\".\"PRODUCT_ID\"") + "=" + getFullName("SPL_WAREHOUSE1", "\"PRODUCT\".\"PRODUCT_ID\""), Cardinality.CN_1);
        createJoin(dataFoundation, getFullName("SPL_WAREHOUSE2", "\"CUSTOMER_CALL\".\"CUSTOMER_ID\"") + "=" + getFullName("SPL_WAREHOUSE2", "\"CUSTOMER\".\"CUSTOMER_ID\""), Cardinality.CN_1);
        createJoin(dataFoundation, getFullName("SPL_WAREHOUSE2", "\"CUSTOMER\".\"CUSTOMER_ID\"") + "=" + getFullName("SPL_WAREHOUSE2", "\"SO_HEADER\".\"CUSTOMER_ID\""), Cardinality.C1_N);

        // Gets joins identifier
        System.out.println("\n\t\t- Join ID : " + join.getIdentifier());
        
        // Creates primary keys
        System.out.println("\n\t- Create primary keys for column #0 of \"PURCHASE\" table and for column \"PRODUCT\".\"CATEGORY_ID\"");
        PrimaryKey purchaseTablePrimaryKey = dataFoundationFactory.createPrimaryKey(purchaseTable);
        Column orderIdColumn = purchaseTable.getColumns().get(0);
        purchaseTablePrimaryKey.getColumns().add(orderIdColumn);

        PrimaryKey productTablePrimaryKey = dataFoundationFactory.createPrimaryKey(productTable);
        for (Column productTableColumn : productTable.getColumns()) {
            if (productTableColumn.getName().equals("CATEGORY_ID")) {
                productTablePrimaryKey.getColumns().add(productTableColumn);
                break;
            }
        }

        // Creates column properties
        System.out.println("\n\t- Create column properties Nullable=false, Signed=false and Visible=true for \"PURCHASE\".\"ORDER_ID\" column");
        orderIdColumn.setNullable(false);
        orderIdColumn.setSigned(false);
        orderIdColumn.setVisible(true);
        
        // Retrieves data foundation master view and arrange table views position
        System.out.println("\n\t- Arrange tables on the Master view");
        DataFoundationView masterView = dataFoundation.getMasterView();
        Table table = null;
        TableView tableView = null;
        
		for(int i = 0; i < dataFoundation.getTables().size(); i++) {
			table = dataFoundation.getTables().get(i);
			tableView = dataFoundationFactory.createTableView(table, masterView);
			tableView.setX((i * 200) % 1000);
			tableView.setY((i / 5) * 50);
			tableView.setWidth(150);
			tableView.setTableState(TableState.COLLAPSED);
		}
        
        // Adds a data foundation view and table views
        System.out.println("\n\t- Adds a data foundation view \"My data foundation view\" and table views for \"CUSTOMER_CALL\", \"PRODUCT\" and \"PURCHASE\" tables");
        DataFoundationView dataFoundationView = dataFoundationFactory.createDataFoundationView("My data foundation view", dataFoundation);
        dataFoundationView.setDescription("This is my data foundation view");
        TableView tableView1 = dataFoundationFactory.createTableView(productTable, dataFoundationView);
        TableView tableView2 = dataFoundationFactory.createTableView(purchaseTable, dataFoundationView);
        TableView tableView3 = dataFoundationFactory.createTableView(customerCallTable, dataFoundationView);

        // Sets table views properties
        System.out.println("\n\t- Set table views properties on \"My data foundation view\"");
        System.out.println("\n\t\t- Set TableState \"COLLAPSED\" to \"PRODUCT\" table view on \"My data foundation view\"");
        tableView1.setTableState(TableState.COLLAPSED);
        tableView1.setX(500);
        tableView1.setY(200);
        System.out.println("\n\t\t- Set TableState \"EXPANDED\" to \"PURCHASE\" table view on \"My data foundation view\"");
        tableView2.setTableState(TableState.EXPANDED);
        tableView2.setWidth(300);
        System.out.println("\n\t\t- Set TableState \"JOINS_ONLY\" to \"CUSTOMER_CALL\" table view on \"My data foundation view\"");
        tableView3.setTableState(TableState.JOINS_ONLY);
        tableView3.setX(500);
        tableView3.setY(50);

        // Saves the data foundation
        System.out.println(String.format("\n\t- Save data foundation locally into \"%sMSU_SPL_Warehouse.dfx\"", LOCAL_FOLDER));
        localResourceService.save(dataFoundation, LOCAL_FOLDER + "MSU_SPL_Warehouse.dfx", true);

        //
        // ** Business Layer creation
        //

        BusinessLayerFactory businessLayerFactory = context.getService(BusinessLayerFactory.class);

        // Creates the business layer
        System.out.println("\n- Create business layer");
        RelationalBusinessLayer businessLayer = businessLayerFactory.createRelationalBusinessLayer("MSU_SPL_Warehouse", LOCAL_FOLDER + "MSU_SPL_Warehouse.dfx");

        // Creates the folder that contains the business layer
        System.out.println("\n\t- Create folder \"Products\"");
        Folder blxFolder1 = businessLayerFactory.createBlItem(Folder.class, "Products", businessLayer.getRootFolder());
        blxFolder1.setDescription("Products folder");
        blxFolder1.setState(ItemState.ACTIVE);

        // Creates a dimension in the business layer
        System.out.println("\n\t- Create dimension \"Sport Products\"");
        Dimension blxDimension1 = businessLayerFactory.createBlItem(Dimension.class, "Sport Products", blxFolder1);
        blxDimension1.setDescription("All Sport products");
        blxDimension1.setAccessLevel(AccessLevel.PUBLIC);
        blxDimension1.setDataType(DataType.STRING);
        blxDimension1.setState(ItemState.ACTIVE);
        RelationalBinding binding = (RelationalBinding) blxDimension1.getBinding();
        binding.setSelect(getFullName("SPL_WAREHOUSE1", "PRODUCT.PRODUCT_NAME"));
        binding.setWhere(getFullName("SPL_WAREHOUSE1", "PRODUCT.CATEGORY_NAME") + " IN ('Bikes', 'Golf', 'Ski', 'Tennis')");

        // Sets source information to the dimension
        System.out.println("\n\t- Set source information to the dimension \"Sport Products\"");
        blxDimension1.setTechnicalInformation("My technical information");
        blxDimension1.setMapping("My mapping");
        blxDimension1.setLineage("My lineage");
        
        // Creates a dimension attribute
        System.out.println("\n\t- Create attribute \"Category\" for \"Sport Products\"");
        Attribute blxDimensionAttribute1 = businessLayerFactory.createBlItem(Attribute.class, "Category", blxDimension1);
        blxDimensionAttribute1.setDescription("Product Category");
        blxDimensionAttribute1.setAccessLevel(AccessLevel.PUBLIC);
        blxDimensionAttribute1.setDataType(DataType.DATE_TIME);
        blxDimensionAttribute1.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimensionAttribute1.getBinding();
        binding.setSelect(getFullName("SPL_WAREHOUSE1", "PRODUCT.CATEGORY_NAME"));

        // Creates a dimension in the business layer
        System.out.println("\n\t- Create dimension \"Products\"");
        Dimension blxDimension2 = businessLayerFactory.createBlItem(Dimension.class, "Products", blxFolder1);
        blxDimension2.setDescription("All products");
        blxDimension2.setAccessLevel(AccessLevel.PUBLIC);
        blxDimension2.setDataType(DataType.STRING);
        blxDimension2.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimension2.getBinding();
        binding.setSelect(getFullName("SPL_WAREHOUSE1", "PRODUCT.PRODUCT_NAME"));

        // Creates a dimension attribute
        System.out.println("\n\t- Create attribute \"Shipped Date in July\" for \"Products\"");
        Attribute blxDimensionAttribute2 = businessLayerFactory.createBlItem(Attribute.class, "Shipped Date in July", blxDimension2);
        blxDimensionAttribute2.setDescription("Select all Shipped Date for July month");
        blxDimensionAttribute2.setAccessLevel(AccessLevel.PUBLIC);
        blxDimensionAttribute2.setDataType(DataType.DATE_TIME);
        blxDimensionAttribute2.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimensionAttribute2.getBinding();
        binding.setSelect(getFullName("SPL_WAREHOUSE2", "SO_HEADER.SHIPPED_DATE"));
        binding.setWhere(getFullName("SPL_WAREHOUSE2", "SO_HEADER.SHIPPED_DATE BETWEEN") + " '06/30/2005 00:00:00' AND '07/31/2005 23:59:59'");

        // Sets predefined date time format to the dimension attribute
        System.out.println("\n\t- Set predefined date time format \"LONG_DATE_TIME\" to the attribute \"Shipped Date in July\"");
        PredefinedDateTimeFormat predefinedDateTimeFormat = businessLayerFactory.createPredefinedDateTimeFormat(blxDimensionAttribute2);
        predefinedDateTimeFormat.setFormat(PredefinedDateTimeFormatType.LONG_DATE_TIME);
        
        // Creates a dimension attribute
        System.out.println("\n\t- Create attribute \"Shipped Date\" for \"Products\"");
        Attribute blxDimensionAttribute3 = businessLayerFactory.createBlItem(Attribute.class, "Shipped Date", blxDimension2);
        blxDimensionAttribute3.setDescription("Select all Shipped Date");
        blxDimensionAttribute3.setAccessLevel(AccessLevel.PUBLIC);
        blxDimensionAttribute3.setDataType(DataType.DATE_TIME);
        blxDimensionAttribute3.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimensionAttribute3.getBinding();
        binding.setSelect(getFullName("SPL_WAREHOUSE2", "SO_HEADER.SHIPPED_DATE"));
        
        // Sets custom date time format to the dimension attribute
        System.out.println("\n\t- Set custom date time format \"[Red]MM-dd-yyyy hh:mm:ss\" to the attribute \"Shipped Date\"");
        CustomDateTimeFormat customDateTimeFormat = businessLayerFactory.createCustomDateTimeFormat(blxDimensionAttribute3);
        customDateTimeFormat.getDateTimeFormat().setFormat("MM-dd-yyyy hh:mm:ss");
        customDateTimeFormat.getDateTimeFormat().setColor(FormatColor.RED);

        // Creates a dimension in the business layer
        System.out.println("\n\t- Create dimension \"Products Id\"");
        Dimension blxDimension3 = businessLayerFactory.createBlItem(Dimension.class, "Products Id", blxFolder1);
        blxDimension3.setDescription("List all Products Id");
        blxDimension3.setAccessLevel(AccessLevel.PUBLIC);
        blxDimension3.setDataType(DataType.STRING);
        blxDimension3.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxDimension3.getBinding();
        binding.setSelect(getFullName("SPL_WAREHOUSE1", "PRODUCT.PRODUCT_ID"));

        // Creates a folder in the business layer
        System.out.println("\n\t- Create folder \"Purchase\"");
        Folder blxFolder2 = businessLayerFactory.createBlItem(Folder.class, "Purchase", businessLayer.getRootFolder());
        blxFolder2.setDescription("Purchase folder");
        blxFolder2.setState(ItemState.ACTIVE);

        // Creates a measure in the business layer
        System.out.println("\n\t- Create measure \"Total Cost\"");
        Measure blxMeasure = businessLayerFactory.createBlItem(Measure.class, "Total Cost", blxFolder2);
        blxMeasure.setDescription("Total cost");
        blxMeasure.setAccessLevel(AccessLevel.PUBLIC);
        blxMeasure.setDataType(DataType.NUMERIC);
        blxMeasure.setState(ItemState.ACTIVE);
        binding = (RelationalBinding) blxMeasure.getBinding();
        binding.setSelect("Sum("+getFullName("SPL_WAREHOUSE1", "\"PURCHASE\".\"COST\"")+")");

        // Creates a predefined filter in the business layer
        System.out.println("\n\t- Create predefined filter \"Nokia 7360\"");
        NativeRelationalFilter blxFilter = businessLayerFactory.createBlItem(NativeRelationalFilter.class, "Nokia 7360", blxFolder2);
        blxFilter.setDescription("Nokia 7360");
        blxFilter.getBinding().setWhere(getFullName("SPL_WAREHOUSE1", "PRODUCT.PRODUCT_NAME") + "='Nokia 7360'");
        blxFilter.setState(ItemState.ACTIVE);

        // Creates custom properties in the business layer
        System.out.println("\n\t- Create custom property for the business layer");
        businessLayerFactory.createCustomProperty("Property on business layer", "Value of business layer property", businessLayer);

        System.out.println("\n\t- Create custom property for the filter \"Nokia 7360\"");
        businessLayerFactory.createCustomProperty("Property on filter", "Value of filter property", blxFilter);

        // Creates a business layer view in the business layer
        System.out.println("\n\t- Create business layer view");
        BusinessLayerView businessLayerView = businessLayerFactory.createBusinessLayerView("My personal view", businessLayer);
        businessLayerView.getObjects().add(blxMeasure);
        businessLayerView.getObjects().add(blxFilter);

        // Saves the business layer
        System.out.println(String.format("\n\t- Save business layer locally into \"%sMSU_SPL_Warehouse.blx\"", LOCAL_FOLDER));
        localResourceService.save(businessLayer, LOCAL_FOLDER + "MSU_SPL_Warehouse.blx", true);

        //
        // ** Business Layer publication
        //

        System.out.println(String.format("\n\t- Publish business layer into \"%s/MSU_SPL_Warehouse.unx\"", CmsResourceService.UNIVERSES_ROOT));
        cmsResourceService.publish(LOCAL_FOLDER + "MSU_SPL_Warehouse.blx", CmsResourceService.UNIVERSES_ROOT, true);

        // Releases the loaded resources after usage to avoid memory leak
        localResourceService.close(businessLayer);
        localResourceService.close(dataFoundation);
        localResourceService.close(connection1);

    }

    private void checkCatalog(String shortcutPath, String catalogName) {
        ConnectionShortcut connectionShortcut = (ConnectionShortcut) localResourceService.load(shortcutPath);
        String catalog = dataFederatorService.getCatalog(connectionShortcut);
        if (catalog == null)
            dataFederatorService.deploy(connectionShortcut, catalogName);
    }

    private String getFullName(String shortName, String name) {
        return "@catalog('" + shortName + "').\"SPL_Warehouse.dbo\"." + name;
    }

    private SQLJoin createJoin(DataFoundation dataFoundation, String expression, Cardinality cardinality) {
        
    	SQLJoin join = dataFoundationFactory.createSqlJoin(expression, dataFoundation);
        join.setCardinality(cardinality);
        return join;

    }
}

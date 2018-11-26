This project is to get the BOBJ Users and Groups from SAP BOBJ CMC Server. The Java Project uses SAP BOBJ SDK's and connects to CMC Server and pull the users and groups created in the BOBJ Server. We need to do some initial setup to run this Java project.

Follow the configurations as mentioned in the URL in Eclipse.

To Deploy the BI Semantic Layer Java SDK in a Non-OSGI Eclipse Configuration

https://help.sap.com/viewer/4359a0ef221e4a1098bae432bdd982c1/4.2.3/en-US/45f1ee226e041014910aba7db0e91070.html

Create the project folder by following the steps in the above mentioned URL

The main file to run is BOBJUsersAndGroupsMain.java under src\com\tutorialspot\demo\hr\model folder.

Please follow the below steps to export the project into executable JAR files. This is tested successfully in linux environment.
1. Right Click on the project folder in Eclipse -> Export
2. Java-> Runnable JAR File
3. Select the main class file BOBJUsersAndGroupsMain under Launch Configuration
4. Export Destination - Provide the path of the file and name to save this JAR file prefrebbly in the output_files_jars folder
5. Under Library Handling - Copy the required files into a sub-folder next to the generated JAR
6. Click Next and Finish.

All the complied JAVA files will be avaiable in the bin folder that gets generated.

The users and groups are loaded into a SAP HANA database. Any database can be used to stored the data. We need to use the respective jdbc JAR files for the program to work.

The java program can be executed by running the shell script from the config files folder (BOBJUSersAndGroups_Script.sh). Please modify the shell sript to add the path of the JAR files and BOBJUsersAndGroupsMain.properties properties file.

The SAP BOBJ CMC URL , username and  password needs to be provided in the BOBJUsersAndGroupsMain.properties folder. The authentication it uses is Enterprise.

create three tables in the database.
1.bobj_users - To store the User Ids in the BOBJ system
2.bobj_roles - To store the roles created in the BOOBJ system
3.bobj_user_roles - To store the user id and role id in the bobj system

CREATE TABLE BOBJ_USERS
(
USER_ID varchar(4000),
USER_NAME varchar(4000),
USER_FULL_NAME varchar(4000),
USER_EMAIL_ADDRESS varchar(4000),
SERVER_NAME varchar(4000),
LOAD_DATE varchar(4000)
)

CREATE TABLE BOBJ_ROLES
(
ROLE_ID varchar(4000),
ROLE_NAME varchar(4000),
ROLE_DESCRIPTION varchar(4000),
LOAD_DATE varchar(4000),
SERVER_NAME varchar(4000)
)


CREATE TABLE BOBJ_USER_ROLES
(
USER_ID varchar(4000),
ROLE_ID varchar(4000),
SERVER_NAME varchar(4000),
LOAD_DATE varchar(4000)
)






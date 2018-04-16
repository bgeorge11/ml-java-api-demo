# ml-java-api-demo
A demonstration of using MarkLogic java management and CRUD APIs through parallel Junit suites
Steps 
     - Download the project (maven project)
     - Check the values in the properties file

To build
     > mvn clean
     > mvn install -DskipTests=true # Do not run the tests automatically as it would take time and not in the order you want to execute
To run 
  For the suite where CRUD test cases are not creating databases and setting up of databases done separtely. 

     > mvn -Dtest=CreateDatabasesInParallel test # this will create the number of databases as in database.properties
     > mvn -Dtest=TestAllParallelWithoutDatabase test # this will run the CRUD test cases parallely. The databases created in previous steps will be used 
     > mvn -Dtest=DeleteTestDatabases test # this will delete the databases earlier created. 
     
  For the suite where CRUD test cases are creating and tearing down databases       
     > mvn -Dtest=TestAllParallelWithDatabase test # All CRUD test cases are run parallely. 

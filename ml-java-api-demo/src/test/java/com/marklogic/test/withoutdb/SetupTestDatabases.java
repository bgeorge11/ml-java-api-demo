package com.marklogic.test.withoutdb;


import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.marklogic.mgmt.api.database.Database;
import com.marklogic.mgmt.api.forest.Forest;

@Configuration
@PropertySource(value = { "classpath:database.properties", "classpath:user.properties"}, ignoreResourceNotFound = true)
public class SetupTestDatabases extends AbstractApiTest {

	    private Database db;
	    private Forest f1, f2;

	    @Value("${mlHost}")
	    private String ML_HOST;
	    @Value("${mlUser}")
	    private String ML_USER;
	    @Value("${mlPassword}")
	    private String ML_PASSWORD;
	    @Value("${namePrefix}")
	    private String NAME_PREFIX;
	    @Value("${numTestCases}")
	    private String NUM_DATABASES;
	    @Value("${numThreads}")
	    private String NUM_THREADS;
	    @Value("${forestsPerDatabase}")
	    private String NUM_FORESTS;
	    int INCREMENT = 0;
        
	private void createDatabase(String DB_NAME, 
			 String FOREST1_NAME, 
			 String FOREST2_NAME)
	{
		
			db = api.db(DB_NAME);
			assertFalse(db.exists());
			db.save();
			assertTrue(db.exists());
			
			
			f1 = db.attachNewForest(FOREST1_NAME);
			assertTrue(f1.exists());
			db = api.db(DB_NAME);
			assertTrue(db.getForest().contains(FOREST1_NAME));
			
			if (Integer.parseInt(NUM_FORESTS.trim()) == 2 )
			{
				f2 = db.attachNewForest(FOREST2_NAME);
				assertTrue(f2.exists());
				db = api.db(DB_NAME);
				assertTrue(db.getForest().contains(FOREST2_NAME));
			}
		
			System.out.println(new Date().toString() + " Created Database " +  DB_NAME 
			   + " and forests " );
	}

    @Test
    public void test1() {
    	
    	String methodName = new SetupTestDatabases() {}.getClass().getEnclosingMethod().getName();
    	String className = this.getClass().getName();
    	
    	String prefix = "";
    	String DB_NAME = "";
    	String FOREST1_NAME = "";
    	String FOREST2_NAME = "";
    	
    	INCREMENT = Integer.parseInt(NUM_DATABASES) / Integer.parseInt(NUM_THREADS);
    	    	
    	Date start = new Date();
        System.out.println(start.toString() + " Starting creating databases..." + methodName);
        
        for (int i=0; i< INCREMENT; i++)
        {
        	 //prefix = RandomStringUtils.randomAlphanumeric(8); 
        	
        	 DB_NAME = prefix +  NAME_PREFIX  + i;
        	 FOREST1_NAME = DB_NAME +  "-forest-" + "001";
        	 FOREST2_NAME = DB_NAME + "-forest-"  + "002";
        	 createDatabase (DB_NAME, FOREST1_NAME, FOREST2_NAME);
         }

    	Date end = new Date();
        System.out.println(end.toString() + " Created Databases " + methodName);
        System.out.println("Execution time for " + methodName + " is " + (end.getTime() - start.getTime())/1000 + " seconds.");
    }
    
    @Test
    public void test2()  {
    	
    	String methodName = new SetupTestDatabases() {}.getClass().getEnclosingMethod().getName();
    	String className = this.getClass().getName();
    	
    	String prefix = "";
    	String DB_NAME = "";
    	String FOREST1_NAME = "";
    	String FOREST2_NAME = "";
    	
    	INCREMENT = Integer.parseInt(NUM_DATABASES) / Integer.parseInt(NUM_THREADS);
    	    	
    	Date start = new Date();
        System.out.println(start.toString() + " Starting creating databases..." + methodName);
        
        for (int i=INCREMENT; i<INCREMENT*2; i++)
        {
        	 //prefix = RandomStringUtils.randomAlphanumeric(8); 
        	
        	 DB_NAME = prefix +  NAME_PREFIX  + i;
        	 FOREST1_NAME = DB_NAME +  "-forest-" + "001";
        	 FOREST2_NAME = DB_NAME + "-forest-"  + "002";
        	 createDatabase (DB_NAME, FOREST1_NAME, FOREST2_NAME);
          }

    	Date end = new Date();
        System.out.println(end.toString() + " Created Databases " + methodName);
        System.out.println("Execution time for " + methodName + " is " + (end.getTime() - start.getTime())/1000 + " seconds.");
    }
    
    @Test
    public void test3() {
    	
    	String methodName = new SetupTestDatabases() {}.getClass().getEnclosingMethod().getName();
    	String className = this.getClass().getName();
    	
    	String prefix = "";
    	String DB_NAME = "";
    	String FOREST1_NAME = "";
    	String FOREST2_NAME = "";
    	
    	INCREMENT = Integer.parseInt(NUM_DATABASES) / Integer.parseInt(NUM_THREADS);
    	    	
    	Date start = new Date();
        System.out.println(start.toString() + " Starting creating databases..." + methodName);
        
        for (int i=INCREMENT*2; i<INCREMENT*3; i++)
        {
        	 //prefix = RandomStringUtils.randomAlphanumeric(8); 
        	
        	 DB_NAME = prefix +  NAME_PREFIX  + i;
        	 FOREST1_NAME = DB_NAME +  "-forest-" + "001";
        	 FOREST2_NAME = DB_NAME + "-forest-"  + "002";
        	 createDatabase (DB_NAME, FOREST1_NAME, FOREST2_NAME);
           
        }

    	Date end = new Date();
        System.out.println(end.toString() + " Created Databases " + methodName);
        System.out.println("Execution time for " + methodName + " is " + (end.getTime() - start.getTime())/1000 + " seconds.");
    }

@Test
    public void test4()  {
    	
    	String methodName = new SetupTestDatabases() {}.getClass().getEnclosingMethod().getName();
    	String className = this.getClass().getName();
    	
    	String prefix = "";
    	String DB_NAME = "";
    	String FOREST1_NAME = "";
    	String FOREST2_NAME = "";
    	
    	INCREMENT = Integer.parseInt(NUM_DATABASES) / Integer.parseInt(NUM_THREADS);
    	    	
    	Date start = new Date();
        System.out.println(start.toString() + " Starting creating databases..." + methodName);
        
        for (int i=INCREMENT*3; i<INCREMENT*4; i++)
        {
        	 //prefix = RandomStringUtils.randomAlphanumeric(8); 
        	
        	 DB_NAME = prefix +  NAME_PREFIX  + i;
        	 FOREST1_NAME = DB_NAME +  "-forest-" + "001";
        	 FOREST2_NAME = DB_NAME + "-forest-"  + "002";
        	 createDatabase (DB_NAME, FOREST1_NAME, FOREST2_NAME);
           
        }

    	Date end = new Date();
        System.out.println(end.toString() + " Created Databases " + methodName);
        System.out.println("Execution time for " + methodName + " is " + (end.getTime() - start.getTime())/1000 + " seconds.");
    }
    
@Test
public void test5() {
    	
	String methodName = new SetupTestDatabases() {}.getClass().getEnclosingMethod().getName();
	String className = this.getClass().getName();
	
	String prefix = "";
	String DB_NAME = "";
	String FOREST1_NAME = "";
	String FOREST2_NAME = "";
	
	INCREMENT = Integer.parseInt(NUM_DATABASES) / Integer.parseInt(NUM_THREADS);
	    	
	Date start = new Date();
    System.out.println(start.toString() + " Starting creating databases..." + methodName);
        
        for (int i=INCREMENT*4; i<INCREMENT*5; i++)
        {
        	 //prefix = RandomStringUtils.randomAlphanumeric(8); 
        	
        	 DB_NAME = prefix +  NAME_PREFIX  + i;
        	 FOREST1_NAME = DB_NAME +  "-forest-" + "001";
        	 FOREST2_NAME = DB_NAME + "-forest-"  + "002";
        	 createDatabase (DB_NAME, FOREST1_NAME, FOREST2_NAME);
           
        }

    	Date end = new Date();
        System.out.println(end.toString() + " Created Databases " + methodName);
        System.out.println("Execution time for " + methodName + " is " + (end.getTime() - start.getTime())/1000 + " seconds.");
    }

}
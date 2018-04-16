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
public class DeleteTestDatabases extends AbstractApiTest {

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
	    @Value("${forestsPerDatabase}")
	    private String NUM_FORESTS;
	       
        
       	
    @Test
    public void test() throws Exception {
    	
    	String prefix = "";
    	String DB_NAME = "";
    	String FOREST1_NAME = "";
    	String FOREST2_NAME = "";
    	
    	int NUM_DB = Integer.parseInt(NUM_DATABASES);
    	Date start = new Date();
        System.out.println(start.toString() + " Starting deleting databases..." + this.getClass().getName());
        
        for (int i=0; i<NUM_DB; i++)
        {
        	 DB_NAME = prefix +  NAME_PREFIX  + i;
        	 FOREST1_NAME = DB_NAME +  "-forest-" + "001";
        	 FOREST2_NAME = DB_NAME + "-forest-"  + "002";
        	 
        	 db = api.db(DB_NAME);
             assertTrue(db.exists());
             
             f1 = api.forest(FOREST1_NAME);
             assertTrue(f1.exists());
             db.detach(f1);
             assertTrue(f1.exists());
 			if (Integer.parseInt(NUM_FORESTS.trim()) == 2 )
 			{
	             f2 = api.forest(FOREST2_NAME);
	             assertTrue(f1.exists());
	             db.detach(f2);
	             assertTrue(f2.exists());
 			}
             
             db.delete();
             assertFalse(db.exists());
             
             f1.delete();
             assertFalse(f1.exists());
             
 			if (Integer.parseInt(NUM_FORESTS.trim()) == 2 )
 			{
	             f2.delete();
	             assertFalse(f2.exists());
 			}
             
             System.out.println(new Date().toString() + " Deleted Database " + DB_NAME
            		            + " and forests.");
             
        }

    	Date end = new Date();
        System.out.println(end.toString() + " Deleted Databases " + this.getClass().getName());
        System.out.println("Execution time for " + this.getClass().getName() + " is " + (end.getTime() - start.getTime())/1000 + " seconds.");
    }
    


}
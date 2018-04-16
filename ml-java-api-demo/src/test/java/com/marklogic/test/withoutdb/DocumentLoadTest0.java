package com.marklogic.test.withoutdb;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.DeleteQueryDefinition;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.contentpump.ContentPump;
import com.marklogic.contentpump.utilities.OptionsFileUtil;
import com.marklogic.test.withoutdb.GeneralTestUtils;


@Configuration
@PropertySource(value = { "classpath:contentpump.properties", 
						"classpath:user.properties"}, ignoreResourceNotFound = true)
public class DocumentLoadTest0 extends AbstractApiTest {

    @Value("${mlHost}")
    private String ML_HOST;
    @Value("${mlUser}")
    private String ML_USER;
    @Value("${mlPassword}")
    private String ML_PASSWORD;
    @Value("${mlcpDocPath}")
    private String DOC_PATH;
    @Value("${namePrefix}")
    private String NAME_PREFIX;
        
    private String COLLECTION_NAME = "";
	String DB_NAME = "";
    			
    @After
    public void tearDown() {
    }
    
	public long  countDocuments(DatabaseClient client,String COLLECTION_NAME)
	{
				// create a manager for searching
				QueryManager queryMgr = client.newQueryManager();
				
				// create a search definition
				StringQueryDefinition query = queryMgr.newStringDefinition();
				
				// Restrict the search to the collection
				query.setCollections(COLLECTION_NAME);

				// create a handle for the search results
				SearchHandle resultsHandle = new SearchHandle();

				// run the search
				queryMgr.search(query, resultsHandle);
				return resultsHandle.getTotalResults();
	}
	
	public  long deleteDocuments(DatabaseClient client,String COLLECTION_NAME)
	{
		// create a manager for searching
		QueryManager queryMgr = client.newQueryManager();
		
		DeleteQueryDefinition ddf = queryMgr.newDeleteDefinition();
		StringQueryDefinition query = queryMgr.newStringDefinition();
		
		ddf.setCollections(COLLECTION_NAME);
		queryMgr.delete(ddf);
		
		query.setCollections(COLLECTION_NAME);

		// create a handle for the search results
		SearchHandle resultsHandle = new SearchHandle();

		// run the search
		queryMgr.search(query, resultsHandle);
		
		return resultsHandle.getTotalResults();
		
	}
	
    @Test
    public void testImportDelimitedText() throws Exception {
    	
    	String methodName = new DocumentLoadTest0() {}.getClass().getEnclosingMethod().getName();
    	String className = this.getClass().getName();
    	
    	COLLECTION_NAME = java.util.UUID.randomUUID().toString(); 
    	GeneralTestUtils genTestUtils = new GeneralTestUtils();
	       
        //Find DB Name 
        DB_NAME = genTestUtils.getDBName(className, NAME_PREFIX);
       
        assertNotEquals("ERROR", DB_NAME);
        
        String cmd = "IMPORT -host " + ML_HOST 
        		+ " -username " + ML_USER 
        		+ " -password " +  ML_PASSWORD
        		+ " -input_file_path " + DOC_PATH
				+ " -uri_id id -generate_uri false"
                + " -input_file_type delimited_text"
				+ " -output_collections " + COLLECTION_NAME
                + " -port " + 8000 + " -database " + DB_NAME;
        
        System.out.println("MLCP Command is :: " + cmd);
        
        String[] args = cmd.split(" ");
               
        String[] expandedArgs = null;
        expandedArgs = OptionsFileUtil.expandArguments(args);
        ContentPump.runCommand(expandedArgs);
        
        DatabaseClient client = 
      		  DatabaseClientFactory.newClient(
      		    ML_HOST, 8000, DB_NAME,
      		    new DatabaseClientFactory.DigestAuthContext(ML_USER, ML_PASSWORD));
        
        long docsLoaded = countDocuments(client,COLLECTION_NAME);
        System.out.println("Loaded " + docsLoaded + " documents in collection " + COLLECTION_NAME);
	    long linesFromSourceFile = 0L;
        //Count number of lines in the file 
        File file =new File(DOC_PATH);
        if(file.exists()){
			
		    FileReader fr = new FileReader(file);
		    LineNumberReader lnr = new LineNumberReader(fr);
		    while (lnr.readLine() != null){
            	linesFromSourceFile++;
            }
	            lnr.close();
		     
		}else{
			 System.out.println("File does not exists!");
		        assertEquals(0,docsLoaded);
		}
        
        assertEquals((linesFromSourceFile-1),docsLoaded);
        docsLoaded = deleteDocuments(client,COLLECTION_NAME);

        assertEquals(0,0);
    }   
}
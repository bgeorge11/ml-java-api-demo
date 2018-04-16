package com.marklogic.test.withdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.mgmt.api.database.Database;
import com.marklogic.mgmt.api.forest.Forest;
import com.marklogic.mgmt.api.database.ElementIndex;


@Configuration
@PropertySource(value = { "classpath:DocumentLoadWithDatabase.properties", "classpath:user.properties"}, ignoreResourceNotFound = true)
public class WithDatabaseLoadXMLTest extends com.marklogic.test.withdb.AbstractApiTest {

    private Database db;
    private Forest f1, f2;

    @Value("${mlHost}")
    private String ML_HOST;
    @Value("${mlUser}")
    private String ML_USER;
    @Value("${mlPassword}")
    private String ML_PASSWORD;
    @Value("${mlDbNameSuffix}")
    private String DB_NAME_SUFFIX;
    @Value("${forest1Suffix}")
    private String FOREST1_NAME_SUFFIX;
    @Value("${forest2Suffix}")
    private  String FOREST2_NAME_SUFFIX;
    @Value("${mlRangeElementIndexLocalName}")
    private String RANGE_ELEMENT_INDEX_LOCAL_NAME;
    @Value("${mlRangeElementIndexScalarType}")
    private String RANGE_ELEMENT_INDEX_SCALAR_TYPE;
    @Value("${mlRangeElementIndexNS}")
    private String RANGE_ELEMENT_INDEX_NS;
    @Value("${XMLDocPath}")
    private String XML_DOC_PATH;
    @Value("${XMLDocCollectionSuffix}")
    private String XML_DOC_COLLECTION_SUFFIX;
    private Long TOTAL_XML_DOCS_ADDED=0L;
    
			
	@After
    public void teardown() {
        deleteIfExists(db, f1, f2);
    }
    
    
    public static ArrayList<File> listFilesForFolder(final File folder,
            final boolean recursivity,
            final String patternFileFilter) {

        // Inputs
        boolean filteredFile = false;

        // Ouput
        final ArrayList<File> output = new ArrayList<File> ();

        // Foreach elements
        for (final File fileEntry : folder.listFiles()) {

            // If this element is a directory, do it recursivly
            if (fileEntry.isDirectory()) {
                if (recursivity) {
                    output.addAll(listFilesForFolder(fileEntry, recursivity, patternFileFilter));
                }
            }
            else {
                // If there is no pattern, the file is correct
                if (patternFileFilter.length() == 0) {
                    filteredFile = true;
                }
                // Otherwise we need to filter by pattern
                else {
                    filteredFile = Pattern.matches(patternFileFilter, fileEntry.getName());
                }

                // If the file has a name which match with the pattern, then add it to the list
                if (filteredFile) {
                    output.add(fileEntry);
                }
            }
        }

        return output;
    }
    
    public void loadXMLDocuments(DatabaseClient client, String COLLECTION_NAME) throws Exception  {
    	
    	File fl = new File(XML_DOC_PATH);
    	
    	ArrayList<File> lstFiles = listFilesForFolder(fl,false,".*\\.xml");
    	String fileName = "";
    	
    	for (int i=0; i<lstFiles.size(); i++)
    	{
    		InputStream docStream = new FileInputStream(lstFiles.get(i));
    		fileName = lstFiles.get(i).getName();
    	   	
    		// create a manager for XML documents
    		XMLDocumentManager docMgr = client.newXMLDocumentManager();

    		// create a handle on the content
    		InputStreamHandle handle = new InputStreamHandle(docStream);
    		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
    		// add a collection tag
    		metadata.getCollections().addAll(COLLECTION_NAME);
    		// write the document content
    		docMgr.write("/" + fileName , metadata,handle);
    		TOTAL_XML_DOCS_ADDED++;
    		
    	}
   }
    
	public long  countXMLDocuments(DatabaseClient client,String COLLECTION_NAME)
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
    	
    @Test
    public void test() throws Exception {
    	
    	String prefix = java.util.UUID.randomUUID().toString();
    	
    	String DB_NAME = prefix +  DB_NAME_SUFFIX;
    	String FOREST1_NAME = prefix +  FOREST1_NAME_SUFFIX;
    	String FOREST2_NAME = prefix + FOREST2_NAME_SUFFIX;
    	String COLLECTION_NAME = prefix + XML_DOC_COLLECTION_SUFFIX;
 
    	Date start = new Date();
        System.out.println(start.toString() + " Started Test Case: " + this.getClass().getName());

//Create a database and Range Element Indexes
        db = api.db(DB_NAME );
        assertFalse(db.exists());
    	ElementIndex elementIndex = new ElementIndex();
    	elementIndex.setLocalname(RANGE_ELEMENT_INDEX_LOCAL_NAME);
    	elementIndex.setScalarType(RANGE_ELEMENT_INDEX_SCALAR_TYPE);
    	elementIndex.setInvalidValues("reject");
    	elementIndex.setRangeValuePositions(false);
    	elementIndex.setNamespaceUri(RANGE_ELEMENT_INDEX_NS);
    	elementIndex.setCollation("en");
    	java.util.List<ElementIndex> elementIndexes = new ArrayList<ElementIndex>();
    	elementIndexes.add(elementIndex);
    	db.setRangeElementIndex(elementIndexes);
   	    db.save();
        assertTrue(db.exists());
                
//Attach two forests, names from the properties
        f1 = db.attachNewForest(FOREST1_NAME);
        assertTrue(f1.exists());
        db = api.db(DB_NAME);
        assertTrue(db.getForest().contains(FOREST1_NAME));
        
        f2 = db.attachNewForest(FOREST2_NAME);
        assertTrue(f2.exists());
        db = api.db(DB_NAME);
        assertTrue(db.getForest().contains(FOREST2_NAME));
        
        System.out.println ("Created database " + DB_NAME);

//Load some XML documents
        

      DatabaseClient client = 
        		  DatabaseClientFactory.newClient(
        		    ML_HOST, 8000, DB_NAME,
        		    new DatabaseClientFactory.DigestAuthContext(ML_USER, ML_PASSWORD));
      loadXMLDocuments(client, COLLECTION_NAME);
	  long countOfXMLDocs = countXMLDocuments(client,COLLECTION_NAME);
	  assertEquals(TOTAL_XML_DOCS_ADDED.longValue(), countOfXMLDocs);
            
//Detach forest
    	db.detach(f1);
    	assertTrue(f1.exists());
      db.detach(f2);
      assertTrue(f2.exists());
      db = api.db(DB_NAME);
      
//Delete database
      db.delete();
      assertFalse(db.exists());
      

//Delete detached forests
      f1.delete();
      f2.delete();
      assertFalse(f1.exists());
      assertFalse(f2.exists());
      
  	Date end = new Date();
    System.out.println(end.toString() + " Ended Test Case: " + this.getClass().getName());
    System.out.println("Execution time for " + this.getClass().getName() + " is " + (end.getTime() - start.getTime())/1000 + " seconds.");

    }
    

}
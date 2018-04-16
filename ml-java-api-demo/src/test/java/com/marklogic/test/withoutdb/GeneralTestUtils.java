package com.marklogic.test.withoutdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralTestUtils {
	
	/*This method matches the number associated with the test class name and returns the DB name associated with it
	 * Ex. DocumentLoadTest0 and unit-test- returns unit-test-0
	 *     DocumentLoadTest133 and unit-test- returns unit-test-133
	 *     DocumentLoad3Test200 and unit-test- returns unit-test-200
	 */
	
	public String getDBName(String className, String db_prefix)
	
	{
		Pattern p = Pattern.compile("(\\d+)\\D*$");
        Matcher m = p.matcher(className);
        if (m.find()) {
        	return db_prefix+m.group();
        }
        else
        	return "ERROR";
		
	}

}

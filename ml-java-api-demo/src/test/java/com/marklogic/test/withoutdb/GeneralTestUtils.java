package com.marklogic.test.withoutdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralTestUtils {
	
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

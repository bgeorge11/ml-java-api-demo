package com.marklogic.test.withdb;


import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestAllParallelWithDatabase {

	@Test
	public void test() {
		Class[] cls = { WithDatabaseLoadJSONTest.class, WithDatabaseLoadXMLTest.class};

		// Parallel among classes
        Result results = JUnitCore.runClasses(ParallelComputer.classes(), cls);
        
        System.out.println("-------------TEST RESULTS---------------");
		System.out.println("Total RunCount = " + results.getRunCount());
		System.out.println("Failure Count = " + results.getFailureCount());
        System.out.println("Run Time = " + results.getRunTime() /1000 + " seconds");
		List <Failure> failure = results.getFailures();
		
		for (int i = 0; i < failure.size(); i++) {
			System.out.println("Failure details " + failure.get(i).getTestHeader() + " " + failure.get(i).getException());
		}
		
		assertEquals(0,results.getFailureCount());
	}
}

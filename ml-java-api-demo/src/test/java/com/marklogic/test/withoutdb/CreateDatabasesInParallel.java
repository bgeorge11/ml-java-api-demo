package com.marklogic.test.withoutdb;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class CreateDatabasesInParallel {

	@Test
	public void test() {
		Class[] cls = { SetupTestDatabases.class};
		
		// Parallel among methods
		Result results = JUnitCore.runClasses(ParallelComputer.methods(), cls);
		
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

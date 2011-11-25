package org.tiling.facebender.test;

import junit.framework.*;

public class AllTests extends TestCase {

	public AllTests(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(FaceTest.suite());

		return suite;
	}

}
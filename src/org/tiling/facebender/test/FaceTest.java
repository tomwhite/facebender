package org.tiling.facebender.test;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import junit.framework.*;

import org.tiling.facebender.*;

public class FaceTest extends TestCase {

	public FaceTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(FaceTest.class);
	}

	public void testTransform() {
		Point2D[] points = new Point2D[] {new Point2D.Double(0, 0), new Point2D.Double(1, 0)};
		Face face = new Face();
		AffineTransform trans = face.getFaceTransform(points);
		Point2D[] destPoints = new Point2D[2];
		trans.transform(points, 0, destPoints, 0, points.length);
		assertEquals(Face.ANDROGENOUS_NORM.getPoint(0), destPoints[0]);
		assertEquals(Face.ANDROGENOUS_NORM.getPoint(1), destPoints[1]);
	}

	public void testTransform2() {
		Point2D[] points = new Point2D[] {new Point2D.Double(-3, 1), new Point2D.Double(4, -9)};
		Face face = new Face();
		AffineTransform trans = face.getFaceTransform(points);
		Point2D[] destPoints = new Point2D[2];
		trans.transform(points, 0, destPoints, 0, points.length);
		assertEquals(Face.ANDROGENOUS_NORM.getPoint(0), destPoints[0]); 
		assertEquals(Face.ANDROGENOUS_NORM.getPoint(1), destPoints[1]); 
	}
}
package org.tiling.facebender;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import java.io.*;

import java.util.Iterator;

public class Face implements Serializable, Iterator {

	static final long serialVersionUID = 6521620900506491339L;

	private Point2D[] points = new Point2D[NUMBER_OF_POINTS];
	private Point2D[] bentPoints = new Point2D[NUMBER_OF_POINTS];

	public Face() {
		this(NORM);
	}

	Face(int[][] face) {
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			points[i] = new Point2D.Double(face[i][0], face[i][1]);
			bentPoints[i] = new Point2D.Double(face[i][0], face[i][1]);
		}
		reset();
	}

	public Face(Point2D[] points) {
		getFaceTransform(points).transform(points, 0, this.points, 0, NUMBER_OF_POINTS);
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			bentPoints[i] = (Point2D) this.points[i].clone();
		}
		reset();
	}

	public AffineTransform getFaceTransform(Point2D[] points) {
		// Translate left eye to (0, 0)
		AffineTransform transform = AffineTransform.getTranslateInstance(-points[0].getX(), -points[0].getY());
		// Rotate so right eye lies on y = 0
		transform.preConcatenate(AffineTransform.getRotateInstance(- Math.atan2(points[1].getY() - points[0].getY(), points[1].getX() - points[0].getX())));
		// Scale so eyes are the same distance apart as ANDROGENOUS_NORM
		double distanceBetweenEyes = points[0].distance(points[1]);
		double normDistanceBetweenEyes = ANDROGENOUS_NORM.points[0].distance(ANDROGENOUS_NORM.points[1]);
		transform.preConcatenate(AffineTransform.getScaleInstance(normDistanceBetweenEyes / distanceBetweenEyes, normDistanceBetweenEyes / distanceBetweenEyes));
		// Translate so eyes coincide with ANDROGENOUS_NORM
		transform.preConcatenate(AffineTransform.getTranslateInstance(ANDROGENOUS_NORM.points[0].getX(), ANDROGENOUS_NORM.points[0].getY()));
		return transform;
	}

	public int getNumberOfFeatures() {
		return NUMBER_OF_FEATURES;
	}

	public int getFeatureLength(int feature) {
		return FEATURE_LENGTHS[feature];
	}

	public Point2D getPoint(int point) {
		return bentPoints[point];
	}

	public synchronized void caricature(double factor) {
		// perform exaggeration algorithm on face wrt ANDROGENOUS_NORM
		// factor = 0 gives face, -1 gives ANDROGENOUS_NORM, >0 gives caricature
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			double x = points[i].getX() + factor * (points[i].getX() - ANDROGENOUS_NORM.points[i].getX());
			double y = points[i].getY() + factor * (points[i].getY() - ANDROGENOUS_NORM.points[i].getY());
			bentPoints[i] = new Point2D.Double(x, y);
		}
	}

	protected int tracePoint, traceFeature, traceFeaturePos;

	public void reset() {
		tracePoint = 0;
		traceFeature = 0;
		traceFeaturePos = 0;
	}

	public boolean hasNext() {
		return true;
	}

	public Object next() {
		tracePoint++;
		if (tracePoint >= NUMBER_OF_POINTS) {
			reset();
		} else {
			if (traceFeaturePos < FEATURE_LENGTHS[traceFeature] - 1) {
				traceFeaturePos++;
			} else {
				traceFeaturePos = 0;
				traceFeature++;
			}
		}
		return getPoint();
	}

	public Object previous() {
		tracePoint--;
		if (tracePoint < 0) {
			tracePoint = NUMBER_OF_POINTS;
			traceFeature = NUMBER_OF_FEATURES;
			traceFeaturePos = FEATURE_LENGTHS[traceFeature] - 1;
		} else {
			if (traceFeaturePos == 0) {
				traceFeature--;
				traceFeaturePos = FEATURE_LENGTHS[traceFeature] - 1;
			} else {
				traceFeaturePos--;
			}
		}
		return getPoint();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Point2D getPoint() {
		return bentPoints[tracePoint]; 
	}

	public String getDescription() {
		return (FEATURE_NAMES[traceFeature] + " [" + (traceFeaturePos + 1) + "/" + FEATURE_LENGTHS[traceFeature] + "]");
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < points.length; i++) {
			sb.append(points[i]);
			sb.append('\n');
		}
		return sb.toString();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		for (int i = 0; i < points.length; i++) {
			out.writeDouble(points[i].getX());
			out.writeDouble(points[i].getY());
		}
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		points = new Point2D[NUMBER_OF_POINTS];
		bentPoints = new Point2D[NUMBER_OF_POINTS];
		for (int i = 0; i < points.length; i++) {
			double x = in.readDouble();
			double y = in.readDouble();
			points[i] = new Point2D.Double(x, y);
		}
		for (int i = 0; i < NUMBER_OF_POINTS; i++) {
			bentPoints[i] = (Point2D) this.points[i].clone();
		}
		reset();
	}


	/////////////////////////////////////////////
	// Face data stored in static final arrays //
	/////////////////////////////////////////////

	protected static final int NUMBER_OF_POINTS = 186;
	protected static final int NUMBER_OF_FEATURES = 39;

	protected static final String[] FEATURE_NAMES =	{
		"Left Pupil",
		"Right Pupil",
		"Left Iris",
		"Right Iris",
		"Bottom of Left Eyelid",
		"Bottom of Right Eyelid",
		"Bottom of Left Eye",
		"Bottom of Right Eye",
		"Top of Left Eye",
		"Top of Right Eye",
		"Left Eye Line",
		"Right Eye Line",
		"Left Side of Nose",
		"Right Side of Nose",
		"Left Nostril",
		"Right Nostril",
		"Top of Left Eyebrow",
		"Top of Right Eyebrow",
		"Bottom of Left Eyebrow",
		"Bottom of Right Eyebrow",
		"Top of Upper Lip",
		"Bottom of Upper Lip",
		"Top of Lower Lip",
		"Bottom of Lower Lip",
		"Left Side of Face",
		"Right Side of Face",
		"Left Ear",
		"Right Ear",
		"Jaw",
		"Hairline",
		"Top of Head",
		"Left Cheek Line",
		"Right Cheek Line",
		"Left Cheekbone",
		"Right Cheekbone",
		"Left Upper Lip Line",
		"Right Upper Lip Line",
		"Chin Cleft",
		"Chin Line",
	};

	protected static final int[] FEATURE_LENGTHS = {
		1, 1, 5, 5, 3, 3, 3, 3, 3, 3, 3, 3, 6, 6, 6, 6, 6, 6, 4, 4, 7, 7, 7, 7, 3, 3, 7, 7, 11, 13, 13, 3, 3, 3, 3, 2, 2, 2, 3,
	};


	protected static final int[][] NORM = {
		{135,145},
		{190,145},
		{128,144}, {133,149}, {140,144}, {135,141}, {128,144}, 
		{184,144}, {189,149}, {196,144}, {190,141}, {184,144}, 
		{119,147}, {133,140}, {147,146},
		{177,147}, {190,141}, {203,147},
		{121,147}, {133,150}, {147,146},
		{177,147}, {191,150}, {201,148},
		{118,143}, {132,137}, {148,142},
		{176,143}, {191,137}, {204,143},
		{127,154}, {135,153}, {144,150},
		{178,151}, {187,154}, {196,154},
		{156,140}, {156,153}, {156,165}, {154,172}, {156,179}, {161,182},
		{166,140}, {166,153}, {166,166}, {168,172}, {167,179}, {161,182},
		{150,169}, {147,173}, {146,178}, {148,182}, {153,179}, {161,182},
		{173,169}, {176,172}, {177,178}, {174,182}, {170,179}, {163,182},
		{112,137}, {113,132}, {125,127}, {139,128}, {150,131}, {152,136},
		{171,136}, {173,132}, {186,129}, {199,128}, {208,132}, {211,137},
		{112,138}, {124,132}, {138,134}, {152,136},
		{171,136}, {187,134}, {200,132}, {210,137},
		{137,203}, {149,199}, {156,196}, {162,199}, {168,197}, {177,199}, {187,202},
		{138,203}, {148,203}, {156,202}, {163,203}, {170,202}, {178,202}, {186,202},
		{138,203}, {149,203}, {156,202}, {163,203}, {170,202}, {177,202}, {186,203},
		{141,204}, {148,207}, {155,210}, {163,211}, {171,210}, {179,207}, {185,203},
		{103,141}, {101,160}, {104,181},
		{219,140}, {222,159}, {218,179},
		{99,150}, {92,144}, {88,149}, {90,160}, {94,174}, {99,187}, {104,184},
		{224,149}, {231,144}, {234,151}, {232,160}, {230,173}, {224,185}, {219,184},
		{104,181}, {108,199}, {115,214}, {129,228}, {147,240}, {162,243}, {180,239}, {196,228}, {207,215}, {215,199}, {219,178},
		{101,144}, {107,129}, {114,114}, {120,104}, {131,95}, {146,92}, {160,93}, {174,95}, {188,96}, {201,103}, {210,114}, {217,126}, {222,143},
		{93,204}, {78,173}, {76,142}, {82,101}, {99,70}, {129,46}, {158,44}, {188,45}, {217,64}, {236,94}, {245,134}, {250,168}, {233,200},
		{145,175}, {139,182}, {135,190},
		{178,176}, {185,183}, {190,191},
		{105,178}, {109,184}, {112,190},
		{218,178}, {214,183}, {211,189},
		{159,186}, {159,193},
		{165,186}, {165,193},
		{162,232}, {162,238},
		{153,218}, {162,216}, {173,219},
	};

/*
	protected static final int NUMBER_OF_POINTS = 8;
	protected static final int NUMBER_OF_FEATURES = 4;

	protected static final String[] FEATURE_NAMES =
	{ "Left Pupil",
		"Right Pupil",
		"Nose",
		"Mouth",
	};

	protected static final int[] FEATURE_LENGTHS =
	{1, 1, 3, 3,};


	protected static final int[][] NORM =
	{ {135,145},
		{190,145},
		{156,165}, {154,172}, {161,182},
		{141,204}, {163,211}, {185,203},
	};
*/

	public static final Face ANDROGENOUS_NORM = new Face();

}
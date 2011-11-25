package org.tiling.facebender;

import java.awt.geom.Point2D;

import java.io.Serializable;

public class DynamicFace implements Serializable {

	Point2D[] points;
	int position;
	boolean finished;

	public DynamicFace() {
		points = new Point2D[Face.NUMBER_OF_POINTS];
		reset();
	}

	public void addPoint(Point2D point) throws IllegalStateException {
		if (finished) {
			throw new IllegalStateException();
		}
		points[position++] = point;
		if (position >= points.length) {
			finished = true;
		}
	}

	public void reset() {
		position = 0;
		finished = false;
	}

	public void undo() {
		if (position > 0) {
			position--;
			finished = false;
		}
	}

	public boolean isFinished() {
		return finished;
	}

	public Face generateFace() throws IllegalStateException {
		if (!finished) {
			throw new IllegalStateException();
		}
		return new Face(points);
	}

}
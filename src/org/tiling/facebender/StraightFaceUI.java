package org.tiling.facebender;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.*;

import java.io.Serializable;

import org.tiling.UI;

/**
 * A graphical representation of Face with straight lines.
 */
public class StraightFaceUI extends FaceUI {

	public StraightFaceUI(Face face) {
		super(face);
	}

	public StraightFaceUI(Face face, boolean tracePointOn) {
		super(face, tracePointOn);
	}

	protected void scanFace() {
		int count = 0;
		for (int i = 0; i < paths.length; i++) {
			int featureLength = face.getFeatureLength(i);
			if (featureLength == 1) {
				paths[i] = new Ellipse2D.Double(face.getPoint(count).getX() - 1, face.getPoint(count).getY() - 1, 2, 2);
				count++;
			} else {
				GeneralPath path = new GeneralPath();
				path.moveTo((float) face.getPoint(count).getX(), (float) face.getPoint(count).getY());
				count++;
				for (int j = 1; j < featureLength; j++) {
					Point2D next = face.getPoint(count++);
					path.lineTo((float) next.getX(), (float) next.getY());
				}
				paths[i] = path;
			}
		}
	}
}
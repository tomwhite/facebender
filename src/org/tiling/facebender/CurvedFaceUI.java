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
 * A graphical representation of Face with curved lines.
 */
public class CurvedFaceUI extends FaceUI {

	public CurvedFaceUI(Face face) {
		super(face);
	}

	public CurvedFaceUI(Face face, boolean tracePointOn) {
		super(face, tracePointOn);
	}

	protected void scanFace() {
		int count = 0;
		for (int i = 0; i < paths.length; i++) {
			int featureLength = face.getFeatureLength(i);
			if (featureLength == 1) {
				paths[i] = new Ellipse2D.Double(face.getPoint(count).getX() - 1, face.getPoint(count).getY() - 1, 2, 2);
				count++;
			} else if (featureLength == 2) {
				GeneralPath path = new GeneralPath();
				path.moveTo((float) face.getPoint(count).getX(), (float) face.getPoint(count).getY());
				count++;
				path.lineTo((float) face.getPoint(count).getX(), (float) face.getPoint(count).getY());
				count++;
				paths[i] = path;
			} else if (featureLength % 3 == 1) {
				GeneralPath path = new GeneralPath();
				path.moveTo((float) face.getPoint(count).getX(), (float) face.getPoint(count).getY());
				count++;
				for (int j = 0; j < (featureLength - 1) / 3; j++) {
					Point2D control1 = face.getPoint(count++);
					Point2D control2 = face.getPoint(count++);
					Point2D next = face.getPoint(count++);
					path.curveTo((float) control1.getX(), (float) control1.getY(), (float) control2.getX(), (float) control2.getY(), (float) next.getX(), (float) next.getY());
				}
				paths[i] = path;
			} else if (featureLength % 2 == 1) {
				GeneralPath path = new GeneralPath();
				path.moveTo((float) face.getPoint(count).getX(), (float) face.getPoint(count).getY());
				count++;
				for (int j = 0; j < (featureLength - 1) / 2; j++) {
					Point2D control = face.getPoint(count++);
					Point2D next = face.getPoint(count++);
					path.quadTo((float) control.getX(), (float) control.getY(), (float) next.getX(), (float) next.getY());
				}
				paths[i] = path;
			} else if (featureLength % 6 == 0) {
				GeneralPath path = new GeneralPath();
				path.moveTo((float) face.getPoint(count).getX(), (float) face.getPoint(count).getY());
				count++;
				for (int j = 0; j < (featureLength - 3) / 3; j++) {
					Point2D control1 = face.getPoint(count++);
					Point2D control2 = face.getPoint(count++);
					Point2D next = face.getPoint(count++);
					path.curveTo((float) control1.getX(), (float) control1.getY(), (float) control2.getX(), (float) control2.getY(), (float) next.getX(), (float) next.getY());
				}
				Point2D control = face.getPoint(count++);
				Point2D next = face.getPoint(count++);
				path.quadTo((float) control.getX(), (float) control.getY(), (float) next.getX(), (float) next.getY());
				paths[i] = path;
			} else { // featureLength % 6 == 1
				GeneralPath path = new GeneralPath();
				path.moveTo((float) face.getPoint(count).getX(), (float) face.getPoint(count).getY());
				count++;
				Point2D control = face.getPoint(count++);
				Point2D next = face.getPoint(count++);
				path.quadTo((float) control.getX(), (float) control.getY(), (float) next.getX(), (float) next.getY());
				for (int j = 0; j < (featureLength - 5) / 3; j++) {
					Point2D control1 = face.getPoint(count++);
					Point2D control2 = face.getPoint(count++);
					next = face.getPoint(count++);
					path.curveTo((float) control1.getX(), (float) control1.getY(), (float) control2.getX(), (float) control2.getY(), (float) next.getX(), (float) next.getY());
				}
				control = face.getPoint(count++);
				next = face.getPoint(count++);
				path.quadTo((float) control.getX(), (float) control.getY(), (float) next.getX(), (float) next.getY());
				paths[i] = path;
			}
		}
	}
}
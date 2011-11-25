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
 * A graphical representation of Face.
 */
public abstract class FaceUI implements UI, Serializable {

	private Color backgroundColor = Color.white;

	protected Face face;
	private boolean tracePointOn = false;
	protected transient Shape[] paths;
	private transient Ellipse2D current;
	private float lineWidth = 1.5f;

	private RenderingHints qualityHints;
	private Stroke stroke;

	public FaceUI(Face face) {
		this(face, false);
	}

	public FaceUI(Face face, boolean tracePointOn) {
		this.face = face;
		this.tracePointOn = tracePointOn;
		paths = new Shape[face.getNumberOfFeatures()];
		initialiseGraphics();
		scanFace();
	}

	protected abstract void scanFace();

	protected void initialiseGraphics() {
		qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
											RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}


	public void paint(Graphics2D g2) {
		g2.setRenderingHints(qualityHints);
		g2.setStroke(stroke);

		g2.setColor(Color.black);
		g2.fill(paths[0]);
		g2.fill(paths[1]);
		for (int i = 2; i < paths.length; i++) {
			g2.draw(paths[i]);
		}
		if (tracePointOn) {
			g2.setColor(Color.red);
			double x = face.getPoint().getX();
			double y = face.getPoint().getY();
			current = new Ellipse2D.Double(x - 1, y - 1, 2, 2);
			g2.fill(current);
		}
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}

	public void setBackground(Color c) {
		backgroundColor = c;
	}

	public Color getBackground() {
		return backgroundColor;
	}

	public Rectangle2D getBounds2D() {
		Rectangle2D rect = paths[0].getBounds2D();
		for (int i = 1; i < paths.length; i++) {
			rect.add(paths[i].getBounds2D());
		}
		return rect;
	}
	public Object clone() {
		try {
			FaceUI ui = (FaceUI) super.clone();
			return ui;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}		
	}}
package org.tiling.facebender;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;

import java.io.Serializable;

import org.tiling.UI;

/**
 * A graphical representation of an image.
 */
public class ImageUI implements UI, Serializable {

	public static final float LINE_WIDTH = 0.05f;

	private Color backgroundColor = Color.white;

	private BufferedImage image;
	private List points;

	public ImageUI(BufferedImage image) {
		this.image = image;
		points = new ArrayList();
	}

	public void paint(Graphics2D g2) {
		g2.drawImage(image, null, 0, 0);
		g2.setColor(Color.black);
		for (Iterator i = points.iterator(); i.hasNext(); ) {
			g2.fill((Shape) i.next());
		}
	}

	public void addPoint(Point2D p) {
		points.add(new Ellipse2D.Double(p.getX() - 1, p.getY() - 1, 2, 2));
	}

	public void reset() {
		points.clear();
	}

	public void undo() {
		if (!points.isEmpty()) {
			points.remove(points.size() - 1);
		}
	}

	public void setBackground(Color c) {
		backgroundColor = c;
	}

	public Color getBackground() {
		return backgroundColor;
	}

	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight());
	}
	public Object clone() {
		try {
			ImageUI ui = (ImageUI) super.clone();
			return ui;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}		
	}}
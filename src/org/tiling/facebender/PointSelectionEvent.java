package org.tiling.facebender;

import java.awt.geom.Point2D;
import java.util.EventObject;

public class PointSelectionEvent extends EventObject {
	
	private Point2D selectedPoint;
	
	public PointSelectionEvent(Object source, Point2D selectedPoint) {
		super(source);
		this.selectedPoint = selectedPoint;
	}
	
	public Point2D getSelectedPoint() {
		return selectedPoint;
	}
}
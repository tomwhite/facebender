package org.tiling.facebender;

import java.util.EventListener;

public interface PointSelectionListener extends EventListener {
	
	public void pointSelected(PointSelectionEvent event);

}
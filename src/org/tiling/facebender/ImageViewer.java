package org.tiling.facebender;

import org.tiling.gui.Canvas2D;
import org.tiling.gui.FileManager;
import org.tiling.gui.Viewer2D;
import org.tiling.util.ImageFactory;

import java.awt.geom.AffineTransform;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.HashSet;

import javax.swing.*;

public class ImageViewer extends Viewer2D {

	private ImageFactory factory;
	private BufferedImage image;
	private ImageUI imageUI;
	private Canvas2D canvas;

	public ImageViewer(BufferedImage image) {
		this(image, "Image");
	}

	public ImageViewer(BufferedImage image, String title) {

		super(title);

		setImage(image);

		factory = new ImageFactory(this);

		setVisible(true);

		setUpMenus();

	}

	void setImage(BufferedImage image) {
		this.image = image;
		this.imageUI = new ImageUI(image);
		this.canvas = new Canvas2D(imageUI);
		setCanvas2D(canvas);
		addMouseListener(new MouseClickListener());
	}

	public void addNotify() {
		super.addNotify();
		pack();
		fitToCanvas();
	}


	protected void setUpMenus() {

		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new Loader());
		
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

	}

	public void reset() {
		imageUI.reset();
	}

	public void undo() {
		imageUI.undo();
	}

	protected class Loader extends AbstractAction {
		public Loader() {
			super("Open...");
		}
		public void actionPerformed(ActionEvent e) {
			File file = FileManager.getInstance().chooseFileToOpen(ImageViewer.this);
			if (file != null) {
				setImage(factory.loadBufferedImage(file.getPath()));
			}
		}
	}

	HashSet listeners = new HashSet();

	public void addPointSelectionListener(PointSelectionListener listener) {
		listeners.add(listener);
	}

	public void removePointSelectionListener(PointSelectionListener listener) {
		listeners.remove(listener);
	}

	protected class MouseClickListener extends MouseAdapter implements Serializable {
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() != canvas) {
				return;
			}
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // Left button
				Point2D clickPoint = transformToFaceSpace(e.getX(), e.getY());
				imageUI.addPoint(clickPoint);
				canvas.repaint();
				fireEvent(new PointSelectionEvent(ImageViewer.this, clickPoint));
			}
		}
	}

	private void fireEvent(PointSelectionEvent e) {
		HashSet s;
		synchronized(this) {
			s = (HashSet) listeners.clone();
		}
		for (Iterator i = s.iterator(); i.hasNext(); ) {
			((PointSelectionListener) i.next()).pointSelected(e);
		}
	}

	private Point2D transformToFaceSpace(int x, int y) {
		try {
			Point2D p1 = new Point2D.Float(x, y);
			Point2D p2 = new Point2D.Float();
			getCanvas2D().getFlippedAffineTransform().createInverse().transform(p1, p2);
			return p2;
		} catch (Exception ex) {}
		return null;
	}
}
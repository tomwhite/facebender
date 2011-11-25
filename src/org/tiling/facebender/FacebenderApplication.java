package org.tiling.facebender;

import org.tiling.gui.FileManager;
import org.tiling.util.ImageFactory;
import org.tiling.util.Serializer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class FacebenderApplication extends JFrame {

	private JDesktopPane desktop;

	private FacebenderViewerGenerator referenceFaceViewer;
	private Face referenceFace;
	private DynamicFace dynamicFace;
	private ImageViewer imageViewer;

	public FacebenderApplication() {
		super("Facebender");

		desktop = new JDesktopPane();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(desktop, BorderLayout.CENTER);

		setUpMenus();

		setSize(600, 600);
		setDefaultCloseOperation(3); // JFrame.EXIT_ON_CLOSE in JDK 1.3 onwards
		setVisible(true);
		
		referenceFace = new Face();
		referenceFaceViewer = new FacebenderViewerGenerator(referenceFace);
		referenceFaceViewer.addResetListener(new ResetListener());
		referenceFaceViewer.addBackListener(new UndoListener());
		addJInternalFrame(referenceFaceViewer);

		ImageFactory factory = new ImageFactory(this);
		imageViewer = new ImageViewer(factory.loadBufferedImage(FacebenderApplication.class.getResource("/audreyhepburn.jpg")));
		addJInternalFrame(imageViewer);

		dynamicFace = new DynamicFace();
		imageViewer.addPointSelectionListener(new FaceBuilder());

	}

	private void setUpMenus() {

		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new Loader());
		
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

	}

	private void addJInternalFrame(JInternalFrame internalFrame) {
		internalFrame.pack();
		desktop.add(internalFrame, JLayeredPane.DEFAULT_LAYER);
		desktop.moveToFront(internalFrame);
		try {
			internalFrame.setSelected(true);
		} catch (Exception ex) {
		}
	}

	protected class Loader extends AbstractAction {
		public Loader() {
			super("Open Face...");
		}
		public void actionPerformed(ActionEvent e) {
			File file = FileManager.getInstance().chooseFileToOpen(FacebenderApplication.this);
			if (file != null) {
				Object obj = Serializer.deserialize(file);
				if (obj instanceof Face) {
					addJInternalFrame(new FacebenderViewer((Face) obj));
				}
			}
		}
	}

	protected class FaceBuilder implements PointSelectionListener {
		public void pointSelected(PointSelectionEvent event) {
			dynamicFace.addPoint(event.getSelectedPoint());
			referenceFace.next();
			referenceFaceViewer.repaint();
			if (dynamicFace.isFinished()) {
				Face face = dynamicFace.generateFace();
				FacebenderViewer viewer = new FacebenderViewer(face);
				addJInternalFrame(viewer);
				dynamicFace.reset();
				imageViewer.reset();
			}
		}
	}

	protected class ResetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dynamicFace.reset();
			imageViewer.reset();
			imageViewer.repaint();
			referenceFace.reset();
			referenceFaceViewer.repaint();
		}
	}

	protected class UndoListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dynamicFace.undo();
			imageViewer.undo();
			imageViewer.repaint();
			referenceFace.previous();
			referenceFaceViewer.repaint();
		}
	}

	public static void main(String[] args) {
		FacebenderApplication app = new FacebenderApplication();
	}

}
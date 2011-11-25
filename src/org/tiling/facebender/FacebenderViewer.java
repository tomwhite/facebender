package org.tiling.facebender;

import org.tiling.gui.Canvas2D;
import org.tiling.gui.FileManager;
import org.tiling.gui.Viewer2D;
import org.tiling.util.Serializer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class FacebenderViewer extends Viewer2D {

	private Face face;
	private FaceUI faceUI;
	private Canvas2D canvas;

	JTextField textField;

	public FacebenderViewer(Face face) {
		this(face, "Face");
	}

	public FacebenderViewer(Face face, String title) {

		super(title);

		setFace(face);

		setVisible(true);

		setUpMenus();
		setUpControls();
	}

	public void addNotify() {
		super.addNotify();
		pack();
		fitToCanvas();
	}

	private void setFace(Face face) {
		this.face = face;
//		this.faceUI = new CurvedFaceUI(face);
		this.faceUI = new StraightFaceUI(face);
		this.canvas = new Canvas2D(faceUI);
		setCanvas2D(canvas);
	}

	protected void setUpMenus() {

		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new Loader());
		fileMenu.add(new Saver());
		fileMenu.add(new Printer());
		fileMenu.add(new PostscriptPrinter());
		fileMenu.add(new PrintPreviewerA3());
		fileMenu.add(new PrintPreviewerA4());
		
		JMenu viewMenu = new JMenu("View");
		viewMenu.add(new Centrer());
		viewMenu.add(new Fitter());

		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
		setJMenuBar(menuBar);		

	}

	private void setUpControls() {
		JPanel controlPanel = new JPanel(new BorderLayout(2, 2));
		JLabel label = new JLabel("Caricature factor");
		textField = new JTextField("0.0");
		textField.addActionListener(new CaricatureFactorListener());
		controlPanel.add(label, BorderLayout.WEST);
		controlPanel.add(textField, BorderLayout.CENTER);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
	}

	private class CaricatureFactorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			face.caricature(Double.parseDouble(textField.getText()));
			faceUI.scanFace();
			canvas.repaint();
		}
	}

	protected class Loader extends AbstractAction {
		public Loader() {
			super("Open...");
		}
		public void actionPerformed(ActionEvent e) {
			File file = FileManager.getInstance().chooseFileToOpen(FacebenderViewer.this);
			if (file != null) {
				Object obj = Serializer.deserialize(file);
				if (obj instanceof Face) {
					setFace((Face) obj);
				}
			}
		}
	}
	protected class Saver extends AbstractAction {
		public Saver() {
			super("Save...");
		}
		public void actionPerformed(ActionEvent e) {
			File file = FileManager.getInstance().chooseFileToSave(FacebenderViewer.this);
			if (file != null) {
				Serializer.serialize(face, file);
			}
		}
	}
}
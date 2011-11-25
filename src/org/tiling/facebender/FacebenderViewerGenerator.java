package org.tiling.facebender;

import org.tiling.gui.Canvas2D;
import org.tiling.gui.Viewer2D;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class FacebenderViewerGenerator extends Viewer2D {

	private Face face;
	private FaceUI faceUI;
	private Canvas2D canvas;

	JButton reset;
	JButton back;
	JLabel label;

	public FacebenderViewerGenerator(Face face) {
		this(face, "Face");
	}

	public FacebenderViewerGenerator(Face face, String title) {

		super(title);

		this.face = face;
		this.faceUI = new StraightFaceUI(face, true);
		this.canvas = new Canvas2D(faceUI);
		setCanvas2D(canvas);

		setVisible(true);

		setUpControls();
	}

	public void addNotify() {
		super.addNotify();
		pack();
		fitToCanvas();
	}

	private void setUpControls() {
		JPanel controlPanel = new JPanel(new BorderLayout(2, 2));
		reset = new JButton("Reset");
		label = new JLabel(face.getDescription());
		back = new JButton("Back");
		controlPanel.add(reset, BorderLayout.WEST);
		controlPanel.add(label, BorderLayout.CENTER);
		controlPanel.add(back, BorderLayout.EAST);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
	}

	public void addResetListener(ActionListener resetListener) {
		reset.addActionListener(resetListener);
	}

	public void addBackListener(ActionListener backListener) {
		back.addActionListener(backListener);
	}

	public void repaint() {
		if (label != null) {
			label.setText(face.getDescription());
		}
		super.repaint();
	}

}
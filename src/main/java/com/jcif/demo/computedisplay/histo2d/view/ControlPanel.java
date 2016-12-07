package com.jcif.demo.computedisplay.histo2d.view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ControlPanel extends JPanel {

	int BINS_MIN = 5;

	int BINS_MAX = 250;

	int BINS_INIT = 50;

	public int getBINS_INIT() {
		return BINS_INIT;
	}

	protected JButton brushButton = new JButton("Start histo brush ");

	protected JSlider binXSlider = new JSlider(JSlider.HORIZONTAL, BINS_MIN, BINS_MAX, BINS_INIT);

	protected JSlider binYSlider = new JSlider(JSlider.HORIZONTAL, BINS_MIN, BINS_MAX, BINS_INIT);

	protected JSlider histosize = new JSlider(JSlider.HORIZONTAL, 1, 20, 3);

	public ControlPanel() {
		initGadget();
		initLayout();
	}

	protected void initGadget() {
		// Turn on labels at major tick marks.
		binXSlider.setMajorTickSpacing(25);
		binXSlider.setMinorTickSpacing(10);
		binXSlider.setPaintTicks(true);
		binXSlider.setPaintLabels(true);

		// Turn on labels at major tick marks.
		binYSlider.setMajorTickSpacing(25);
		binYSlider.setMinorTickSpacing(10);
		binYSlider.setPaintTicks(true);
		binYSlider.setPaintLabels(true);

		// Turn on labels at major tick marks.
		histosize.setMajorTickSpacing(5);
		histosize.setMinorTickSpacing(1);
		histosize.setPaintTicks(true);
		histosize.setPaintLabels(true);
	}

	public JSlider getHistosize() {
		return histosize;
	}

	protected void initLayout() {

		this.setLayout(new GridLayout(1, 3));
		this.add(brushButton);
		this.add(binXSlider);
		this.add(binYSlider);
		this.add(histosize);
	}

	public JButton getBrushButton() {
		return brushButton;
	}

	public JSlider getBinXSlider() {
		return binXSlider;
	}

	public JSlider getBinYSlider() {
		return binYSlider;
	}
}

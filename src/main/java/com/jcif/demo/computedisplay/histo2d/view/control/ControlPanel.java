package com.jcif.demo.computedisplay.histo2d.view.control;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.jcif.awt.View;
import com.jcif.opengl.util.DataUtilities.DATA_TYPE;

public class ControlPanel implements View {

	JPanel view = new JPanel();

	int BINS_MIN = 5;

	int BINS_MAX = 250;

	int BINS_INIT = 50;

	public int getBINS_INIT() {
		return BINS_INIT;
	}

	protected JButton histoBrushButton = new JButton("Start histo brush ");

	protected JSlider histoBinXSlider = new JSlider(JSlider.HORIZONTAL, BINS_MIN, BINS_MAX, BINS_INIT);

	protected JSlider histoBinYSlider = new JSlider(JSlider.HORIZONTAL, BINS_MIN, BINS_MAX, BINS_INIT);

	protected JSlider histoSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 3);

	protected JComboBox<DATA_TYPE> dataXtype = new JComboBox<>(DATA_TYPE.values());

	protected JComboBox<DATA_TYPE> dataYtype = new JComboBox<>(DATA_TYPE.values());

	protected JTextField datanumber = new JTextField("number");

	public ControlPanel() {

		initLayout();
	}

	JPanel buildDataPanel() {

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(3, 1));
		panel.add(datanumber);
		panel.add(dataXtype);
		panel.add(dataYtype);
		return panel;
	}

	JPanel buildHistoPanel() {
		JPanel panel = new JPanel();

		// Turn on labels at major tick marks.
		histoBinXSlider.setBorder(new TitledBorder("Bin X value:"));
		histoBinXSlider.setMajorTickSpacing(25);
		histoBinXSlider.setMinorTickSpacing(10);
		histoBinXSlider.setPaintTicks(true);
		histoBinXSlider.setPaintLabels(true);

		// Turn on labels at major tick marks.
		histoBinYSlider.setBorder(new TitledBorder("Bin Y value:"));
		histoBinYSlider.setMajorTickSpacing(25);
		histoBinYSlider.setMinorTickSpacing(10);
		histoBinYSlider.setPaintTicks(true);
		histoBinYSlider.setPaintLabels(true);

		// Turn on labels at major tick marks.
		histoSizeSlider.setBorder(new TitledBorder("Size value:"));
		histoSizeSlider.setMajorTickSpacing(5);
		histoSizeSlider.setMinorTickSpacing(1);
		histoSizeSlider.setPaintTicks(true);
		histoSizeSlider.setPaintLabels(true);

		panel.setLayout(new GridLayout(4, 1));
		panel.add(histoBrushButton);
		panel.add(histoBinXSlider);
		panel.add(histoBinYSlider);
		panel.add(histoSizeSlider);

		return panel;
	}

	public JSlider getHistoSizeSlider() {
		return histoSizeSlider;
	}

	protected void initLayout() {
		view.setLayout(new GridLayout(1, 1));
		JTabbedPane tabpane = new JTabbedPane();

		view.add(tabpane);
		tabpane.add("Data", buildDataPanel());
		tabpane.add("Histogram", buildHistoPanel());

	}

	public JButton getHistoBrushButton() {
		return histoBrushButton;
	}

	public JSlider getHistoBinXSlider() {
		return histoBinXSlider;
	}

	public JSlider getHistoBinYSlider() {
		return histoBinYSlider;
	}

	@Override
	public Component getView() {

		return view;
	}
}

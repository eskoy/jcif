package com.jcif.demo.computedisplay.histo2d.control;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import com.jcif.awt.JFXColorPickerCtrl;
import com.jcif.awt.JPopup;
import com.jcif.mvc.ViewProvider;

public class ControlView implements ViewProvider {

	protected JPanel view = new JPanel();

	protected JTabbedPane tabpane;

	int BINS_MIN = 5;

	int BINS_MAX = 250;

	int BINS_INIT = 50;

	public int getBINS_INIT() {
		return BINS_INIT;
	}

	protected JFXColorPickerCtrl colorPicker = new JFXColorPickerCtrl();

	protected JButton button = new JButton("TEST POPUP");

	protected JButton histoBrushButton = new JButton("Start histo brush ");

	protected JSlider histoBinXSlider = new JSlider(JSlider.HORIZONTAL, BINS_MIN, BINS_MAX, BINS_INIT);

	protected JSlider histoBinYSlider = new JSlider(JSlider.HORIZONTAL, BINS_MIN, BINS_MAX, BINS_INIT);

	protected JSlider histoSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 3);

	public ControlView() {

		initLayout();
	}

	JComponent createFAKEPANEL() {

		JTabbedPane tabpane = new JTabbedPane();
		tabpane.add("essai1", new JPanel());
		tabpane.add("essai2", new JPanel());
		tabpane.add("essai3", new JPanel());
		tabpane.add("essai4", new JPanel());

		tabpane.setSize(300, 300);
		tabpane.setPreferredSize(tabpane.getSize());
		return tabpane;
	}

	JPanel buildHistoPanel() {
		JPanel panel = new JPanel();

		// Turn on labels at major tick marks.
		histoBinXSlider.setBorder(new TitledBorder("Bin X :"));
		histoBinXSlider.setMajorTickSpacing(25);
		histoBinXSlider.setMinorTickSpacing(10);
		histoBinXSlider.setPaintTicks(true);
		histoBinXSlider.setPaintLabels(true);

		// Turn on labels at major tick marks.
		histoBinYSlider.setBorder(new TitledBorder("Bin Y :"));
		histoBinYSlider.setMajorTickSpacing(25);
		histoBinYSlider.setMinorTickSpacing(10);
		histoBinYSlider.setPaintTicks(true);
		histoBinYSlider.setPaintLabels(true);

		// Turn on labels at major tick marks.
		histoSizeSlider.setBorder(new TitledBorder("Size :"));
		histoSizeSlider.setMajorTickSpacing(5);
		histoSizeSlider.setMinorTickSpacing(1);
		histoSizeSlider.setPaintTicks(true);
		histoSizeSlider.setPaintLabels(true);

		JPopup pop = new JPopup(createFAKEPANEL());

		button.addActionListener(e -> {
			pop.setLocationRelativeTo(button);
			pop.setVisible(true);
		});

		panel.setLayout(new GridLayout(6, 1));
		panel.add(colorPicker.getView());
		panel.add(button);
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
		tabpane = new JTabbedPane();
		view.add(tabpane);
		tabpane.add("Histo2d Data Reduction", buildHistoPanel());

	}

	public void insertTab(int index, Component view) {
		tabpane.insertTab(view.getName(), null, view, view.getName(), index);
		tabpane.setSelectedIndex(index);
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

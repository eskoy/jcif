package com.jcif.demo.computedisplay.histo2d.source.view;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import com.jcif.awt.View;
import com.jcif.opengl.util.DataUtilities.DATA_TYPE;

public class SourcePanel implements View {

	public JComboBox<DATA_TYPE> getDataXtype() {
		return dataXtype;
	}

	public JComboBox<DATA_TYPE> getDataYtype() {
		return dataYtype;
	}

	public JSlider getDatanumberSlider() {
		return datanumberSlider;
	}

	JPanel view = new JPanel();

	int DATA_MIN = 0;

	int DATA_MAX = 5;

	int DATA_INIT = 1;

	public int getDATA_INIT() {
		return DATA_INIT;
	}

	protected JComboBox<DATA_TYPE> dataXtype = new JComboBox<>(DATA_TYPE.values());

	protected JComboBox<DATA_TYPE> dataYtype = new JComboBox<>(DATA_TYPE.values());

	protected JSlider datanumberSlider = new JSlider(JSlider.HORIZONTAL, DATA_MIN, DATA_MAX, DATA_INIT);

	public SourcePanel() {

		view = buildDataPanel();
	}

	JPanel buildDataPanel() {

		datanumberSlider.setBorder(new TitledBorder("Data number:"));
		datanumberSlider.setMajorTickSpacing(5);
		datanumberSlider.setMinorTickSpacing(1);
		datanumberSlider.setPaintTicks(true);
		datanumberSlider.setPaintLabels(true);

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(3, 1));
		panel.add(datanumberSlider);
		panel.add(dataXtype);
		panel.add(dataYtype);
		return panel;
	}

	@Override
	public Component getView() {

		return view;
	}
}

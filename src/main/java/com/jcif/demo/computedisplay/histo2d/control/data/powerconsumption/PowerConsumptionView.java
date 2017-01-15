package com.jcif.demo.computedisplay.histo2d.control.data.powerconsumption;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import com.jcif.mvc.ViewProvider;

import info.clearthought.layout.TableLayout;

public class PowerConsumptionView implements ViewProvider {

	JPanel view = new JPanel();

	int DATA_MIN = 0;

	int DATA_MAX = 5;

	int DATA_INIT = 1;

	protected JComboBox<POWER_CONSUMPTION> dataXtype = new JComboBox<>(POWER_CONSUMPTION.values());

	protected JComboBox<POWER_CONSUMPTION> dataYtype = new JComboBox<>(POWER_CONSUMPTION.values());

	protected JSlider datanumberSlider = new JSlider(JSlider.HORIZONTAL, DATA_MIN, DATA_MAX, DATA_INIT);

	public int getDATA_INIT() {
		return DATA_INIT;
	}

	public JComboBox<POWER_CONSUMPTION> getDataXtype() {
		return dataXtype;
	}

	public JComboBox<POWER_CONSUMPTION> getDataYtype() {
		return dataYtype;
	}

	public JSlider getDatanumberSlider() {
		return datanumberSlider;
	}

	public PowerConsumptionView() {

		view = buildDataPanel();
		view.setName("Power Comsumption");
	}

	JPanel buildDataPanel() {

		datanumberSlider.setBorder(new TitledBorder("Data number in million:"));
		datanumberSlider.setMajorTickSpacing(5);
		datanumberSlider.setMinorTickSpacing(1);
		datanumberSlider.setPaintTicks(true);
		datanumberSlider.setPaintLabels(true);

		JPanel panx = new JPanel(new GridLayout(1, 1));
		panx.setBorder(new TitledBorder("X DATATYPE"));
		panx.add(dataXtype);

		JPanel pany = new JPanel(new GridLayout(1, 1));
		pany.setBorder(new TitledBorder("Y DATATYPE"));
		pany.add(dataYtype);

		JPanel panel = new JPanel();

		double size[][] = { { TableLayout.FILL }, { TableLayout.FILL, TableLayout.FILL, TableLayout.FILL } };
		panel.setLayout(new TableLayout(size));
		panel.add(datanumberSlider, "0,0");
		panel.add(panx, "0,1");
		panel.add(pany, "0,2");
		return panel;
	}

	@Override
	public Component getView() {

		return view;
	}
}

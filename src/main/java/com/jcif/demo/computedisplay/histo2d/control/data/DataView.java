package com.jcif.demo.computedisplay.histo2d.control.data;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.jcif.awt.ViewProvider;

public class DataView implements ViewProvider {
	protected JPanel view;
	protected JPanel cards;
	protected JComboBox<String> datasourcecombobox = new JComboBox<>();

	public DataView() {

		view = buildView();
		view.setName("Data");
	}

	public JComboBox<String> getDatasourcecombobox() {
		return datasourcecombobox;
	}

	public JPanel buildView() {

		JPanel panel = new JPanel(new BorderLayout());
		// Put the JComboBox in a JPanel to get a nicer look.
		JPanel comboBoxPane = new JPanel(); // use FlowLayout

		datasourcecombobox.setEditable(false);
		datasourcecombobox.addItemListener(e -> {
			CardLayout cl = (CardLayout) (cards.getLayout());
			cl.show(cards, (String) e.getItem());
		});
		comboBoxPane.add(datasourcecombobox);
		// Create the panel that contains the "cards".
		cards = new JPanel(new CardLayout());
		panel.add(comboBoxPane, BorderLayout.NORTH);
		panel.add(cards, BorderLayout.CENTER);

		return panel;
	}

	public void addCard(Component pane, String key) {

		cards.add(pane, key);
		datasourcecombobox.addItem(key);
	}

	@Override
	public Component getView() {

		return view;
	}
}

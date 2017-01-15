package com.jcif.awt;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JDialog;

public class JPopup extends JDialog {

	public JPopup(Component pane) {
		super();
		this.setUndecorated(true);
		this.setLayout(new GridLayout(1, 1));
		this.add(pane);
		this.pack();
		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setVisible(false);
			}
		});

		pane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setVisible(false);
			}
		});
	}

}

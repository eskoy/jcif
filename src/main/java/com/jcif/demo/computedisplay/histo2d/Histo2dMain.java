package com.jcif.demo.computedisplay.histo2d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.jcif.demo.computedisplay.histo2d.chart.ChartCallback;
import com.jcif.demo.computedisplay.histo2d.chart.ChartCtrl;
import com.jcif.demo.computedisplay.histo2d.control.ControlCtrl;
import com.jcif.demo.computedisplay.histo2d.control.data.DataModel;

public class Histo2dMain {

	protected JPanel view;

	protected DataModel dataModel = new DataModel();

	protected ControlCtrl controlCtrl;

	protected ChartCtrl chartCtrl;

	public JPanel getView() {
		return view;

	}

	public Histo2dMain() {

		try {
			laf();
			init();

		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// updateViewModel();
	}

	protected void laf() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}
	}

	protected void init() throws InvocationTargetException, InterruptedException {

		// construct main view
		chartCtrl = new ChartCtrl(dataModel);

		ChartCallback cb = chartCtrl;
		controlCtrl = new ControlCtrl(cb, dataModel);

		// Create a split pane with the two scroll panes in it.

		JScrollPane controlPane = new JScrollPane(controlCtrl.getView());
		JScrollPane histoPane = new JScrollPane(chartCtrl.getView());
		histoPane.setAutoscrolls(false);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, controlPane, chartCtrl.getView());
		// splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(280);

		// Provide minimum sizes for the two components in the split pane

		controlPane.setMinimumSize(new Dimension(280, 500));
		histoPane.setMinimumSize(new Dimension(500, 500));

		view = new JPanel(new BorderLayout(1, 1));
		view.add(splitPane);
	}

	public static void main(String[] args) {

		Histo2dMain ctrl = new Histo2dMain();

		SwingUtilities.invokeLater(() -> {

			final JFrame jframe = new JFrame(
					"Use histogram2d compute shader to build synthetic view of large dataset ( 1 to 5 million of record)");
			jframe.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowevent) {
					jframe.dispose();
					System.exit(0);
				}
			});

			jframe.getContentPane().add(ctrl.getView(), BorderLayout.CENTER);
			jframe.setSize(1280, 720);
			jframe.setVisible(true);

		});
	}
}

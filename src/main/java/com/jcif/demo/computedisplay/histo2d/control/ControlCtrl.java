package com.jcif.demo.computedisplay.histo2d.control;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jcif.demo.computedisplay.histo2d.chart.ChartCallback;
import com.jcif.demo.computedisplay.histo2d.chart.histo2d.Histo2dModel;
import com.jcif.demo.computedisplay.histo2d.control.data.DataCtrl;
import com.jcif.demo.computedisplay.histo2d.control.data.DataModel;
import com.jcif.mvc.MtoVCallBack;
import com.jcif.mvc.ViewProvider;

public class ControlCtrl implements ViewProvider {

	protected DataModel sourceModel = new DataModel();

	protected Histo2dModel histo2dModel;

	protected ControlView controlView = new ControlView();

	protected DataCtrl dataCtrl;

	protected MtoVCallBack callBack;

	@Override
	public Component getView() {
		return controlView.getView();
	}

	public ControlCtrl(ChartCallback cb, DataModel dataModel) {
		// init source
		histo2dModel = cb.getHistoModel();
		callBack = cb;
		dataCtrl = new DataCtrl(callBack, dataModel);
		insert(0, dataCtrl.getView());
		dataCtrl.modelToView();

		histo2dModel.setHistoXSize(controlView.getBINS_INIT());
		histo2dModel.setHistoYSize(controlView.getBINS_INIT());

		controlView.getHistoBinXSlider().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (!controlView.getHistoBinXSlider().getValueIsAdjusting()) {

					modelToview();
				}
			}
		});

		controlView.getHistoBinYSlider().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (!controlView.getHistoBinYSlider().getValueIsAdjusting()) {

					modelToview();
				}
			}
		});

		controlView.getHistoSizeSlider().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (!controlView.getHistoSizeSlider().getValueIsAdjusting()) {

					modelToview();
				}
			}
		});

		controlView.getHistoBrushButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				cb.startBrushOnView();
			}
		});

	}

	public JButton getHistoBrushButton() {
		return controlView.getHistoBrushButton();
	}

	public void insert(int index, Component view) {
		controlView.insertTab(index, view);

	}

	public void modelToview() {
		int value = controlView.getHistoSizeSlider().getValue();
		histo2dModel.setPointSize(value);
		int ybins = controlView.getHistoBinYSlider().getValue();
		histo2dModel.setHistoYSize(ybins);
		int xbins = controlView.getHistoBinXSlider().getValue();
		histo2dModel.setHistoXSize(xbins);
		callBack.modelToView();
	}

}

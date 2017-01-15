package com.jcif.demo.computedisplay.histo2d.chart;

import com.jcif.demo.computedisplay.histo2d.chart.histo2d.Histo2dModel;
import com.jcif.mvc.MtoVCallBack;

public interface ChartCallback extends MtoVCallBack {

	public Histo2dModel getHistoModel();

	void startBrushOnView();

}

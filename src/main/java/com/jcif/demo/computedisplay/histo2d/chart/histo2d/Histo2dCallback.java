package com.jcif.demo.computedisplay.histo2d.chart.histo2d;

import com.jcif.mvc.MtoVCallBack;

public interface Histo2dCallback extends MtoVCallBack {

	public Histo2dModel getHistoModel();

	void startBrushOnView();

}

package com.jcif.demo.computedisplay.histo2d.control.data;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import com.jcif.awt.CallBack;
import com.jcif.awt.ViewProvider;
import com.jcif.demo.computedisplay.histo2d.control.data.generated.GeneratedCtrl;
import com.jcif.demo.computedisplay.histo2d.control.data.powerconsumption.PowerConsumptionCtrl;

public class DataCtrl implements ViewProvider, CallBack {

	protected GeneratedCtrl generatedCtrl;

	protected PowerConsumptionCtrl powerConsumptionCtrl;

	protected DataView sourceView = new DataView();

	protected Map<String, CallBack> ctrlMap = new HashMap<>();

	@Override
	public Component getView() {
		return sourceView.getView();
	}

	public DataCtrl(CallBack cb, DataModel model) {

		generatedCtrl = new GeneratedCtrl(cb, model);
		powerConsumptionCtrl = new PowerConsumptionCtrl(cb, model);

		addDataSource(generatedCtrl.getView(), generatedCtrl);
		addDataSource(powerConsumptionCtrl.getView(), powerConsumptionCtrl);
		sourceView.getDatasourcecombobox().addItemListener(e -> {
			ctrlMap.get(e.getItem()).modelToView();
			;
		});

	}

	void addDataSource(Component comp, CallBack cb) {
		sourceView.addCard(comp, comp.getName());
		ctrlMap.put(comp.getName(), cb);
	}

	@Override
	public void modelToView() {
		ctrlMap.get(sourceView.getDatasourcecombobox().getSelectedItem()).modelToView();
	}

}

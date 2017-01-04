package com.jcif.demo.computedisplay.histo2d.control.data.powerconsumption;

import java.awt.Component;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jcif.awt.CallBack;
import com.jcif.awt.ViewProvider;
import com.jcif.demo.computedisplay.histo2d.control.data.DataModel;
import com.jcif.demo.computedisplay.histo2d.control.data.powerconsumption.io.PowerConsumptionFileReader;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLSharedContextInstance;
import com.jcif.structure.model.StructureDataset;
import com.jcif.structure.process.MinMaxProcess;
import com.jcif.structure.process.NormalizeProcess;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

public class PowerConsumptionCtrl implements ViewProvider, CallBack {

	protected CallBack callBack;

	protected DataModel sourceModel;

	protected float[][] minMaxs;

	protected StructureDataset ds;

	protected PowerConsumptionView powerConsumptionView = new PowerConsumptionView();

	@Override
	public Component getView() {
		return powerConsumptionView.getView();
	}

	public void loadDataFromFile() {
		String path = "household_power_consumption";
		PowerConsumptionFileReader reader = new PowerConsumptionFileReader();
		int numberOfRecord = 1000000;
		ds = reader.read(path, numberOfRecord);
		MinMaxProcess minMaxProcess = new MinMaxProcess(POWER_CONSUMPTION.values());
		ds.stream().process(minMaxProcess).start();
		minMaxs = minMaxProcess.getMinMaxs();
	}

	public PowerConsumptionCtrl(CallBack cb, DataModel model) {
		callBack = cb;
		sourceModel = model;

		loadDataFromFile();

		powerConsumptionView.getDatanumberSlider().addChangeListener(e -> {
			if (!powerConsumptionView.getDatanumberSlider().getValueIsAdjusting()) {
				modelToView();
			}

		});
		powerConsumptionView.getDataXtype().addActionListener(e -> {
			modelToView();
		});

		powerConsumptionView.getDataYtype().addActionListener(e -> {
			modelToView();
		});
	}

	@Override
	public void modelToView() {

		int numbervalue = powerConsumptionView.getDatanumberSlider().getValue();
		POWER_CONSUMPTION xtype = (POWER_CONSUMPTION) powerConsumptionView.getDataXtype().getSelectedItem();
		POWER_CONSUMPTION ytype = (POWER_CONSUMPTION) powerConsumptionView.getDataYtype().getSelectedItem();
		GLContext sharedContext = GLSharedContextInstance.getInstance().getGLSharedContext();
		sharedContext.makeCurrent();
		computeNewData(1000000, xtype, ytype, sharedContext.getGL().getGL4());
		sharedContext.release();
		callBack.modelToView();

	}

	public void computeNewData(int size, POWER_CONSUMPTION x, POWER_CONSUMPTION y, GL4 gl) {
		sourceModel.setDataNumber(size);

		if (sourceModel.getGpuValueX() != null)
			sourceModel.getGpuValueX().release(gl);

		ByteBuffer tmpBuffer = GLBufferFactory.allocate(ds.getSize() * Float.BYTES);
		FloatBuffer floatbuffervalues = tmpBuffer.asFloatBuffer();

		NormalizeProcess normalizeProcess = new NormalizeProcess(x, minMaxs[x.ordinal()], floatbuffervalues);

		ds.stream().process(normalizeProcess).start();

		sourceModel.setGpuValueX(GLBufferFactory.hostoGpuData(tmpBuffer, gl));

		if (sourceModel.getGpuValueY() != null)
			sourceModel.getGpuValueY().release(gl);

		tmpBuffer = GLBufferFactory.allocate(ds.getSize() * Float.BYTES);
		floatbuffervalues = tmpBuffer.asFloatBuffer();

		normalizeProcess = new NormalizeProcess(y, minMaxs[y.ordinal()], floatbuffervalues);

		ds.stream().process(normalizeProcess).start();

		sourceModel.setGpuValueY(GLBufferFactory.hostoGpuData(tmpBuffer, gl));

		if (sourceModel.getGpuValueindices() != null)
			sourceModel.getGpuValueindices().release(gl);
		sourceModel.setGpuValueindices(GLBufferFactory.hostoGpuData(this.createIndices(size), gl));

	}

	public DataModel getModel() {
		return sourceModel;
	}

	public ByteBuffer createIndices(int nb) {

		ByteBuffer bb = GLBufferFactory.allocate(nb * Integer.BYTES);
		IntBuffer floatbuffervalues = bb.asIntBuffer();

		for (int i = 0; i < nb; i++) {
			// ID
			floatbuffervalues.put(i, i);
			// X

		}

		return bb;
	}
}

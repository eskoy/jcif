package com.jcif.demo.computedisplay.histo2d.control.data.generated;

import java.awt.Component;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.jcif.demo.computedisplay.histo2d.control.data.DataModel;
import com.jcif.mvc.MtoVCallBack;
import com.jcif.mvc.ViewProvider;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLSharedContextInstance;
import com.jcif.opengl.util.DataUtilities;
import com.jcif.opengl.util.DataUtilities.DATA_TYPE;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

public class GeneratedCtrl implements ViewProvider, MtoVCallBack {

	protected MtoVCallBack callBack;

	protected DataModel sourceModel;

	protected GeneratedView generatedView = new GeneratedView();

	@Override
	public Component getView() {
		return generatedView.getView();
	}

	public GeneratedCtrl(MtoVCallBack cb, DataModel model) {
		callBack = cb;
		sourceModel = model;
		generatedView.getDatanumberSlider().addChangeListener(e -> {
			if (!generatedView.getDatanumberSlider().getValueIsAdjusting()) {
				modelToView();
			}

		});
		generatedView.getDataXtype().addActionListener(e -> {
			modelToView();
		});

		generatedView.getDataYtype().addActionListener(e -> {
			modelToView();
		});
	}

	@Override
	public void modelToView() {

		int numbervalue = generatedView.getDatanumberSlider().getValue();
		DATA_TYPE xtype = (DATA_TYPE) generatedView.getDataXtype().getSelectedItem();
		DATA_TYPE ytype = (DATA_TYPE) generatedView.getDataYtype().getSelectedItem();
		GLContext sharedContext = GLSharedContextInstance.getInstance().getGLSharedContext();
		sharedContext.makeCurrent();
		computeNewData(numbervalue * 1000000, xtype, ytype, sharedContext.getGL().getGL4());
		sharedContext.release();
		callBack.modelToView();

	}

	public void computeNewData(int size, DATA_TYPE x, DATA_TYPE y, GL4 gl) {
		sourceModel.setDataNumber(size);

		if (sourceModel.getGpuValueX() != null)
			sourceModel.getGpuValueX().release(gl);
		sourceModel.setGpuValueX(GLBufferFactory.hostoGpuData(DataUtilities.createNewData1d(size, x), gl));

		if (sourceModel.getGpuValueY() != null)
			sourceModel.getGpuValueY().release(gl);
		sourceModel.setGpuValueY(GLBufferFactory.hostoGpuData(DataUtilities.createNewData1d(size, y), gl));

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

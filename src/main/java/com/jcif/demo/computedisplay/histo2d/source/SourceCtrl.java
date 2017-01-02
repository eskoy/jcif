package com.jcif.demo.computedisplay.histo2d.source;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.jcif.awt.CallBack;
import com.jcif.demo.computedisplay.histo2d.source.view.SourcePanel;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLSharedContextInstance;
import com.jcif.opengl.util.DataUtilities;
import com.jcif.opengl.util.DataUtilities.DATA_TYPE;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

public class SourceCtrl {

	protected CallBack callBack;

	protected SourceModel sourceModel = new SourceModel();

	protected SourcePanel sourceView = new SourcePanel();

	public SourcePanel getView() {
		return sourceView;
	}

	public SourceCtrl(CallBack cb) {
		callBack = cb;

		sourceView.getDatanumberSlider().addChangeListener(e -> {
			if (!sourceView.getDatanumberSlider().getValueIsAdjusting()) {
				modelToview();
			}

		});
		sourceView.getDataXtype().addActionListener(e -> {
			modelToview();
		});

		sourceView.getDataYtype().addActionListener(e -> {
			modelToview();
		});
	}

	public void modelToview() {

		int numbervalue = sourceView.getDatanumberSlider().getValue();
		DATA_TYPE xtype = (DATA_TYPE) sourceView.getDataXtype().getSelectedItem();
		DATA_TYPE ytype = (DATA_TYPE) sourceView.getDataYtype().getSelectedItem();
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

	public SourceModel getModel() {
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

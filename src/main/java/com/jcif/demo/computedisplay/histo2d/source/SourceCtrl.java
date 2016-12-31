package com.jcif.demo.computedisplay.histo2d.source;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

		sourceView.getDatanumberSlider().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (!sourceView.getDatanumberSlider().getValueIsAdjusting()) {
					int value = sourceView.getDatanumberSlider().getValue();
					GLContext sharedContext = GLSharedContextInstance.getInstance().getGLSharedContext();
					computeNewData(value * 1000000, DATA_TYPE.LINEAR, DATA_TYPE.COSINUS,
							sharedContext.getGL().getGL4());
					callBack.modelToView();
				}
			}
		});

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

package com.jcif.demo.computedisplay.histo2d.chart.histo2d;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.swing.SwingWorker.StateValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcif.demo.computedisplay.histo2d.control.data.DataModel;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLSharedContextInstance;
import com.jcif.opengl.glpainter.histo.Histo2d;
import com.jcif.opengl.glpainter.histo.Histo2dPainter;
import com.jogamp.opengl.GLContext;

public class Histo2dCtrl {

	protected StateValue updatestateValue;

	private final static Logger logger = LoggerFactory.getLogger(Histo2dCtrl.class);

	protected Histo2dComputeHandler histo2dComputeHandler;

	protected Histo2d mouseData = new Histo2d();

	protected Histo2d histo2d = new Histo2d();

	protected Histo2dPainter histo2dPainter = new Histo2dPainter();

	public Histo2dPainter getHisto2dPainter() {
		return histo2dPainter;
	}

	public Histo2dPainter getMousePainter() {
		return mousePainter;
	}

	protected Histo2dPainter mousePainter = new Histo2dPainter();

	protected DataModel dataModel = new DataModel();

	protected Histo2dModel histoModel = new Histo2dModel();

	public Histo2dModel getHistoModel() {
		return histoModel;
	}

	public Histo2dCtrl(DataModel dm, GLContext sharedContext) {

		dataModel = dm;

		init(sharedContext);
	}

	protected void init(GLContext sharedContext) {

		histo2dComputeHandler = new Histo2dComputeHandler(sharedContext.getGL().getGL4());

	}

	public ByteBuffer[] computeHisto2d() {

		GLContext sharedContext = GLSharedContextInstance.getInstance().getGLSharedContext();
		sharedContext.makeCurrent();

		int histox = histoModel.getHistoXSize();
		int histoy = histoModel.getHistoYSize();

		IntBuffer histodata = histo2dComputeHandler.compute(sharedContext.getGL().getGL4(), dataModel.getGpuValueY(),
				dataModel.getGpuValueX(), dataModel.getGpuValueindices(), dataModel.getDataNumber(), 0, 1f, histox,
				histoy);

		ByteBuffer[] data = histo2dComputeHandler.createHisto2dFromBuffer(histodata, histoy, histox);

		sharedContext.release();

		return data;
	}

	protected void updateHisto2dPainter(Histo2dModel viewmodel) {

		histo2d.setXYs(viewmodel.getHistoBuffer()[0]);
		histo2d.setColors(viewmodel.getHistoBuffer()[1]);
		histo2d.setCounts(viewmodel.getHistoBuffer()[2]);
		histo2d.setPointSize(viewmodel.getPointSize());
		histo2dPainter.update(histo2d);

	}

	public ByteBuffer[] createMouseData(float x, float y) {
		ByteBuffer bb = GLBufferFactory.allocate(Float.BYTES * 2);
		FloatBuffer floatbuffervalues = bb.asFloatBuffer();
		ByteBuffer bbColor = GLBufferFactory.allocate(Float.BYTES * 4);
		FloatBuffer floatColorbuffervalues = bbColor.asFloatBuffer();

		floatbuffervalues.put(0, x);
		floatbuffervalues.put(1, y);

		float r = 0.25f;
		float g = 0.25f;
		float b = 0.25f;

		floatColorbuffervalues.put(0, r);
		floatColorbuffervalues.put(1, g);
		floatColorbuffervalues.put(2, b);
		floatColorbuffervalues.put(3, 1f);

		return new ByteBuffer[] { bb, bbColor };

	}

	public void reduceModel() {

		histoModel.setHistoBuffer(computeHisto2d());
		updateHisto2dPainter(histoModel);
	}

	public void mouseMoved(float x, float y) {

		ByteBuffer[] data = createMouseData(x, y);

		mouseData.setXYs(data[0]);
		mouseData.setColors(data[1]);
		mouseData.setPointSize(30f);
		// simplification works with square histo

		mousePainter.update(mouseData);

		if (histoModel.getHistoBuffer()[0] != null) {
			histo2dComputeHandler.filterPoint(histoModel.getHistoBuffer(), x, y, 0.01f);
			updateHisto2dPainter(histoModel);
		}

	}

}

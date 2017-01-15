package com.jcif.demo.computedisplay.histo2d.chart;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcif.demo.computedisplay.histo2d.chart.background.BackGroundCtrl;
import com.jcif.demo.computedisplay.histo2d.chart.histo2d.Histo2dCtrl;
import com.jcif.demo.computedisplay.histo2d.chart.histo2d.Histo2dModel;
import com.jcif.demo.computedisplay.histo2d.control.data.DataModel;
import com.jcif.mvc.MtoVCallBack;
import com.jcif.mvc.ViewProvider;
import com.jcif.opengl.GLSharedContextInstance;
import com.jcif.opengl.windowtoolkit.GLPainterController;
import com.jcif.opengl.windowtoolkit.WindowToolkitFactory;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.awt.GLCanvas;

public class ChartCtrl implements MtoVCallBack, ViewProvider, ChartCallback {

	private final static Logger logger = LoggerFactory.getLogger(ChartCtrl.class);

	protected GLPainterController<GLCanvas> gLPainterController = WindowToolkitFactory.newSwingGLPainterController();

	protected BackGroundCtrl backGroundCtrl = new BackGroundCtrl();

	protected Histo2dCtrl histo2dCtrl;

	@Override
	public Histo2dModel getHistoModel() {
		return histo2dCtrl.getHistoModel();
	}

	public ChartCtrl(DataModel dm) {
		init(dm);
	}

	protected void init(DataModel dm) {

		// compose opengl layer
		GLContext sharedContext = GLSharedContextInstance.getInstance().getGLSharedContext();
		logger.info("" + GLSharedContextInstance.getInstance().getGLDeviceProperties());

		sharedContext.makeCurrent();

		histo2dCtrl = new Histo2dCtrl(dm, sharedContext);

		gLPainterController.getBackGround().setColor(Color.WHITE);
		gLPainterController.setSharedContext(sharedContext);
		gLPainterController.addPainter(backGroundCtrl.getWorldcoaslinepainter());
		gLPainterController.addPainter(backGroundCtrl.getGridPainter());
		gLPainterController.addPainter(histo2dCtrl.getHisto2dPainter());
		gLPainterController.addPainter(histo2dCtrl.getMousePainter());

		sharedContext.release();

		gLPainterController.getDisplayComponent().addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				float x = e.getX();
				x /= gLPainterController.getBackGround().getViewPort()[2];
				float y = e.getY();
				y /= gLPainterController.getBackGround().getViewPort()[3];
				x = x * 2 - 1;
				y = -(y * 2 - 1);

			}

		});

		gLPainterController.getDisplayComponent().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				modelToView();

			}

		});

	}

	@Override
	public void modelToView() {

		histo2dCtrl.modelToView();
		gLPainterController.repaint();

	}

	@Override
	public void startBrushOnView() {
		gLPainterController.getDisplayComponent().addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				float x = e.getX();
				x /= gLPainterController.getBackGround().getViewPort()[2];
				float y = e.getY();
				y /= gLPainterController.getBackGround().getViewPort()[3];
				x = x * 2 - 1;
				y = -(y * 2 - 1);

				histo2dCtrl.mouseMoved(x, y);

				gLPainterController.repaint();

			}

		});

	}

	@Override
	public Component getView() {

		return gLPainterController.getDisplayComponent();
	}
}

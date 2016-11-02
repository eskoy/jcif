package com.jcif.demo.windowtoolkit.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker.StateValue;

import com.jcif.demo.application.Histo2dComputeHandler;
import com.jcif.demo.windowtoolkit.swing.model.BusinessModel;
import com.jcif.opengl.GlSharedContextInstance;
import com.jcif.opengl.glpainter.grid.Grid;
import com.jcif.opengl.glpainter.grid.GridPainter;
import com.jcif.opengl.glpainter.scatterchart.ScatterChart;
import com.jcif.opengl.glpainter.scatterchart.ScatterChartPainter;
import com.jcif.opengl.windowtoolkit.GLSwingCanvas;
import com.jogamp.nativewindow.AbstractGraphicsDevice;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;

public class Histo2dController {

	protected GLSwingCanvas gLSwingCanvas;

	protected GridPainter gridPainter = new GridPainter();

	protected Grid grid = new Grid();

	protected ScatterChartPainter scatterChartPainter = new ScatterChartPainter();

	protected ScatterChart scatterChart = new ScatterChart();

	protected JPanel view;

	protected StateValue updatestateValue;

	protected BusinessModel businessModel;

	protected Histo2dComputeHandler histo2dComputeHandler;

	protected int histoSize = 250;

	public JPanel getView() {
		return view;

	}

	public Histo2dController() {

		try {
			init();

		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// updateViewModel();
	}

	public ByteBuffer[] updateViewModel() {

		final GLDrawableFactory factory = GLDrawableFactory.getFactory(GLProfile.get(GLProfile.GL4bc));
		final AbstractGraphicsDevice device = GlSharedContextInstance.getInstance().getGLSharedAutoDrawable()
				.getNativeSurface().getGraphicsConfiguration().getScreen().getDevice();
		GLCapabilitiesImmutable caps = GlSharedContextInstance.getInstance().getGLCapabilities();
		final GLDrawable drawable = factory.createDummyDrawable(device, true, caps, null);
		drawable.setRealized(true);
		GLContext sharedContext = drawable
				.createContext(GlSharedContextInstance.getInstance().getGLSharedAutoDrawable().getContext());
		sharedContext.makeCurrent();

		IntBuffer histodata = histo2dComputeHandler.compute(sharedContext.getGL().getGL4(),
				businessModel.getGpuValueA(), businessModel.getGpuValueB(), businessModel.getGpuValueindices(),
				businessModel.getDataNumber(), 0, 1.0f, histoSize);

		ByteBuffer[] data = histo2dComputeHandler.createPointWithHisto(histodata, histoSize);

		sharedContext.release();

		return data;
	}

	public void update() {

		final long time = System.currentTimeMillis();
		// if (updatestateValue == null || updatestateValue == StateValue.DONE)
		// {
		// SwingWorker<ByteBuffer[], Void> worker = new
		// SwingWorker<ByteBuffer[], Void>() {
		// @Override
		// public ByteBuffer[] doInBackground() {
		//
		// return updateViewModel();
		// }
		//
		// @Override
		// public void done() {
		//
		// try {
		// updateChart(get(), histoSize);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// Random rand = new Random();
		// long newtime = System.currentTimeMillis() - time;
		// System.err.println(
		// "Time to compute & diplay histo is in ms " + newtime + " " +
		// Thread.currentThread());
		// updateGrid(rand.nextInt(20), Color.CYAN);
		// gLSwingCanvas.repaint();
		// updatestateValue = this.getState();
		//
		// }
		// };
		//
		// worker.execute();
		//
		// }

		updateChart(updateViewModel(), 40);
		Random rand = new Random();
		updateGrid(rand.nextInt(20), Color.CYAN);
		gLSwingCanvas.repaint();

	}

	protected void init() throws InvocationTargetException, InterruptedException {

		this.updateGrid(8, Color.CYAN);

		GLContext sharedContext = GlSharedContextInstance.getInstance().getGLSharedContext();
		System.err.println(GlSharedContextInstance.getInstance().getGLDeviceProperties());

		sharedContext.makeCurrent();
		businessModel = new BusinessModel(6000000, sharedContext.getGL().getGL4());
		histo2dComputeHandler = new Histo2dComputeHandler(sharedContext.getGL().getGL4());
		sharedContext.release();

		gLSwingCanvas = new GLSwingCanvas();
		gLSwingCanvas.setSharedContext(sharedContext);
		gLSwingCanvas.addPainter(gridPainter);
		gLSwingCanvas.addPainter(scatterChartPainter);

		this.updateChart(updateViewModel(), histoSize);

		view = new JPanel(new BorderLayout(1, 1));
		view.add(gLSwingCanvas, BorderLayout.CENTER);
		view.add(new JLabel("SwingLabel"), BorderLayout.NORTH);

		view.setBackground(Color.cyan);

		gLSwingCanvas.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				update();

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	protected void updateChart(ByteBuffer[] data, int count) {

		scatterChart.setXYs(data[0]);
		scatterChart.setColors(data[1]);
		scatterChart.setCount(count);
		scatterChartPainter.update(scatterChart);

	}

	protected void updateGrid(int nb, Color col) {
		grid.setColor(col);
		grid.setStep(nb);
		gridPainter.update(grid);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			final JFrame jframe = new JFrame("HistoController demo");
			jframe.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowevent) {
					jframe.dispose();
					System.exit(0);
				}
			});

			Histo2dController ctrl = new Histo2dController();
			jframe.getContentPane().add(ctrl.getView(), BorderLayout.CENTER);
			jframe.setSize(640, 480);
			jframe.setVisible(true);
		});
	}
}

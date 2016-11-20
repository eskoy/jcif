package com.jcif.demo.computedisplay.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcif.demo.application.Histo2dComputeHandler;
import com.jcif.demo.computedisplay.swing.model.BusinessModel;
import com.jcif.demo.computedisplay.swing.model.ViewModel;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.GlSharedContextInstance;
import com.jcif.opengl.glpainter.grid.Grid;
import com.jcif.opengl.glpainter.grid.GridPainter;
import com.jcif.opengl.glpainter.point.Points;
import com.jcif.opengl.glpainter.point.PointPainter;
import com.jcif.opengl.windowtoolkit.GLPainterController;
import com.jcif.opengl.windowtoolkit.WindowToolkitFactory;
import com.jogamp.nativewindow.AbstractGraphicsDevice;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

public class Histo2dController {

	private final static Logger logger = LoggerFactory.getLogger(Histo2dController.class);

	protected GLPainterController<GLCanvas> gLPainterController = WindowToolkitFactory.newSwingGLPainterController();

	protected GridPainter gridPainter = new GridPainter();

	protected Grid grid = new Grid();

	protected PointPainter scatterChartPainter = new PointPainter();

	protected PointPainter mousePainter = new PointPainter();

	protected Points mouseData = new Points();

	protected Points scatterChart = new Points();

	protected JPanel view;

	protected StateValue updatestateValue;

	protected BusinessModel businessModel;

	protected ViewModel viewModel;

	protected JButton brushButton = new JButton("Start histo brush ");

	protected Histo2dComputeHandler histo2dComputeHandler;

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
		viewModel.setHistoSize(250);
		IntBuffer histodata = histo2dComputeHandler.compute(sharedContext.getGL().getGL4(),
				businessModel.getGpuValueA(), businessModel.getGpuValueB(), businessModel.getGpuValueindices(),
				businessModel.getDataNumber(), 0, 1.0f, viewModel.getHistoSize(), viewModel.getHistoSize());

		ByteBuffer[] data = histo2dComputeHandler.createPointWithHisto(histodata, viewModel.getHistoSize(),
				viewModel.getHistoSize());

		sharedContext.release();

		return data;
	}

	public ByteBuffer[] createMouseData(float x, float y) {
		ByteBuffer bb = GlBufferFactory.allocate(Float.BYTES * 2);
		FloatBuffer floatbuffervalues = bb.asFloatBuffer();
		ByteBuffer bbColor = GlBufferFactory.allocate(Float.BYTES * 4);
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

	public void update() {

		final long time = System.currentTimeMillis();
		if (updatestateValue == null || updatestateValue == StateValue.DONE) {
			SwingWorker<ByteBuffer[], Void> worker = new SwingWorker<ByteBuffer[], Void>() {
				@Override
				public ByteBuffer[] doInBackground() {

					return updateViewModel();
				}

				@Override
				public void done() {

					try {
						viewModel.setScatterchartdata(get());
						updateChart(viewModel);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Random rand = new Random();
					long newtime = System.currentTimeMillis() - time;
					System.err.println(
							"Time to compute & diplay histo is in ms " + newtime + " " + Thread.currentThread());
					updateGrid(rand.nextInt(20), Color.CYAN);
					gLPainterController.repaint();
					updatestateValue = this.getState();

				}
			};

			worker.execute();

		}

	}

	protected void init() throws InvocationTargetException, InterruptedException {

		this.updateGrid(8, Color.CYAN);

		GLContext sharedContext = GlSharedContextInstance.getInstance().getGLSharedContext();
		logger.info("" + GlSharedContextInstance.getInstance().getGLDeviceProperties());

		sharedContext.makeCurrent();
		viewModel = new ViewModel();

		businessModel = new BusinessModel(6000000, sharedContext.getGL().getGL4());
		histo2dComputeHandler = new Histo2dComputeHandler(sharedContext.getGL().getGL4());
		sharedContext.release();

		gLPainterController.setSharedContext(sharedContext);
		gLPainterController.addPainter(gridPainter);
		gLPainterController.addPainter(scatterChartPainter);
		gLPainterController.addPainter(mousePainter);

		// this.updateChart(viewModel);

		view = new JPanel(new BorderLayout(1, 1));
		view.add(gLPainterController.getDisplayComponent(), BorderLayout.CENTER);
		view.add(brushButton, BorderLayout.NORTH);

		view.setBackground(Color.cyan);

		brushButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gLPainterController.getDisplayComponent().addMouseMotionListener(new MouseMotionListener() {

					@Override
					public void mouseMoved(MouseEvent e) {
						float x = e.getX();
						x /= gLPainterController.getBackGround().getViewPort()[2];
						float y = e.getY();
						y /= gLPainterController.getBackGround().getViewPort()[3];
						x = x * 2 - 1;
						y = -(y * 2 - 1);

						ByteBuffer[] data = createMouseData(x, y);

						mouseData.setXYs(data[0]);
						mouseData.setColors(data[1]);
						mouseData.setPointSize(30f);
						// simplification works with square histo

						mouseData.setCount(1);
						mousePainter.update(mouseData);

						if (viewModel.getHistoSize() != 0) {
							histo2dComputeHandler.filterPoint(viewModel.getScatterchartdata(), x, y, 0.01f);
							updateChart(viewModel);
						}

						gLPainterController.getDisplayComponent().repaint();

					}

					@Override
					public void mouseDragged(MouseEvent e) {
						// TODO Auto-generated method stub

					}
				});

			}
		});

		gLPainterController.getDisplayComponent().addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				float x = e.getX();
				x /= gLPainterController.getBackGround().getViewPort()[2];
				float y = e.getY();
				y /= gLPainterController.getBackGround().getViewPort()[3];
				x = x * 2 - 1;
				y = -(y * 2 - 1);
				System.err.println(" mousePressed" + x + " " + y);

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		gLPainterController.getDisplayComponent().addKeyListener(new KeyListener() {

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

	protected void updateChart(ViewModel viewmodel) {

		scatterChart.setXYs(viewmodel.getScatterchartdata()[0]);
		scatterChart.setColors(viewmodel.getScatterchartdata()[1]);
		// simplification works with square histo
		scatterChart.setCount(viewmodel.getHistoSize() * viewmodel.getHistoSize());
		scatterChartPainter.update(scatterChart);

	}

	protected void updateGrid(int nb, Color col) {
		grid.setColor(col);
		grid.setStep(nb);
		gridPainter.update(grid);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			final JFrame jframe = new JFrame("Histo compute shader press space bar !!!!!!");
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

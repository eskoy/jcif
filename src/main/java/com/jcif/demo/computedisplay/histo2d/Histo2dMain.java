package com.jcif.demo.computedisplay.histo2d;

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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcif.demo.application.Histo2dComputeHandler;
import com.jcif.demo.computedisplay.histo2d.data.viewdata.BusinessModel;
import com.jcif.demo.computedisplay.histo2d.data.viewdata.ViewModel;
import com.jcif.demo.computedisplay.histo2d.view.ControlPanel;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLSharedContextInstance;
import com.jcif.opengl.glpainter.grid.Grid;
import com.jcif.opengl.glpainter.grid.GridPainter;
import com.jcif.opengl.glpainter.histo.Histo2d;
import com.jcif.opengl.glpainter.histo.Histo2dPainter;
import com.jcif.opengl.windowtoolkit.GLPainterController;
import com.jcif.opengl.windowtoolkit.WindowToolkitFactory;
import com.jogamp.nativewindow.AbstractGraphicsDevice;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

public class Histo2dMain {

	private final static Logger logger = LoggerFactory.getLogger(Histo2dMain.class);

	protected GLPainterController<GLCanvas> gLPainterController = WindowToolkitFactory.newSwingGLPainterController();

	protected GridPainter gridPainter = new GridPainter();

	protected Grid grid = new Grid();

	protected Histo2dPainter histo2dPainter = new Histo2dPainter();

	protected Histo2dPainter mousePainter = new Histo2dPainter();

	protected Histo2d mouseData = new Histo2d();

	protected Histo2d histo2d = new Histo2d();

	protected JPanel view;

	protected StateValue updatestateValue;

	protected BusinessModel businessModel;

	protected ViewModel viewModel;

	protected ControlPanel controlPanel = new ControlPanel();

	protected Histo2dComputeHandler histo2dComputeHandler;

	public JPanel getView() {
		return view;

	}

	public Histo2dMain() {

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

	public ByteBuffer[] computeHisto2d() {

		final GLDrawableFactory factory = GLDrawableFactory.getFactory(GLProfile.get(GLProfile.GL4bc));
		final AbstractGraphicsDevice device = GLSharedContextInstance.getInstance().getGLSharedAutoDrawable()
				.getNativeSurface().getGraphicsConfiguration().getScreen().getDevice();
		GLCapabilitiesImmutable caps = GLSharedContextInstance.getInstance().getGLCapabilities();
		final GLDrawable drawable = factory.createDummyDrawable(device, true, caps, null);
		drawable.setRealized(true);
		GLContext sharedContext = drawable
				.createContext(GLSharedContextInstance.getInstance().getGLSharedAutoDrawable().getContext());
		sharedContext.makeCurrent();

		int histox = viewModel.getHistoXSize();
		int histoy = viewModel.getHistoYSize();

		IntBuffer histodata = histo2dComputeHandler.compute(sharedContext.getGL().getGL4(),
				businessModel.getGpuValueY(), businessModel.getGpuValueX(), businessModel.getGpuValueindices(),
				businessModel.getDataNumber(), 0, 1f, histox, histoy);

		ByteBuffer[] data = histo2dComputeHandler.createHisto2dFromBuffer(histodata, histoy, histox);

		sharedContext.release();

		return data;
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

	public void modelToView() {

		final long time = System.currentTimeMillis();
		if (updatestateValue == null || updatestateValue == StateValue.DONE) {
			SwingWorker<ByteBuffer[], Void> worker = new SwingWorker<ByteBuffer[], Void>() {
				@Override
				public ByteBuffer[] doInBackground() {

					return computeHisto2d();
				}

				@Override
				public void done() {

					try {
						viewModel.setHistoBuffer(get());
						updateHisto2dPainter(viewModel);
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

					gLPainterController.repaint();
					updatestateValue = this.getState();

				}
			};

			worker.execute();

		}

	}

	protected void init() throws InvocationTargetException, InterruptedException {

		this.updateGrid(8, Color.CYAN);

		GLContext sharedContext = GLSharedContextInstance.getInstance().getGLSharedContext();
		logger.info("" + GLSharedContextInstance.getInstance().getGLDeviceProperties());

		sharedContext.makeCurrent();
		viewModel = new ViewModel();

		businessModel = new BusinessModel(1000000, sharedContext.getGL().getGL4());
		histo2dComputeHandler = new Histo2dComputeHandler(sharedContext.getGL().getGL4());
		sharedContext.release();

		gLPainterController.setSharedContext(sharedContext);
		gLPainterController.addPainter(histo2dPainter);
		gLPainterController.addPainter(gridPainter);
		gLPainterController.addPainter(mousePainter);

		view = new JPanel(new BorderLayout(1, 1));
		view.add(gLPainterController.getDisplayComponent(), BorderLayout.CENTER);
		view.add(controlPanel, BorderLayout.NORTH);
		this.addControlPanelListener();

		this.viewModel.setHistoXSize(controlPanel.getBINS_INIT());
		this.viewModel.setHistoYSize(controlPanel.getBINS_INIT());

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
				modelToView();

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	protected void addControlPanelListener() {
		controlPanel.getBrushButton().addActionListener(new ActionListener() {

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

						mousePainter.update(mouseData);

						if (viewModel.getHistoBuffer()[0] != null) {
							histo2dComputeHandler.filterPoint(viewModel.getHistoBuffer(), x, y, 0.01f);
							updateHisto2dPainter(viewModel);
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

		controlPanel.getBinXSlider().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (!controlPanel.getBinXSlider().getValueIsAdjusting()) {
					int bins = controlPanel.getBinXSlider().getValue();
					viewModel.setHistoXSize(bins);
					modelToView();
				}
			}
		});

		controlPanel.getBinYSlider().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (!controlPanel.getBinYSlider().getValueIsAdjusting()) {
					int bins = controlPanel.getBinYSlider().getValue();
					viewModel.setHistoYSize(bins);
					modelToView();
				}
			}
		});

		controlPanel.getHistosize().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (!controlPanel.getHistosize().getValueIsAdjusting()) {
					int value = controlPanel.getHistosize().getValue();
					histo2d.setPointSize(value);
					modelToView();
				}
			}
		});

	}

	protected void updateHisto2dPainter(ViewModel viewmodel) {

		histo2d.setXYs(viewmodel.getHistoBuffer()[0]);
		histo2d.setColors(viewmodel.getHistoBuffer()[1]);
		histo2d.setCounts(viewmodel.getHistoBuffer()[2]);
		histo2dPainter.update(histo2d);

	}

	protected void updateGrid(int nb, Color col) {

		grid.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 60));
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

			Histo2dMain ctrl = new Histo2dMain();
			jframe.getContentPane().add(ctrl.getView(), BorderLayout.CENTER);
			jframe.setSize(640, 480);
			jframe.setVisible(true);
		});
	}
}

package com.jcif.demo.computedisplay.histo3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcif.demo.computedisplay.histo3d.model.BusinessModel;
import com.jcif.demo.computedisplay.histo3d.model.ViewModel;
import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLSharedContextInstance;
import com.jcif.opengl.glpainter.cube.Cube;
import com.jcif.opengl.glpainter.cube.CubePainter;
import com.jcif.opengl.glpainter.histo.Histo3d;
import com.jcif.opengl.glpainter.histo.Histo3dPainter;
import com.jcif.opengl.windowtoolkit.GLPainterController;
import com.jcif.opengl.windowtoolkit.WindowToolkitFactory;
import com.jogamp.nativewindow.AbstractGraphicsDevice;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

public class Histo3dMain {

	private final static Logger logger = LoggerFactory.getLogger(Histo3dMain.class);

	protected GLPainterController<GLCanvas> gLPainterController = WindowToolkitFactory.newSwingGLPainterController();

	protected Histo3dPainter histo3dPainter = new Histo3dPainter();

	protected Histo3d histo3d = new Histo3d();

	protected CubePainter cubePainter = new CubePainter();

	public Cube cube = new Cube();

	protected JPanel view;

	protected StateValue updatestateValue;

	protected BusinessModel businessModel;

	protected ViewModel viewModel;

	protected JButton brushButton = new JButton("Start histo brush ");

	protected Histo3dComputeHandler histo3dComputeHandler;

	public JPanel getView() {
		return view;

	}

	public Histo3dMain() {

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
		final AbstractGraphicsDevice device = GLSharedContextInstance.getInstance().getGLSharedAutoDrawable()
				.getNativeSurface().getGraphicsConfiguration().getScreen().getDevice();
		GLCapabilitiesImmutable caps = GLSharedContextInstance.getInstance().getGLCapabilities();
		final GLDrawable drawable = factory.createDummyDrawable(device, true, caps, null);
		drawable.setRealized(true);
		GLContext sharedContext = drawable
				.createContext(GLSharedContextInstance.getInstance().getGLSharedAutoDrawable().getContext());
		sharedContext.makeCurrent();
		viewModel.setHistoSize(10);

		GLBuffer[] buffer = { businessModel.getGpuValueA(), businessModel.getGpuValueB(),
				businessModel.getGpuValueB() };

		float[] min = { 0, 0, 0 };
		float[] max = { 1, 1, 1 };
		int[] nbins = { 10, 10, 10 };

		IntBuffer histodata = histo3dComputeHandler.compute(sharedContext.getGL().getGL4(),
				businessModel.getGpuValueindices(), businessModel.getDataNumber(), buffer, min, max, nbins);

		ByteBuffer[] data = histo3dComputeHandler.createPointWithHisto(histodata, nbins);

		sharedContext.release();

		return data;
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
						viewModel.setHistoBuffer(get());
						updateChart(viewModel);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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

		GLContext sharedContext = GLSharedContextInstance.getInstance().getGLSharedContext();
		logger.info("" + GLSharedContextInstance.getInstance().getGLDeviceProperties());

		sharedContext.makeCurrent();
		viewModel = new ViewModel();

		cube.setColor(Color.green);
		cubePainter.update(cube);
		businessModel = new BusinessModel(100000, sharedContext.getGL().getGL4());
		histo3dComputeHandler = new Histo3dComputeHandler(sharedContext.getGL().getGL4());
		sharedContext.release();

		gLPainterController.setSharedContext(sharedContext);
		gLPainterController.addPainter(cubePainter);
		gLPainterController.addPainter(histo3dPainter);

		// this.updateChart(viewModel);

		view = new JPanel(new BorderLayout(1, 1));
		view.add(gLPainterController.getDisplayComponent(), BorderLayout.CENTER);
		view.add(brushButton, BorderLayout.NORTH);

		view.setBackground(Color.cyan);

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

		histo3d.setXYZs(viewmodel.getHistoBuffer()[0]);
		histo3d.setColors(viewmodel.getHistoBuffer()[1]);
		// simplification works with square histo
		histo3d.setCount(viewmodel.getHistoSize() * viewmodel.getHistoSize());
		histo3dPainter.update(histo3d);

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

			Histo3dMain ctrl = new Histo3dMain();
			jframe.getContentPane().add(ctrl.getView(), BorderLayout.CENTER);
			jframe.setSize(640, 480);
			jframe.setVisible(true);
		});
	}
}

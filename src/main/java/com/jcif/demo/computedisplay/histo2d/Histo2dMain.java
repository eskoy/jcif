package com.jcif.demo.computedisplay.histo2d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcif.awt.CallBack;
import com.jcif.awt.ColorUtil;
import com.jcif.demo.computedisplay.histo2d.chart.Histo2dModel;
import com.jcif.demo.computedisplay.histo2d.control.ControlCtrl;
import com.jcif.demo.computedisplay.histo2d.control.data.DataModel;
import com.jcif.demo.geo.PolyUtil;
import com.jcif.demo.geo.polyline.PolylineShapeFileUtil;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLPainter;
import com.jcif.opengl.GLSharedContextInstance;
import com.jcif.opengl.glpainter.grid.Grid;
import com.jcif.opengl.glpainter.grid.GridPainter;
import com.jcif.opengl.glpainter.histo.Histo2d;
import com.jcif.opengl.glpainter.histo.Histo2dPainter;
import com.jcif.opengl.glpainter.poly.Poly;
import com.jcif.opengl.glpainter.poly.PolyLinePainter;
import com.jcif.opengl.windowtoolkit.GLPainterController;
import com.jcif.opengl.windowtoolkit.WindowToolkitFactory;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.awt.GLCanvas;

public class Histo2dMain implements CallBack {

	public static String MAP_FILE_PATH = "10m_coastline.shp";

	private final static Logger logger = LoggerFactory.getLogger(Histo2dMain.class);

	protected GLPainterController<GLCanvas> gLPainterController = WindowToolkitFactory.newSwingGLPainterController();

	protected GridPainter gridPainter = new GridPainter();

	protected Grid grid = new Grid();

	protected Histo2dPainter histo2dPainter = new Histo2dPainter();

	protected Histo2dPainter mousePainter = new Histo2dPainter();

	protected GLPainter<?> worldcoaslinepainter;

	protected Histo2d mouseData = new Histo2d();

	protected Histo2d histo2d = new Histo2d();

	protected JPanel view;

	protected StateValue updatestateValue;

	protected DataModel dataModel = new DataModel();

	protected Histo2dModel viewModel;

	protected ControlCtrl controlCtrl;

	protected Histo2dComputeHandler histo2dComputeHandler;

	public JPanel getView() {
		return view;

	}

	public Histo2dMain() {

		try {
			laf();
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

		GLContext sharedContext = GLSharedContextInstance.getInstance().getGLSharedContext();
		sharedContext.makeCurrent();

		int histox = viewModel.getHistoXSize();
		int histoy = viewModel.getHistoYSize();

		IntBuffer histodata = histo2dComputeHandler.compute(sharedContext.getGL().getGL4(), dataModel.getGpuValueY(),
				dataModel.getGpuValueX(), dataModel.getGpuValueindices(), dataModel.getDataNumber(), 0, 1f, histox,
				histoy);

		ByteBuffer[] data = histo2dComputeHandler.createHisto2dFromBuffer(histodata, histoy, histox);

		sharedContext.release();

		return data;
	}

	protected GLPainter<?> createWorldCoaslineLayer() {

		List<ByteBuffer> polygonBuffer = new ArrayList<>();
		List<Poly> polygonList = new ArrayList<>();
		PolylineShapeFileUtil.loadPolygon(polygonBuffer, MAP_FILE_PATH);

		PolyUtil.normalize(polygonBuffer);

		int paletteSize = 2;
		Color[] color = ColorUtil.computeRandomColor(paletteSize);

		int i = 0;
		for (ByteBuffer current : polygonBuffer) {
			Poly poly = new Poly();
			poly.setColor(color[i++ % paletteSize]);
			poly.setSize(poly.getSize());
			poly.setPolygon(current);
			polygonList.add(poly);
		}

		PolyLinePainter painter = new PolyLinePainter();
		painter.update(polygonList);

		return painter;
	}

	protected void laf() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}
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

	@Override
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
		viewModel = new Histo2dModel();

		histo2dComputeHandler = new Histo2dComputeHandler(sharedContext.getGL().getGL4());
		sharedContext.release();

		worldcoaslinepainter = createWorldCoaslineLayer();
		// compose opengl layer

		gLPainterController.getBackGround().setColor(Color.WHITE);
		gLPainterController.setSharedContext(sharedContext);
		gLPainterController.addPainter(worldcoaslinepainter);
		gLPainterController.addPainter(gridPainter);
		gLPainterController.addPainter(histo2dPainter);
		gLPainterController.addPainter(mousePainter);

		// construct main view

		controlCtrl = new ControlCtrl(this, viewModel, dataModel);

		// Create a split pane with the two scroll panes in it.

		JScrollPane controlPane = new JScrollPane(controlCtrl.getView());
		JScrollPane histoPane = new JScrollPane(gLPainterController.getDisplayComponent());

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, controlPane, histoPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(280);

		// Provide minimum sizes for the two components in the split pane

		controlPane.setMinimumSize(new Dimension(280, 500));
		histoPane.setMinimumSize(new Dimension(500, 500));

		view = new JPanel(new BorderLayout(1, 1));
		view.add(splitPane);

		this.addControlPanelListener();

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
		controlCtrl.getHistoBrushButton().addActionListener(new ActionListener() {

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

	}

	protected void updateHisto2dPainter(Histo2dModel viewmodel) {

		histo2d.setXYs(viewmodel.getHistoBuffer()[0]);
		histo2d.setColors(viewmodel.getHistoBuffer()[1]);
		histo2d.setCounts(viewmodel.getHistoBuffer()[2]);
		histo2d.setPointSize(viewmodel.getPointSize());
		histo2dPainter.update(histo2d);

	}

	protected void updateGrid(int nb, Color col) {

		grid.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 60));
		grid.setStep(nb);
		gridPainter.update(grid);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			final JFrame jframe = new JFrame("Histo compute shader use control panel to display it !!!!!!");
			jframe.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowevent) {
					jframe.dispose();
					System.exit(0);
				}
			});

			Histo2dMain ctrl = new Histo2dMain();
			jframe.getContentPane().add(ctrl.getView(), BorderLayout.CENTER);
			jframe.setSize(1280, 720);
			jframe.setVisible(true);
		});
	}
}

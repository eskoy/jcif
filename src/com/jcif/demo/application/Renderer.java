package com.jcif.demo.application;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcif.opengl.GLDeviceProperties;
import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.glpainter.grid.Grid;
import com.jcif.opengl.glpainter.grid.GridPainter;
import com.jcif.opengl.glpainter.scatterchart.ScatterChart;
import com.jcif.opengl.glpainter.scatterchart.ScatterChartPainter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class Renderer implements GLEventListener {
	private final static Logger logger = LoggerFactory.getLogger(Renderer.class);

	private volatile boolean stopped = false;
	private volatile boolean dirty = true;

	private final GLWindow glWindow;
	private int width = 100;
	private int height = 100;
	private Keyboard keyboard;
	private ByteBuffer[] bbHisto;
	private GlBuffer gpuValueA;
	private GlBuffer gpuValueB;
	private GlBuffer gpuValueindices;
	private Histo2dComputeHandler histo2dComputeHandler;
	private GridPainter gridPainter;
	public Grid grid = new Grid();
	private ScatterChartPainter scatterChartPainter;
	private int size;
	private int histosize;

	public Renderer(GLWindow glWindow, Keyboard keyboard) {
		this.glWindow = glWindow;
		this.keyboard = keyboard;
	}

	public void stop() {
		stopped = true;
	}

	public void redraw() {
		dirty = true;
	}

	public void run() {
		Renderer.this.glWindow.display();

		while (!stopped) {
			if (dirty) {

				Renderer.this.glWindow.display();

				dirty = true;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					logger.warn(e.getMessage(), e);
				}
			}

			stopped = keyboard.isPressed(KeyEvent.VK_ESCAPE);
		}

		Renderer.this.glWindow.destroy();
	}

	@Override
	public void init(GLAutoDrawable drawable) {

		grid.setStep(10);
		GL4 gl = drawable.getGL().getGL4();

		logger.info("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());

		GLDeviceProperties device = new GLDeviceProperties(gl);

		logger.info("" + device);

		size = 10000;
		gridPainter = new GridPainter();
		gridPainter.init(gl);
		scatterChartPainter = new ScatterChartPainter();
		scatterChartPainter.init(gl);
		histo2dComputeHandler = new Histo2dComputeHandler(gl);
		gpuValueA = GlBufferFactory.hostoGpuData(histo2dComputeHandler.createNewData(size), gl);
		gpuValueB = GlBufferFactory.hostoGpuData(histo2dComputeHandler.createNewData(size), gl);
		gpuValueindices = GlBufferFactory.hostoGpuData(histo2dComputeHandler.createIndices(size), gl);

	}

	@Override
	public void display(GLAutoDrawable drawable) {

		GL4 gl = drawable.getGL().getGL4();

		if (keyboard.isReleased(KeyEvent.VK_SPACE)) {

			histosize = 40;
			gpuValueindices.bind(gl);
			gpuValueindices.write(gl, 0, histo2dComputeHandler.updateIndices(size, 0), size);
			gpuValueindices.release(gl);

			Random rand = new Random();

			IntBuffer histodata = histo2dComputeHandler.compute(gl, gpuValueA, gpuValueB, gpuValueindices, size, 0,
					rand.nextFloat(), histosize);
			bbHisto = histo2dComputeHandler.createPointWithHisto(histodata, histosize);
			ScatterChart chart = new ScatterChart();
			chart.setXYs(bbHisto[0]);
			chart.setColors(bbHisto[1]);
			chart.setCount(histosize * histosize);
			scatterChartPainter.update(chart);

			grid.setStep(rand.nextInt(8));
			grid.setColor(Color.GREEN);
			gridPainter.update(grid);

		}

		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);

		gl.glViewport(0, 0, width, height);

		// Clear screen
		gl.glClearColor(0.1f, 0.0f, 0.1f, 1f);
		gl.glClear(GL2ES2.GL_COLOR_BUFFER_BIT);

		gridPainter.paint(gl);
		scatterChartPainter.paint(gl);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {

		this.width = w;
		this.height = h;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

}

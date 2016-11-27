package com.jcif.demo.application;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

public class Main {

	public static void main(String[] args) {
		Main test = new Main();

		test.run();
	}

	private final Renderer renderer;

	public Main() {
		GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL4ES3));

		caps.setDoubleBuffered(true);

		GLWindow glWindow = GLWindow.create(caps);

		glWindow.setTitle("Histo compute shader press space bar !!!!!!");

		glWindow.setSize(800, 800);

		glWindow.setFullscreen(false);
		glWindow.setUndecorated(false);
		glWindow.setPointerVisible(true);
		glWindow.setVisible(true);

		Keyboard keyboard = new Keyboard();
		glWindow.addKeyListener(keyboard);

		renderer = new Renderer(glWindow, keyboard);

		glWindow.addGLEventListener(renderer);
	}

	public void run() {
		renderer.run();
	}

}

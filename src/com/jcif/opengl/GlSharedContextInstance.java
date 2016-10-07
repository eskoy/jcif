package com.jcif.opengl;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;

public class GlSharedContextInstance {

	private static GlSharedContextInstance instance = null;

	private GLAutoDrawable sharedDrawable;

	private GLDeviceProperties gLDeviceProperties;

	private GlSharedContextInstance() {
		GLProfile profile = GLProfile.get(GLProfile.GL4bc);
		GLCapabilities caps = new GLCapabilities(profile);
		caps.setDoubleBuffered(true);
		caps.setAlphaBits(24);
		GLDrawableFactory factory = GLDrawableFactory.getFactory(profile);
		this.sharedDrawable = factory.createDummyAutoDrawable(null, true, caps, null);
		this.sharedDrawable.display();
		this.getGLSharedContext().makeCurrent();
		this.gLDeviceProperties = new GLDeviceProperties(getGLSharedContext().getGL().getGL4());
		this.getGLSharedContext().release();
	}

	public GLDeviceProperties getGLDeviceProperties() {
		return this.gLDeviceProperties;
	}

	public static GlSharedContextInstance getInstance() {
		if (instance == null) {
			instance = new GlSharedContextInstance();
		}
		return instance;
	}

	public GLCapabilitiesImmutable getGLCapabilities() {
		return instance.sharedDrawable.getRequestedGLCapabilities();
	}

	public GLAutoDrawable getGLSharedAutoDrawable() {
		return this.sharedDrawable;
	}

	public GLContext getGLSharedContext() {
		return this.sharedDrawable.getContext();
	}
}

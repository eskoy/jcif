package com.jcif.opengl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL4;

public class GlShaderProgram extends GLProgram {

	protected final static Logger logger = LoggerFactory.getLogger(GlShaderProgram.class);

	protected GLShader vertShader;

	protected GLShader fragShader;

	public GlShaderProgram(GL4 gl, String vertex, String fragment) {
		super(gl);
		vertShader = new GLShader(GL4.GL_VERTEX_SHADER, vertex, gl);
		fragShader = new GLShader(GL4.GL_FRAGMENT_SHADER, fragment, gl);
		gl.glAttachShader(this.getId(), vertShader.getId());
		gl.glAttachShader(this.getId(), fragShader.getId());
		gl.glLinkProgram(this.getId());
	}

	@Override
	public void dispose(GL4 gl) {
		gl.glDetachShader(this.getId(), vertShader.getId());
		gl.glDeleteShader(vertShader.getId());
		gl.glDetachShader(this.getId(), fragShader.getId());
		gl.glDeleteShader(fragShader.getId());
		super.dispose(gl);
	}

}

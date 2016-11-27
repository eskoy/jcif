package com.jcif.opengl.glpainter;

import com.jcif.opengl.GLSLProvider;

public enum GLSL implements GLSLProvider {

	ColorFragment(String.join(NEW_LINE, //
			"varying vec4 varying_Color;", //
			"void main (void) {", //
			"    gl_FragColor = varying_Color;", //
			"}"));

	protected String shader;

	private GLSL(String shader) {
		this.shader = shader;
	}

	@Override
	public String getGLSL() {

		return shader;
	}

}

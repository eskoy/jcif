package com.jcif.opengl.glpainter.polygon;

import com.jcif.opengl.GLSLProvider;
import com.jcif.opengl.glpainter.GLSL;

public enum GLSLPOLYGON implements GLSLProvider {

	Vertex(String.join(NEW_LINE, //
			"in  vec4 inVertex;", //
			"uniform vec4 color;", //
			"out vec4 varying_Color;", //
			"void main(void) {", //
			"gl_Position =   inVertex;", //
			"varying_Color = color;", //
			"}")) //

	, Fragment(GLSL.ColorFragment.getGLSL());

	protected String shader;

	private GLSLPOLYGON(String shader) {
		this.shader = shader;
	}

	@Override
	public String getGLSL() {

		return shader;
	}

}

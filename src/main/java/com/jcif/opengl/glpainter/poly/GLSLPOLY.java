package com.jcif.opengl.glpainter.poly;

import com.jcif.opengl.GLSLProvider;
import com.jcif.opengl.glpainter.GLSL;

public enum GLSLPOLY implements GLSLProvider {

	Vertex(String.join(NEW_LINE, //
			"layout (location=0) in  vec4 inVertex;", //
			"layout (location=0) uniform vec4 color;", //
			"out vec4 varying_Color;", //
			"void main(void) {", //
			"gl_Position =   inVertex;", //
			"varying_Color = color;", //
			"}")) //

	, Fragment(GLSL.ColorFragment.getGLSL());

	protected String shader;

	private GLSLPOLY(String shader) {
		this.shader = shader;
	}

	@Override
	public String getGLSL() {

		return shader;
	}

}

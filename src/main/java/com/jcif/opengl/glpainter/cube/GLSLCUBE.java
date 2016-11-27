package com.jcif.opengl.glpainter.cube;

import com.jcif.opengl.GLSLProvider;
import com.jcif.opengl.glpainter.GLSL;

public enum GLSLCUBE implements GLSLProvider {

	Vertex(String.join(NEW_LINE, //
			"in vec3  attribute_Position;", //
			"uniform mat4 viewMatrix, projMatrix;", //
			"uniform vec4 color;", //
			"out vec4 varying_Color;", //
			"void main(void) {", //
			"gl_PointSize = 5f;", //
			"gl_Position = projMatrix * viewMatrix *  vec4(attribute_Position.x,attribute_Position.y, attribute_Position.z, 1.0);", //
			"varying_Color = color;", //
			"}")) //

	, Fragment(GLSL.ColorFragment.getGLSL());

	protected String shader;

	private GLSLCUBE(String shader) {
		this.shader = shader;
	}

	@Override
	public String getGLSL() {

		return shader;
	}

}

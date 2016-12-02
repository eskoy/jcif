package com.jcif.opengl.glpainter.point;

import com.jcif.opengl.GLSLProvider;

public enum GLSLPOINT implements GLSLProvider {

	Vertex(String.join(NEW_LINE, //
			"in vec2  attribute_Position;", //
			"in vec4  attribute_Color;", //
			"uniform float pointSize;", //
			"out vec4 varying_Color;", //
			"void main(void) {", //
			" mat4 uniform_Projection = mat4(1);", //
			" gl_PointSize = pointSize;", //
			"//  gl_Position = uniform_Projection * attribute_Position;", //
			"//  gl_Position =  uniform_Projection * vec4(attribute_Position.x, attribute_Position.y, 0, 1);", //
			"gl_Position = vec4(attribute_Position.x,attribute_Position.y, 0, 1.0);", //
			"varying_Color = attribute_Color;", //
			"}")) //

	,

	Fragment(String.join(NEW_LINE, //
			"varying vec4 varying_Color;", //
			"void main (void) {", //
			" // float alpha = 1 - smoothstep(0, 1, 2 * distance(gl_PointCoord.st, vec2(0.5, 0.5)));", //
			" // gl_FragColor = vec4(varying_Color.rgb, varying_Color.a * alpha);", //
			"    gl_FragColor = varying_Color;", //
			"}"));

	protected String shader;

	private GLSLPOINT(String shader) {
		this.shader = shader;
	}

	@Override
	public String getGLSL() {

		return shader;
	}

}

package com.jcif.opengl.glpainter.histo;

import com.jcif.opengl.GLSLProvider;

public enum GLSLHISTO implements GLSLProvider {

	Vertex2d(String.join(NEW_LINE, //
			"layout (location=0) in vec2  attribute_Position;", //
			"layout (location=1) in vec4  attribute_Color;", //
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

	, Vertex3d(String.join(NEW_LINE, //
			"layout (location=0) in vec3  attribute_Position;", //
			"layout (location=1) in vec4  attribute_Color;", //
			"out vec4 varying_Color;", //
			"vec4 pos;", //
			"uniform float pointSize;", //
			"uniform mat4 viewMatrix, projMatrix;", //
			"void main(void)", //
			"{", //
			"	gl_PointSize = pointSize;", //
			"   varying_Color = attribute_Color;", //
			"   gl_Position = projMatrix * viewMatrix *  vec4(attribute_Position.x,attribute_Position.y, attribute_Position.z, 1.0);", //
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

	private GLSLHISTO(String shader) {
		this.shader = shader;
	}

	@Override
	public String getGLSL() {

		return shader;
	}

}

package com.jcif.opengl.glpainter.polygon;

import java.util.List;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLBufferFactory.GL_TYPE;
import com.jcif.opengl.GLBufferFactory.GL_USAGE;
import com.jcif.opengl.GLPainter;
import com.jcif.opengl.GLShaderProgram;
import com.jcif.opengl.GLUtil;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL4;

public class PolygonsPainter implements GLPainter<List<Polygon>> {

	protected List<Polygon> polygons;

	protected GLShaderProgram polygonProgram;

	public PolygonsPainter() {

	}

	@Override
	public void init(GL4 gl) {
		// polygonProgram = new GLShaderProgram(gl,
		// GLUtil.loadAsText(getClass(), "Polygon.vert"),
		// GLUtil.loadAsText(getClass(), "Polygon.frag"));

		polygonProgram = new GLShaderProgram(gl, GLSLPOLYGON.Vertex, GLSLPOLYGON.Fragment);

	}

	@Override
	public void update(List<Polygon> t) {
		polygons = t;

	}

	@Override
	public void paint(GL4 gl, int... viewport) {

		polygonProgram.beginUse(gl);
		for (Polygon polygon : polygons) {

			GLBuffer gpuLines = GLBufferFactory.newGLBuffer(gl, GL_TYPE.ARRAY_BUFFER, GL_USAGE.DYNAMIC_DRAW);
			gpuLines.bind(gl);
			gpuLines.allocate(gl, polygon.getPolygon(), polygon.getPolygon().capacity());
			gpuLines.release(gl);
			// gl.glEnable(GL4.GL_PROGRAM_POINT_SIZE);
			// shapesLinesProgram.setUniform(gl, "pointSize",
			// polygon.getSize());

			gl.glLineWidth(polygon.getSize());

			gl.glEnableVertexAttribArray(0);

			gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpuLines.getId());
			// Associate Vertex attribute 0 with the last bound VBO
			gl.glVertexAttribPointer(0 /* the vertex attrib ute */, 2, GL4.GL_FLOAT, false /* normalized? */,
					2 * 4 /* stride */, 0 /* The bound VBO data offset */);

			polygonProgram.setUniform(gl, "color", GLUtil.colorAsVec4(polygon.getColor()));

			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_LINE);
			gl.glDrawArrays(GL2.GL_POLYGON, 0, polygon.getPolygon().capacity() / 4 / 2);

			// gl.glDrawArrays(GL4.GL_LINES, 0, bb.capacity() / 4 / 2);

			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_FILL);
			gl.glDisableVertexAttribArray(0);

		}
		polygonProgram.endUse(gl);
	}
}

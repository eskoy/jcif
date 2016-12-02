package com.jcif.opengl.glpainter.point;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLPainter;
import com.jcif.opengl.GLShaderProgram;
import com.jogamp.opengl.GL4;

public class PointPainter implements GLPainter<Points> {

	protected GLShaderProgram pointProgram;

	protected Points points = new Points();

	public PointPainter() {

	}

	@Override
	public void update(Points t) {
		this.points = t;

	}

	@Override
	public void init(GL4 gl) {
		// this.pointProgram = new GLShaderProgram(gl,
		// GLUtil.loadAsText(getClass(), "Point.vert"),
		// GLUtil.loadAsText(getClass(), "Point.frag"));

		this.pointProgram = new GLShaderProgram(gl, GLSLPOINT.Vertex, GLSLPOINT.Fragment);

	}

	@Override
	public void paint(GL4 gl, int... viewport) {
		if (this.points.getCount() > 0) {
			this.pointProgram.beginUse(gl);
			gl.glEnable(GL4.GL_PROGRAM_POINT_SIZE);
			gl.glEnableVertexAttribArray(0);
			gl.glEnableVertexAttribArray(1);

			this.pointProgram.setUniform(gl, "pointSize", this.points.getPointSize());

			GLBuffer gpudata = GLBufferFactory.hostoGpuData(this.points.getXYs(), gl);
			gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpudata.getId());
			// Associate Vertex attribute 0 with the last bound VBO
			gl.glVertexAttribPointer(0 /* the vertex attribute */, 2, GL4.GL_FLOAT, false /* normalized? */,
					0 /* stride */, 0 /* The bound VBO data offset */);

			GLBuffer gpuColor = GLBufferFactory.hostoGpuData(this.points.getColors(), gl);
			gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpuColor.getId());
			// Associate Vertex attribute 0 with the last bound VBO
			gl.glVertexAttribPointer(1 /* the vertex attribute */, 4, GL4.GL_FLOAT, false /* normalized? */,
					0 /* stride */, 0 /* The bound VBO data offset */);

			gl.glDrawArrays(GL4.GL_POINTS, 0, this.points.getCount());

			gl.glDisableVertexAttribArray(0);
			gl.glDisableVertexAttribArray(1);

			this.pointProgram.endUse(gl);
		}

	}
}

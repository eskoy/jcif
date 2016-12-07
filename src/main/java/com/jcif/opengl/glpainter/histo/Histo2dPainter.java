package com.jcif.opengl.glpainter.histo;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLPainter;
import com.jcif.opengl.GLShaderProgram;
import com.jogamp.opengl.GL4;

public class Histo2dPainter implements GLPainter<Histo2d> {

	protected GLShaderProgram program;

	protected Histo2d histo2d = new Histo2d();

	public Histo2dPainter() {

	}

	@Override
	public void update(Histo2d t) {
		this.histo2d = t;

	}

	@Override
	public void init(GL4 gl) {
		// this.pointProgram = new GLShaderProgram(gl,
		// GLUtil.loadAsText(getClass(), "Point.vert"),
		// GLUtil.loadAsText(getClass(), "Point.frag"));

		this.program = new GLShaderProgram(gl, GLSLHISTO.Vertex2d, GLSLHISTO.Fragment);

	}

	@Override
	public void paint(GL4 gl, int... viewport) {
		if (this.histo2d.getCounts() != null) {
			this.program.beginUse(gl);
			gl.glEnable(GL4.GL_PROGRAM_POINT_SIZE);
			gl.glEnableVertexAttribArray(0);
			gl.glEnableVertexAttribArray(1);

			this.program.setUniform(gl, "pointSize", this.histo2d.getPointSize());

			GLBuffer gpudata = GLBufferFactory.hostoGpuData(this.histo2d.getXYs(), gl);
			gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpudata.getId());
			// Associate Vertex attribute 0 with the last bound VBO
			gl.glVertexAttribPointer(0 /* the vertex attribute */, 2, GL4.GL_FLOAT, false /* normalized? */,
					0 /* stride */, 0 /* The bound VBO data offset */);

			GLBuffer gpuColor = GLBufferFactory.hostoGpuData(this.histo2d.getColors(), gl);
			gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpuColor.getId());
			// Associate Vertex attribute 0 with the last bound VBO
			gl.glVertexAttribPointer(1 /* the vertex attribute */, 4, GL4.GL_FLOAT, false /* normalized? */,
					0 /* stride */, 0 /* The bound VBO data offset */);

			gl.glDrawArrays(GL4.GL_POINTS, 0, this.histo2d.getCounts().capacity() / Float.BYTES);

			gl.glDisableVertexAttribArray(0);
			gl.glDisableVertexAttribArray(1);

			this.program.endUse(gl);
		}

	}
}

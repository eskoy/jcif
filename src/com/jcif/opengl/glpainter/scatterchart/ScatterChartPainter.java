package com.jcif.opengl.glpainter.scatterchart;

import com.jcif.opengl.GLPainter;
import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.GlShaderProgram;
import com.jcif.opengl.GlUtil;
import com.jogamp.opengl.GL4;

public class ScatterChartPainter implements GLPainter<ScatterChart> {

	protected GlShaderProgram pointProgram;

	protected ScatterChart scatterChart = new ScatterChart();

	public ScatterChartPainter() {

	}

	@Override
	public void update(ScatterChart t) {
		this.scatterChart = t;

	}

	@Override
	public void init(GL4 gl) {
		this.pointProgram = new GlShaderProgram(gl, GlUtil.loadAsText(getClass(), "ScatterChart.vert"),
				GlUtil.loadAsText(getClass(), "ScatterChart.frag"));

	}

	@Override
	public void paint(GL4 gl, int... viewport) {
		if (this.scatterChart.getCount() > 0) {
			this.pointProgram.beginUse(gl);
			gl.glEnable(GL4.GL_PROGRAM_POINT_SIZE);
			gl.glEnableVertexAttribArray(0);
			gl.glEnableVertexAttribArray(1);

			GlBuffer gpudata = GlBufferFactory.hostoGpuData(this.scatterChart.getXYs(), gl);
			gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpudata.getId());
			// Associate Vertex attribute 0 with the last bound VBO
			gl.glVertexAttribPointer(0 /* the vertex attribute */, 2, GL4.GL_FLOAT, false /* normalized? */,
					0 /* stride */, 0 /* The bound VBO data offset */);

			GlBuffer gpuColor = GlBufferFactory.hostoGpuData(this.scatterChart.getColors(), gl);
			gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpuColor.getId());
			// Associate Vertex attribute 0 with the last bound VBO
			gl.glVertexAttribPointer(1 /* the vertex attribute */, 4, GL4.GL_FLOAT, false /* normalized? */,
					0 /* stride */, 0 /* The bound VBO data offset */);

			gl.glDrawArrays(GL4.GL_POINTS, 0, this.scatterChart.getCount());

			gl.glDisableVertexAttribArray(0);
			gl.glDisableVertexAttribArray(1);

			this.pointProgram.endUse(gl);
		}

	}
}

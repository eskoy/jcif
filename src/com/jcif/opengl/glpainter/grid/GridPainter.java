package com.jcif.opengl.glpainter.grid;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.jcif.opengl.GLPainter;
import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.GlShaderProgram;
import com.jcif.opengl.GlUtil;
import com.jcif.opengl.GlBufferFactory.TYPE;
import com.jcif.opengl.GlBufferFactory.USAGE;
import com.jogamp.opengl.GL4;

public class GridPainter implements GLPainter<Grid> {

	protected GlShaderProgram gridProgram;

	public GridPainter(GL4 gl) {

		gridProgram = new GlShaderProgram(gl, GlUtil.loadAsText(getClass(), "Grid.vert"),
				GlUtil.loadAsText(getClass(), "Grid.frag"));
	}

	protected ByteBuffer createUniformGrid(int nb) {

		ByteBuffer bb = GlBufferFactory.allocate(nb * 8 * Float.BYTES);
		FloatBuffer floatbuffervalues = bb.asFloatBuffer();

		float factor = 2f / nb;
		// inter x
		for (int i = 0; i < nb; i++) {

			floatbuffervalues.put(i * factor - 1f);
			floatbuffervalues.put(-1f);

			floatbuffervalues.put(i * factor - 1f);
			floatbuffervalues.put(1f);

			floatbuffervalues.put(-1f);
			floatbuffervalues.put(i * factor - 1f);

			floatbuffervalues.put(1f);
			floatbuffervalues.put(i * factor - 1f);

		}

		floatbuffervalues.position(0);
		return bb;
	}

	@Override
	public void paint(GL4 gl, Grid object, int... viewport) {
		GlBuffer gpuGrid = GlBufferFactory.newGLBuffer(gl, TYPE.ARRAY_BUFFER, USAGE.STATIC_DRAW);
		gpuGrid.bind(gl);
		ByteBuffer grid = createUniformGrid(object.getStep());

		gpuGrid.allocate(gl, grid, grid.capacity());
		gpuGrid.release(gl);

		gridProgram.beginUse(gl);

		gl.glEnable(GL4.GL_PROGRAM_POINT_SIZE);

		gl.glEnableVertexAttribArray(0);

		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpuGrid.getId());
		// Associate Vertex attribute 0 with the last bound VBO
		gl.glVertexAttribPointer(0 /* the vertex attrib ute */, 2, GL4.GL_FLOAT, false /* normalized? */,
				0 /* stride */, 0 /* The bound VBO data offset */);

		gridProgram.setUniform(gl, "color", GlUtil.colorAsVec4(object.getColor()));

		gl.glDrawArrays(GL4.GL_LINES, 0, object.getStep() * 4);

		gl.glDisableVertexAttribArray(0);

		gridProgram.endUse(gl);

	}
}

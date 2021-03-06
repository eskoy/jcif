package com.jcif.opengl.glpainter.grid;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLBufferFactory.GL_TYPE;
import com.jcif.opengl.GLBufferFactory.GL_USAGE;
import com.jcif.opengl.GLPainter;
import com.jcif.opengl.GLShaderProgram;
import com.jcif.opengl.GLUtil;
import com.jogamp.opengl.GL4;

public class GridPainter implements GLPainter<Grid> {

	protected Grid grid = new Grid();

	protected GLShaderProgram gridProgram;

	public GridPainter() {

	}

	@Override
	public void init(GL4 gl) {
		// gridProgram = new GLShaderProgram(gl, GLUtil.loadAsText(getClass(),
		// "Grid.vert"),
		// GLUtil.loadAsText(getClass(), "Grid.frag"));

		gridProgram = new GLShaderProgram(gl, GLSLGRID.Vertex, GLSLGRID.Fragment);
	}

	protected ByteBuffer createUniformGrid(int nb) {

		ByteBuffer bb = GLBufferFactory.allocate(nb * 8 * Float.BYTES);
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
	public void update(Grid t) {
		this.grid = t;

	}

	@Override
	public void paint(GL4 gl, int... viewport) {
		GLBuffer gpuGrid = GLBufferFactory.newGLBuffer(gl, GL_TYPE.ARRAY_BUFFER, GL_USAGE.STATIC_DRAW);
		gpuGrid.bind(gl);
		ByteBuffer grid = createUniformGrid(this.grid.getStep());

		gpuGrid.allocate(gl, grid, grid.capacity());
		gpuGrid.release(gl);

		gridProgram.beginUse(gl);

		gl.glEnable(GL4.GL_PROGRAM_POINT_SIZE);

		gl.glEnableVertexAttribArray(0);

		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpuGrid.getId());
		// Associate Vertex attribute 0 with the last bound VBO
		gl.glVertexAttribPointer(0 /* the vertex attrib ute */, 2, GL4.GL_FLOAT, false /* normalized? */,
				0 /* stride */, 0 /* The bound VBO data offset */);

		gridProgram.setUniform(gl, "color", GLUtil.colorAsVec4(this.grid.getColor()));

		gl.glDrawArrays(GL4.GL_LINES, 0, this.grid.getStep() * 4);

		gl.glDisableVertexAttribArray(0);

		gridProgram.endUse(gl);

	}
}

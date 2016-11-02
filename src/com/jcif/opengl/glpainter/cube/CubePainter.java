package com.jcif.opengl.glpainter.cube;

import java.nio.ByteBuffer;

import com.jcif.opengl.GLPainter;
import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.GlBufferFactory.GL_TYPE;
import com.jcif.opengl.GlBufferFactory.GL_USAGE;
import com.jcif.opengl.GlShaderProgram;
import com.jcif.opengl.GlUtil;
import com.jogamp.opengl.GL4;

public class CubePainter implements GLPainter<Cube> {

	// storage for Matrices
	float projMatrix[] = new float[16];
	float viewMatrix[] = new float[16];

	protected Cube cube = new Cube();

	protected GlShaderProgram cubeProgram;

	private int projMatrixLoc;

	private int viewMatrixLoc;

	public CubePainter() {

		this.projMatrix = buildProjectionMatrix(53.13f, 0.75f, 1.0f, 30.0f, this.projMatrix);
	}

	@Override
	public void init(GL4 gl) {
		cubeProgram = new GlShaderProgram(gl, GlUtil.loadAsText(getClass(), "Cube.vert"),
				GlUtil.loadAsText(getClass(), "Cube.frag"));

	}

	// sets the square matrix mat to the identity matrix,
	// size refers to the number of rows (or columns)
	void setIdentityMatrix(float[] mat, int size) {

		// fill matrix with 0s
		for (int i = 0; i < size * size; ++i)
			mat[i] = 0.0f;

		// fill diagonal with 1s
		for (int i = 0; i < size; ++i)
			mat[i + i * size] = 1.0f;
	}

	// ------------------
	// VECTOR STUFF
	//

	// res = a cross b;
	void crossProduct(float a[], float b[], float res[]) {

		res[0] = a[1] * b[2] - b[1] * a[2];
		res[1] = a[2] * b[0] - b[2] * a[0];
		res[2] = a[0] * b[1] - b[0] * a[1];
	}

	// Normalize a vec3
	void normalize(float a[]) {

		float mag = (float) Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);

		a[0] /= mag;
		a[1] /= mag;
		a[2] /= mag;
	}

	float[] buildProjectionMatrix(float fov, float ratio, float nearP, float farP, float[] projMatrix) {

		float f = 1.0f / (float) Math.tan(fov * (Math.PI / 360.0));

		setIdentityMatrix(projMatrix, 4);

		projMatrix[0] = f / ratio;
		projMatrix[1 * 4 + 1] = f;
		projMatrix[2 * 4 + 2] = (farP + nearP) / (nearP - farP);
		projMatrix[3 * 4 + 2] = (2.0f * farP * nearP) / (nearP - farP);
		projMatrix[2 * 4 + 3] = -1.0f;
		projMatrix[3 * 4 + 3] = 0.0f;

		return projMatrix;
	}

	// ------------------
	// View Matrix
	//
	// note: it assumes the camera is not tilted,
	// i.e. a vertical up vector (remmeber gluLookAt?)
	//

	float[] setCamera(float posX, float posY, float posZ, float lookAtX, float lookAtY, float lookAtZ,
			float[] viewMatrix) {

		float[] dir = new float[3];
		float[] right = new float[3];
		float[] up = new float[3];

		up[0] = 0.0f;
		up[1] = 1.0f;
		up[2] = 0.0f;

		dir[0] = (lookAtX - posX);
		dir[1] = (lookAtY - posY);
		dir[2] = (lookAtZ - posZ);
		normalize(dir);

		crossProduct(dir, up, right);
		normalize(right);

		crossProduct(right, dir, up);
		normalize(up);

		float[] aux = new float[16];

		viewMatrix[0] = right[0];
		viewMatrix[4] = right[1];
		viewMatrix[8] = right[2];
		viewMatrix[12] = 0.0f;

		viewMatrix[1] = up[0];
		viewMatrix[5] = up[1];
		viewMatrix[9] = up[2];
		viewMatrix[13] = 0.0f;

		viewMatrix[2] = -dir[0];
		viewMatrix[6] = -dir[1];
		viewMatrix[10] = -dir[2];
		viewMatrix[14] = 0.0f;

		viewMatrix[3] = 0.0f;
		viewMatrix[7] = 0.0f;
		viewMatrix[11] = 0.0f;
		viewMatrix[15] = 1.0f;

		setTranslationMatrix(aux, -posX, -posY, -posZ);

		multMatrix(viewMatrix, aux);

		return viewMatrix;
	}

	// Defines a transformation matrix mat with a translation
	void setTranslationMatrix(float[] mat, float x, float y, float z) {

		setIdentityMatrix(mat, 4);
		mat[12] = x;
		mat[13] = y;
		mat[14] = z;
	}

	//
	// a = a * b;
	//
	void multMatrix(float[] a, float[] b) {

		float[] res = new float[16];

		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				res[j * 4 + i] = 0.0f;
				for (int k = 0; k < 4; ++k) {
					res[j * 4 + i] += a[k * 4 + i] * b[j * 4 + k];
				}
			}
		}
		System.arraycopy(res, 0, a, 0, 16);
	}

	protected ByteBuffer createCube() {

		float[] cubeArray = new float[] { -1.0f, -1.0f, -1.0f, // triangle 1 :
																// begin
				-1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, // triangle 1 : end
				1.0f, 1.0f, -1.0f, // triangle 2 : begin
				-1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, // triangle 2 : end
				1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f,
				-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f };

		return GlBufferFactory.asFloatBuffer(cubeArray);
	}

	public void resize(int x, int y, int width, int height) {
		float ratio;
		// Prevent a divide by zero, when window is too short
		// (you can't make a window of zero width).
		if (height == 0)
			height = 1;

		ratio = (1.0f * width) / height;
		this.projMatrix = buildProjectionMatrix(53.13f, ratio, 1.0f, 30.0f, this.projMatrix);
	}

	@Override
	public void update(Cube t) {
		cube = t;

	}

	@Override
	public void paint(GL4 gl, int... viewport) {
		setCamera(5f, 5f, 2, 0.5f, 0.5f, -1, this.viewMatrix);

		GlBuffer gpuCube = GlBufferFactory.newGLBuffer(gl, GL_TYPE.ARRAY_BUFFER, GL_USAGE.STATIC_DRAW);
		gpuCube.bind(gl);
		ByteBuffer grid = createCube();
		gpuCube.allocate(gl, grid, grid.capacity());
		gpuCube.release(gl);

		cubeProgram.beginUse(gl);

		// Get a handle for our "MVP" uniform
		// Only during the initialisation

		// Send our transformation to the currently bound shader, in the "MVP"
		// uniform
		// This is done in the main loop since each model will have a different
		// MVP matrix (At least for the M part)
		// glUniformMatrix4fv(mvp_handle, 1, GL_FALSE, &mvp[0][0]);

		this.projMatrixLoc = gl.glGetUniformLocation(cubeProgram.getId(), "projMatrix");
		this.viewMatrixLoc = gl.glGetUniformLocation(cubeProgram.getId(), "viewMatrix");

		// set the view and the projection matrix
		gl.glUniformMatrix4fv(this.projMatrixLoc, 1, false, this.projMatrix, 0);
		gl.glUniformMatrix4fv(this.viewMatrixLoc, 1, false, this.viewMatrix, 0);

		gl.glEnable(GL4.GL_PROGRAM_POINT_SIZE);

		gl.glEnableVertexAttribArray(0);

		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, gpuCube.getId());
		// Associate Vertex attribute 0 with the last bound VBO
		gl.glVertexAttribPointer(0 /* the vertex attrib ute */, 3, GL4.GL_FLOAT, false /* normalized? */,
				0 /* stride */, 0 /* The bound VBO data offset */);

		cubeProgram.setUniform(gl, "color", GlUtil.colorAsVec4(this.cube.getColor()));

		gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 12 * 3);

		gl.glDisableVertexAttribArray(0);

		cubeProgram.endUse(gl);

	}
}

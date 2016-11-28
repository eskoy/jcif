package com.jcif.opengl;

public interface GLSLProvider {

	public static String NEW_LINE = System.getProperty("line.separator");

	public static String GROUP_SIZE = String.join(NEW_LINE, "#define GROUP_COUNT 4096", //
			"#define GROUP_SIZE 512", //
			"#define GRID_SIZE   (GROUP_COUNT*GROUP_SIZE)", //
			"layout(local_size_x=GROUP_SIZE) in;");

	public static String GLOBAL_ID = "const int globalId=int(gl_GlobalInvocationID.x);";

	String getGLSL();

	public static String buildGLSL(String glsl) {
		return String.join(GLSLProvider.NEW_LINE, GROUP_SIZE, GLOBAL_ID, glsl);
	}
}

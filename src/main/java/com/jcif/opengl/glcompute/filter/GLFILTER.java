package com.jcif.opengl.glcompute.filter;

import com.jcif.opengl.GLSLProvider;

public enum GLFILTER implements GLSLProvider {

	FILTER2D(String.join(NEW_LINE, //
			"layout(std430, binding = 0) volatile coherent buffer SSBO_0 {    ", //
			"int backend[];", //
			" };", //
			"layout(std430, binding = 1) restrict readonly buffer SSBO_1 {", //
			"int bitmask[]; ", //
			"};", //
			"layout(std430, binding = 2) restrict readonly buffer SSBO_2 {", //
			"float valuesA[];", //
			"};", //
			"layout(std430, binding = 3) restrict readonly buffer SSBO_3 {", //
			"float valuesB[];", //
			"};", //
			"layout(location=0) uniform int UNI_0;", //
			"const int pulseCount = UNI_0.x;", //
			"layout(location=1) uniform vec4 UNI_1;", //
			"const float minValueA=UNI_1.x,", //
			"            maxValueA=UNI_1.y,", //
			"            minValueB=UNI_1.z,", //
			"            maxValueB=UNI_1.w;", //
			"void main(void)", //
			"{", //
			"const int highMask=1<<31;", //
			"const bool wrapA=minValueA>maxValueA;", //
			"const bool wrapB=minValueB>maxValueB;", // "
			"for(int id=globalId;id<pulseCount;id+=GRID_SIZE)", //
			"{", //
			"if((bitmask[id])!=0)", //
			"{", //
			"int index=id; ", //
			"const float valueA=valuesA[index];", //
			"const float valueB=valuesB[index];", //
			"const bool c1A=valueA>=minValueA;", //
			"const bool c2A=valueA<=maxValueA;", //
			"const bool c1B=valueB>=minValueB;", //
			"const bool c2B=valueB<=maxValueB;", //
			"const bool keep=(wrapA ? (c1A||c2A) : (c1A&&c2A))&& (wrapB ? (c1B||c2B) : (c1B&&c2B));", //
			"if(keep) backend[id]=1;", //
			"}", //
			"}", //
			"}"));

	protected String shader;

	private GLFILTER(String shader) {
		this.shader = shader;
	}

	@Override
	public String getGLSL() {
		return GLSLProvider.buildGLSL(shader);
	}

}

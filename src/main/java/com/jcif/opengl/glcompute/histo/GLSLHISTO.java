package com.jcif.opengl.glcompute.histo;

import com.jcif.opengl.GLSLProvider;

public enum GLSLHISTO implements GLSLProvider {

	HISTO1D(String.join(NEW_LINE, //
			"layout(std430, binding = 0) volatile coherent buffer SSBO_0 {    ", //
			"int counts[];", //
			" };", //
			"layout(std430, binding = 1) restrict readonly buffer SSBO_1 {", //
			"uint bitmask[]; ", //
			"};", //
			"layout(std430, binding = 2) restrict readonly buffer SSBO_2 {", //
			"float values[];", //
			"};", //
			"layout(location=0) uniform ivec2 UNI_0;", //
			"const int pulseCount = UNI_0.x;", //
			"const int binCount  = UNI_0.y;", //
			"layout(location=1) uniform vec2 UNI_1;", //
			"const float factor= UNI_1.x;", //
			"const float increment = UNI_1.y;", //
			"void main(void)", //
			"{", //
			"const int lastBin= binCount-1;", //
			"for(int id=globalId;id<pulseCount;id+=GRID_SIZE)", //
			"{", //
			"if((bitmask[id])!=0)", //
			"{", //
			"const float value=values[id]; ", //
			"const float pos=fma(value,factor,increment); ", //
			"const int bin=clamp(int(floor(pos)),0,lastBin); ", //
			"atomicAdd(counts[bin],1);", //
			"}", //
			"}", //
			"}")),

	HISTO2D(String.join(NEW_LINE, //
			"layout(std430, binding = 0) volatile coherent buffer SSBO_0 {    ", //
			"int counts[];", //
			" };", //
			"layout(std430, binding = 1) restrict readonly buffer SSBO_1 {", //
			"uint bitmask[]; ", //
			"};", //
			"layout(std430, binding = 2) restrict readonly buffer SSBO_2 {", //
			"float valuesA[];", //
			"};", //
			"layout(std430, binding = 3) restrict readonly buffer SSBO_3 {", //
			"float valuesB[];", //
			"};", //
			"layout(location=0) uniform ivec3 UNI_0;", //
			"const int pulseCount = UNI_0.x;", //
			"const ivec2 binCount  = UNI_0.yz;", //
			"layout(location=1) uniform vec4 UNI_1;", //
			"const vec2 factor= UNI_1.xy;", //
			"const vec2 increment = UNI_1.zw;", //
			"void main(void)", //
			"{", //
			"const ivec2 firstBin=ivec2(0);", //
			"const ivec2 lastBin=binCount-ivec2(1);", //
			"for(int id=globalId;id<pulseCount;id+=GRID_SIZE)", //
			"{", //
			"if((bitmask[id])!=0)", //
			"{", //
			"const vec2 value=vec2(valuesA[id],valuesB[id]); ", //
			"const vec2 pos=fma(value,factor,increment); ", //
			"const ivec2 bin=clamp(ivec2(floor(pos)),firstBin,lastBin);", //
			"const int binId=bin.y*binCount.x+bin.x; ", //
			"atomicAdd(counts[binId],1);", //
			"}", //
			"}", //
			"}")),

	HISTO3D(String.join(NEW_LINE, //
			"layout(std430, binding = 0) volatile coherent buffer SSBO_0 {    ", //
			"int counts[];", //
			" };", //
			"layout(std430, binding = 1) restrict readonly buffer SSBO_1 {", //
			"uint bitmask[]; ", //
			"};", //
			"layout(std430, binding = 2) restrict readonly buffer SSBO_2 {", //
			"float valuesA[];", //
			"};", //
			"layout(std430, binding = 3) restrict readonly buffer SSBO_3 {", //
			"float valuesB[];", //
			"};", //
			"layout(std430, binding = 4) restrict readonly buffer SSBO_4 {", //
			"float valuesC[];", //
			"};", //
			"layout(location=0) uniform ivec4 UNI_0;", //
			"const int pulseCount = UNI_0.x;", //
			"const ivec3 binCount  = UNI_0.yzw;", //
			"layout(location=1) uniform vec3 UNI_1;", //
			"const vec3 factor= UNI_1.xyz;", //
			"layout(location=2) uniform vec3 UNI_2;", //
			"const vec3 increment= UNI_2.xyz;", //
			"void main(void)", //
			"{", //
			"const ivec3 firstBin=ivec3(0);", //
			"const ivec3 lastBin=binCount-ivec3(1);", //
			"for(int id=globalId;id<pulseCount;id+=GRID_SIZE)", //
			"{", //
			"if((bitmask[id])!=0)", //
			"{", //
			"const vec3 value=vec3(valuesA[id],valuesB[id],valuesC[id]); ", //
			"const vec3 pos=fma(value,factor,increment); ", //
			"const ivec3 bin=clamp(ivec3(floor(pos)),firstBin,lastBin);", //
			"const int binId=(bin.z*binCount.y+bin.y)*binCount.x+bin.x;  ", //
			"atomicAdd(counts[binId],1);", //
			"}", //
			"}", //
			"}"));

	protected String shader;

	private GLSLHISTO(String shader) {
		this.shader = shader;
	}

	@Override
	public String getGLSL() {
		return GLSLProvider.buildGLSL(shader);
	}

}

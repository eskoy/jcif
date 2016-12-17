package com.jcif.structuremodel.impl;

import java.nio.ByteBuffer;

import com.jcif.structuremodel.StructureMember;

public class FloatBufferAccesImpl implements BufferAcces {

	int structureSize;

	int ordinal;

	ByteBuffer buffer;

	public FloatBufferAccesImpl(StructureMember m, ByteBuffer b) {
		structureSize = m.getMemoryDefinition().structureSize();
		ordinal = m.getMemoryDefinition().ordinal();
		buffer = b;
	}

	@Override
	public float getValue(int index) {
		return buffer.getFloat((index * structureSize) + ordinal);
	}

	@Override
	public String getDisplayValue(int index) {
		return "" + getValue(index);
	}

	@Override
	public void setValue(int index, float value) {
		buffer.putFloat((index * structureSize) + ordinal, value);

	}

}

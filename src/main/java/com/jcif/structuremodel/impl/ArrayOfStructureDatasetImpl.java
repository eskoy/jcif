package com.jcif.structuremodel.impl;

import java.nio.ByteBuffer;

import com.jcif.structuremodel.StructureDataset;
import com.jcif.structuremodel.StructureMember;

public class ArrayOfStructureDatasetImpl implements StructureDataset {

	ByteBuffer buffer;

	BufferAcces[] bufferAccess;

	public ArrayOfStructureDatasetImpl(int size, StructureMember... definitions) {
		buffer = ByteBuffer.allocate(size * definitions[0].getMemoryDefinition().structureSize());
		bufferAccess = new BufferAcces[definitions.length];
		int i = 0;
		for (StructureMember current : definitions) {
			bufferAccess[i] = new FloatBufferAccesImpl(current, buffer);
			i++;
		}

	}

	@Override
	public float getValue(StructureMember member, int index) {
		return bufferAccess[member.ordinal()].getValue(index);
	}

	@Override
	public String getDisplayValue(StructureMember member, int index) {
		return bufferAccess[member.ordinal()].getDisplayValue(index);
	}

	@Override
	public void setValue(StructureMember member, int index, float value) {
		bufferAccess[member.ordinal()].setValue(index, value);

	}

}

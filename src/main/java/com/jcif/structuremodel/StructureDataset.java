package com.jcif.structuremodel;

import com.jcif.structurestream.StructureStream;
import com.jcif.structurestream.StructureStreamSupport;

public interface StructureDataset {

	float getValue(StructureMember member, int index);

	void setValue(StructureMember member, int index, float value);

	String getDisplayValue(StructureMember member, int index);

	int getSize();

	default StructureStream stream() {
		return StructureStreamSupport.stream(this);
	}

}

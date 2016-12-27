package com.jcif.structure.model;

import com.jcif.structure.stream.StructureStream;
import com.jcif.structure.stream.StructureStreamSupport;

public interface StructureDataset {

	float getValue(StructureMember member, int index);

	void setValue(StructureMember member, int index, float value);

	String getDisplayValue(StructureMember member, int index);

	int getSize();

	default StructureStream stream() {
		return StructureStreamSupport.stream(this);
	}

}

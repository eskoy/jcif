package com.jcif.structure.model;

import com.jcif.structure.model.impl.ArrayOfStructureDatasetImpl;

public class StructureDatasets {

	public static StructureDataset newArrayOfStructureDatasetImpl(int size, StructureMember... definitions) {

		return new ArrayOfStructureDatasetImpl(size, definitions);
	}

}

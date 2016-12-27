package com.jcif.structure.stream;

import com.jcif.structure.model.StructureDataset;
import com.jcif.structure.stream.impl.StructureStreamImpl;

public class StructureStreamSupport {

	public static StructureStream stream(StructureDataset ds) {
		return new StructureStreamImpl(ds);
	}
}

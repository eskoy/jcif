package com.jcif.structure.stream.impl;

import java.util.ArrayList;
import java.util.List;

import com.jcif.structure.model.StructureDataset;
import com.jcif.structure.process.StructureProcess;
import com.jcif.structure.stream.StructureStream;

public class StructureStreamImpl implements StructureStream {

	protected StructureDataset structureDataset;

	protected List<StructureProcess> processList = new ArrayList<StructureProcess>();

	public StructureStreamImpl(StructureDataset ds) {
		structureDataset = ds;
	}

	@Override
	public StructureStream process(StructureProcess process) {
		processList.add(process);
		return this;
	}

	@Override
	public void start() {
		for (int i = 0; i < structureDataset.getSize(); i++) {
			for (StructureProcess p : processList) {
				p.process(structureDataset, i);
			}

		}

	}

}

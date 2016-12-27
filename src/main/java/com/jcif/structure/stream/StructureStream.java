package com.jcif.structure.stream;

import com.jcif.structure.process.StructureProcess;

public interface StructureStream {

	StructureStream process(StructureProcess process);

	void start();
}

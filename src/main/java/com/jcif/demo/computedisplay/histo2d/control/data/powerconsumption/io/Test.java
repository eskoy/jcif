package com.jcif.demo.computedisplay.histo2d.control.data.powerconsumption.io;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.jcif.demo.computedisplay.histo2d.control.data.powerconsumption.POWER_CONSUMPTION;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.structure.model.StructureDataset;
import com.jcif.structure.process.MinMaxProcess;
import com.jcif.structure.process.NormalizeProcess;

public class Test {

	public static void main(String[] args) {
		String path = "household_power_consumption";

		PowerConsumptionFileReader reader = new PowerConsumptionFileReader();

		int numberOfRecord = 1000000;

		StructureDataset ds = reader.read(path, numberOfRecord);

		MinMaxProcess process = new MinMaxProcess(POWER_CONSUMPTION.Sub_metering_1);

		ds.stream().process(process).start();

		float[] minmax = process.getMinMaxs()[0];

		ByteBuffer tmpBuffer = GLBufferFactory.allocate(ds.getSize() * Float.BYTES);
		FloatBuffer floatbuffervalues = tmpBuffer.asFloatBuffer();

		NormalizeProcess normalizeProcess = new NormalizeProcess(POWER_CONSUMPTION.Sub_metering_1,
				minmax, floatbuffervalues);

		ds.stream().process(normalizeProcess).start();

	}
}

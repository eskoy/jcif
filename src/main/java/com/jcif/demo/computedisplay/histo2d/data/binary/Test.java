package com.jcif.demo.computedisplay.histo2d.data.binary;

import com.jcif.demo.computedisplay.histo2d.data.Household_power_consumption_definition;
import com.jcif.structuremodel.StructureDataset;
import com.jcif.structurestream.MinMaxProcess;

public class Test {

	public static void main(String[] args) {
		String path = "household_power_consumption";

		Household_power_consumption_reader reader = new Household_power_consumption_reader();

		StructureDataset ds = reader.read(path);

		MinMaxProcess process = new MinMaxProcess(Household_power_consumption_definition.Sub_metering_1);

		ds.stream().process(process).start();

		float[] minmax = process.getMinMaxs()[0];

		System.err.println("Household_power_consumption_definition.Sub_metering_1  " + minmax[0] + " " + minmax[1]);

	}
}

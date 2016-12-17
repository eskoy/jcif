package com.jcif.demo.computedisplay.histo2d.data.inputdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.jcif.structuremodel.StructureDataset;
import com.jcif.structuremodel.StructureDatasets;

public class Household_power_consumption_reader {

	public final StructureDataset read(String path) {
		// InputStream is = this.getClass().getResourceAsStream(path);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Scanner scanner = new Scanner(inputStream);

		long time = System.currentTimeMillis();
		int size = 1000000; // dont count definition

		StructureDataset ds = StructureDatasets.newArrayOfStructureDatasetImpl(size,
				Household_power_consumption_definition.values());

		Household_power_consumption_definition[] values = { Household_power_consumption_definition.Global_intensity,
				Household_power_consumption_definition.Global_active_power,
				Household_power_consumption_definition.Global_reactive_power,
				Household_power_consumption_definition.Sub_metering_1,
				Household_power_consumption_definition.Sub_metering_2,
				Household_power_consumption_definition.Sub_metering_3 };

		int index = 0;
		scanner.next();
		while (index < 1000000) {
			String line = scanner.next();
			if (!line.contains("?")) {
				String[] record = line.split(";");

				for (Household_power_consumption_definition member : values) {
					ds.setValue(member, index, Float.parseFloat(record[member.ordinal()]));
				}

				index++;
			}
		}
		scanner.close();

		time = System.currentTimeMillis() - time;
		System.err.println(size + "  time in ms " + time);
		return ds;
	}

}

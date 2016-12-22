package com.jcif.demo.computedisplay.histo2d.data.binary;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.jcif.demo.computedisplay.histo2d.data.Household_power_consumption_definition;
import com.jcif.structuremodel.StructureDataset;
import com.jcif.structuremodel.StructureDatasets;

public class Household_power_consumption_reader {

	public final StructureDataset read(String path) {

		final Path zip = Paths.get(path + ".zip");
		StructureDataset ds = null;
		// création d'une instance de FileSystem pour gérer les zip

		try (final FileSystem fs = FileSystems.newFileSystem(zip, null)) {

			Path mf = fs.getPath(path + ".txt");
			// Path du fichier à accéder dans l'archive

			BufferedReader readBuffer = Files.newBufferedReader(mf, Charset.defaultCharset());

			String line = "";
			long time = System.currentTimeMillis();
			int size = 1000000; // dont count definition
			int index = 0;
			ds = StructureDatasets.newArrayOfStructureDatasetImpl(size,
					Household_power_consumption_definition.values());
			Household_power_consumption_definition[] values = { Household_power_consumption_definition.Global_intensity,
					Household_power_consumption_definition.Global_active_power,
					Household_power_consumption_definition.Global_reactive_power,
					Household_power_consumption_definition.Sub_metering_1,
					Household_power_consumption_definition.Sub_metering_2,
					Household_power_consumption_definition.Sub_metering_3 };
			// skip first line
			readBuffer.readLine();
			while ((line = readBuffer.readLine()) != null && index < size) {

				if (!line.contains("?")) {
					String[] record = line.split(";");

					for (Household_power_consumption_definition member : values) {
						ds.setValue(member, index, Float.parseFloat(record[member.ordinal()]));
					}

					index++;
				}
			}
			time = System.currentTimeMillis() - time;
			System.err.println(size + "  time in ms " + time);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

}

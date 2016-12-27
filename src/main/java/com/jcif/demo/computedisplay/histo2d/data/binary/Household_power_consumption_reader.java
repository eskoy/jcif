package com.jcif.demo.computedisplay.histo2d.data.binary;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import com.jcif.demo.computedisplay.histo2d.data.Household_power_consumption_definition;
import com.jcif.structure.model.StructureDataset;
import com.jcif.structure.model.StructureDatasets;

public class Household_power_consumption_reader {

	public final StructureDataset read(String path, int numberOfrecord) {

		final Path zip = Paths.get(path + ".zip");
		StructureDataset ds = null;
		// création d'une instance de FileSystem pour gérer les zip

		try (final FileSystem fs = FileSystems.newFileSystem(zip, null)) {

			Path mf = fs.getPath(path + ".txt");
			// Path du fichier à accéder dans l'archive

			BufferedReader readBuffer = Files.newBufferedReader(mf, Charset.defaultCharset());

			String line = "";
			long currentTime = System.currentTimeMillis();
			int size = numberOfrecord; // dont count definition
			int index = 0;
			ds = StructureDatasets.newArrayOfStructureDatasetImpl(size,
					Household_power_consumption_definition.values());
			Household_power_consumption_definition[] numbervalues = {
					Household_power_consumption_definition.Global_intensity,
					Household_power_consumption_definition.Global_active_power,
					Household_power_consumption_definition.Global_reactive_power,
					Household_power_consumption_definition.Sub_metering_1,
					Household_power_consumption_definition.Sub_metering_2,
					Household_power_consumption_definition.Sub_metering_3 };

			SimpleDateFormat parserDate = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat parserTime = new SimpleDateFormat("HH:mm:ss");

			// skip first line
			readBuffer.readLine();
			while ((line = readBuffer.readLine()) != null && index < size) {

				if (!line.contains("?")) {
					String[] record = line.split(";");

					float date = parserDate.parse(record[Household_power_consumption_definition.Date.ordinal()])
							.getTime();
					ds.setValue(Household_power_consumption_definition.Date, index, date);

					float time = parserTime.parse(record[Household_power_consumption_definition.Time.ordinal()])
							.getTime();
					ds.setValue(Household_power_consumption_definition.Time, index, time);
					for (Household_power_consumption_definition member : numbervalues) {
						ds.setValue(member, index, Float.parseFloat(record[member.ordinal()]));
					}

					index++;
				}
			}
			currentTime = System.currentTimeMillis() - currentTime;
			System.err.println(size + "  time in ms " + currentTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

}

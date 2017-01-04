package com.jcif.demo.computedisplay.histo2d.control.data.powerconsumption.io;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import com.jcif.demo.computedisplay.histo2d.control.data.powerconsumption.POWER_CONSUMPTION;
import com.jcif.structure.model.StructureDataset;
import com.jcif.structure.model.StructureDatasets;

public class PowerConsumptionFileReader {

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
					POWER_CONSUMPTION.values());
			POWER_CONSUMPTION[] numbervalues = {
					POWER_CONSUMPTION.Global_intensity,
					POWER_CONSUMPTION.Global_active_power,
					POWER_CONSUMPTION.Global_reactive_power,
					POWER_CONSUMPTION.Sub_metering_1,
					POWER_CONSUMPTION.Sub_metering_2,
					POWER_CONSUMPTION.Sub_metering_3 };

			SimpleDateFormat parserDate = new SimpleDateFormat(
					POWER_CONSUMPTION.Date.getBinaryDefinition().pattern());
			SimpleDateFormat parserTime = new SimpleDateFormat(
					POWER_CONSUMPTION.Time.getBinaryDefinition().pattern());

			// skip first line
			readBuffer.readLine();
			while ((line = readBuffer.readLine()) != null && index < size) {

				if (!line.contains("?")) {
					String[] record = line.split(";");

					float date = parserDate.parse(record[POWER_CONSUMPTION.Date.ordinal()])
							.getTime();
					ds.setValue(POWER_CONSUMPTION.Date, index, date);

					float time = parserTime.parse(record[POWER_CONSUMPTION.Time.ordinal()])
							.getTime();
					ds.setValue(POWER_CONSUMPTION.Time, index, time);
					for (POWER_CONSUMPTION member : numbervalues) {
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

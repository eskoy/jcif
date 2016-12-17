package com.jcif.demo.computedisplay.histo2d.data.inputdata;

public class Test {

	public static void main(String[] args) {
		String path = "household_power_consumption.txt";

		Household_power_consumption_reader reader = new Household_power_consumption_reader();

		reader.read(path);
		System.err.println("end");

	}
}

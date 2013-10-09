package org.eclipse.recommenders.jayes.benchmark;

import java.io.PrintWriter;

import com.google.caliper.config.InvalidConfigurationException;
import com.google.caliper.runner.CaliperMain;
import com.google.caliper.runner.InvalidBenchmarkException;
import com.google.caliper.util.InvalidCommandException;

public class Main {

	public static void main(String[] args) throws InvalidCommandException, InvalidBenchmarkException,
			InvalidConfigurationException {
		CaliperMain.exitlessMain(args, new PrintWriter(System.out), new PrintWriter(System.err));
	}

}

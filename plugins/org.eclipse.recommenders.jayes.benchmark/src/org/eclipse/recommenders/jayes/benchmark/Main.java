package org.eclipse.recommenders.jayes.benchmark;

import java.io.PrintWriter;
import java.util.List;

import com.google.caliper.config.InvalidConfigurationException;
import com.google.caliper.runner.CaliperMain;
import com.google.caliper.runner.InvalidBenchmarkException;
import com.google.caliper.util.InvalidCommandException;

public class Main {

    private static final int WARMUP_ITERATIONS = 15;
    private static final int REPETITIONS = 20;
    private static final String MODEL = "org.eclipse.jface:org.eclipse.jface:zip:call:3.0.0";

    public static void main(String[] args) throws InvalidCommandException, InvalidBenchmarkException,
            InvalidConfigurationException {
        CaliperMain.exitlessMain(args, new PrintWriter(System.out), new PrintWriter(System.err));
    }

    public static class CaliperlessMain {

        public static void main(String[] args) throws Exception {
            IOBenchmarkBinary ioBenchMark = new IOBenchmarkBinary(MODEL);
            ioBenchMark.timeJavaDeserializationToBayesianNetwork(WARMUP_ITERATIONS);
            ioBenchMark.timeJavaDeserialization(WARMUP_ITERATIONS);
            ioBenchMark.timeJavaDeserializationToJTA(WARMUP_ITERATIONS);
            ioBenchMark.timeJavaDeserializationWithMinDegreeToJTA(WARMUP_ITERATIONS);

            ioBenchMark.timeBinaryDeserialization(WARMUP_ITERATIONS);
            ioBenchMark.timeBinaryDeserializationToJTA(WARMUP_ITERATIONS);
            ioBenchMark.timeBinaryDeserializationWithMinDegreeToJTA(WARMUP_ITERATIONS);

            long time;
            List<?> deserialization;
            // ///////////////////////////

            time = System.nanoTime();
            deserialization = ioBenchMark.timeJavaDeserializationToBayesianNetwork(REPETITIONS);
            System.out.println(deserialization.size() + " Java Serialization To BayesianNetwork Elapsed time:\t"
                    + ((System.nanoTime() - time) / (REPETITIONS * Math.pow(10, 6))));

            time = System.nanoTime();
            deserialization = ioBenchMark.timeJavaDeserialization(REPETITIONS);
            System.out.println(deserialization.size() + " Java Serialization Elapsed time:\t"
                    + ((System.nanoTime() - time) / (REPETITIONS * Math.pow(10, 6))));

            time = System.nanoTime();
            deserialization = ioBenchMark.timeJavaDeserializationToJTA(REPETITIONS);
            System.out.println(deserialization.size() + " Java Serialization to JTA Elapsed time:\t"
                    + ((System.nanoTime() - time) / (REPETITIONS * Math.pow(10, 6))));

            time = System.nanoTime();
            deserialization = ioBenchMark.timeJavaDeserializationWithMinDegreeToJTA(REPETITIONS);
            System.out.println(deserialization.size() + " Java Serialization w/ MinDegree Elapsed time:\t"
                    + ((System.nanoTime() - time) / (REPETITIONS * Math.pow(10, 6))));

            time = System.nanoTime();
            deserialization = ioBenchMark.timeBinaryDeserialization(REPETITIONS);
            System.out.println(deserialization.size() + " Binary Format Elapsed time:"
                    + ((System.nanoTime() - time) / (REPETITIONS * Math.pow(10, 6))));

            time = System.nanoTime();
            deserialization = ioBenchMark.timeBinaryDeserializationToJTA(REPETITIONS);
            System.out.println(deserialization.size() + " Binary Format to JTA Elapsed time:\t"
                    + ((System.nanoTime() - time) / (REPETITIONS * Math.pow(10, 6))));

            time = System.nanoTime();
            deserialization = ioBenchMark.timeBinaryDeserializationWithMinDegreeToJTA(REPETITIONS);
            System.out.println(deserialization.size() + " Binary Format w/ MinDegree Elapsed time:\t"
                    + ((System.nanoTime() - time) / (REPETITIONS * Math.pow(10, 6))));

        }
    }

}

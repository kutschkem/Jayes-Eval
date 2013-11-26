package org.eclipse.recommenders.jayes.benchmark.util;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * needed until caliper has proper windows support...
 */
public class BenchmarkRunner {

    public static void main(String[] args) throws Exception {
        Class<?> benchmarkClass = Class.forName(args[0]);
        int warmup = Integer.parseInt(args[1]);
        int repetitions = Integer.parseInt(args[2]);
        Object benchmark = benchmarkClass.newInstance();

        Method[] methods = benchmarkClass.getMethods();

        for (Method method : methods) {
            if (method.getName().startsWith("time")
                    && Arrays.equals(method.getParameterTypes(), new Class[] { int.class })) {
                method.invoke(benchmark, warmup);
                long time = System.nanoTime();
                method.invoke(benchmark, repetitions);
                double elapsedTime = (System.nanoTime() - time) / 1e6;
                System.out.println(method.getName() + ": " + elapsedTime);
            }
        }

    }

}

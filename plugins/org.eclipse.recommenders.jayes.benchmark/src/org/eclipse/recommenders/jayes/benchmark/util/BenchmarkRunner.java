package org.eclipse.recommenders.jayes.benchmark.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * needed until caliper has proper windows support...
 */
public class BenchmarkRunner {

    public static void main(String[] args) throws Exception {
        if (args.length > 3 && args[3].equals("-i")) {
            System.out.println("press any key to continue");
            System.in.read();
        }
        Class<?> benchmarkClass = Class.forName(args[0]);
        int warmup = Integer.parseInt(args[1]);
        int repetitions = Integer.parseInt(args[2]);
        Object benchmark = benchmarkClass.newInstance();

        Method[] methods = benchmarkClass.getMethods();

        Arrays.sort(methods, new Comparator<Method>() {

            @Override
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (Method method : methods) {
            if (method.getName().startsWith("time")
                    && Arrays.equals(method.getParameterTypes(), new Class[] { int.class })) {
                method.invoke(benchmark, warmup);

                List<Double> times = new ArrayList<Double>();
                for (int i = 0; i < repetitions; i++) {
                    long time = System.nanoTime();
                    method.invoke(benchmark, 1);
                    double elapsedTime = (System.nanoTime() - time) / 1e6;
                    times.add(elapsedTime);
                }
                System.out.println(method.getName() + ": " + median(times));
            }
        }

    }

    private static double median(List<Double> times) {
        Collections.sort(times);
        return times.get(times.size() / 2);
    }

}

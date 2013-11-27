/*******************************************************************************
 * Copyright (c) 2013 Michael Kutschke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Kutschke - initial API and implementation
 ******************************************************************************/
package org.eclipse.recommenders.jayes.benchmark;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;
import org.eclipse.recommenders.internal.jayes.io.util.BayesNetConverter;
import org.eclipse.recommenders.jayes.BayesNet;
import org.eclipse.recommenders.jayes.benchmark.util.ModelLoader;
import org.eclipse.recommenders.jayes.inference.junctionTree.JunctionTreeAlgorithm;
import org.eclipse.recommenders.jayes.inference.junctionTree.JunctionTreeBuilder;
import org.eclipse.recommenders.jayes.io.jbif.JayesBifReader;
import org.eclipse.recommenders.jayes.io.jbif.JayesBifWriter;
import org.eclipse.recommenders.jayes.util.triangulation.MinDegree;

import com.google.caliper.Benchmark;

@SuppressWarnings("deprecation")
public class IOBenchmarkBinary extends Benchmark {
    private List<byte[]> networks_java;
    private List<byte[]> jayesNets_bin;

    private static final boolean COMPRESS = false;

    public IOBenchmarkBinary() throws Exception {
        this("jre:jre:zip:call:1.0.0");
    }

    public IOBenchmarkBinary(String modelCoordinate) throws Exception {
        ModelLoader modelLoader = new ModelLoader(modelCoordinate);
        this.networks_java = serializeJava(modelLoader.getIntermediateNetworks());
        this.jayesNets_bin = serializeBinary(modelLoader.getJayesNetworks());

        System.out.println(countBytes(networks_java));
        System.out.println(countBytes(jayesNets_bin));
    }

    private int countBytes(List<byte[]> networks_java2) {
        int size = 0;
        for (byte[] bs : networks_java2) {
            size += bs.length;
        }
        return size;
    }

    private List<byte[]> serializeBinary(List<BayesNet> jayesNetworks) throws IOException {
        List<byte[]> serialized = new ArrayList<byte[]>();
        for (BayesNet network : jayesNetworks) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JayesBifWriter wrtr = new JayesBifWriter(compress(out));
            wrtr.write(network);
            wrtr.close();
            serialized.add(out.toByteArray());
        }
        return serialized;
    }

    private OutputStream compress(ByteArrayOutputStream out) throws IOException {
        if (COMPRESS) {
            return new GZIPOutputStream(out);
        } else {
            return out;
        }
    }

    private List<byte[]> serializeJava(List<BayesianNetwork> intermediateNetworks) throws IOException {
        List<byte[]> serialized = new ArrayList<byte[]>();
        for (BayesianNetwork network : intermediateNetworks) {
            serialized.add(writeNetwork(network));
        }

        return serialized;
    }

    public List<Integer> timeJavaDeserializationToJTA(int rep) throws IOException, ClassNotFoundException {
        List<Integer> l = new ArrayList<Integer>();
        BayesNetConverter converter = new BayesNetConverter();
        for (int i = 0; i < rep; i++) {
            for (byte[] serialized : networks_java) {
                BayesianNetwork net = readNetwork(serialized);
                JunctionTreeAlgorithm algo = new JunctionTreeAlgorithm();
                algo.setNetwork(converter.transform(net));
                l.add(algo.hashCode());
            }
        }
        return l;
    }

    public List<Integer> timeJavaDeserializationWithMinDegreeToJTA(int rep) throws IOException, ClassNotFoundException {
        List<Integer> l = new ArrayList<Integer>();
        BayesNetConverter converter = new BayesNetConverter();
        for (int i = 0; i < rep; i++) {
            for (byte[] serialized : networks_java) {
                BayesianNetwork net = readNetwork(serialized);
                JunctionTreeAlgorithm algo = new JunctionTreeAlgorithm();
                algo.setJunctionTreeBuilder(JunctionTreeBuilder.forHeuristic(new MinDegree()));
                algo.setNetwork(converter.transform(net));
                l.add(algo.hashCode());
            }
        }
        return l;
    }

    public List<Integer> timeBinaryDeserializationToJTA(int rep) throws IOException, ClassNotFoundException {
        List<Integer> l = new ArrayList<Integer>();
        for (int i = 0; i < rep; i++) {
            for (byte[] serialized : jayesNets_bin) {
                JayesBifReader rdr = new JayesBifReader(toCompressedInputStream(serialized));
                BayesNet net = rdr.read();
                rdr.close();
                JunctionTreeAlgorithm algo = new JunctionTreeAlgorithm();
                algo.setNetwork(net);
                l.add(algo.hashCode());
            }
        }
        return l;
    }

    private InputStream toCompressedInputStream(byte[] serialized) throws IOException {
        if (COMPRESS) {
            return new GZIPInputStream(new ByteArrayInputStream(serialized));
        } else {
            return new ByteArrayInputStream(serialized);
        }
    }

    public List<Integer> timeBinaryDeserializationWithMinDegreeToJTA(int rep) throws IOException,
            ClassNotFoundException {
        List<Integer> l = new ArrayList<Integer>();
        for (int i = 0; i < rep; i++) {
            for (byte[] serialized : jayesNets_bin) {
                JayesBifReader rdr = new JayesBifReader(toCompressedInputStream(serialized));
                BayesNet net = rdr.read();
                rdr.close();
                JunctionTreeAlgorithm algo = new JunctionTreeAlgorithm();
                algo.setJunctionTreeBuilder(JunctionTreeBuilder.forHeuristic(new MinDegree()));
                algo.setNetwork(net);
                l.add(algo.hashCode());
            }
        }
        return l;
    }

    public List<Integer> timeJavaDeserialization(int rep) throws IOException, ClassNotFoundException {
        List<Integer> l = new ArrayList<Integer>();
        BayesNetConverter converter = new BayesNetConverter();
        for (int i = 0; i < rep; i++) {
            for (byte[] serialized : networks_java) {
                BayesianNetwork net = readNetwork(serialized);
                l.add(converter.transform(net).hashCode());
            }
        }
        return l;
    }

    public List<Integer> timeJavaDeserializationToBayesianNetwork(int rep) throws IOException, ClassNotFoundException {
        List<Integer> l = new ArrayList<Integer>();
        for (int i = 0; i < rep; i++) {
            for (byte[] serialized : networks_java) {
                BayesianNetwork net = readNetwork(serialized);
                l.add(net.hashCode());
            }
        }
        return l;
    }

    public List<Integer> timeBinaryDeserialization(int rep) throws IOException, ClassNotFoundException {
        List<Integer> l = new ArrayList<Integer>();
        for (int i = 0; i < rep; i++) {
            for (byte[] serialized : jayesNets_bin) {
                JayesBifReader rdr = new JayesBifReader(toCompressedInputStream(serialized));
                BayesNet net = rdr.read();
                rdr.close();
                l.add(net.hashCode());
            }
        }
        return l;
    }

    private BayesianNetwork readNetwork(byte[] serialized) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(toCompressedInputStream(serialized));
        BayesianNetwork net = (BayesianNetwork) objectInputStream.readObject();
        objectInputStream.close();
        return net;
    }

    private byte[] writeNetwork(BayesianNetwork network) throws IOException {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(compress(byteArrayStream));
        objectStream.writeObject(network);
        objectStream.close();
        byte[] serialized = byteArrayStream.toByteArray();
        return serialized;
    }

}

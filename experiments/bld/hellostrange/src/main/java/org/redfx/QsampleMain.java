package org.redfx;

import org.redfx.strange.*;
import org.redfx.strange.algorithm.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.*;
import org.redfx.strangefx.render.*;

import java.util.Random;


public class QsampleMain {

    public static void main(String[] args) {
        runHelloStrange();
        // runBB84(); // need to fix platform-specific dependencies
    }

    public static void runHelloStrange() {
        System.out.println("Using high-level Strange API to generate random bits");
        System.out.println("----------------------------------------------------");
        int randomBit = Classic.randomBit();
        System.out.println("Generate one random bit, which can be 0 or 1. Result = "+randomBit);
        int cntZero = 0;
        int cntOne = 0;
        for (int i = 0; i < 10000; i++) {
            if (Classic.randomBit() > 0) {
                cntOne ++;
            } else {
                cntZero ++;
            }
        }
        System.out.println("Generated 10000 random bits, "+cntZero+" of them were 0, and "+cntOne+" were 1.");
    }

    public static void runBB84() {
        final int SIZE = 8;
        Random random = new Random();

        boolean[] aliceBits = new boolean[SIZE]; // random bits chosen by Alice
        boolean[] bobBits = new boolean[SIZE]; // bits measured by Bob
        boolean[] aliceBase = new boolean[SIZE]; // random bases chosen by Alice
        boolean[] bobBase = new boolean[SIZE]; // random bases chosen by Bob

        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        Program program = new Program(SIZE);
        Step prepareStep = new Step(); 
        Step superPositionStep = new Step(); 
        Step superPositionStep2 = new Step(); 
        Step measureStep = new Step(); 
        for (int i = 0; i < SIZE; i++) {
            aliceBits[i] = random.nextBoolean();
            // if Alice's bit is 1, apply a X gate to the |0> state
            if (aliceBits[i]) prepareStep.addGate(new X(i));
            aliceBase[i] = random.nextBoolean();
            // if Alice's base for this bit is 1, apply a H gate
            if (aliceBase[i]) superPositionStep.addGate(new Hadamard(i));
            bobBase[i] = random.nextBoolean();
            // if Bob decides to measure in base 1, apply a H gate
            if (bobBase[i]) superPositionStep2.addGate(new Hadamard(i));
            // Finally, Bob measures the result
            measureStep.addGate(new Measurement(i));
        }

        program.addStep(prepareStep);
        program.addStep(superPositionStep);
        program.addStep(superPositionStep2);
        program.addStep(measureStep);

        Result result = simulator.runProgram(program);
        Qubit[] qubit = result.getQubits();
        int[] measurement = new int[SIZE];
        StringBuffer key = new StringBuffer();
        for (int i = 0; i < SIZE; i++) {
            measurement[i] = qubit[i].measure();
            bobBits[i] = measurement[i] == 1;
            if (aliceBase[i] != bobBase[i]) {
                // If the random bases chosen by Alice and Bob for this bit are different, ignore values
                System.err.println("Different bases used, ignore values "+aliceBits[i]+" and "+ bobBits[i]);
            } else {
                // Alice and Bob picked the same bases. The inital value from Alice matches the measurement from Bob.
                // this bit now becomes part of the secret key
                System.err.println("Same bases used. Alice sent " + (aliceBits[i] ? "1" : "0") + " and Bob received " + (bobBits[i] ? "1" : "0"));
                key.append(aliceBits[i] ? "1" : "0");
            }
        }
        System.err.println("Secret key = "+key);

        Renderer.renderProgram(program);

    }
}

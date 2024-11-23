# Description

This sample is using the bld build tool (see https://rife2.com/bld) to compile and run a Java application
using strange.
The sample is one of the simplest possible Strange samples, and it is not using low-level quantum API's.
It will generate 10,000 random bits, by using a quantum algorithm that brings a qubit in a superposition state and measure t.

# Usage
```
./bld download
./bld compile
./bld run
```

The output of the last command is similar to the following:

```
Using high-level Strange API to generate random bits
----------------------------------------------------
Generate one random bit, which can be 0 or 1. Result = 0
Generated 10000 random bits, 4935 of them were 0, and 5065 were 1.
```


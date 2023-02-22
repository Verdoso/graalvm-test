# GraalVM test

[![made-with-java](https://img.shields.io/badge/Made%20with-Java-ca0000.svg)](https://openjdk.java.net/)

A test case to compare the peformance of the Java version vs a native version of some code I was using in a different project where we noticed big performance differences.
Related to [issue #5968](https://github.com/oracle/graal/issues/5968) at the Graal project.

## Prerequisites

Running under an "x64 Native Tools Command Prompt" and having GraalVM CE 22.3.1 installed at GRAAL_HOME, you can perform the test with the commands below

## Package .jar and create native version

```cmd
set JAVA_HOME=%GRAAL_HOME%
mvn -Pnative native:compile
```

## Run both versions

```cmd
cd target
```

### Java version

```cmd
%GRAAL_HOME%\bin\java -jar graalvm-test-0.0.1-SNAPSHOT.jar
```

### Native version

```cmd
graalvm_test.exe
```

[![made-with-java](https://img.shields.io/badge/Made%20with-Java-ca0000.svg)](https://openjdk.java.net/)

# GraalVM test

Running under an "x64 Native Tools Command Prompt" and having GraalVM CE 22.3.1 installed at GRAAL_HOME


## Package .jar and create native version
```
set JAVA_HOME=%GRAAL_HOME%
mvn -Pnative native:compile
```

## Run both versions
```
cd target
```
### Java version
```
%GRAAL_HOME%\bin\java -jar graalvm-test-0.0.1-SNAPSHOT.jar
```
### Native version
```
graalvm_test.exe
```

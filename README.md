# SpringJavaFXGraal

## Overview
An extremely minimal demonstration setup for a `Windows` desktop application that combines Spring Boot and JavaFX,
built with Maven and compiled to a native image using GraalVM.
It includes Spring Boot JDBC for data access and optional AtlantaFX styling.

## Quick Specifications
- Windows 10/11 OS
- Java 25 (LTS)
- GraalVM 25, Windows x64
- Spring Boot 4.0.4
- JavaFX 22
- PostgreSQL 42.7.3

## Installation

### Prerequisites:

Install Visual Studio Build Tools and the Windows SDK.
Follow the official GraalVM guide:
https://www.graalvm.org/latest/getting-started/windows/#prerequisites-for-native-image-on-windows

### GraalVM installation:
1. **Download GraalVM binaries:** [GraalVM.org](https://www.graalvm.org/downloads/)
```
version: GraalVM 25, Windows x64
```

2. **Extract binaries to your preferred location:**

Example:
```
C:\GraalVM\graalvm-jdk-25.0.2+10.1
```
Verify the `\bin` path:
```
C:\GraalVM\graalvm-jdk-25.0.2+10.1\bin
```

3. **Set required environment variables using `Command Prompt`:**

Example:
```cmd
setx GRAALVM_HOME "C:\GraalVM\graalvm-jdk-25.0.2+10.1"
setx JAVA_HOME "C:\GraalVM\graalvm-jdk-25.0.2+10.1"
setx PATH "%PATH%;%JAVA_HOME%\bin"
```

Restart Command Prompt, then verify:
```cmd
java --version
```

Sample output expectation:
```
java 25.0.2 2026-01-20 LTS
Java(TM) SE Runtime Environment Oracle GraalVM 25.0.2+10.1 (build 25.0.2+10-LTS-jvmci-b01)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 25.0.2+10.1 (build 25.0.2+10-LTS-jvmci-b01, mixed mode, sharing)
```

> [!NOTE]
> GraalVM is used to build the native image.
> The application runs as a native executable and does not require a separate JVM.

## Usage
1. **Navigate to project directory:**
```cmd
cd <your-repo-directory>
```

2. **Build the application JAR:**
```cmd
.\mvnw clean install
```
Run and test the application using the generated JAR.
This ensures JavaFX dependencies are packaged correctly and avoids the “JavaFX runtime components are missing” error.

3. **Run Spring Boot AOT to generate native artifacts:**
```cmd
.\mvnw -Pnative spring-boot:process-aot
```

4. **Run the tracing agent to collect reachability metadata:**
```cmd
java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image -jar target\example-1.0.0.jar
```
Interact with the application UI and exercise all features to capture complete metadata.

> [!NOTE]
> Update the JAR name if the artifact name or version changes.

5. **Build native executable:**
```cmd
.\mvnw -Pnative native:compile
```

### Optional
Skip tests by adding `-DskipTests` to any of the above Maven command.

## Demo Video
[Watch demo](https://drive.google.com/file/d/1pB09Dx_yZi2xPBBKaJPoTiz0n7HcAt6M/view)
> [!NOTE]
> A video to show the usage. Database setup instructions are not included.

## Cautionary Tale
Why use Spring Boot JDBC instead of JPA?

JPA relies on Hibernate ORM, which depends on reflection.
GraalVM has strict constraints around reflection, which makes Hibernate integration notoriously difficult.
Therefore, JDBC is the more viable choice, as it provides greater control and stability.

Attempts to work around these limitations require extensive configuration and often fail. I spent way too much time trying to make this work without success.

A potentially useful reference for enabling Hibernate with GraalVM is:

https://github.com/Vadym79/aws-lambda-java-25/tree/main/aws-lambda-java-25-hibernate-aurora-dsql-as-graalvm-native-image

And it's associating forum discussion:

https://discourse.hibernate.org/t/how-to-correctly-disable-byte-buddy-for-graalvm-native-image/12163

This example uses Hibernate and HikariCP directly. It does not include Spring Boot.

## Libraries
- [JavaFX](https://openjfx.io)
- [AtlantaFX](https://github.com/mkpaz/atlantafx)

## References
- [Gluon with Spring Boot example](https://github.com/cnico/GluonWithSpring/tree/main)
- [GraalVM Reachability Metadata](https://github.com/oracle/graalvm-reachability-metadata/tree/master)

## License
This project is licensed under the MIT License.

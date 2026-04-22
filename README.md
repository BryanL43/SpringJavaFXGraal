# SpringJavaFXGraal

## Overview
A minimal setup for a `Windows` desktop application compiled to a native image with GraalVM.
Uses Spring Boot JDBC and JavaFX, and builds with Maven.

Includes:
- Spring Boot JDBC for data access
- Optional AtlantaFX styling, remove the dependencies if you are not using it
- Inno Setup configuration for a custom non-admin installer
- CI/CD workflow to build and package the installer for release

## Quick Specifications
- Windows 10/11 OS
- Java 25 (LTS)
- GraalVM 25, Windows x64
- Spring Boot 4.0.4
- JavaFX 22
- PostgreSQL 42.7.3
- Inno Setup 6.7.1

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
1. **Navigate to the project directory:**
```cmd
cd <your-repo-directory>
```

2. **Create the `dev` configuration:**

Copy the example file and rename it:
```cmd
cp src\main\resources\application-dev.yml.example src\main\resources\application-dev.yml
```
Open `src\main\resources\application-dev.yml` and replace placeholders with your settings.

3. **Set active profile (points to dev configuration):**

Powershell:
```cmd
$env:SPRING_PROFILES_ACTIVE="dev"
```

Command Prompt:
```cmd
set SPRING_PROFILES_ACTIVE=dev
```

4. **Build the application JAR:**
```cmd
.\mvnw clean package
```
Builds the application and runs TestFX tests in headless mode using Monocle.

5. **Generate Spring Ahead-of-Time (AOT) Artifacts and Reachability Metadata:**
```cmd
.\mvnw test -Pnative-trace
```

This attaches the GraalVM tracing agent to TestFX to collect reachability metadata automatically.
Note that it will take control of your I/O because the tracing agent requires a non-headless UI.

> [!WARNING]
> Ensure your tests behave like end-to-end tests and exercise **ALL** UI flows through real user interactions.

6. **Build the native executable:**
```cmd
.\mvnw package -Pnative
```

7. **Build the installer via CLI (Inno Setup):**
```cmd
iscc installer.iss
```

If `iscc` is not in your PATH, use the full path to the compiler, for example:
```cmd
& "C:\Program Files (x86)\Inno Setup 6\ISCC.exe" installer\installer.iss
```

### Optional
Skip tests by adding `-DskipTests` to any of the above Maven command.

### Extra
For local development, use the run configuration in the `.run` directory or run manually:
```cmd
.\mvnw javafx:run
```
This builds the JAR and runs the application.

## GitHub Secrets

The CI/CD workflow requires database credentials stored as GitHub Secrets.

1. Go to your repository on GitHub
2. Open **Settings → Secrets and variables → Actions**
3. Add the following `Repository secrets`:

- `SPRING_DATASOURCE_JDBCURL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

Use the same values as your local `application-dev.yml`.

Example:
- `SPRING_DATASOURCE_JDBCURL=jdbc:postgresql://localhost:5432/your_database`
- `SPRING_DATASOURCE_USERNAME=your_username`
- `SPRING_DATASOURCE_PASSWORD=your_password`

These secrets are used by the workflow to run tests, build the native executable, and package the installer.

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
- [In-depth HikariCP configurations](https://oneuptime.com/blog/post/2025-07-02-java-hikaricp-connection-pooling/view)

## License
This project is licensed under the MIT License.

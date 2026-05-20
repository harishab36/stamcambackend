# AGENTS.md - StamCamBackend Developer Guide

## Project Overview
**StamCamBackend** is a Java 24 Maven project (org.fp:StamCamBackend:1.0-SNAPSHOT). It follows standard Maven directory structure with clear separation of production code, resources, and tests.

## Directory Structure & Code Organization

### Core Locations
- `/src/main/java/` - Production Java source code (currently empty, create package structure here)
- `/src/main/resources/` - Configuration files (properties, XML configs, etc.)
- `/src/test/java/` - Test source code organized mirror to `/src/main/java/`
- `/pom.xml` - Maven project definition (declares Java 24 as source/target)

### Expected Package Setup
When adding new features, create packages under `src/main/java/` following Java conventions:
- Example: `org.fp.stamcam.services`, `org.fp.stamcam.controllers`, `org.fp.stamcam.models`
- Mirror test structure: `src/test/java/org/fp/stamcam/services/` for test classes

## Build, Test & Compilation Commands

### Maven Workflows
```bash
# Compile project (Java 24 - required for compilation at source/target 24)
mvn clean compile

# Run all tests
mvn test

# Package as JAR
mvn package

# Skip tests during packaging
mvn package -DskipTests

# Run specific test class
mvn test -Dtest=YourTestClassName

# Clean build artifacts
mvn clean
```

### Local Development
- Use Maven Wrapper in `.mvn/wrapper/maven-wrapper.jar` for consistent builds
- IDE support: IntelliJ IDEA, Eclipse, NetBeans, VS Code all configured in `.gitignore`
- Build output goes to `target/` directory (git-ignored)

## Java 24 Specifics
- **Source/Target Version**: Java 24 (configured in `pom.xml`)
- This enables latest language features but requires Java 24 or later runtime
- Character encoding: UTF-8 (`project.build.sourceEncoding`)

## Key Development Patterns to Follow

### Adding Dependencies
Edit `pom.xml` - add `<dependency>` blocks within `<dependencies>` section. Common patterns:
- Web: Spring Boot (if REST API backend)
- Data: JDBC drivers, JPA/Hibernate
- Testing: JUnit 5, Mockito
- Example format:
  ```xml
  <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>5.9.3</version>
      <scope>test</scope>
  </dependency>
  ```

### Testing Convention
- Test class names end with `Test` or `Tests` suffix
- Place in `src/test/java/` matching production package structure
- Maven Surefire plugin automatically runs `*Test.java` classes

### Configuration Files
- Place application properties in `src/main/resources/` (e.g., `application.properties`)
- Access via classpath in code: templates, configs, bundled data

## Critical Tasks & Debugging

### Troubleshooting Builds
- **Java Version Error**: Verify Java 24+ is installed: `java --version`
- **Dependency Issues**: Run `mvn dependency:tree` to inspect dependency resolution
- **Stale Artifacts**: Run `mvn clean` before retry
- **IDE Sync Issues**: Reimport Maven project in IDE (IntelliJ: Maven panel → Reimport)

### Project Initialization
This is a skeleton - initial work should:
1. Define core packages and models in `src/main/java/`
2. Add necessary dependencies to `pom.xml` matching project goals
3. Create corresponding test structure in `src/test/java/`
4. Add configuration files to `src/main/resources/`

## Git & Ignored Artifacts
- `target/` directory is ignored - safe to delete, Maven regenerates
- `.idea/`, `.vscode/`, IDE-specific folders ignored
- `.DS_Store` ignored on macOS
- Only commit source code, pom.xml, and documentation

## IDE Integration Notes
- **IntelliJ IDEA**: `pom.xml` auto-detected; right-click project → "Reimport"
- **Maven Plugin**: Available in `.mvn/` wrapper; prefer `mvn` CLI for consistency
- **Run Configurations**: Create in IDE pointing to Maven goals (clean compile test)


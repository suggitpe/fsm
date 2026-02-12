# Technology Stack

## Build System

- Gradle (Groovy DSL)
- Gradle wrapper included for consistent builds

## Language & Runtime

- Kotlin 2.1.0
- Java 21 (source and target compatibility)
- Kotlin reflection support

## Core Dependencies

- SLF4J 2.0.16 for logging
- Logback 1.5.12 for logging implementation (test runtime)

## Testing Framework

- JUnit Jupiter 5.11.3 (JUnit 5)
- Kotest Assertions 5.9.1 for fluent assertions
- Mockito 5.14.2 for mocking
- PIT (Pitest) for mutation testing

## Common Commands

### Build and Test
```bash
# Clean, build, and run tests with coverage
./gradlew clean build jacocoTestReport

# Run tests only
./gradlew test

# Run mutation tests
./gradlew pitest
```

### Project Structure
```bash
# List all tasks
./gradlew tasks

# Build specific subproject
./gradlew :fsmlib:build
```

## Code Quality

- JaCoCo for test coverage reporting
- Pitest for mutation testing (4 threads, JUnit 5 plugin)
- Test results exported to JSON format in build/jacoco/test-results.json

## Logging

- Use SLF4J for all logging
- Logger instances created via companion objects: `LoggerFactory.getLogger(this::class.java)`
- Logback configuration in test resources (logback-test.xml)

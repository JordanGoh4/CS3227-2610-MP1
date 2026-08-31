# Luck Holiday Planner

Luck is a Java desktop travel planner that combines a task-management chatbot
with trip-specific itineraries, weather information, currency conversion, and
a JavaFX graphical interface.

## Prerequisites

- JDK 25
- Gradle 9.2 or newer, unless using the included Gradle wrapper
- An internet connection for weather and currency features

The project keeps all Java source code under `src/main/java`, which is the
Gradle source root.

## Set up the project

Open the project folder in IntelliJ IDEA or VS Code and configure the project
SDK to JDK 25. The Gradle wrapper is included, so Gradle does not need to be
installed separately.

## Build and test

Windows PowerShell:

```powershell
.\gradlew build
.\gradlew test
```

Linux or macOS:

```bash
./gradlew build
./gradlew test
```

## Run Luck

To launch the JavaFX travel-planner GUI:

```powershell
.\gradlew run
```

On Linux or macOS, use `./gradlew run`.

The configured GUI entry point is `luck.gui.Launcher`. The console entry point
is `luck.Luck`.

## Create and run the fat JAR

Generate the packaged application with:

```powershell
.\gradlew shadowJar
```

The output is written to `release/luck.jar`. Run it with:

```powershell
java -jar release\luck.jar
```

## Documentation

- [User Guide](docs/UserGuide.md) — setup, features, commands, and testing
- [Developer Guide](docs/DeveloperGuide.md) — architecture and development
- [Reflections](docs/Reflections.md) — AI-assisted software engineering
- [Development Logs](logs/prompts_summary.md) — consolidated project summaries

## Project structure

```text
src/main/java/luck/     Application source code
src/test/java/          JUnit tests
src/main/resources/     Bundled resources such as the GUI background
data/                   Persisted tasks, trips, and itineraries
docs/                   User, developer, and reflection documents
logs/                   Consolidated development summaries
release/                Latest packaged JAR
```

For detailed setup instructions and known limitations, refer to the User
Guide.

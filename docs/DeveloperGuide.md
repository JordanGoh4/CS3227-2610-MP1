# Luck Developer Guide

## Architecture

Luck is a Java 25 Gradle application. Its entry point is `luck.Luck`.

```text
src/main/java/luck/
├── Luck.java
├── command/    Command interface, handler, context, and command classes
├── model/      Task, Todo, Deadline, Event, TaskList, and TaskType
├── storage/    TaskStorage and TaskParser
├── ui/         ConsoleUI
├── util/       DateTimeParser
└── exception/  LuckException
```

## Command design

`CommandHandler` maps command keywords to objects implementing the `Command`
interface. `CommandContext` supplies the shared `TaskList`, `TaskStorage`,
and `ConsoleUI` services. Each command class performs one operation, which
makes new commands easy to add without expanding the main input loop.

To add a command:

1. Create a class in `luck.command` implementing `Command`.
2. Validate its arguments and throw `LuckException` for invalid input.
3. Register it in the `CommandHandler` constructor.
4. Add unit tests under the matching `luck.command` test package.
5. Update `docs/UserGuide.md`.

## Build and test

```powershell
gradle build
gradle test
gradle run
```

The project requires Java 25. Gradle uses the configured application entry
point `luck.Luck` and connects the `run` task to standard input.

## Fat JAR

The Shadow plugin creates a bundled application JAR:

```powershell
gradle shadowJar
```

The output is written to `build/libs/duke.jar` according to the current
Gradle configuration. It can be run with:

```powershell
java -jar build/libs/duke.jar
```

## Coding and Git standards

Java code follows `.codex/skills/seedu-java-coding-standard/SKILL.md`.
Commits and branch names follow `.codex/skills/seedu-git-standard/SKILL.md`.

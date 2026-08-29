# Luck Project Development Summary

This file records the important development work and decisions made for the project.
It is intended to help future development sessions quickly understand the current state.

## Core application development

- Renamed the chatbot from Duke to Luck.
- Refactored the application into packages under `src/main/java/luck`.
- Organised code into `command`, `model`, `storage`, `ui`, `util`, `exception`, and `gui` packages.
- Refactored commands into separate command classes managed by `CommandHandler`.
- Added date/time parsing and validation for deadlines and events.
- Added task deletion with the `delete` command.
- Added keyword search with the `find` command.
- Added persistence through `data/luck.txt`.

## Gradle and testing

- Added Gradle build support and JUnit 5 tests.
- Configured Java 25 compilation.
- Added tests for task lists, parsing, date/time handling, commands, and persistence.
- The test suite has passed after fixing locale-sensitive AM/PM formatting.
- The Shadow plugin is configured for fat JAR creation, but dependency resolution may fail if
  Gradle cannot access the Gradle Plugin Portal.
- The project is stored in OneDrive, which can lock files in `build`; stop Gradle/Java processes
  or pause OneDrive synchronisation if `gradle clean` cannot delete the build directory.

## Coding standards and project practice

- Added the project-specific `seedu-java-coding-standard` skill.
- Added the project-specific `seedu-git-standard` skill.
- Updated agent instructions to follow the Java coding standard and Git commit standard.
- Documentation was added or updated in the `docs` folder.

## GUI development

- Added JavaFX entry points: `luck.gui.Main` and `luck.gui.Launcher`.
- Replaced the Hello World screen with a two-panel Luck Travel Planner interface.
- Added an itinerary dashboard backed by the existing task list and storage.
- Added a chat panel connected to the existing `CommandHandler`.
- Added refresh and delete actions for itinerary items.
- The current GUI provides a foundation for future travel APIs such as weather, flights, hotels,
  and maps.
- Visual GUI testing is pending because computer-use support is not available in the current
  Codex session.

## Relevant commits

- `b3fecff` — Refactored Luck.java for more OOP
- `2af0c5f` — Added packages and changed folder structure
- `1f5c9cf` — Added delete command and refactored command files
- `cff833f` — Merged Gradle support
- `deb0e71` — Added Gradle and JUnit
- `e98ebb2` — Added coding standards
- `acdd276` — Added Git standard skill
- `d160acd` — Mandated Git standard in agent rules
- `1d4aea5` — Added task search command
- `6ee38d7` — Updated project documentation

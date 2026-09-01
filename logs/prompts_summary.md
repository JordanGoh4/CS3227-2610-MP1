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
- GUI command output is routed into the chat panel, and `bye` closes the GUI window.
- The GUI explains that `list` is unnecessary because the itinerary is always visible on the left.
- Organised the GUI into Itinerary, Weather, and Trip Info tabs with chat kept
  permanently on the right.
- Kept the chat interface permanently on the right while the travel tabs remain on the left.
- Weather commands now select the Weather tab automatically and show the latest result there.
- Task-related commands now select the Itinerary tab, and the chat input expands to the panel width.
- Visual GUI testing is pending because computer-use support is not available in the current
  Codex session.

## Travel feature development

- Added the first travel-specific feature: `weather <destination>`.
- Extended the weather command with `weather <destination> forecast` for five-day forecasts.
- Added `WeatherService` using Open-Meteo geocoding and forecast APIs.
- Added `WeatherCommand` and registered it with `CommandHandler`.
- Documented the command and API/network limitations.
- Added `currency <amount> <FROM> to <TO>` using the keyless Frankfurter API.
- Added persistent Trip Info fields for destination, dates, currency, and notes.
- Kept currency responses in chat instead of the Weather tab.

## Multi-trip planning

- Upgraded Trip Info to support multiple saved trips with selectable details,
  including destination, dates, currency, and notes.
- Selecting a trip now refreshes the Weather tab for that trip's destination in
  a background thread, with loading and error states.
- Updated the User Guide to explain GUI tabs, multi-trip itineraries, automatic
  weather refreshes, currency conversion, and storage locations.
- Restructured the User Guide around a concise, user-focused Features section
  and quick-start workflow.
- Split each chatbot command into its own User Guide subsection and documented
  known setup, network, and storage limitations.
- Restructured DeveloperGuide.md using the attached Developer Guide as a
  structural reference while adapting all content to Luck's architecture.
- Added Mermaid sequence diagrams for weather requests and trip itinerary
  switching to the Developer Guide.
- Added `src/main/resources/travel.png` as the high-definition JavaFX travel-planner background.
- Added JUnit coverage for trip validation, trip persistence, and invalid
  currency input, and expanded project documentation accordingly.
- Added a reflection on using structured documentation, sequence diagrams, and
  separate User and Developer Guides to communicate the evolving design.
- Strengthened Developer Guide acknowledgements with specific references for
  SE-EDU, JavaFX, Open-Meteo, Frankfurter, Gradle, Shadow, and JUnit.
- Refactored the JavaFX GUI by extracting shared styles and the Weather,
  Itinerary, Chat, and Trip Info panels from `Main.java`.
- Removed the redundant itinerary Refresh button because the GUI refreshes
  automatically after commands and trip changes.
- Added validation for recognised destinations, future trip dates, and ordered
  start/end dates, with tests for invalid destination and date scenarios.

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

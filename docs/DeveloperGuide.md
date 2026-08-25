# Developer Guide

## Purpose and Scope

This developer guide describes the design and maintenance considerations for the `Luck` project. The system is intentionally small: a single Java class that prints an ASCII banner.

## Repository Layout

- `src/main/java/Luck.java` — main application source.
- `docs/` — documentation (UserGuide, DeveloperGuide, Reflections).
- `logs/` — development interaction summaries.

## Coding Style

- Keep methods small and focused.
- Use descriptive names and constants for static content.
- Add Javadoc comments for public methods and classes.

## Build

This project uses the plain JDK toolchain and the Swing GUI toolkit. From repository root:

```bash
javac -d out src/main/java/Luck.java
```

Run with:

```bash
javac -d out src/main/java/Luck.java
java -cp out Luck
```

Notes on GUI:

- The application uses Swing and must be launched on the Event Dispatch Thread (EDT). The `main` method invokes `SwingUtilities.invokeLater` to create the GUI.
- The banner is displayed in a non-editable `JTextArea` with a monospaced font.

## Extending the Project

If you wish to extend the project:

- Add new classes under `src/main/java/` and keep package structure consistent.
- Add unit tests (recommended) in a `src/test/java/` hierarchy and use a build tool such as Maven or Gradle for test automation.

## Acknowledgements

- Portions of the repository documentation and small refactors were produced with assistance from an AI large language model. The code and documentation were reviewed and edited by the developers to ensure accuracy.

# User Guide

## Overview

`Luck` is a tiny Java command-line app that prints an ASCII banner to standard output. It is a minimal demonstration project intended for teaching and review.

## Features

- Print a multi-line ASCII banner to the console.

## Prerequisites

- Java JDK 8 or newer installed and reachable via `javac`/`java` on your PATH.

## Build & Run

From the repository root run:

```bash
javac -d out src/main/java/Luck.java
java -cp out Luck
```

This will compile `Luck.java` into the `out` directory and run the `Luck` main class. The program opens a small desktop window that displays the ASCII banner.

## Testing

This project has no automated tests. To manually test, run the commands above and verify a window titled "Luck" opens showing the ASCII banner.

## Project Structure

- `src/main/java/Luck.java` — application entry point.
- `docs/` — user and developer documentation.
- `logs/` — development interaction summaries.

## Contact

For questions about the code or documentation, open an issue or contact the repository maintainer.

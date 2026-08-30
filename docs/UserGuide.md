# Luck User Guide

## Overview

Luck is a Java 25 command-line task manager. It lets users create, view,
search, complete, uncomplete, and delete todo, deadline, and event tasks.

## Prerequisites

- Java Development Kit (JDK) 25.
- Gradle 9.2 or newer.

Verify the installations with:

```powershell
java -version
gradle --version
```

## Build and run

From the repository root:

```powershell
gradle build
gradle run
```

## Commands

```text
todo <description>
deadline <description> /by <date>
event <description> /from <start> /to <end>
list
find <keyword>
mark <task number>
unmark <task number>
delete <task number>
bye
```

Examples:

```text
todo pack passport
deadline submit report /by 25/08/2026 1430
event flight /from 25/08/2026 0900 /to 25/08/2026 1200
find passport
mark 1
delete 2
```

Deadline dates must use a supported numeric format such as `25/08/2026`,
`25/08/2026 1430`, or `25/08/2026 14:30`. Invalid commands are rejected
without changing the task list.

## Saving tasks

Luck automatically saves tasks to `data/luck.txt` after task changes and
loads them when the application starts.

## Testing

Run the automated JUnit tests with:

```powershell
gradle test
```

The tests cover command validation, task-list operations, parsing, date/time
handling, persistence, and task searching.
## Travel weather

Use `weather <destination>` to retrieve the current weather for a destination.
For example:

```text
weather Tokyo
```

This feature requires an internet connection and uses Open-Meteo's geocoding and
weather forecast APIs. If the service is unavailable, Luck displays an error.

To see a five-day forecast, add `forecast` after the destination:

```text
weather Tokyo forecast
```

## Currency conversion

Use the latest available exchange rate with:

```text
currency 100 USD to JPY
```

This feature requires an internet connection and uses the keyless Frankfurter API.

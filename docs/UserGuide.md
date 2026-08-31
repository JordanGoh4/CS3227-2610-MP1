# Luck User Guide

## Overview

Luck is a desktop travel-planning assistant with a chat interface. Use it to
organise trips, manage itineraries, check weather, and convert currencies.

## Features

- Create and manage multiple trips.
- Keep a separate itinerary for each trip.
- Add, search, complete, and delete itinerary tasks.
- View weather for the selected destination.
- Check a five-day weather forecast.
- Convert currencies using the latest available exchange rate.
- Save trip details and itineraries locally.

## Quick start

Install JDK 25 and Gradle 9.2 or newer. From the project root, run:

```powershell
gradle run
```

The GUI keeps the travel tabs on the left and the chat interface on the right.
Type a command in the chat box and press Enter or select **Send**.

To create a distributable JAR, run:

```powershell
gradle shadowJar
java -jar release/luck.jar
```

## Managing trips

Open the **Trip Info** tab and select **New trip**. Enter a trip name,
destination, dates, home currency, and notes, then select **Save trip details**.

Selecting a different trip loads its own itinerary. New tasks are added to the
currently selected trip. The Weather tab also refreshes for the selected trip's
destination.

## Commands

### Add a todo: `todo`

Adds a simple task to the selected trip.

Format:

```text
todo <description>
```

Example:

```text
todo book hotel
```

### Add a deadline: `deadline`

Adds a task with a due date.

Format:

```text
deadline <description> /by <date>
```

Example:

```text
deadline renew passport /by 25/08/2026
```

### Add an event: `event`

Adds a task with a start and end time.

Format:

```text
event <description> /from <start> /to <end>
```

Example:

```text
event visit museum /from 25/08/2026 0900 /to 25/08/2026 1200
```

### Find tasks: `find`

Finds tasks whose descriptions contain a keyword, ignoring case.

Format:

```text
find <keyword>
```

Example:

```text
find hotel
```

### Mark a task: `mark`

Marks a task as completed using its itinerary number.

Format:

```text
mark <task number>
```

### Unmark a task: `unmark`

Marks a completed task as not completed.

Format:

```text
unmark <task number>
```

### Delete a task: `delete`

Deletes a task from the selected trip.

Format:

```text
delete <task number>
```

### Check weather: `weather`

Shows current weather for a destination and updates the Weather tab.

Format:

```text
weather <destination>
```

Example:

```text
weather Tokyo
```

Use `forecast` to display a five-day forecast:

```text
weather Tokyo forecast
```

### Convert currency: `currency`

Converts an amount using the latest available exchange rate.

Format:

```text
currency <amount> <FROM> to <TO>
```

Example:

```text
currency 100 USD to JPY
```

### Exit Luck: `bye`

Closes the GUI or ends the console session.

```text
bye
```

Dates should use a numeric format such as `25/08/2026`, `25/08/2026 1430`,
or `25/08/2026 14:30`.

## Data and internet access

GUI trip details are saved in `data/trip-info.properties`, and trip itineraries
are saved in `data/trips/`. The console task list uses `data/luck.txt`.

Weather and currency commands require an internet connection. If an API is
unavailable, Luck displays an error without closing the application.

## Known issues

- Weather and currency results depend on external APIs and cannot be retrieved
  without an internet connection.
- The GUI requires JavaFX dependencies and JDK 25.
- Gradle may be unable to delete the `build` folder while the GUI, VS Code,
  Java, or OneDrive is using compiled files. Stop those processes before using
  `gradle clean`.
- Automatic currency suggestions currently cover a limited set of common
  destination countries; users can still enter a currency code manually.

## Testing

Run the automated tests with:

```powershell
gradle test
```

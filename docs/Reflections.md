# Reflections on AI-assisted Software Engineering

This document reflects on using AI (LLMs) to assist in developing Luck,
including design, implementation, testing, documentation, and code quality.

## Prompt Example 1 — Refactor source to improve readability

- Prompt: "Refactor `Luck.java` to extract the ASCII banner into a constant, add a getter, and include Javadoc; keep behavior identical."
- Why: The prompt is specific about the desired refactor and the constraints (behavior must be identical). This led to a minimal, safe change that improved maintainability.
- Outcome: The banner was moved to a `private static final` constant and a `getBanner()` method was added. This makes testability and reuse easier.

## Prompt Example 2 — Generate a User Guide

- Prompt: "Write a concise User Guide for this Java CLI app that explains prerequisites, build and run commands, and manual testing steps."
- Why: Asking for a concise and structured user guide produced documentation that peer testers can follow easily.
- Outcome: `docs/UserGuide.md` was updated with the current Gradle commands,
  supported commands, persistence behavior, and test instructions.

## Prompt Example 3 — Create a Reflection and Logs

- Prompt: "Create a Reflections.md describing three interesting prompts used and create logs summarizing the development interactions."
- Why: This meta prompt encouraged the creation of both reflective material and verifiable logs to support grading and review.
- Outcome: `docs/Reflections.md` and entries in `logs/` were added. The logs provide an audit trail of actions taken.

## Lessons Learned

- Precise prompts yield conservative, safe code changes.
- Always verify AI-generated output; small mistakes can creep in with assumptions about environment or tooling.
- Use AI to draft documentation and boilerplate; humans should review for accuracy.

## Prompt Example 4 — Organize commands using OOP

- Prompt: "Create separate command classes under the `luck.command` package."
- Why: Separating command responsibilities makes the code easier to extend and
  allows future travel-related commands to be added independently.
- Outcome: `CommandHandler` now dispatches to command objects such as
  `FindCommand`, `DeleteCommand`, and `DeadlineCommand`.

## Prompt Example 5 — Add high-value tests

- Prompt: "Add JUnit tests for the most important task, parsing, storage, and
  command behaviors."
- Why: Focused tests provide confidence in core behavior while keeping the test
  suite maintainable.
- Outcome: Tests cover approximately the top 50% of high-value methods,
  including validation, persistence, searching, and task-list operations.

## Testing commitment

Tests should be reviewed and updated after every code change. The coverage
target prioritizes complex, core, and critical business logic rather than
maximizing a percentage without considering test value.

## Test Coverage Target

JUnit tests focus on approximately the top 50% of the highest-value methods, prioritising
core task-list operations, command validation, parsing, and persistence behavior. Tests
should be reviewed and updated after every code change so that they continue to reflect
Luck's current package structure and behavior.

## Additional travel-feature testing

Tests were expanded for trip validation, multiple-trip persistence, and invalid
currency input. Network-dependent API calls are excluded from unit tests because
external services can be unavailable or return changing data; those integrations
are verified manually and handled with user-friendly errors.

## Corrections after follow-up prompts

Some initial AI-generated changes did not fully match the intended design.
The first travel GUI placed the itinerary and chat in a tab arrangement,
although the desired workflow was to keep chat permanently on the right and
the travel tabs on the left. Follow-up prompting led to a split layout and
automatic selection of the Weather or Itinerary tab after commands.

The first GUI command integration also allowed command output to appear in the
terminal and did not close the window when `bye` was entered. This was corrected
by adding a GUI-specific output adapter and handling the boolean result returned
by `CommandHandler`.

The first multi-trip implementation stored separate trip details but still
used one shared itinerary. Further prompting identified this mismatch and led
to trip-specific task storage and itinerary switching.

The background image was initially too small and made text difficult to read.
Follow-up changes generated a higher-definition resource and added translucent
dark panels, light text, and improved panel sizing.

These corrections showed that AI output must be checked against the intended
user workflow, not only whether the code compiles. Clearer follow-up prompts
and manual GUI review were necessary to reveal interaction and presentation
problems that unit tests would not catch.

## Documentation and design communication

The Developer Guide was reorganised using the supplied Developer Guide as a
structural reference. The reference helped identify useful sections such as
architecture, components, implementation details, testing, and limitations.

Mermaid sequence diagrams were added for the weather command and trip
selection flow. These diagrams make interactions between the GUI, command
handler, services, storage, and external APIs easier to understand than prose
alone.

The documentation required several revisions because the application changed
from a simple task manager into a multi-trip travel planner. This reinforced
the importance of updating documentation after architectural and feature
changes rather than treating it as a final submission task.

The User Guide and Developer Guide serve different audiences. The User Guide
is intentionally concise and task-oriented, while the Developer Guide gives
more detail about architecture, implementation, testing, and limitations.

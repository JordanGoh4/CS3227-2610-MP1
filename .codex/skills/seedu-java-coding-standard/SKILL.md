---
name: seedu-java-coding-standard
description: Apply the SE-EDU intermediate Java coding standard when creating, reviewing, or modifying Java code in this project.
---

# Luck Java coding standard

Apply the [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html)
to all Java source and test code in this repository. Use the Google Java Style Guide for topics not covered by SE-EDU.

- Keep every class in a suitable lowercase package.
- Use PascalCase classes, camelCase methods/variables, and SCREAMING_SNAKE_CASE constants.
- Use explicit imports; never use wildcard imports.
- Use four-space indentation, K&R braces, and lines no longer than 120 characters.
- Always use braces for conditionals and loops.
- Keep variables in the smallest practical scope and encapsulate class state.
- Add descriptive Javadocs to public classes and public methods.
- Name tests with `featureUnderTest_testScenario_expectedBehavior()` where useful.
- Preserve behavior during style-only refactors and run relevant Gradle tests afterward.

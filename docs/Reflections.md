# Reflections on AI-assisted Software Engineering

This document reflects on using AI (LLMs) to assist in software engineering tasks for this small project. Below are three example prompts used during development and explanations of why they were helpful.

## Prompt Example 1 — Refactor source to improve readability

- Prompt: "Refactor `Luck.java` to extract the ASCII banner into a constant, add a getter, and include Javadoc; keep behavior identical."
- Why: The prompt is specific about the desired refactor and the constraints (behavior must be identical). This led to a minimal, safe change that improved maintainability.
- Outcome: The banner was moved to a `private static final` constant and a `getBanner()` method was added. This makes testability and reuse easier.

## Prompt Example 2 — Generate a User Guide

- Prompt: "Write a concise User Guide for this Java CLI app that explains prerequisites, build and run commands, and manual testing steps."
- Why: Asking for a concise and structured user guide produced documentation that peer testers can follow easily.
- Outcome: `docs/UserGuide.md` was created with exact commands to compile and run the app.

## Prompt Example 3 — Create a Reflection and Logs

- Prompt: "Create a Reflections.md describing three interesting prompts used and create logs summarizing the development interactions."
- Why: This meta prompt encouraged the creation of both reflective material and verifiable logs to support grading and review.
- Outcome: `docs/Reflections.md` and entries in `logs/` were added. The logs provide an audit trail of actions taken.

## Lessons Learned

- Precise prompts yield conservative, safe code changes.
- Always verify AI-generated output; small mistakes can creep in with assumptions about environment or tooling.
- Use AI to draft documentation and boilerplate; humans should review for accuracy.

---
name: test-ui
description: Run console UI test cases from a command/output plan, compare actual vs expected output, stop on failure, and show the recorded session log.
---

# Test UI

Run the current Java console app against a list of command/output test cases recorded in `test/ui-test-plan.md`.

## Inputs

- Read the test plan file from `test/ui-test-plan.md`.
- Each test case must define:
  - aim
  - inputs
  - expected output
- Use the program under test from the repository root.

## Execution process

1. For each test case in order:
   1. Prepare the command list for the test case.
   2. Run the program with those inputs.
   3. Capture the full console input and output for that session.
   4. Compare the actual console output against the expected output.
   5. If the output differs, stop immediately and report:
      - the test case name / aim
      - the actual output
      - the expected output
      - the console input record
      - a fail result

2. If all tests pass, report the full recorded session logs and a pass result.

## Output requirements

- Show the command input and console output for each case in a plain-text record.
- Preserve the command order exactly as run.
- If there is a mismatch, terminate the session immediately and do not continue to later tests.
- Keep the output concise but complete enough to verify the behavior.

## Implementation notes

- Use the project root as the working directory.
- Prefer a direct console runner such as `java -cp out Luck` after compilation.
- If the program is not built, compile it first with `javac -d out src/main/java/*.java`.
- If a test case fails, do not suppress the actual output; show both expected and actual outputs in the failure report.

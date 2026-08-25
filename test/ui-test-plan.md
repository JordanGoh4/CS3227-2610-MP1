# UI Test Plan

This file records the command-output checks for the console UI.

## Test Session Format

Each test case includes:
- aim
- inputs
- expected output
- actual output recorded during execution
- pass/fail result

## Test Cases

### Test Case 1: Basic greeting
- Aim: Confirm the chatbot greets the user and exits on bye.
- Inputs:
  - `bye`
- Expected output:
  ```text
  ____________________________________________________________
  [CHATBOT BANNER]
  Hello! I'm Luck.
  What can I do for you?
  ____________________________________________________________
  Bye. Hope to see you again soon!
  ____________________________________________________________
  ```

### Test Case 2: Echo list command
- Aim: Confirm the app echoes user input and handles `list` without crashing.
- Inputs:
  - `list`
  - `bye`
- Expected output:
  ```text
  ____________________________________________________________
  [CHATBOT BANNER]
  Hello! I'm Luck.
  What can I do for you?
  ____________________________________________________________
  ____________________________________________________________
     list
  ____________________________________________________________
  ____________________________________________________________
     Bye. Hope to see you again soon!
  ____________________________________________________________
  ```

### Test Case 3: Task creation and listing
- Aim: Confirm that normal text is stored and then listed.
- Inputs:
  - `read book`
  - `return book`
  - `list`
  - `bye`
- Expected output:
  ```text
  ____________________________________________________________
  [CHATBOT BANNER]
  Hello! I'm Luck.
  What can I do for you?
  ____________________________________________________________
  ____________________________________________________________
     read book
  ____________________________________________________________
  ____________________________________________________________
     added: read book
  ____________________________________________________________
  ____________________________________________________________
     return book
  ____________________________________________________________
  ____________________________________________________________
     added: return book
  ____________________________________________________________
  ____________________________________________________________
     Here are the tasks in your list:
     1.[ ] read book
     2.[ ] return book
  ____________________________________________________________
  ____________________________________________________________
     Bye. Hope to see you again soon!
  ____________________________________________________________
  ```

## Test Execution Rules

- Run each test case in order.
- Record the input and actual console output.
- For each case, compare the actual output against the expected output.
- If a mismatch is detected, stop the test session immediately.
- Report both the expected and actual outputs, then halt further testing.

## Failure Handling

If a command output differs from the expected result:
- print a failure summary
- show the expected output
- show the actual output
- stop the session immediately

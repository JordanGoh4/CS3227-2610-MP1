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
  - `todo read book`
  - `todo return book`
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
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
     Got it. I've added this task:
       [T][ ] return book
     Now you have 2 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[T][ ] return book
  ____________________________________________________________
  ____________________________________________________________
     Bye. Hope to see you again soon!
  ____________________________________________________________
  ```

### Test Case 4: Save tasks to disk on happy path
- Aim: Confirm that the task list is written to `data/duke.txt` after valid task changes.
- Inputs:
  - `todo read book`
  - `deadline return book /by Sunday`
  - `event project meeting /from Mon 2pm /to 4pm`
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
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Sunday)
     Now you have 2 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 3 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: Sunday)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
  ____________________________________________________________
  ____________________________________________________________
     Bye. Hope to see you again soon!
  ____________________________________________________________
  ```

- Expected file content:
  ```text
  T | 0 | read book
  D | 0 | return book | Sunday
  E | 0 | project meeting | Mon 2pm to 4pm
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

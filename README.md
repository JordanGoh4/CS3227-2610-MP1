# CS3227 Holiday Planner (scaffold)

Minimal instructions to build and run the project.

Prerequisites
- Java JDK 17 installed and on your PATH.

Build with Gradle wrapper (recommended):

Windows PowerShell:
```powershell
.\gradlew build
.\gradlew test
```

Unix/macOS:
```bash
./gradlew build
./gradlew test
```

Notes:
- The Gradle build config includes JavaFX settings. The application `mainClass` is not set yet — the UI/Launcher will be added later.
- You can compile and run single Java files directly using `javac` / `java` for quick checks. Example (from project root):

```bash
javac -d out src/main/java/Luck.java
java -cp out Luck
```

If you want me to add a Gradle `application.mainClass` and a runnable JavaFX launcher now, tell me and I'll scaffold it.
# Luck project template

This is a project template for a greenfield Java project. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Luck.java` file, right-click it, and choose `Run Luck.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    ____        _        
   |  _ \ _   _| | _____ 
   | | | | | | | |/ / _ \
   | |_| | |_| |   <  __/
   |____/ \__,_|_|\_\___|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

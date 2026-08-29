package luck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;

import luck.command.CommandContext;
import luck.command.CommandHandler;
import luck.exception.LuckException;
import luck.model.TaskList;
import luck.storage.TaskStorage;
import luck.ui.ConsoleUI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests Luck commands through the application's OOP command structure. */
class LuckStateValidationTest {
    private TaskList taskList;
    private TaskStorage storage;
    private CommandHandler handler;
    private Path dataFile;

    @BeforeEach
    void setUp() throws Exception {
        dataFile = Files.createTempFile("luck-test", ".txt");
        Files.deleteIfExists(dataFile);

        taskList = new TaskList();
        storage = new TaskStorage(dataFile);
        handler = new CommandHandler(
                new CommandContext(taskList, storage, new ConsoleUI()));
    }

    @Test
    void todoCommand_validInput_taskAdded() throws Exception {
        handler.handle("todo buy milk");

        assertEquals(1, taskList.size());
        assertEquals("buy milk", taskList.get(0).getDescription());
    }

    @Test
    void todoCommand_emptyInput_taskListUnchanged() {
        assertThrows(LuckException.class, () -> handler.handle("todo   "));

        assertEquals(0, taskList.size());
    }

    @Test
    void deadlineCommand_validDate_taskAdded() throws Exception {
        handler.handle("deadline submit report /by 25/08/2026");

        assertEquals(1, taskList.size());
        assertEquals("D", taskList.get(0).getTypeIcon());
    }

    @Test
    void deadlineCommand_invalidDate_exceptionThrown() {
        assertThrows(LuckException.class,
                () -> handler.handle("deadline submit report /by 25-08-2026"));

        assertEquals(0, taskList.size());
    }

    @Test
    void eventCommand_validInput_taskAdded() throws Exception {
        handler.handle("event team sync /from 2pm /to 4pm");

        assertEquals(1, taskList.size());
        assertEquals("E", taskList.get(0).getTypeIcon());
    }

    @Test
    void markCommands_validIndex_statusUpdated() throws Exception {
        handler.handle("todo study for exam");

        handler.handle("mark 1");
        assertEquals("X", taskList.get(0).getStatusIcon());

        handler.handle("unmark 1");
        assertEquals(" ", taskList.get(0).getStatusIcon());
    }

    @Test
    void deleteCommand_validIndex_taskRemoved() throws Exception {
        handler.handle("todo buy milk");
        handler.handle("todo read book");

        handler.handle("delete 1");

        assertEquals(1, taskList.size());
        assertEquals("read book", taskList.get(0).getDescription());
    }

    @Test
    void deleteCommand_invalidIndex_taskListUnchanged() throws Exception {
        handler.handle("todo buy milk");

        assertThrows(LuckException.class, () -> handler.handle("delete 99"));

        assertEquals(1, taskList.size());
        assertEquals("buy milk", taskList.get(0).getDescription());
    }

    @Test
    void taskCommands_validInput_tasksPersisted() throws Exception {
        handler.handle("todo read book");
        handler.handle("deadline return book /by 25/08/2026");
        handler.handle("event project meeting /from 2pm /to 4pm");

        TaskStorage reloadedStorage = new TaskStorage(dataFile);
        assertEquals(3, reloadedStorage.loadTasks().size());
    }

    @Test
    void byeCommand_executed_sessionEnds() throws Exception {
        assertFalse(handler.handle("bye"));
    }
}

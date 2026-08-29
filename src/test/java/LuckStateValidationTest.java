package luck;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import luck.exception.LuckException;
import luck.model.Task;

class LuckStateValidationTest {
    private static final String METHOD_ADD_TODO = "addTodoTask";
    private static final String METHOD_ADD_DEADLINE = "addDeadlineTask";
    private static final String METHOD_ADD_EVENT = "addEventTask";
    private static final String METHOD_MARK = "markTask";
    private static final String METHOD_UNMARK = "unmarkTask";

    @BeforeEach
    void resetState() throws Exception {
        java.nio.file.Path dataFile = java.nio.file.Path.of("data", "duke.txt");
        java.nio.file.Files.createDirectories(dataFile.getParent());
        java.nio.file.Files.deleteIfExists(dataFile);

        Field taskCountField = Luck.class.getDeclaredField("taskCount");
        taskCountField.setAccessible(true);
        taskCountField.setInt(null, 0);

        Field tasksField = Luck.class.getDeclaredField("TASKS");
        tasksField.setAccessible(true);
        Task[] tasks = (Task[]) tasksField.get(null);
        java.util.Arrays.fill(tasks, null);
    }

    @Test
    void interleaved_valid_and_invalid_todo_inputs_keep_state_consistent() throws Exception {
        invokeAndExpectNoException(METHOD_ADD_TODO, "buy milk");
        assertEquals(1, getTaskCount());

        invokeAndExpectLuckException(METHOD_ADD_TODO, "   ", "This can't be empty, do better.");
        assertEquals(1, getTaskCount());
        assertEquals("buy milk", getTaskAt(0).getDescription());

        invokeAndExpectNoException(METHOD_ADD_TODO, "read book");
        assertEquals(2, getTaskCount());

        invokeAndExpectLuckException(METHOD_MARK, "99", "This ain't valid my friend.");
        assertEquals(2, getTaskCount());
        assertEquals(" ", getTaskAt(1).getStatusIcon());

        invokeAndExpectNoException(METHOD_MARK, "2");
        assertEquals("X", getTaskAt(1).getStatusIcon());
    }

    @Test
    void invalid_deadline_and_event_inputs_do_not_corrupt_internal_task_list() throws Exception {
        invokeAndExpectNoException(METHOD_ADD_DEADLINE, "submit report /by Friday");
        assertEquals(1, getTaskCount());
        assertEquals("D", getTaskAt(0).getTypeIcon());

        invokeAndExpectLuckException(METHOD_ADD_DEADLINE, "submit report", "This ain't valid my friend.");
        assertEquals(1, getTaskCount());
        assertEquals("submit report", getTaskAt(0).getDescription());

        invokeAndExpectNoException(METHOD_ADD_EVENT, "team sync /from 2pm /to 4pm");
        assertEquals(2, getTaskCount());
        assertEquals("E", getTaskAt(1).getTypeIcon());

        invokeAndExpectLuckException(METHOD_ADD_EVENT, "team sync /from 2pm", "This ain't valid my friend.");
        assertEquals(2, getTaskCount());
        assertEquals("team sync", getTaskAt(1).getDescription());
    }

    @Test
    void invalid_mark_and_unmark_commands_do_not_change_valid_task_state() throws Exception {
        invokeAndExpectNoException(METHOD_ADD_TODO, "study for exam");
        invokeAndExpectNoException(METHOD_ADD_TODO, "submit assignment");

        invokeAndExpectNoException(METHOD_MARK, "1");
        assertEquals("X", getTaskAt(0).getStatusIcon());

        invokeAndExpectLuckException(METHOD_MARK, "abc", "This ain't valid my friend.");
        assertEquals("X", getTaskAt(0).getStatusIcon());
        assertEquals(" ", getTaskAt(1).getStatusIcon());

        invokeAndExpectNoException(METHOD_UNMARK, "1");
        assertEquals(" ", getTaskAt(0).getStatusIcon());

        invokeAndExpectLuckException(METHOD_UNMARK, "0", "This ain't valid my friend.");
        assertEquals(" ", getTaskAt(0).getStatusIcon());
        assertEquals(" ", getTaskAt(1).getStatusIcon());
    }

    @Test
    void saveTasksWritesExpectedFormatToDisk() throws Exception {
        java.nio.file.Path dataFile = java.nio.file.Path.of("data", "luck.txt");
        java.nio.file.Files.createDirectories(dataFile.getParent());
        java.nio.file.Files.deleteIfExists(dataFile);

        invokeAndExpectNoException(METHOD_ADD_TODO, "read book");
        invokeAndExpectNoException(METHOD_ADD_DEADLINE, "return book /by Sunday");
        invokeAndExpectNoException(METHOD_ADD_EVENT, "project meeting /from Mon 2pm /to 4pm");

        String content = java.nio.file.Files.readString(dataFile);
        assertTrue(content.contains("T | 0 | read book"));
        assertTrue(content.contains("D | 0 | return book | Sunday"));
        assertTrue(content.contains("E | 0 | project meeting | Mon 2pm to 4pm"));
    }

    private static void invokeAndExpectNoException(String methodName, String arg) throws Exception {
        Method method = Luck.class.getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);
        method.invoke(null, arg);
    }

    private static void invokeAndExpectLuckException(String methodName, String arg, String expectedMessage)
            throws Exception {
        Method method = Luck.class.getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);

        try {
            method.invoke(null, arg);
            fail("Expected LuckException to be thrown for: " + arg);
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof LuckException,
                    "Expected LuckException but got: " + e.getCause());
            assertEquals(expectedMessage, e.getCause().getMessage());
        }
    }

    private static int getTaskCount() throws Exception {
        Field field = Luck.class.getDeclaredField("taskCount");
        field.setAccessible(true);
        return field.getInt(null);
    }

    private static Task getTaskAt(int index) throws Exception {
        Field field = Luck.class.getDeclaredField("TASKS");
        field.setAccessible(true);
        Task[] tasks = (Task[]) field.get(null);
        return tasks[index];
    }
}

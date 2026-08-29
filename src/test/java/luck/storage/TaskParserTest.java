package luck.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import luck.model.Deadline;
import luck.model.Event;
import luck.model.Task;

/** Tests conversion between stored text and task objects. */
class TaskParserTest {
    private final TaskParser parser = new TaskParser();

    @Test
    void parseAll_validContent_supportedTasksCreated() {
        String content = "T | 0 | buy milk\n"
                + "D | 1 | submit report | 2026-08-25T00:00\n"
                + "E | 0 | meeting | 2pm to 4pm";

        List<Task> tasks = parser.parseAll(content);

        assertEquals(3, tasks.size());
        assertEquals("buy milk", tasks.get(0).getDescription());
        assertTrue(tasks.get(1) instanceof Deadline);
        assertTrue(tasks.get(1).isDone());
        assertTrue(tasks.get(2) instanceof Event);
    }

    @Test
    void serialize_todoTask_expectedFormat() {
        String result = parser.serialize(new luck.model.Todo("read book"));

        assertEquals("T | 0 | read book", result);
    }

    @Test
    void parseAll_malformedLine_emptyResult() {
        assertTrue(parser.parseAll("not a valid task").isEmpty());
    }
}

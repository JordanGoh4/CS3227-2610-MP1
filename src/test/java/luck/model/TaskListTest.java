package luck.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import luck.exception.LuckException;

/** Tests task-list collection operations and index validation. */
class TaskListTest {
    @Test
    void addAndGet_tasksReturnedInInsertionOrder() throws Exception {
        TaskList list = new TaskList();
        list.add(new Todo("first"));
        list.add(new Todo("second"));

        assertEquals(2, list.size());
        assertEquals("first", list.get(0).getDescription());
        assertEquals("second", list.get(1).getDescription());
    }

    @Test
    void remove_validIndex_taskDeleted() throws Exception {
        TaskList list = new TaskList();
        list.add(new Todo("first"));
        list.add(new Todo("second"));

        list.remove(0);

        assertEquals(1, list.size());
        assertEquals("second", list.get(0).getDescription());
    }

    @Test
    void getAndRemove_invalidIndex_exceptionThrown() {
        TaskList list = new TaskList();

        assertThrows(LuckException.class, () -> list.get(0));
        assertThrows(LuckException.class, () -> list.remove(-1));
    }
}

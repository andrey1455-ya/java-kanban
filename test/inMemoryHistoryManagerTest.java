import manager.HistoryManager;
import manager.Managers;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class inMemoryHistoryManagerTest {


    private HistoryManager historyManager;

    @BeforeEach
    public void createManager() {
        historyManager = Managers.getDefaultHistory();
    }

    Task task1 = new Task(1, "Task 1", "Task 1 Description");
    Task task2 = new Task(2, "Task 2", "Task 2 Description");
    Task task3 = new Task(3, "Task 3", "Task 3 Description");

    @Test
    void shouldTaskAddToHistory() {
        historyManager.addTaskToHistory(task1);
        assertTrue(historyManager.getHistory().contains(task1));
    }

    @Test
    void shouldTaskRemoveFromHistory() {
        historyManager.addTaskToHistory(task1);
        historyManager.remove(1);
        assertFalse(historyManager.getHistory().contains(task1));
    }

    @Test
    void shouldGetEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void addTaskWithEqualId() {
        Task task4 = new Task(4, "Task 4", "Task 5 Description");
        Task task5 = new Task(4, "Task 5", "Task 4 Description");
        historyManager.addTaskToHistory(task4);
        historyManager.addTaskToHistory(task5);
        assertEquals(1, historyManager.getHistory().size());
        assertTrue(historyManager.getHistory().contains(task5));
    }

    @Test
    void shouldFirstTaskRemoveFromHistory() {
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        historyManager.remove(task1.getId());
        ArrayList<Task> thisHistory = new ArrayList<>();
        thisHistory.add(task2);
        thisHistory.add(task3);
        assertEquals(historyManager.getHistory(), thisHistory);
    }

    @Test
    void shouldMiddleTaskRemoveFromHistory() {
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        historyManager.remove(task2.getId());
        ArrayList<Task> thisHistory = new ArrayList<>();
        thisHistory.add(task1);
        thisHistory.add(task3);
        assertEquals(historyManager.getHistory(), thisHistory);
    }

    @Test
    void shouldLastTaskRemoveFromHistory() {
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        historyManager.remove(task3.getId());
        ArrayList<Task> thisHistory = new ArrayList<>();
        thisHistory.add(task1);
        thisHistory.add(task2);
        assertEquals(historyManager.getHistory(), thisHistory);
    }

    @Test
    void shouldTaskFromEmptyHistory() {
        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldRemoveOneTask() {
        historyManager.addTaskToHistory(task1);
        historyManager.remove(task1.getId());
        assertTrue(historyManager.getHistory().isEmpty());
    }
}

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T inMemoryTaskManager;

    @BeforeEach
    public void initTaskManager() throws IOException {
        inMemoryTaskManager = createTaskManager();
    }

    protected abstract T createTaskManager() throws IOException;

    @Test
    public void shouldBeInMemoryTaskManagerPutAllTaskTypes() {
        //prepare
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask1 = new Subtask(1, 2, "Subtask1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        //do
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask1);
        //check
        assertEquals(task1, inMemoryTaskManager.getTaskById(1), "Таска из менеджера не равна добавленной");
        assertEquals(epic1, inMemoryTaskManager.getEpicById(2), "Эпик из менеджера не равен добавленному");
        assertEquals(subtask1, inMemoryTaskManager.getSubtaskById(3), "Сабтаска из менеджера не равна добавленной");
    }

    @Test
    public void shouldNotConflictTasksWithOneId() {
        Task task1 = new Task(1, "task 1", "desc task 1", Status.NEW, Duration.ofMinutes(10)
                , LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Task task2 = new Task(2, "task 2", "desc task 2", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now());
        inMemoryTaskManager.addNewTask(task2);
        Integer id2 = inMemoryTaskManager.addNewTask(task1).getId();
        assertEquals(task1.getId(), id2);
    }

    @Test
    public void shouldTestNotChangeAddInMemoryTaskManager() {
        Task task1 = new Task(1, "task 1", "desc task 1", Status.NEW, Duration.ofMinutes(1), LocalDateTime.now());
        inMemoryTaskManager.addNewTask(task1);
        Task task2 = inMemoryTaskManager.getTaskById(1);
        assertEquals(task1, task2);
    }
}

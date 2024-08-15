import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class inMemoryHistoryManagerTest {
    private TaskManager inMemoryTaskManager = Managers.getDefault();

    @Test
    public void shouldBeTasksAddedToHistory() {//задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
        //prepare
        Task task1 = new Task(1, "Task 1", "Description 1");
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Subtask subtask1 = new Subtask(1, 2, "Subtask1", "Subtask 1 Description", Status.NEW);
        //do
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(2);
        inMemoryTaskManager.getSubtaskById(3);
        List<Task> history = inMemoryTaskManager.getHistory();
        //check
        assertEquals(task1, history.get(0), "Таска в истории не соответствует вызванной");
        assertEquals(epic1, history.get(1), "Эпик в истории не соответствует вызванному");
        assertEquals(subtask1, history.get(2), "Сабтаска в истории не соответствует вызванной");
        task1.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.getTaskById(1);
        history = inMemoryTaskManager.getHistory();
        assertEquals(task1, history.get(3), "Таска в истории не соответствует вызванной");
    }
}
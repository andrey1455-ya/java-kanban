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
    public void shouldBeTasksAddedToHistory() {
        //prepare
        Task task1 = new Task(1, "Task 1", "Task 1 Description");
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Task task2 = new Task(1, "Task 2", "Task 2 Description");
        Subtask subtask1 = new Subtask(1, 2, "Subtask 1", "Subtask 1 Description", Status.NEW);
        String errorText = "Задача в листе не равна вызванной ранее";
        //do
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.getEpicById(2);
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.getSubtaskById(3);
        List<Task> testList = inMemoryTaskManager.getHistory();
        //check
        assertEquals(task1, testList.get(0), errorText);
        assertEquals(epic1, testList.get(1), errorText);
        assertEquals(subtask1, testList.get(2), errorText);
        //do
        task1.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.getTaskById(1);
        List<Task> testList2 = inMemoryTaskManager.getHistory();
        //check
        assertEquals(epic1, testList2.get(0), errorText);
        assertEquals(subtask1, testList2.get(1), errorText);
        assertEquals(task1, testList2.get(2), errorText);
        //do
        inMemoryTaskManager.addNewTask(task2);
        inMemoryTaskManager.getTaskById(4);
        List<Task> testList3 = inMemoryTaskManager.getHistory();
        //check
        assertEquals(epic1, testList3.get(0), errorText);
        assertEquals(subtask1, testList3.get(1), errorText);
        assertEquals(task1, testList3.get(2), errorText);
        assertEquals(task2, testList3.get(3), errorText);
        //do
        inMemoryTaskManager.deleteTaskById(1);
        List<Task> testList4 = inMemoryTaskManager.getHistory();
        //check
        assertEquals(epic1, testList4.get(0), errorText);
        assertEquals(subtask1, testList4.get(1), errorText);
        assertEquals(task2, testList4.get(3), errorText);
        //do
        inMemoryTaskManager.deleteSubtaskById(3);
        List<Task> testList5 = inMemoryTaskManager.getHistory();
        //check
        assertEquals(epic1, testList5.get(0), errorText);
        assertEquals(task2, testList5.get(3), errorText);
        //do
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.getSubtaskById(5);
        List<Task> testList6 = inMemoryTaskManager.getHistory();
        //check
        assertEquals(epic1, testList6.get(0), errorText);
        assertEquals(task2, testList6.get(3), errorText);
        assertEquals(subtask1, testList6.get(4), errorText);
        //do
        inMemoryTaskManager.deleteEpicById(2);
        List<Task> testList7 = inMemoryTaskManager.getHistory();
        //check
        assertEquals(task2, testList7.get(3), errorText);
    }
}
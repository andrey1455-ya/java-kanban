import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    TaskManager inMemoryTaskManager = Managers.getDefault();

    private FileBackedTaskManager fileBackedTaskManager;
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = File.createTempFile("test", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void shouldFileBackedManagerSaveAndLoadEmptyFile() throws IOException {
        fileBackedTaskManager.save();
        String str = Files.readString(file.toPath());
        Assertions.assertEquals(str, "id,type,name,status,description,epic\n");
    }

    @Test
    public void shouldTasksSaveToFile() throws IOException {
        Task task = new Task("Task 1", "Task 1 Description");
        Epic epic = new Epic("Epic 1", "Epic 1 Description", Status.NEW);
        Subtask subtask = new Subtask(2, "Subtask 1", "Subtask 1 Description", Status.NEW, 2);
        fileBackedTaskManager.addNewTask(task);
        fileBackedTaskManager.addNewEpic(epic);
        fileBackedTaskManager.addNewSubtask(subtask);
        fileBackedTaskManager.save();
        String savedData = Files.readString(file.toPath());
        String expectedData = "id,type,name,status,description,epic\n" +
                task.getId() + ",TASK,Task 1,NEW,Task 1 Description,\n" +
                epic.getId() + ",EPIC,Epic 1,NEW,Epic 1 Description,\n" +
                subtask.getId() + ",SUBTASK,Subtask 1,NEW,Subtask 1 Description," + epic.getId() + "\n";
        assertEquals(expectedData, savedData);
    }

    @Test
    public void shouldLoadTasksFromFile() throws IOException {
        String fileContent = "id,type,name,status,description,epic\n" +
                "1,TASK,Task 1,NEW,Task 1 Description,\n" +
                "2,EPIC,Epic 1,NEW,Epic 1 Description,\n" +
                "3,SUBTASK,Subtask 1,NEW,Subtask 1 Description,2\n";
        Files.writeString(file.toPath(), fileContent);
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        ArrayList<Task> tasks = loadedManager.getAllTasks();
        ArrayList<Epic> epics = loadedManager.getAllEpics();
        ArrayList<Subtask> subtasks = loadedManager.getAllSubtasks();
        assertEquals(1, tasks.size());
        assertEquals(1, epics.size());
        assertEquals(1, subtasks.size());
        Task task = tasks.get(0);
        Epic epic = epics.get(0);
        Subtask subtask = subtasks.get(0);
        assertEquals("Task 1", task.getName());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals("Epic 1", epic.getName());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals("Subtask 1", subtask.getName());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(2, subtask.getEpicId());
    }
}
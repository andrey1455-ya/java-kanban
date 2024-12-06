import exception.TaskValidationException;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    TaskManager inMemoryTaskManager = Managers.getDefault();

    @Override
    public void shouldBeInMemoryTaskManagerPutAllTaskTypes() { //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
        super.shouldBeInMemoryTaskManagerPutAllTaskTypes();
    }

    @Test
    public void shouldBeImmutabilityTaskWhenAddingToInMemoryTaskManager() { //тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
        //prepare
        Task task1 = new Task(1, "Task 1", "Description 1");
        String name = task1.getName();
        String description = task1.getDescription();
        //do
        inMemoryTaskManager.addNewTask(task1);
        //check
        assertEquals(name, inMemoryTaskManager.getTaskById(1).getName(),
                "Имя задачи в менеджере отличается от добавленной");
        assertEquals(description, inMemoryTaskManager.getTaskById(1)
                .getDescription(), "Описание задачи в менеджере отличается от добавленной");

    }


    @Test
    public void shouldBeInMemoryTaskManagerDeleteTasks() { //Проверка, что все задачи удаляются из списка тасок/эпиков/сабтасок
        //prepare
        int numberOfTasksToCreated = 5;
        createTasks(5);
        //check
        assertEquals(numberOfTasksToCreated, inMemoryTaskManager.getAllTasks().size(),
                "В список тасок добавились не все таски");
        assertEquals(numberOfTasksToCreated, inMemoryTaskManager.getAllEpics().size(),
                "В список эпиков добавились не все эпики");
        assertEquals(numberOfTasksToCreated, inMemoryTaskManager.getAllSubtasks().size(),
                "В список сабтасок добавились не все сабтасок");
        //do
        inMemoryTaskManager.deleteAllTasks();
        inMemoryTaskManager.deleteAllEpics();
        inMemoryTaskManager.deleteAllSubtasks();
        //check
        assertEquals(0, inMemoryTaskManager.getAllTasks().size(), "Список тасок !=0");
        assertEquals(0, inMemoryTaskManager.getAllEpics().size(), "Список эпиков !=0");
        assertEquals(0, inMemoryTaskManager.getAllSubtasks().size(), "Список сабтасок !=0");
    }

    @Test
    public void shouldGetAllSubtaskFromEpicById() { //Проверка, что по id эпика получаем все добавленные в него сабтаски
        //prepare
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Subtask subtask1 = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask2 = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        ArrayList<Subtask> testSubtasks = new ArrayList<>();
        //do
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.addNewSubtask(subtask2);
        testSubtasks.add(inMemoryTaskManager.getSubtaskById(2));
        testSubtasks.add(inMemoryTaskManager.getSubtaskById(3));
        //check
        assertEquals(testSubtasks, inMemoryTaskManager.getSubtasksForEpic(1),
                "Сабтаски у эпика не совпадают с добавленными");
    }

    @Test
    public void cantGetTaskByNotExistId() { //Проверка что по несуществующему id ничего не возвращается
        //prepare
        Task task1 = new Task(16, "Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask1 = new Subtask(1, 2, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        //do
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask1);
        //check
        assertNull(inMemoryTaskManager.getTaskById(inMemoryTaskManager.getAllTasks().size() + 10));
        assertNull(inMemoryTaskManager.getEpicById(inMemoryTaskManager.getAllEpics().size() + 10));
        assertNull(inMemoryTaskManager.getSubtaskById(inMemoryTaskManager.getAllSubtasks().size() + 10));
    }

    @Test
    public void shouldBeTaskReallyUpdated() {
        //prepare
        Task task = new Task(1,"Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Task updatedTask = new Task(1, "Task 1 updated", "Description 1 updated", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        //do
        inMemoryTaskManager.addNewTask(task);
        inMemoryTaskManager.updateTask(updatedTask);
        //check
        assertEquals(inMemoryTaskManager.getTaskById(1), updatedTask, "Таска в менеджере не равна обновлённой");
    }

    @Test
    public void shouldBeEpicReallyUpdated() {
        //prepare
        Epic epic = new Epic(1, "Epic 1", "Epic Description 1");
        Epic updatedEpic = new Epic(1, "Epic 1 updated", "Epic Description 1 updated");
        //do
        inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.updateEpic(updatedEpic);
        //check
        assertEquals(inMemoryTaskManager.getEpicById(1), updatedEpic, "Таска в менеджере не равна обновлённой");
    }

    @Test
    public void shouldBeSubtaskReallyUpdated() {
        //prepare
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask updatedSubtask = new Subtask(2, 1, "Subtask 1 updated",
                "Subtask 1 Description updated ", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        //do
        inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.addNewSubtask(subtask);
        inMemoryTaskManager.updateSubtask(updatedSubtask);
        //check
        assertEquals(inMemoryTaskManager.getSubtaskById(2), updatedSubtask,
                "Таска в менеджере не равна обновлённой");
    }

    @Test
    public void shouldCantAddEpicToEpic() {//Проверка, что объект Epic нельзя добавить в самого себя в виде подзадачи;
        //prepare
        Epic epic1 = new Epic(1, "Epic 1", "description");
        Subtask subtask = new Subtask(1, 1, "Epic1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        //do
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask);
        //check
        assertFalse(inMemoryTaskManager.getAllEpics().contains(subtask),
                "Объект Epic нельзя добавить в самого себя");
    }

    @Test
    public void shouldCantMakeSubtaskOwnEpic() {//Проверка, что объект Subtask нельзя сделать своим же эпиком
        //prepare
        Epic epic1 = new Epic(1, "Epic 1", "description");
        Subtask subtask = new Subtask(1, 1, "Epic1", "Epic 1 Description", Status.NEW);
        //do
        inMemoryTaskManager.addNewEpic(epic1);
        //check
        inMemoryTaskManager.addNewSubtask(subtask);
        assertFalse(inMemoryTaskManager.getAllSubtasks().contains(epic1),
                "Объект Subtask нельзя сделать своим же эпиком");
    }

    @Test
    public void shouldEpicStatusNewWhenAllSubtaskStatusNew() {
        Epic epic = new Epic(1, "epic1", "des1", Status.NEW);
        int id = inMemoryTaskManager.addNewEpic(epic).getId();
        Subtask subtask1 = new Subtask(1, 1, "subtask1", "desc subtask1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask2 = new Subtask(2, 1, "subtask2", "desc subtask2", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask3 = new Subtask(3, 1, "subtask3", "desc subtask3", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.addNewSubtask(subtask2);
        inMemoryTaskManager.addNewSubtask(subtask3);
        Assertions.assertEquals(Status.NEW, inMemoryTaskManager.getEpicById(id).getStatus());
    }

    @Test
    public void shouldEpicStatusDoneWhenAllSubtaskStatusDone() {
        Epic epic = new Epic("epic1", "des1", Status.NEW);
        int id = inMemoryTaskManager.addNewEpic(epic).getId();
        Subtask subtask1 = new Subtask(1, 1, "subtask1", "desc subtask1", Status.DONE,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask2 = new Subtask(2, 1, "subtask2", "desc subtask2", Status.DONE,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask3 = new Subtask(3, 1, "subtask3", "desc subtask3", Status.DONE,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.addNewSubtask(subtask2);
        inMemoryTaskManager.addNewSubtask(subtask3);
        Assertions.assertEquals(Status.DONE, inMemoryTaskManager.getEpicById(id).getStatus());
    }

    @Test
    public void shouldEpicStatusInProgressWhenAllSubtaskStatusInProgress() {
        Epic epic = new Epic("epic1", "des1", Status.NEW);
        int id = inMemoryTaskManager.addNewEpic(epic).getId();
        Subtask subtask1 = new Subtask(1, 1, "subtask1", "desc subtask1", Status.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask2 = new Subtask(2, 1, "subtask2", "desc subtask2", Status.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Subtask subtask3 = new Subtask(3, 1, "subtask3", "desc subtask3", Status.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.addNewSubtask(subtask2);
        inMemoryTaskManager.addNewSubtask(subtask3);
        Assertions.assertEquals(Status.IN_PROGRESS, inMemoryTaskManager.getEpicById(id).getStatus());
    }

    public void createTasks(int count) {// Доп. метод для создания задач
        ArrayList<Task> testTasks = new ArrayList<>();
        ArrayList<Epic> testEpics = new ArrayList<>();
        ArrayList<Subtask> testSubtasks = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Task task = new Task(i, "Task " + i, "Description " + i, Status.NEW,
                    Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
            testTasks.add(task);
        }
        for (int i = 0; i < count; i++) {
            Epic epic = new Epic(i, "Epic " + i, "Epic " + i + " Description", Status.NEW,
                    Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
            testEpics.add(epic);
        }
        for (int i = 1; i <= count; i++) {
            Subtask subtask = new Subtask(i, count + 1, // +1 для добавления всех сабтасок к самому первому эпику
                    "Subtask " + i, "Subtask " + i
                    + " Description", Status.NEW,
                    Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
            testSubtasks.add(subtask);
        }
        for (Task testTask : testTasks) {
            inMemoryTaskManager.addNewTask(testTask);
        }
        for (Epic testEpic : testEpics) {
            inMemoryTaskManager.addNewEpic(testEpic);
        }
        for (Subtask testSubtask : testSubtasks) {
            inMemoryTaskManager.addNewSubtask(testSubtask);
        }
    }

    @Test
    public void shouldDoNotSaveTaskInPrioritizedTasksIfIntersect() {
        Task task1 = new Task(1, "Task 1", "Description task 1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Task task2 = new Task(2, "Task 2", "Description task 2", Status.NEW, Duration.ofMinutes(20)
                , LocalDateTime.of(2024, 2, 12, 16, 30, 0));
        Task task3 = new Task(3, "Task 3", "Description task 3", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 2, 12, 16, 10, 0));
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewTask(task2);
        Assertions.assertThrows(TaskValidationException.class, () -> inMemoryTaskManager.addNewTask(task3));
    }
}
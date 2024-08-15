import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryTaskManagerTest {

    TaskManager inMemoryTaskManager = Managers.getDefault();


    @Test
    public void shouldBeInMemoryTaskManagerPutAllTaskTypes() { //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
        //prepare
        Task task1 = new Task(1, "Task 1", "Description 1");
        Task task2 = task1;
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Epic epic2 = epic1;
        Subtask subtask1 = new Subtask(1, 2, "Subtask1", "Subtask 1 Description", Status.NEW);
        Subtask subtask2 = subtask1;
        //do
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask1);
        //check
        assertEquals(task2, inMemoryTaskManager.getTaskById(1), "Таска из менеджера не равна добавленной");
        assertEquals(epic2, inMemoryTaskManager.getEpicById(2), "Эпик из менеджера не равен добавленному");
        assertEquals(subtask2, inMemoryTaskManager.getSubtaskById(3), "Сабтаска из менеджера не равна добавленной");
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
    public void shouldGetAllSubtaskFromEpicById() { //Проверка что по id эпика получаем все добавленные в него сабтаскии
        //prepare
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Subtask subtask1 = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW);
        Subtask subtask2 = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW);
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
        Task task1 = new Task(16, "Task 1", "Description 1");
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Subtask subtask1 = new Subtask(1, 2, "Subtask 1", "Subtask 1 Description", Status.NEW);
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
        Task task = new Task("Task 1", "Description 1");
        Task updatedTask = new Task(1, "Task 1 updated", "Description 1 updated");
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
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description");
        Subtask subtask = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW);
        Subtask updatedSubtask = new Subtask(2, 1, "Subtask 1 updated",
                "Subtask 1 Description updated ", Status.NEW);
        //do
        inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.addNewSubtask(subtask);
        inMemoryTaskManager.updateSubtask(updatedSubtask);
        //check
        assertEquals(inMemoryTaskManager.getSubtaskById(2), updatedSubtask,
                "Таска в менеджере не равна обновлённой");
    }

    @Test
    public void shouldCantAddEpicToEpic() {
        //prepare
        Epic epic1 = new Epic(1, "Epic 1", "description");
        Subtask subtask = new Subtask(1, 1, "Epic1", "Epic 1 Description", Status.NEW);
        //do
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask);
        //check
        assertFalse(inMemoryTaskManager.getAllEpics().contains(subtask),
                "Объект Epic нельзя добавить в самого себя");
    }

    @Test
    public void shouldCantMakeSubtaskOwnEpic() {
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

    public void createTasks(int count) {// Доп. метод для создания задач
        ArrayList<Task> testTasks = new ArrayList<>();
        ArrayList<Epic> testEpics = new ArrayList<>();
        ArrayList<Subtask> testSubtasks = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Task task = new Task(i, "Task " + i, "Description " + i);
            testTasks.add(task);
        }
        for (int i = 0; i < count; i++) {
            Epic epic = new Epic(i, "Epic " + i, "Epic " + i + " Description");
            testEpics.add(epic);
        }
        for (int i = 1; i <= count; i++) {
            Subtask subtask = new Subtask(i, count + 1, // +1 для добавления всех сабтасок к самому первому эпику
                    "Subtask " + i, "Subtask " + i
                    + " Description", Status.NEW);
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
}
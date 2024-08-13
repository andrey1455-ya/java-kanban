package test;

import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryTaskManagerTest {

    TaskManager inMemoryTaskManager = Managers.getDefault();


    @Test
    public void shouldBeInMemoryTaskManagerPutAllTaskTypes() { //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
        Task task1 = new Task(1, "Task 1", "Description 1");
        Task task2 = task1;
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Epic epic2 = epic1;
        Subtask subtask1 = new Subtask(1, 2, "Subtask1", "Subtask 1 Description", Status.NEW);
        Subtask subtask2 = subtask1;
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask1);
        assertEquals(task2, inMemoryTaskManager.getTaskById(1), "Таска из менеджера не равна добавленной");
        assertEquals(epic2, inMemoryTaskManager.getEpicById(2), "Эпик из менеджера не равен добавленному");
        assertEquals(subtask2, inMemoryTaskManager.getSubtaskById(3), "Сабтаска из менеджера не равна добавленной");
    }

    @Test
    public void shouldBeImmutabilityTaskWhenAddingToInMemoryTaskManager() { //тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
        Task task1 = new Task(1, "Task 1", "Description 1");
        String name = task1.getName();
        String description = task1.getDescription();
        inMemoryTaskManager.addNewTask(task1);
        assertEquals(name, inMemoryTaskManager.getTaskById(1).getName(), "Имя задачи в менеджере отличается от добавленной");
        assertEquals(description, inMemoryTaskManager.getTaskById(1).getDescription(), "Описание задачи в менеджере отличается от добавленной");

    }

    @Test
    public void shouldBeInMemoryTaskManagerDeleteTasks() { //Проверка что все задачи удаляются из списка тасок/эпиков/сабтасок
        Task task1 = new Task(16, "Task 1", "Description 1");
        Task task2 = new Task(12, "Task 2", "Description 2");
        Task task3 = new Task(16, "Task 3", "Description 3");
        Task task4 = new Task(12, "Task 4", "Description 4");
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Epic epic2 = new Epic(1, "Epic 2", "Epic 2 Description");
        Epic epic3 = new Epic(1, "Epic 3", "Epic 3 Description");
        Epic epic4 = new Epic(1, "Epic 4", "Epic 4 Description");
        Subtask subtask1 = new Subtask(1, 5, "Subtask 1", "Subtask 1 Description", Status.NEW);
        Subtask subtask2 = new Subtask(1, 5, "Subtask 2", "Subtask 2 Description", Status.NEW);
        Subtask subtask3 = new Subtask(1, 5, "Subtask 3", "Subtask 3 Description", Status.NEW);
        Subtask subtask4 = new Subtask(1, 5, "Subtask 4", "Subtask 4 Description", Status.NEW);
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewTask(task2);
        inMemoryTaskManager.addNewTask(task3);
        inMemoryTaskManager.addNewTask(task4);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewEpic(epic2);
        inMemoryTaskManager.addNewEpic(epic3);
        inMemoryTaskManager.addNewEpic(epic4);
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.addNewSubtask(subtask2);
        inMemoryTaskManager.addNewSubtask(subtask3);
        inMemoryTaskManager.addNewSubtask(subtask4);
        assertEquals(4, inMemoryTaskManager.getAllTasks().size(), "В список тасок добавились не все таски");
        assertEquals(4, inMemoryTaskManager.getAllEpics().size(), "В список эпиков добавились не все эпики");
        assertEquals(4, inMemoryTaskManager.getAllSubtasks().size(), "В список сабтасок добавились не все сабтасок");
        inMemoryTaskManager.deleteAllTasks();
        inMemoryTaskManager.deleteAllEpics();
        inMemoryTaskManager.deleteAllSubtasks();
        assertEquals(0, inMemoryTaskManager.getAllTasks().size(), "Список тасок !=0");
        assertEquals(0, inMemoryTaskManager.getAllEpics().size(), "Список эпиков !=0");
        assertEquals(0, inMemoryTaskManager.getAllSubtasks().size(), "Список сабтасок !=0");
    }

    @Test
    public void shouldGetAllSubtaskFromEpicById() { //Проверка что по id эпика получаем все добавленные в него сабтаскии
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Subtask subtask1 = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW);
        Subtask subtask2 = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW);
        ArrayList<Subtask> testSubtasks = new ArrayList<>();
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.addNewSubtask(subtask2);
        testSubtasks.add(inMemoryTaskManager.getSubtaskById(2));
        testSubtasks.add(inMemoryTaskManager.getSubtaskById(3));
        assertEquals(testSubtasks, inMemoryTaskManager.getSubtasksForEpic(1), "Сабтаски у эпика не совпадают с добавленными");
    }

    @Test
    public void cantGetTaskByNotExistId() { //Проверка что по несуществующему id ничего не возвращается
        Task task1 = new Task(16, "Task 1", "Description 1");
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Subtask subtask1 = new Subtask(1, 2, "Subtask 1", "Subtask 1 Description", Status.NEW);
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewSubtask(subtask1);
        assertNull(inMemoryTaskManager.getTaskById(inMemoryTaskManager.getAllTasks().size() + 10));
        assertNull(inMemoryTaskManager.getEpicById(inMemoryTaskManager.getAllEpics().size() + 10));
        assertNull(inMemoryTaskManager.getSubtaskById(inMemoryTaskManager.getAllSubtasks().size() + 10));

    }
    /*
    Тут должен быть тест:
    -проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    но в менеджера не добавляются задачи с заданным id, он генерируется автоматически
    */
}
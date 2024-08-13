package test;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TasksTest {


    @Test //проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    public void shouldBeOneTaskEqualsAnotherTaskWithSameId() {
        Task task1 = new Task(1, "Task 1", "Description 1");
        Task task2 = new Task(1, "Task 2", "Description 2");
        assertEquals(task1, task2, "Задачи не равны");
    }

    @Test
    public void shouldBeEpicEqualsAnotherEpicWithSameId() {
        Epic epic1 = new Epic(1, "Epic 1", "Epic 1 Description");
        Epic epic2 = new Epic(1, "Epic 2", "Epic 2 Description", Status.DONE);
        assertEquals(epic1, epic2, "Эпики не равны");
    }

    @Test
    public void shouldBeSubtaskEqualsAnotherSubtaskWithSameId() {
        Subtask subtask1 = new Subtask(1, 1, "Subtask1", "Subtask 1 Description", Status.NEW);
        Subtask subtask2 = new Subtask(1, 2, "Subtask2", "Subtask 2 Description", Status.DONE);
        assertEquals(subtask1, subtask2, "Сабтаски не равны");
    }

    /*
    По ТЗ тут должны быть тесты:
    -проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    -проверьте, что объект Subtask нельзя сделать своим же эпиком;
    но это невозможно, в программе и так нет возможности добавить эпик в качестве подзадачи в другой эпик,
    так как методы по созданию/обновлению принимают конкретные типы.
    */

}

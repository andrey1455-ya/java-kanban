import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");


        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task(16, "Таска 1", "Описание таски 1",null, Duration.ofHours(1), LocalDateTime.of(2000, 9, 23, 10, 20));
        Task task2 = new Task(12, "Таска 2", "Описание таски 2",null, Duration.ofHours(2), LocalDateTime.of(2021, 9, 23, 10, 20));
        Task task3 = new Task(16, "Таска 3", "Описание таски 3",null, Duration.ofHours(3), LocalDateTime.of(2022, 9, 23, 10, 20));
        Task task4 = new Task(12, "Таска 4", "Описание таски 4",null, Duration.ofHours(4), LocalDateTime.of(2023, 9, 23, 10, 20));
        Task task5 = new Task(1, "Таска 1 обновлённая", "Обновлённое описание таски 1", Status.DONE, Duration.ofHours(5), LocalDateTime.of(2024, 9, 23, 10, 20));
        Task task6 = new Task(2, "Таска 2 обновлённая", "Обновлённое описание таски 2", Status.IN_PROGRESS, Duration.ofHours(6), LocalDateTime.of(2025, 9, 23, 10, 20));

        Epic epic1 = new Epic(1212, "Эпик1", "Описание эпик1",null, Duration.ofHours(7), LocalDateTime.of(2026, 9, 23, 10, 20));
        Epic epic2 = new Epic(60890, "Эпик2", "Описание эпик2",null, Duration.ofHours(8), LocalDateTime.of(2027, 9, 23, 10, 20));
        Epic epic3 = new Epic(6, "Эпик 2 обновленный", "Обновлённое описание эпика 2",null, Duration.ofHours(3), LocalDateTime.of(2028, 9, 23, 10, 20));
        Subtask subtask1 = new Subtask(23, 5, "Сабтаска 1 для эпика 1", "Описание сабтаски 1 для эпика 1", Status.NEW, Duration.ofHours(9), LocalDateTime.of(2029, 9, 23, 10, 20));
        Subtask subtask2 = new Subtask(4356, 5, "Сабтаска 1 для эпика 2", "Описание сабтаски 1 для эпика 2", Status.NEW, Duration.ofHours(10), LocalDateTime.of(2030, 9, 23, 10, 20));
        Subtask subtask3 = new Subtask(3245, 6, "Сабтаска 2 для эпика 2", "Описание сабтаски 2 для эпика 2", Status.NEW, Duration.ofHours(11), LocalDateTime.of(2031, 9, 23, 10, 20));
        Subtask subtask4 = new Subtask(7, 5, "Сабтаска 1 для эпика 1 обновлённая", "Обновлённое описание сабтаски 1 для эпика 1", Status.DONE, Duration.ofHours(12), LocalDateTime.of(2032, 9, 23, 10, 20));
        Subtask subtask5 = new Subtask(8, 5, "Сабтаска 2 для эпика 1 обновлённая", "Обновлённое описание сабтаски 2 для эпика 1", Status.DONE, Duration.ofHours(13), LocalDateTime.of(2033, 9, 23, 10, 20));
        Subtask subtask6 = new Subtask(9, 6, "Сабтаска 4 для эпика 2", "Описание сабтаски 4 для эпика 2", Status.DONE, Duration.ofHours(14), LocalDateTime.of(2034, 9, 23, 10, 20));

        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewTask(task2);
        inMemoryTaskManager.addNewTask(task3);
        inMemoryTaskManager.addNewTask(task4);
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewEpic(epic2);
        printDelimeter();
        System.out.println("Тестируем таски:");
        printDelimeter();
        System.out.println("Список тасок:");
        System.out.println(inMemoryTaskManager.getAllTasks());
        printDelimeter();
        System.out.println("Одна таска с id=1:");
        System.out.println(inMemoryTaskManager.getTaskById(1));
        System.out.println("Вторая таска с id=2:");
        System.out.println(inMemoryTaskManager.getTaskById(2));
        printDelimeter();
        System.out.println("История хранит 2 вызванных таски");
        System.out.println(inMemoryTaskManager.getHistory());
        printDelimeter();
        System.out.println("Обновляем статус таски 1");
        inMemoryTaskManager.updateTask(task5);
        inMemoryTaskManager.updateTask(task6);
        printDelimeter();
        System.out.println("Список тасок:");
        System.out.println(inMemoryTaskManager.getAllTasks());
        printDelimeter();
        System.out.println("Удаляем одну таску c ID=1");
        inMemoryTaskManager.deleteTaskById(1);
        printDelimeter();
        System.out.println("Список тасок:");
        System.out.println(inMemoryTaskManager.getAllTasks());
        printDelimeter();
        System.out.println("Удаляем все оставшиеся таски:");
        inMemoryTaskManager.deleteAllTasks();
        printDelimeter();
        System.out.println("Список тасок:");
        System.out.println(inMemoryTaskManager.getAllTasks());
        printDelimeter();
        System.out.println("Тестируем эпики и сабтаски:");
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getAllEpics());
        printDelimeter();
        System.out.println("Добавляем в Эпик 1 две сабтаски...");
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.addNewSubtask(subtask2);
        System.out.println("Добавляем в Эпик 2 одну сабтаску...");
        inMemoryTaskManager.addNewSubtask(subtask3);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(inMemoryTaskManager.getAllEpics());
        printDelimeter();
        System.out.println("Обновляем статус сабтаски 1 для эпика 1");
        inMemoryTaskManager.updateSubtask(subtask4);
        printDelimeter();
        System.out.println("Получаем сабтаски для эпика 1:");
        System.out.println(inMemoryTaskManager.getSubtasksForEpic(5));
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(inMemoryTaskManager.getAllEpics());
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Обновляем статус сабтаски 2 для эпика 1");
        inMemoryTaskManager.updateSubtask(subtask5);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(inMemoryTaskManager.getAllEpics());
        printDelimeter();
        printDelimeter();
        System.out.println("Обновляем Эпик 2");
        inMemoryTaskManager.updateEpic(epic3);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(inMemoryTaskManager.getAllEpics());
        printDelimeter();
        System.out.println("Обновляем статус сабтаски 1 для эпика 2");
        inMemoryTaskManager.updateSubtask(subtask6);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(inMemoryTaskManager.getAllEpics());
        printDelimeter();
        System.out.println("Удаляем сабтаску 1 для эпика 2");
        inMemoryTaskManager.deleteSubtaskById(9);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(inMemoryTaskManager.getAllEpics());
        printDelimeter();
        System.out.println("Удаляем все сабтаски");
        inMemoryTaskManager.deleteAllSubtasks();
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(inMemoryTaskManager.getAllEpics());
        printDelimeter();
        System.out.println("Удаляем все Эпики");
        inMemoryTaskManager.deleteAllEpics();
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(inMemoryTaskManager.getAllEpics());
        printDelimeter();

    }

    public static void printDelimeter() { //Печать разделителей в консоли для разделения действий
        System.out.println("-".repeat(90));
    }


}




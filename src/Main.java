import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task(16, "Таска 1", "Описание таски 1");
        Task task2 = new Task(12, "Таска 2", "Описание таски 2");
        Task task3 = new Task(16, "Таска 3", "Описание таски 3");
        Task task4 = new Task(12, "Таска 4", "Описание таски 4");
        Task task5 = new Task(1, "Таска 1 обновлённая", "Обновлённое описание таски 1", Status.DONE);
        Task task6 = new Task(2, "Таска 2 обновлённая", "Обновлённое описание таски 2", Status.IN_PROGRESS);

        Epic epic1 = new Epic(1212, "Эпик1", "Описание эпик1");
        Epic epic2 = new Epic(60890, "Эпик2", "Описание эпик2");
        Epic epic3 = new Epic(6, "Эпик 2 обновленный", "Обновлённое описание эпика 2");
        Subtask subtask1 = new Subtask(23, 5, "Сабтаска 1 для эпика 1", "Описание сабтаски 1 для эпика 1", Status.NEW);
        Subtask subtask2 = new Subtask(4356, 5, "Сабтаска 1 для эпика 2", "Описание сабтаски 1 для эпика 2", Status.NEW);
        Subtask subtask3 = new Subtask(3245, 6, "Сабтаска 2 для эпика 2", "Описание сабтаски 2 для эпика 2", Status.NEW);
        Subtask subtask4 = new Subtask(7, 5, "Сабтаска 1 для эпика 1 обновлённая", "Обновлённое описание сабтаски 1 для эпика 1", Status.DONE);
        Subtask subtask5 = new Subtask(8, 5, "Сабтаска 2 для эпика 1 обновлённая", "Обновлённое описание сабтаски 2 для эпика 1", Status.DONE);
        Subtask subtask6 = new Subtask(9, 6, "Сабтаска 4 для эпика 2", "Описание сабтаски 4 для эпика 2", Status.DONE);
        Subtask subtask7 = new Subtask(3234, 2, "Сабтаска 4 для эпика 2", "Описание сабтаски 4 для эпика 2", Status.NEW);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.addNewTask(task4);
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        printDelimeter();
        System.out.println("Тестируем таски:");
        printDelimeter();
        System.out.println("Список тасок:");
        System.out.println(taskManager.getAllTasks());
        printDelimeter();
        System.out.println("Одна таска с id=1:");
        System.out.println(taskManager.getTaskById(1));
        printDelimeter();
        System.out.println("Обновляем статус таски 1");
        taskManager.updateTask(task5);
        taskManager.updateTask(task6);
        printDelimeter();
        System.out.println("Список тасок:");
        System.out.println(taskManager.getAllTasks());
        printDelimeter();
        System.out.println("Удаляем одну таску c ID=1");
        taskManager.deleteTaskById(1);
        printDelimeter();
        System.out.println("Список тасок:");
        System.out.println(taskManager.getAllTasks());
        printDelimeter();
        System.out.println("Удаляем все оставшиеся таски:");
        taskManager.deleteAllTasks();
        printDelimeter();
        System.out.println("Список тасок:");
        System.out.println(taskManager.getAllTasks());
        printDelimeter();

        System.out.println("Тестируем эпики и сабтаски:");
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getAllEpics());
        printDelimeter();
        System.out.println("Добавляем в Эпик 1 две сабтаски...");
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        System.out.println("Добавляем в Эпик 2 одну сабтаску...");
        taskManager.addNewSubtask(subtask3);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(taskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(taskManager.getAllEpics());
        printDelimeter();
        System.out.println("Обновляем статус сабтаски 1 для эпика 1");
        taskManager.updateSubtask(subtask4);
        printDelimeter();
        System.out.println("Получаем сабтаски для эпика 1:");
        System.out.println(taskManager.getSubtasksForEpic(5));
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(taskManager.getAllEpics());
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(taskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Обновляем статус сабтаски 2 для эпика 1");
        taskManager.updateSubtask(subtask5);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(taskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(taskManager.getAllEpics());
        printDelimeter();
        printDelimeter();
        System.out.println("Обновляем Эпик 2");
        taskManager.updateEpic(epic3);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(taskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(taskManager.getAllEpics());
        printDelimeter();
        System.out.println("Обновляем статус сабтаски 1 для эпика 2");
        taskManager.updateSubtask(subtask6);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(taskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(taskManager.getAllEpics());
        printDelimeter();
        System.out.println("Удаляем сабтаску 1 для эпика 2");
        taskManager.deleteSubtaskById(9);
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(taskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(taskManager.getAllEpics());
        printDelimeter();
        System.out.println("Удаляем все сабтаски");
        taskManager.deleteAllSubtasks();
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(taskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(taskManager.getAllEpics());
        printDelimeter();
        System.out.println("Удаляем все Эпики");
        taskManager.deleteAllEpics();
        printDelimeter();
        System.out.println("Список сабтасок:");
        System.out.println(taskManager.getAllSubtasks());
        printDelimeter();
        System.out.println("Список эпиков с Id сабтасок':");
        System.out.println(taskManager.getAllEpics());
        printDelimeter();
    }

    public static void printDelimeter() {
        System.out.println("-".repeat(90));
    }
}




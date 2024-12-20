package manager;

import exception.NotFoundException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //Методы для Тасок
    ArrayList<Task> getAllTasks();

    void deleteAllTasks();

    Task getTaskById(int id) throws NotFoundException;

    Task addNewTask(Task newTask);

    Task updateTask(Task updatedTask);

    void deleteTaskById(int id);

    //Методы для Эпиков
    ArrayList<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpicById(int id) throws NotFoundException;

    Epic addNewEpic(Epic newEpic);

    Epic updateEpic(Epic updatedEpic);

    void deleteEpicById(int id);

    List<Subtask> getSubtasksForEpic(int id);

    //Методы для сабтасок
    ArrayList<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtaskById(int id) throws NotFoundException;

    Subtask addNewSubtask(Subtask newSubtask);

    Subtask updateSubtask(Subtask updatedSubtask);

    void deleteSubtaskById(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}

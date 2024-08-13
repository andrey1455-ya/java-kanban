package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    //Методы для Тасок
    Collection<Task> getAllTasks();

    void deleteAllTasks();

    Task getTaskById(int id);

    Task addNewTask(Task newTask);

    Task updateTask(Task updatedTask);

    void deleteTaskById(int id);

    //Методы для Эпиков
    Collection<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpicById(int id);

    Epic addNewEpic(Epic newEpic);

    Epic updateEpic(Epic updatedEpic);

    void deleteEpicById(int id);

    ArrayList<Subtask> getSubtasksForEpic(int id);

    //Методы для сабтасок
    Collection<Subtask> getAllSubtasks();

    HashMap<Integer, Epic> deleteAllSubtasks();

    Subtask getSubtaskById(int id);

    Subtask addNewSubtask(Subtask newSubtask);

    void updateSubtask(Subtask updatedSubtask);

    void deleteSubtaskById(int id);

    List<Task> getHistory();
}

package Manager;

import TasksType.Epic;
import TasksType.Subtask;
import TasksType.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    int id = 1;

    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();

    //=========================================Методы для Тасок=========================================================
    public HashMap<Integer, Task> getAllTasks() { // Метод получения всех Тасок
        return taskHashMap;
    }

    public HashMap<Integer, Task> deleteAllTasks() { // Метод удаления всех Тасок
        taskHashMap.clear();
        return taskHashMap;
    }

    public Task getTask(int id) { // Метод получения Таски по id.
        return taskHashMap.get(id);
    }

    public Task addNewTask(Task newTask) { // Метод добавления новой Таски
        int newId = id++;
        newTask.setId(newId);
        taskHashMap.put(newTask.getId(), newTask);
        return newTask;
    }

    public Task updateTask(Task updatedTask) { // Метод обновления Таски.
        taskHashMap.put(updatedTask.getId(), updatedTask);
        return updatedTask;
    }

    public HashMap<Integer, Task> deleteTask(int id) {  // Метод удаления Таски по id.
        taskHashMap.remove(id);
        return taskHashMap;
    }


    //=========================================Методы для Эпиков========================================================
    public HashMap<Integer, Epic> getAllEpics(){ //Метод получения всех эпиков
        return epicHashMap;
    }

    public void deleteAllEpics() { // Метод удаления всех эпиков
        epicHashMap.clear();
    }

    public Epic getEpic(int id) { // Метод получения Эпика по id.
        return epicHashMap.get(id);
    }

    public Epic addNewEpic(Epic newEpic) { // Метод добавления нового Эпика
        int newId = generateNewId();
        newEpic.setId(newId);
        epicHashMap.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public Epic updateEpic(Epic newEpic) { // Метод обновления Эпика.
        epicHashMap.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public HashMap<Integer, Epic> deleteEpic(int id) {  // Метод удаления Эпика по id.
        epicHashMap.remove(id);
        return epicHashMap;
    }

    public HashMap<Integer, Subtask> getSubtasksForEpic(int id) { //Метод получения списка сабтасок по id Эпика
        Epic epic = epicHashMap.get(id);
        return epic.getSubtasksForEpic();
    }

    //=======================================Методы для сабтасок========================================================
    public ArrayList<Subtask> getAllSubtasks() { //Получение списка всех сабтасок
        ArrayList<Subtask> subtaskForShow = new ArrayList<>();
        for (Epic epic : epicHashMap.values()) {
            subtaskForShow.addAll(epic.getSubtasksForEpic().values());
        }
        return subtaskForShow;
    }

    public HashMap<Integer, Epic> deleteAllSubtasks() { //Удаление всех сабтасок
        for (Epic epic : epicHashMap.values()) {
            epic.getSubtasksForEpic().clear();
        }
        return epicHashMap;
    }

    public Subtask getSubtaskById(int id) { //Получение сабтаски по id
        Subtask subtaskById = null;
        for (Epic epic : epicHashMap.values()) {
            for (Subtask subtask : epic.getSubtasksForEpic().values()) {
                if (id == subtask.getId()) {
                    subtaskById = epic.getSubtasksForEpic().get(id);
                }
            }
        }
        return subtaskById;
    }

    public Subtask addNewSubtaskToEpic(int epicId, Subtask newSubtask) { //Метод добавления сабтаски в эпик
        int newId = generateNewId();
        newSubtask.setId(newId);
        Epic epic = epicHashMap.get(epicId);
        epic.addSubtaskToEpic(newSubtask);
        epic.setStatus(epic.isSubtasksDone());
        return newSubtask;
    }

    public Subtask updateSubtask(int epicId, Subtask newSubtask) { //Метод обновления сабтаски
        Epic epic = epicHashMap.get(epicId);
        int newId = generateNewId();
        newSubtask.setId(newId);
        HashMap<Integer, Subtask> subtaskFromEpic = epic.getSubtasksForEpic();
        subtaskFromEpic.put(newSubtask.getId(), newSubtask);
        epic.setStatus(epic.isSubtasksDone());
        return newSubtask;
    }

    public HashMap<Integer, Epic> deletetSubtaskById(int id) { //Удаление сабтаски по id
        for (Epic epic : epicHashMap.values()) {
            epic.getSubtasksForEpic().remove(id);
        }
        return epicHashMap;
    }

    //------------------------------------Вспомогательные методы--------------------------------------------------------
    private Integer generateNewId() { // Универсальный метод для генерации ID тасок/эпиков/сабтасок
        return id++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskManager that = (TaskManager) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "id=" + id +
                ", taskHashMap=" + taskHashMap +
                ", epicHashMap=" + epicHashMap +
                '}';
    }
}
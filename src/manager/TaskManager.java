package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {

    private int id = 1;

    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    public HashMap<Integer, Subtask> subtasksHashMap = new HashMap<>();

    //Методы для Тасок
    public Collection<Task> getAllTasks() { // Метод получения всех Тасок
        return taskHashMap.values();
    }

    public void deleteAllTasks() { // Метод удаления всех Тасок
        taskHashMap.clear();
    }

    public Task getTaskById(int id) { // Метод получения Таски по id.
        return taskHashMap.get(id);
    }

    public Task addNewTask(Task newTask) { // Метод добавления новой Таски
        newTask.setId(generateNewId());
        taskHashMap.put(newTask.getId(), newTask);
        return newTask;
    }

    public Task updateTask(Task updatedTask) { // Метод обновления Таски.
        if (taskHashMap.containsKey(updatedTask.getId())) {
            taskHashMap.put(updatedTask.getId(), updatedTask);
            return updatedTask;
        } else {
            System.out.println("Такой таски нет, нечего обновлять");
            return null;
        }
    }

    public void deleteTaskById(int id) {  // Метод удаления Таски по id.
        taskHashMap.remove(id);
    }

    //Методы для Эпиков
    public Collection<Epic> getAllEpics() { //Метод получения всех эпиков
        return epicHashMap.values();
    }

    public void deleteAllEpics() { // Метод удаления всех эпиков
        epicHashMap.clear();
        subtasksHashMap.clear();
    }

    public Epic getEpicById(int id) { // Метод получения Эпика по id.
        return epicHashMap.get(id);
    }

    public Epic addNewEpic(Epic newEpic) { // Метод добавления нового Эпика
        newEpic.setId(generateNewId());
        epicHashMap.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public Epic updateEpic(Epic updatedEpic) { // Метод обновления Эпика.
        if (epicHashMap.containsKey(updatedEpic.getId())) {
            ArrayList<Integer> epicSubtasksIds;
            epicSubtasksIds = epicHashMap.get(updatedEpic.getId()).getSubtasksIds();
            updatedEpic.setSubtaskIds(epicSubtasksIds);
            epicHashMap.put(updatedEpic.getId(), updatedEpic);
            return updatedEpic;
        } else {
            System.out.println("Такого эпика не существует");
            return null;
        }
    }

    public void deleteEpicById(int id) {  // Метод удаления Эпика по id.
        ArrayList<Integer> subtasksIdsForEpic = epicHashMap.get(id).getSubtasksIds();
        for (Integer subtaskId : subtasksIdsForEpic) {
            subtasksHashMap.remove(subtaskId);
        }
        epicHashMap.remove(id);
    }

    public ArrayList<Subtask> getSubtasksForEpic(int id) { //Метод получения списка сабтасок по id Эпика
        ArrayList<Integer> subtasksIdsForEpic = epicHashMap.get(id).getSubtasksIds();
        ArrayList<Subtask> subtasksForEpic = new ArrayList<>();
        for (int subtaskId : subtasksIdsForEpic) {
            subtasksForEpic.add(subtasksHashMap.get(subtaskId));
        }
        return subtasksForEpic;
    }

    //Методы для сабтасок
    public Collection<Subtask> getAllSubtasks() { //Получение списка всех сабтасок
        return subtasksHashMap.values();
    }

    public HashMap<Integer, Epic> deleteAllSubtasks() { //Удаление всех сабтасок
        subtasksHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.cleanSubtaskIds();
            epic.setStatus(Status.NEW);
        }
        return epicHashMap;
    }

    public Subtask getSubtaskById(int id) { //Получение сабтаски по id
        return subtasksHashMap.get(id);
    }

    public Subtask addNewSubtask(Subtask newSubtask) { //Метод добавления сабтаски в эпик
        if (epicHashMap.containsKey(newSubtask.getEpicId())) {
            newSubtask.setId(generateNewId());
            subtasksHashMap.put(newSubtask.getId(), newSubtask);
            Epic epic = epicHashMap.get(newSubtask.getEpicId());
            epic.addSubtaskId(newSubtask.getId());
            checkEpicStatus(epic.getId());
            return newSubtask;
        } else {
            System.out.println("В сабтаске указан ID несуществующего эпика");
            return null;
        }
    }

    public void updateSubtask(Subtask updatedSubtask) {
        Subtask subtask = subtasksHashMap.get(updatedSubtask.getId());
        if (subtask == null) {
            return;
        }
        Epic epic = epicHashMap.get(updatedSubtask.getEpicId());
        if (epic == null) {
            return;
        }
        subtasksHashMap.put(updatedSubtask.getId(), updatedSubtask);
        checkEpicStatus(epic.getId());

    }

    public void deleteSubtaskById(int id) { //Удаление сабтаски по id
        subtasksHashMap.remove(id);
        for (Epic epic : epicHashMap.values()) {
            epic.getSubtasksIds().remove(Integer.valueOf(id));
            checkEpicStatus(epic.getId());
        }
    }

    //Вспомогательные методы
    private Integer generateNewId() { // Универсальный метод для генерации ID тасок/эпиков/сабтасок
        return id++;
    }

    private void checkEpicStatus(int epicId) {// Метод для проверки статуса эпика
        Epic epic = epicHashMap.get(epicId);
        if (epic.getSubtasksIds().isEmpty()) {
                epic.setStatus(Status.NEW);
                return;
            }
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (int id : subtasksIds) {
            epicSubtasks.add(subtasksHashMap.get(id));
        }
        boolean isAllDone = true;
        boolean isAllNew = true;
        for (Subtask subtask : epicSubtasks) {
            if (!subtask.getStatus().equals(Status.DONE)) {
                isAllDone = false;
            }
            if (!subtask.getStatus().equals(Status.NEW)) {
                isAllNew = false;
            }
            if (!isAllDone && !isAllNew) {
                break;
            }
        }
        if (isAllDone) {
            epic.setStatus(Status.DONE);
        } else if (isAllNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
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
                ", subtasksHashMap=" + subtasksHashMap +
                '}';
    }

}
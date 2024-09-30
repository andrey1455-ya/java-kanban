package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int id = 1;
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksHashMap = new HashMap<>();

    //Методы для Тасок
    @Override
    public ArrayList<Task> getAllTasks() { // Метод получения всех Тасок
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public void deleteAllTasks() { // Метод удаления всех Тасок
        for (Integer key : taskHashMap.keySet()) {
            inMemoryHistoryManager.remove(key);
        }
        taskHashMap.clear();
    }

    @Override
    public Task getTaskById(int id) { // Метод получения Таски по id.
        inMemoryHistoryManager.addTaskToHistory(taskHashMap.get(id));
        return taskHashMap.get(id);
    }

    @Override
    public Task addNewTask(Task newTask) { // Метод добавления новой Таски
        newTask.setId(generateNewId());
        taskHashMap.put(newTask.getId(), newTask);
        return newTask;
    }

    @Override
    public Task updateTask(Task updatedTask) { // Метод обновления Таски.
        if (taskHashMap.containsKey(updatedTask.getId())) {
            taskHashMap.put(updatedTask.getId(), updatedTask);
            return updatedTask;
        } else {
            System.out.println("Такой таски нет, нечего обновлять");
            return null;
        }
    }

    @Override
    public void deleteTaskById(int id) {
        inMemoryHistoryManager.remove(id);// Метод удаления Таски по id.
        taskHashMap.remove(id);
    }

    //Методы для Эпиков
    @Override
    public ArrayList<Epic> getAllEpics() { //Метод получения всех эпиков
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public void deleteAllEpics() { // Метод удаления всех эпиков
        for (Integer key : epicHashMap.keySet()) {
            inMemoryHistoryManager.remove(key);
        }
        for (Integer key : subtasksHashMap.keySet()) {
            inMemoryHistoryManager.remove(key);
        }
        epicHashMap.clear();
        subtasksHashMap.clear();
    }

    @Override
    public Epic getEpicById(int id) { // Метод получения Эпика по id.
        inMemoryHistoryManager.addTaskToHistory(epicHashMap.get(id));
        return epicHashMap.get(id);
    }

    @Override
    public Epic addNewEpic(Epic newEpic) { // Метод добавления нового Эпика
        newEpic.setId(generateNewId());
        epicHashMap.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    @Override
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

    @Override
    public void deleteEpicById(int id) {  // Метод удаления Эпика по id.
        inMemoryHistoryManager.remove(id);
        ArrayList<Integer> subtasksIdsForEpic = epicHashMap.get(id).getSubtasksIds();
        for (Integer subtaskId : subtasksIdsForEpic) {
            subtasksHashMap.remove(subtaskId);
            inMemoryHistoryManager.remove(subtaskId);
        }
        epicHashMap.remove(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksForEpic(int id) { //Метод получения списка сабтасок по id Эпика
        ArrayList<Integer> subtasksIdsForEpic = epicHashMap.get(id).getSubtasksIds();
        ArrayList<Subtask> subtasksForEpic = new ArrayList<>();
        for (int subtaskId : subtasksIdsForEpic) {
            subtasksForEpic.add(subtasksHashMap.get(subtaskId));
        }
        return subtasksForEpic;
    }

    //Методы для сабтасок
    @Override
    public ArrayList<Subtask> getAllSubtasks() { //Получение списка всех сабтасок
        return new ArrayList<>(subtasksHashMap.values());
    }

    @Override
    public HashMap<Integer, Epic> deleteAllSubtasks() {
        for (Integer key : subtasksHashMap.keySet()) {
            inMemoryHistoryManager.remove(key);
        }//Удаление всех сабтасок
        subtasksHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.cleanSubtaskIds();
            epic.setStatus(Status.NEW);
        }
        return epicHashMap;
    }

    @Override
    public Subtask getSubtaskById(int id) { //Получение сабтаски по id
        inMemoryHistoryManager.addTaskToHistory(subtasksHashMap.get(id));
        return subtasksHashMap.get(id);
    }

    @Override
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

    @Override
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

    @Override
    public void deleteSubtaskById(int id) {
        inMemoryHistoryManager.remove(id);//Удаление сабтаски по id
        subtasksHashMap.remove(id);
        for (Epic epic : epicHashMap.values()) {
            epic.getSubtasksIds().remove(Integer.valueOf(id));
            checkEpicStatus(epic.getId());
        }
    }

    @Override
    public List<Task> getHistory() { //Метод получения истории
        return inMemoryHistoryManager.getHistory();
    }

    //Вспомогательные методы
    private Integer generateNewId() { //Универсальный метод для генерации ID тасок/эпиков/сабтасок
        return id++;
    }

    private void checkEpicStatus(int epicId) { // Метод для проверки статуса эпика
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
        InMemoryTaskManager that = (InMemoryTaskManager) o;
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
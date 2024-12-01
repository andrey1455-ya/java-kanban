package manager;

import exception.TaskValidationException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    private int id = 1;
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    public final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    public final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    public final HashMap<Integer, Subtask> subtasksHashMap = new HashMap<>();
    private final Comparator<Task> comparator = (Comparator.comparing(Task::getStartTime));
    private final Set<Task> prioritizedTasks = new TreeSet<>(comparator);


    //Методы для Тасок
    @Override
    public ArrayList<Task> getAllTasks() { // Метод получения всех Тасок
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public void deleteAllTasks() { // Метод удаления всех Тасок
        for (Task task : taskHashMap.values()) {
            inMemoryHistoryManager.remove(task.getId());
            prioritizedTasks.remove(task);
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
        addTaskInPrioritizedTasks(newTask);
        return newTask;
    }

    @Override
    public Task updateTask(Task updatedTask) { // Метод обновления Таски.
        if (taskHashMap.containsKey(updatedTask.getId())) {
            taskHashMap.put(updatedTask.getId(), updatedTask);
            addTaskInPrioritizedTasks(updatedTask);
            return updatedTask;
        } else {
            System.out.println("Такой таски нет, нечего обновлять");
            return null;
        }
    }

    @Override
    public HashMap<Integer, Task> deleteTaskById(int id) { // Метод удаления Таски по id.
        Task task = taskHashMap.get(id);
        inMemoryHistoryManager.remove(id);
        taskHashMap.remove(id);
        prioritizedTasks.remove(task);
        return taskHashMap;
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
    public HashMap<Integer, Epic> deleteEpicById(int id) {  // Метод удаления Эпика по id.
        inMemoryHistoryManager.remove(id);
        ArrayList<Integer> subtasksIdsForEpic = epicHashMap.get(id).getSubtasksIds();
        for (Integer subtaskId : subtasksIdsForEpic) {
            subtasksHashMap.remove(subtaskId);
            inMemoryHistoryManager.remove(subtaskId);
            prioritizedTasks.remove(subtasksHashMap.get(subtaskId));
        }
        epicHashMap.remove(id);
        return epicHashMap;
    }

    @Override
    public List<Subtask> getSubtasksForEpic(int id) { //Метод получения списка сабтасок по id Эпика
        ArrayList<Integer> subtasksIdsForEpic = epicHashMap.get(id).getSubtasksIds();
        return subtasksHashMap.values().stream()
                .filter(subtask -> subtasksIdsForEpic.contains(subtask.getId()))
                .toList();
    }

    private void updateTimeForEpic(Epic epic) {
        List<Subtask> epicSubtasks = subtasksHashMap.values().stream()
                .filter(subtask -> epic.getSubtasksIds().contains(subtask.getId()))
                .toList();

        Optional<Subtask> startTime = epicSubtasks.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .min(comparator);
        if (startTime.isPresent()) {
            Subtask startTimeSubtask = startTime.get();
            if (startTimeSubtask.getStartTime() != null) {
                epic.setStartTime(startTimeSubtask.getStartTime());
            }
        }
        Optional<Subtask> endTime = epicSubtasks.stream()
                .filter(subtask -> subtask.getEndTime() != null)
                .max(Comparator.comparing(Task::getEndTime));
        if (endTime.isPresent()) {
            Subtask endTimeSubtask = endTime.get();
            if (endTimeSubtask.getEndTime() != null) {
                epic.setEndTime(endTimeSubtask.getEndTime());
            }
        }
        Duration allDuration = Duration.ofMinutes(0);
        for (Subtask subtask : epicSubtasks) {
            if (subtask.getDuration() != null) {
                allDuration = allDuration.plus(subtask.getDuration());
            }
        }
        epic.setDuration(allDuration);
    }

    //Методы для сабтасок
    @Override
    public ArrayList<Subtask> getAllSubtasks() { //Получение списка всех сабтасок
        return new ArrayList<>(subtasksHashMap.values());
    }

    @Override
    public void deleteAllSubtasks() { //Удаление всех сабтасок
        for (Integer key : subtasksHashMap.keySet()) {
            inMemoryHistoryManager.remove(key);
        }
        subtasksHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.cleanSubtaskIds();
            epic.setStatus(Status.NEW);
            updateTimeForEpic(epic);
        }
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
            updateTimeForEpic(epic);
            addTaskInPrioritizedTasks(newSubtask);
            return newSubtask;
        } else {
            System.out.println("В сабтаске указан ID несуществующего эпика");
            return null;
        }
    }

    @Override
    public Subtask updateSubtask(Subtask updatedSubtask) {
        Subtask subtask = subtasksHashMap.get(updatedSubtask.getId());
        if (subtask == null) {
            return subtask;
        }
        Epic epic = epicHashMap.get(updatedSubtask.getEpicId());
        if (epic == null) {
            return subtask;
        }
        subtasksHashMap.put(updatedSubtask.getId(), updatedSubtask);
        checkEpicStatus(epic.getId());
        updateTimeForEpic(epic);
        addTaskInPrioritizedTasks(updatedSubtask);
        return subtask;
    }

    @Override
    public HashMap<Integer, Subtask> deleteSubtaskById(int id) { //Удаление сабтаски по id
        inMemoryHistoryManager.remove(id);
        Subtask subtask = subtasksHashMap.remove(id);
        prioritizedTasks.remove(subtask);
        for (Epic epic : epicHashMap.values()) {
            epic.getSubtasksIds().remove(Integer.valueOf(id));
            checkEpicStatus(epic.getId());
            updateTimeForEpic(epic);
        }
        return subtasksHashMap;
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

    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    private void addTaskInPrioritizedTasks(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) {
            return;
        }
        if (!isOverlappingTasks(task)) {
            throw new TaskValidationException("Задача пересекаются");
        }
        prioritizedTasks.add(task);
    }

    public boolean isOverlappingTasks(Task newTask) {
        List<Task> sortedList = getPrioritizedTasks();
        if (newTask.getStartTime() == null) {
            return true;
        } else {
            return sortedList.stream()
                    .filter(task1 -> !(task1.getEndTime().isBefore(newTask.getStartTime())
                            || task1.getEndTime().isEqual(newTask.getStartTime())))
                    .filter(task1 -> !(task1.getStartTime().isAfter(newTask.getEndTime())
                            || task1.getStartTime().isEqual(newTask.getEndTime())))
                    .collect(Collectors.toSet()).isEmpty();
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
package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : taskHashMap.values()) {
                writer.write(task.toString() + "\n");
            }
            for (Epic epic : epicHashMap.values()) {
                writer.write(epic.toString() + "\n");
            }
            for (Subtask subtask : subtasksHashMap.values()) {
                writer.write(subtask.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(String.format("Ошибка при сохранении данных в файл: %s", file.getName()));
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines.subList(1, lines.size())) {
                String[] fields = line.split(",");
                TaskType taskType = TaskType.valueOf(fields[1]);
                switch (taskType) {
                    case TASK:
                        Task task = Task.fromString(line);
                        manager.addNewTask(task);
                        break;
                    case SUBTASK:
                        Subtask subtask = Subtask.fromString(line);
                        manager.addNewSubtask(subtask);
                        break;
                    case EPIC:
                        Epic epic = Epic.fromString(line);
                        manager.addNewEpic(epic);
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Неизвестный тип задачи: %s", taskType));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(String.format("Ошибка при чтении данных из файла: %s", file.getName()));
        }
        return manager;
    }

    @Override
    public Task addNewTask(Task task) {
        Task newTask = super.addNewTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic addNewEpic(Epic epic) {
        Epic newEpic = super.addNewEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask addNewSubtask(Subtask subtask) {
        Subtask newSubtask = super.addNewSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updatedEpic = super.updateEpic(epic);
        save();
        return updatedEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = super.updateSubtask(subtask);
        save();
        return updatedSubtask;
    }

    @Override
    public HashMap<Integer, Task> deleteTaskById(int id) {
        HashMap<Integer, Task> tasks = super.deleteTaskById(id);
        save();
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> deleteEpicById(int id) {
        HashMap<Integer, Epic> epics = super.deleteEpicById(id);
        save();
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> deleteSubtaskById(int id) {
        HashMap<Integer, Subtask> subtasks = super.deleteSubtaskById(id);
        save();
        return subtasks;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }
}
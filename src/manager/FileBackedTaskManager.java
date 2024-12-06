package manager;

import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskConverter;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
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
                writer.write(TaskConverter.toString(task) + "\n");
            }
            for (Epic epic : epicHashMap.values()) {
                writer.write(TaskConverter.toString(epic) + "\n");
            }
            for (Subtask subtask : subtasksHashMap.values()) {
                writer.write(TaskConverter.toString(subtask) + "\n");
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
                Task task = TaskConverter.fromString(line);
                switch (task.getType()) {
                    case TASK:
                        manager.addNewTask(task);
                        break;
                    case SUBTASK:
                        manager.addNewSubtask((Subtask) task);
                        break;
                    case EPIC:
                        manager.addNewEpic((Epic) task);
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Неизвестный тип задачи: %s", task.getType()));
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
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
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

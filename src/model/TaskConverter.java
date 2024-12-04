package model;

import manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskConverter {
    private static final String format = "HH:mm dd.MM.yyyy";

    public static String toString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if (task.getType() == TaskType.TASK) {
            return String.format("%d,%s,%s,%s,%s,%d,%s",
                    task.getId(),
                    TaskType.TASK,
                    task.getName(),
                    task.getStatus(),
                    task.getDescription(),
                    task.getDuration().toMinutes(),
                    task.getStartTime().format(formatter));
        } else if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,%s,%s,%s,%s,%d,%d,%s",
                    subtask.getId(),
                    TaskType.SUBTASK,
                    subtask.getName(),
                    subtask.getStatus(),
                    subtask.getDescription(),
                    subtask.getEpicId(),
                    subtask.getDuration().toMinutes(),
                    subtask.getStartTime().format(formatter));
        } else if (task.getType() == TaskType.EPIC) {
            Epic epic = (Epic) task;
            return String.format("%d,%s,%s,%s,%s,%d,%s",
                    epic.getId(),
                    TaskType.EPIC,
                    epic.getName(),
                    epic.getStatus(),
                    epic.getDescription(),
                    epic.getDuration() != null ? epic.getDuration().toMinutes() : 0,
                    epic.getStartTime() != null ? epic.getStartTime().format(formatter) : "null");
        } else {
            throw new IllegalArgumentException("Неподдерживаемый тип задачи.");
        }
    }

    public static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType taskType = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        long durationMinutes = Long.parseLong(fields[taskType == TaskType.TASK || taskType == TaskType.EPIC ? 5 : 6]);
        Duration duration = Duration.ofMinutes(durationMinutes);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime startTime = fields[taskType == TaskType.TASK || taskType == TaskType.EPIC ? 6 : 7].equals("null")
                ? null
                : LocalDateTime.parse(fields[taskType == TaskType.TASK || taskType == TaskType.EPIC ? 6 : 7], formatter);

        if (taskType == TaskType.TASK) {
            return new Task(id, name, description, status, duration, startTime);
        } else if (taskType == TaskType.SUBTASK) {
            int epicId = Integer.parseInt(fields[5]);
            return new Subtask(id, name, description, epicId, status, duration, startTime);
        } else if (taskType == TaskType.EPIC) {
            return new Epic(id, name, description, status, duration, startTime);
        } else {
            throw new IllegalArgumentException(String.format("Неподдерживаемый тип задачи: %s", taskType));
        }
    }

}

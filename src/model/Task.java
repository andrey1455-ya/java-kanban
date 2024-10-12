package model;

import manager.TaskType;

import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;

    public Task(int id, String name, String description, Status status) { //Конструктор со всеми полями
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, String description) { //Конструктор без статуса, по умолчанию NEW
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status) { //Конструктор без id
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) { //Конструктор без id
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,", id, TaskType.TASK, name, status, description);
    }

    public static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType taskType = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        if (taskType == TaskType.TASK) {
            return new Task(id, name, description, status);
        }
        throw new IllegalArgumentException(String.format("Неподдерживаемый тип задачи: %s", taskType));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

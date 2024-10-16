package model;

import manager.TaskType;

import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtaskIds(ArrayList<Integer> idsList) {
        this.subtasksIds = idsList;
    }

    public void addSubtaskId(int subtaskId) {
        subtasksIds.add(subtaskId);
    }

    public void cleanSubtaskIds() {
        subtasksIds.clear();
    }

    public static Epic fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType taskType = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        if (taskType == TaskType.EPIC) {
            return new Epic(id, name, description, status);
        }
        throw new IllegalArgumentException(String.format("Неподдерживаемый тип задачи: %s", taskType));
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,", getId(), TaskType.EPIC, getName(), getStatus(), getDescription());
    }
}

package model;

import manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Integer> subtasksIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    public static Epic fromString(String value) {
        return (Epic) TaskConverter.fromString(value);
    }
}

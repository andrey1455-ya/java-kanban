package model;

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

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() + '\'' +
                ", name=" + getName() + '\'' +
                ", description=" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasksIds=" + getSubtasksIds() + '}';
    }
}

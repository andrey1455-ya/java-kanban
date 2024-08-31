package model;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, int epicId, String name, String description, Status status) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                ", epicId=" + epicId +
                ", status=" + getStatus() +
                '}';
    }
}

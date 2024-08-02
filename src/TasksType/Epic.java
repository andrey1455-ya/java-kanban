package TasksType;

import Status.Status;

import java.util.HashMap;


public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasksForEpic = new HashMap<>();

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public void addSubtaskToEpic(Subtask subtask) {
        subtasksForEpic.put(subtask.getId(), subtask);
    }

    public HashMap<Integer, Subtask> getSubtasksForEpic(){
        return subtasksForEpic;
    }

    public Status isSubtasksDone() { // Метод для смены статуса эпика.
        int doneCounter = 0;
        Status epicSubtasksStatus = Status.NEW;
        for (Subtask subtask : subtasksForEpic.values()) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                epicSubtasksStatus = Status.IN_PROGRESS;
            } else if (subtask.getStatus() == Status.DONE) {
                doneCounter++;
            }
        }
//        if (doneCounter<subtasksForEpic.size()){
//            epicSubtasksStatus = Status.IN_PROGRESS;
//        }
        if (doneCounter == subtasksForEpic.size()) {
            epicSubtasksStatus = Status.DONE;
        }
        return epicSubtasksStatus;
    }

    @Override
    public String toString() {
        return "Epic{name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasksForEpic=" + subtasksForEpic +'}';
    }
}

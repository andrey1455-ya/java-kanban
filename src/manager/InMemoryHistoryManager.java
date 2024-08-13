package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> taskHistoryList = new ArrayList<>(10);

    @Override
    public void addTaskToHistory(Task task) {
        if (task != null) {
            if (taskHistoryList.size() < 10) {
                taskHistoryList.add(task);
            } else {
                taskHistoryList.removeFirst();
                taskHistoryList.add(task);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistoryList;
    }
}
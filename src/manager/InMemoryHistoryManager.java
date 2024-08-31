package manager;

import model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY_SIZE = 10;
    private final LinkedList<Task> taskHistoryList = new LinkedList<>();

    @Override
    public void addTaskToHistory(Task task) {
        if (task != null) {
            if (taskHistoryList.size() < MAX_HISTORY_SIZE) {
                taskHistoryList.add(task);
            } else {
                taskHistoryList.removeFirst();
                taskHistoryList.add(task);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(taskHistoryList);
    }

    @Override
    public void remove(int id) {

    }
}
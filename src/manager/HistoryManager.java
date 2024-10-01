package manager;

import model.Task;

import java.util.List;

public interface HistoryManager {

    void addTaskToHistory(Task task);

    List<Task> getHistory();

    void remove(int id);
}

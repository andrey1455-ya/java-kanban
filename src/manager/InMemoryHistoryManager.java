package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> taskHistoryMap = new HashMap<>();

    @Override
    public void addTaskToHistory(Task task) { //Добавление задачи в историю
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() { //Получение списка задач в из истории
        List<Task> historyList = new ArrayList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            historyList.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return historyList;
    }

    @Override
    public void remove(int id) { //Удаление задачи из истории
        Node<Task> node = taskHistoryMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    private void linkLast(Task task) { //Добавление задачи в конец двусвязного списка
        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        taskHistoryMap.put(task.getId(), newNode);
    }

    private void removeNode(Node<Task> node) { //Удаление задачи из двусвязного списка
        if (node == null) {
            return;
        }
        Node<Task> prevNode = node.prev;
        Node<Task> nextNode = node.next;
        if (prevNode == null) {
            head = nextNode;
        } else {
            prevNode.next = nextNode;
            node.prev = null;
        }
        if (nextNode == null) {
            tail = prevNode;
        } else {
            nextNode.prev = prevNode;
            node.next = null;
        }
        node.data = null;
    }
}
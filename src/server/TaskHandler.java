package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import manager.TaskManager;
import exception.TaskValidationException;
import model.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            switch (method) {
                case "GET":
                    if (path.equals("/tasks")) {
                        getTask(exchange);
                    } else if (path.matches("/tasks/\\d+")) {
                        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                        getTaskById(exchange, id);
                    } else {
                        sendNotFound(exchange, "Unknown path: " + path);
                    }
                    break;
                case "POST":
                    if (path.equals("/tasks")) {
                        Task task = gson.fromJson(body, Task.class);
                        createTask(exchange, task);
                    } else if (path.matches("/tasks/\\d+")) {
                        Task task = gson.fromJson(body, Task.class);
                        updateTask(exchange, task);
                    } else {
                        sendNotFound(exchange, "Unknown path: " + path);
                    }
                    break;
                case "DELETE":
                    if (path.matches("/tasks/\\d+")) {
                        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                        deleteTask(exchange, id);
                    } else {
                        sendNotFound(exchange, "Unknown path: " + path);
                    }
                    break;
                default:
                    sendNotFound(exchange, "Method not allowed: " + method);
                    break;
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange, e.getMessage());
        }
    }

    public void getTask(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        String json = gson.toJson(tasks);
        sendText(exchange, json);
    }

    public void getTaskById(HttpExchange exchange, int id) throws IOException, NotFoundException {
        Task task = taskManager.getTaskById(id);
        String json = gson.toJson(task);
        sendText(exchange, json);
    }

    public void createTask(HttpExchange exchange, Task task) throws IOException {
        try {
            String id = String.valueOf(taskManager.addNewTask(task));
            sendCode(exchange, "Таска успешно добавлена, id = " + id);
        } catch (TaskValidationException e) {
            sendHasInteractions(exchange, e.getMessage());
        }
    }

    public void updateTask(HttpExchange exchange, Task task) throws IOException {
        try {
            taskManager.updateTask(task);
            sendCode(exchange, "Таска успешно обновлена");
        } catch (TaskValidationException e) {
            sendHasInteractions(exchange, e.getMessage());
        }
    }

    public void deleteTask(HttpExchange exchange, int id) throws IOException, NotFoundException {
        taskManager.deleteTaskById(id);
        sendText(exchange, "Таска успешно удалена");
    }
}

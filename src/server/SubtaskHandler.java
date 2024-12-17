package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import manager.TaskManager;
import exception.TaskValidationException;
import model.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            switch (method) {
                case "GET":
                    if (path.equals("/subtasks/")) {
                        getSubtask(exchange);
                    } else if (path.matches("/subtasks/\\d+")) {
                        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                        getSubtaskById(exchange, id);
                    } else {
                        sendNotFound(exchange, "Unknown path: " + path);
                    }
                    break;
                case "POST":
                    if (path.equals("/subtasks")) {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        int id = subtask.getId();
                        if (id == 0) {
                            createSubtask(exchange, subtask);
                        } else {
                            updateSubtask(exchange, subtask);
                        }
                    } else {
                        sendNotFound(exchange, "Unknown path: " + path);
                    }
                    break;
                case "DELETE":
                    if (path.matches("/subtasks/\\d+")) {
                        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                        deleteSubtask(exchange, id);
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

    public void getSubtask(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        String json = gson.toJson(subtasks);
        sendText(exchange, json);
    }

    public void getSubtaskById(HttpExchange exchange, int id) throws IOException, NotFoundException {
        Subtask subtask = taskManager.getSubtaskById(id);
        if (subtask == null) {
            sendNotFound(exchange, "Сабтаска с id " + id + " не найдена");
        } else {
            String json = gson.toJson(subtask);
            sendText(exchange, json);
        }
    }

    public void createSubtask(HttpExchange exchange, Subtask subtask) throws IOException {
        try {
            String id = String.valueOf(taskManager.addNewSubtask(subtask));
            sendCode(exchange, "Сабтаска успешно добавлена, id = " + id);
        } catch (TaskValidationException e) {
            sendHasInteractions(exchange, e.getMessage());
        }
    }

    public void updateSubtask(HttpExchange exchange, Subtask subtask) throws IOException, NotFoundException {
        try {
            taskManager.updateSubtask(subtask);
            sendCode(exchange, "Сабтаска успешно обновлена");
        } catch (TaskValidationException e) {
            sendHasInteractions(exchange, e.getMessage());
        }
    }

    public void deleteSubtask(HttpExchange exchange, int id) throws IOException, NotFoundException {
        taskManager.deleteSubtaskById(id);
        sendText(exchange, "Сабтаска успешно удалена");
    }
}

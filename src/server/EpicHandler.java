package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import manager.TaskManager;
import model.Epic;
import model.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("GET".equals(method) && path.equals("/epics")) {
                getEpics(exchange);
            }
            if ("GET".equals(method) && path.matches("/epics/\\d+")) {
                int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                getEpicById(exchange, id);
            }
            if ("GET".equals(method) && path.matches("/epics/\\d+/subtasks")) {
                int id = Integer.parseInt(path.split("/")[2]);
                getEpicSubtasks(exchange, id);
            }
            if ("POST".equals(method) && path.equals("/epics")) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(body, Epic.class);
                createEpic(exchange, epic);
            }
            if ("DELETE".equals(method) && path.matches("/epics/\\d+")) {
                int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                deleteEpic(exchange, id);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange, e.getMessage());
        }
    }

    public void getEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getAllEpics();
        String json = gson.toJson(epics);
        sendText(exchange, json);
    }

    public void getEpicById(HttpExchange exchange, int id) throws IOException, NotFoundException {
        Epic epic = taskManager.getEpicById(id);
        String json = gson.toJson(epic);
        sendText(exchange, json);
    }

    public void getEpicSubtasks(HttpExchange exchange, int id) throws IOException, NotFoundException {
        Epic epic = taskManager.getEpicById(id);
        List<Integer> subTaskId = epic.getSubtasksIds();
        List<Subtask> subTasks = new ArrayList<>();
        for (Integer ids : subTaskId) {
            subTasks.add(taskManager.getSubtaskById(ids));
        }
        String json = gson.toJson(subTasks);
        sendText(exchange, json);
    }

    public void createEpic(HttpExchange exchange, Epic epic) throws IOException {
        String id = String.valueOf(taskManager.addNewEpic(epic));
        sendCode(exchange, "Задача успешно добавлена, id = " + id);
    }

    public void deleteEpic(HttpExchange exchange, int id) throws IOException, NotFoundException {
        taskManager.deleteEpicById(id);
        sendText(exchange, "Задача успешно удалена");

    }
}

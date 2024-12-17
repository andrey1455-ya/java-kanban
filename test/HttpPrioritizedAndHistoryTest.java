import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exception.NotFoundException;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import model.Epic;
import model.Subtask;
import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpPrioritizedAndHistoryTest {

    private HttpTaskServer server;
    private TaskManager taskManager;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @AfterEach
    void shutDown() {
        server.stop();
    }

    @Test
    public void shouldRequestGetPrioritizedList() throws IOException, InterruptedException {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.addNewTask(task1);
        Task task2 = new Task(2, "Task 2", "Description task 2", Status.NEW,
                Duration.ofHours(1), LocalDateTime.now().plusHours(2));
        taskManager.addNewTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        List<Task> prioritizedTasks = gson.fromJson(responseBody, new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(2, prioritizedTasks.size());
        assertEquals(task1.getId(), prioritizedTasks.get(0).getId());
        assertEquals(task2.getId(), prioritizedTasks.get(1).getId());
        assertEquals(task1.getName(), prioritizedTasks.get(0).getName());
        assertEquals(task2.getName(), prioritizedTasks.get(1).getName());
    }

    @Test
    public void shouldRequestGetHistory() throws IOException, InterruptedException, NotFoundException {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now());
        Task task2 = new Task(1, "Task 2", "Description 2", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusHours(2));
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask(1, 3, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewSubtask(subtask);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> history = taskManager.getHistory();
        assertEquals(4, history.size());
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
        assertTrue(history.contains(epic));
        assertTrue(history.contains(subtask));
    }
}

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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpTasksTest {

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
    public void shouldRequestGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task(16, "Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromResponse = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertNotNull(tasksFromResponse);
        assertEquals(1, tasksFromResponse.size());
        assertEquals("Task 1", tasksFromResponse.getFirst().getName());
    }

    @Test
    public void shouldRequestGetTaskById() throws IOException, InterruptedException {
        Task task = new Task(16, "Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int taskId = taskManager.addNewTask(task).getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task taskFromResponse = gson.fromJson(response.body(), Task.class);
        assertNotNull(taskFromResponse);
        assertEquals("Task 1", taskFromResponse.getName());
    }

    @Test
    public void shouldRequestAddTask() throws IOException, InterruptedException {
        Task task = new Task(16, "Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.
                ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = taskManager.getAllTasks();
        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Task 1", tasksFromManager.getFirst().getName());
    }

    @Test
    public void shouldRequestUpdateTask() throws IOException, InterruptedException, NotFoundException {
        Task task = new Task(16, "Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        Task updatedTask = new Task(1, "Task 1 updated", " updated Description task 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int taskId = taskManager.addNewTask(task).getId();
        String taskJson = gson.toJson(updatedTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Task testingTask = taskManager.getTaskById(taskId);
        assertEquals("Task 1 updated", testingTask.getName());
    }

    @Test
    public void shouldRequestDeleteTask() throws IOException, InterruptedException {
        Task task = new Task(16, "Task 1", "Description 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int taskId = taskManager.addNewTask(task).getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertThrows(NotFoundException.class, () -> taskManager.getTaskById(taskId));
    }
}

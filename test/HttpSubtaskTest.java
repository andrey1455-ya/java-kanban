import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exception.NotFoundException;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
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


public class HttpSubtaskTest {

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
    public void shouldRequestGetAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int epicId = taskManager.addNewEpic(epic).getId();
        Subtask subtask = new Subtask(1, epicId, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> subtasksFromResponse = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {
        }.getType());
        assertNotNull(subtasksFromResponse);
        assertEquals(1, subtasksFromResponse.size());
        assertEquals("Subtask 1", subtasksFromResponse.getFirst().getName());
    }

    @Test
    public void shouldRequestGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int epicId = taskManager.addNewEpic(epic).getId();
        Subtask subtask = new Subtask(1, epicId, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + "2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask subtaskFromResponse = gson.fromJson(response.body(), Subtask.class);
        assertNotNull(subtaskFromResponse);
        assertEquals("Subtask 1", subtaskFromResponse.getName());
    }

    @Test
    public void shouldRequestAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewSubtask(subtask);
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();
        assertNotNull(subtasksFromManager);
        assertEquals(1, subtasksFromManager.size());
        assertEquals("Subtask 1", subtasksFromManager.getFirst().getName());
    }

    @Test
    public void shouldRequestUpdateSubtask() throws IOException, InterruptedException, NotFoundException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int epicId = taskManager.addNewEpic(epic).getId();
        Subtask subtask = new Subtask(1, epicId, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewSubtask(subtask);
        Subtask updatedSubtask = new Subtask(2, epicId, "Subtask 1 updated",
                "Subtask 1 Description updated ", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        String subtaskJson = gson.toJson(updatedSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Subtask testingSubtask = taskManager.getSubtaskById(2);
        assertEquals("Subtask 1 updated", testingSubtask.getName());
    }

    @Test
    public void shouldRequestDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int epicId = taskManager.addNewEpic(epic).getId();
        Subtask subtask = new Subtask(1, epicId, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + "1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertThrows(NotFoundException.class, () -> taskManager.getSubtaskById(subtask.getId()));
    }
}

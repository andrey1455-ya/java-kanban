import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exception.NotFoundException;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Status;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpEpicsTest {
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
    public void shouldRequestGetAllEpics() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> epicsFromResponse = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {
        }.getType());
        assertNotNull(epicsFromResponse);
        assertEquals(1, epicsFromResponse.size());
        assertEquals("Epic 1", epicsFromResponse.getFirst().getName());
    }

    @Test
    public void shouldRequestGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int epicId = taskManager.addNewEpic(epic).getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic epicFromResponse = gson.fromJson(response.body(), Epic.class);
        assertNotNull(epicFromResponse);
        assertEquals("Epic 1", epicFromResponse.getName());
    }

    @Test
    public void shouldRequestGetEpicSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int epicId = taskManager.addNewEpic(epic).getId();
        Subtask subtask = new Subtask(1, 1, "Subtask 1", "Subtask 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        taskManager.addNewSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId + "/subtasks");
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
    public void shouldRequestAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> epicsFromManager = taskManager.getAllEpics();
        assertNotNull(epicsFromManager);
        assertEquals(1, epicsFromManager.size());
        assertEquals("Epic 1", epicsFromManager.getFirst().getName());
    }

    @Test
    public void shouldRequestDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Epic 1 Description", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 2, 12, 16, 20, 0));
        int epicId = taskManager.addNewEpic(epic).getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertThrows(NotFoundException.class, () -> taskManager.getEpicById(epicId));
    }
}

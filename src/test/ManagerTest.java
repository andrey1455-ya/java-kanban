package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ManagerTest {

    @Test
    public void shouldBeManagerReturnsReadyToWorkInMemoryTaskManager(){
        TaskManager inMemoryTaskManager = Managers.getDefault();
        assertInstanceOf(InMemoryTaskManager.class, inMemoryTaskManager);
    }

    @Test
    public void shouldBeManagerReturnsReadyToWorkInMemoryHistoryManager(){
        HistoryManager inHistoryTaskManager = Managers.getDefaultHistory();
        assertInstanceOf(InMemoryHistoryManager.class, inHistoryTaskManager);
    }
}

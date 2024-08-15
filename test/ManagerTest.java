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
        //do
        TaskManager inMemoryTaskManager = Managers.getDefault();
        //check
        assertInstanceOf(InMemoryTaskManager.class, inMemoryTaskManager);
    }

    @Test
    public void shouldBeManagerReturnsReadyToWorkInMemoryHistoryManager(){
        //do
        HistoryManager inHistoryTaskManager = Managers.getDefaultHistory();
        //check
        assertInstanceOf(InMemoryHistoryManager.class, inHistoryTaskManager);
    }
}

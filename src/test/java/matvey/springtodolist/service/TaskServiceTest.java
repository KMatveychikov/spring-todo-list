package matvey.springtodolist.service;

import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.Comment;
import matvey.springtodolist.model.FileInfo;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.model.User;
import matvey.springtodolist.repository.FileInfoRepository;
import matvey.springtodolist.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    TaskRepository taskRepository = mock(TaskRepository.class);
    FileInfoRepository fileInfoRepository = mock(FileInfoRepository.class);
    AuthService authService = mock(AuthService.class);

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("nullnull"));
    }

    @Test
    void testAddTask() throws IOException {
        AddTaskRequest request = mock(AddTaskRequest.class);
        when(request.getTitle()).thenReturn("Test Title");
        when(request.getText()).thenReturn("Test Text");
        when(request.getResponsibleUserId()).thenReturn("123");
        when(authService.getCurrentUserId()).thenReturn("456");

        TaskService taskService = new TaskService(taskRepository, fileInfoRepository, authService);

        Task result = taskService.addTask(request);

        verify(taskRepository).save(argThat(task -> {
            assertEquals("Test Title", task.getTitle());
            assertEquals("Test Text", task.getText());
            assertEquals("123", task.getResponsibleUserId());
            assertEquals("456", task.getOwnerUserId());
            assertFalse(task.isCompleted());

            return true;
        }));
    }

    @Test
    void testUpdateTask() {
        TaskService taskService = new TaskService(taskRepository, fileInfoRepository, authService);
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);
        Task testTask = new Task();
        Task updatedTask = taskService.updateTask(testTask);
        verify(taskRepository).save(testTask);
        assertEquals(testTask, updatedTask);
    }

    @Test
    void testGetTaskById() {
        TaskService taskService = new TaskService(taskRepository, fileInfoRepository, authService);
        String taskId = "1";
        Task expectedTask = new Task(taskId, "Sample task", "text", new ArrayList<>(), new ArrayList<>(), "ownerId", "responsibleId", new ArrayList<>(), false);
        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(expectedTask));
        Task actualTask = taskService.getTaskById(taskId);
        assertEquals(expectedTask, actualTask);
    }

    @Test
    void testAddComment() {
        TaskService taskService = new TaskService(taskRepository, fileInfoRepository, authService);
        Task task1 = new Task("1", "Sample task", "text", new ArrayList<>(), new ArrayList<>(), "ownerId", "responsibleId", new ArrayList<>(), false);
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task1));
        Task task = taskService.addComment("1", "This is a comment");
        List<Comment> comments = task.getComments();
        assertEquals(1, comments.size());
        Comment comment = comments.get(0);
        assertEquals("This is a comment", comment.getText());
    }

    @Test
    void addPerformerUser() {
        TaskService taskService = new TaskService(taskRepository, fileInfoRepository, authService);
        Task task = new Task("1", "Sample task", "text", new ArrayList<>(), new ArrayList<>(), "ownerId", "responsibleId", new ArrayList<>(), false);
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));
        when(authService.isPresentUser(anyString())).thenReturn(true);

        Task updatedTask = taskService.addPerformerUser("1", "432");
        assertEquals(task.getPerformerUsersId().size(), updatedTask.getPerformerUsersId().size());
        assertEquals(task.getPerformerUsersId().get(0), updatedTask.getPerformerUsersId().get(0));
    }

    @Test
    @Disabled
    void addFileToTask() throws IOException {


    }

    @Test
    @Disabled
    void addDefaultTaskDirectory() {
    }
}
package matvey.springtodolist.service;

import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.*;
import matvey.springtodolist.repository.FileInfoRepository;
import matvey.springtodolist.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskServiceTest {
    TaskRepository taskRepository = mock(TaskRepository.class);
    FileInfoRepository fileInfoRepository = mock(FileInfoRepository.class);
    AuthService authService = mock(AuthService.class);

    TaskService taskService = new TaskService(
            taskRepository,
            fileInfoRepository,
            authService);

    String exampleText = "LoremIpsum";

    Todo todo = new Todo(UUID.randomUUID().toString(), exampleText, false);

    Task task = new Task("1",
            "testTask",
            "text",
            new ArrayList<>(),
            new ArrayList<>(),
            "1",
            "1",
            new ArrayList<>(),
            LocalDateTime.now(),
            new ArrayList<>(),
            new ArrayList(List.of(todo)),
            false);

    AddTaskRequest addTaskRequest = new AddTaskRequest("testTask",
            "text",
            LocalDateTime.now(),
            "1",
            "1");
    User user = new User("1",
            "testUser",
            "testUser@example.com",
            "12345",
            Role._USER,
            List.of());

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("nullnull"));
    }

    @Test
    void testAddTask() throws IOException {
        Task testTask = taskService.addTask(addTaskRequest);

        assertEquals(addTaskRequest.getTitle(), testTask.getTitle());
        assertEquals(addTaskRequest.getText(), testTask.getText());
        assertEquals(addTaskRequest.getResponsibleUserId(), testTask.getResponsibleUserId());

    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

        Task testTask = taskService.getTaskById(task.get_id());

        assertNotNull(testTask);
        assertEquals(task, testTask);
    }

    @Test
    void editText() {
        String exampleText = "LoremIpsum";
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

        Task testTask = taskService.editText(task.get_id(), exampleText);

        assertEquals(exampleText, testTask.getText());
    }

    @Test
    void addTag() {
        Tag tag = new Tag("id", "title", "#FFFF");

        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

        Task testTask = taskService.addTag(task.get_id(), tag.getTitle(), tag.getColor());

        assertNotNull(testTask.getTags());
        assertNotNull(tag.getTitle(), testTask.getTags().get(0).getTitle());
        assertNotNull(tag.getColor(), testTask.getTags().get(0).getColor());

    }

    @Test
    void removeTag() {
        Tag tag = new Tag("id", "title", "#FFFF");
        Task testTask = new Task("1",
                "testTask",
                "text",
                new ArrayList<>(),
                new ArrayList<>(),
                "1",
                "1",
                new ArrayList<>(),
                LocalDateTime.now(),
                new ArrayList(List.of(tag)),
                new ArrayList<>(),
                false);
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(testTask));

        taskService.removeTag(testTask.get_id(), tag.getId());

        assertFalse(testTask.getTags().contains(tag));
    }

    @Test
    void addTodo() {

        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));
        taskService.addTodo(task.get_id(), exampleText);

        assertTrue(task.getTodos().size() > 0);
        assertEquals(exampleText, task.getTodos().get(0).getText());
    }

    @Test
    void testGetTodoById() {
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

        Todo testTodo = taskService.getTodoById(task.get_id(), todo.getId());

        assertEquals(exampleText, testTodo.getText());
        assertEquals(todo.getId(), testTodo.getId());
    }

    @Test
    void testCompleteTodo() {
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

        taskService.completeTodo(task.get_id(), task.getTodos().get(0).getId());

        assertTrue(task.getTodos().get(0).isCompleted());
    }

    @Test
    void testCompleteTask() {
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

        taskService.completeTask(task.get_id());

        assertTrue(task.isCompleted());

    }

    @Test
    void addComment() {
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));
        when(authService.getCurrentUserName()).thenReturn(user.getUsername());
        taskService.addComment(task.get_id(), exampleText);

        assertEquals(task.getComments().get(0).getText(), exampleText);
        assertEquals(task.getComments().get(0).getAuthorName(), user.getUsername());
    }

    @Test
    void addPerformerUser() {
    }

    @Test
    void removePerformerUser() {
    }

    @Test
    void addFileToTask() {
    }

    @Test
    void addDefaultTaskDirectory() {
    }
}
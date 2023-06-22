package matvey.springtodolist.service;

import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.Board;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.repository.BoardRepository;
import matvey.springtodolist.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BoardServiceTest {

    AuthService authService = mock(AuthService.class);
    TaskService taskService = mock(TaskService.class);
    BoardRepository boardRepository = mock(BoardRepository.class);


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddBoard() {
        BoardService boardService = new BoardService(authService, taskService, boardRepository);
        when(authService.getCurrentUserId()).thenReturn("testUser");
        Board exampleBoard = new Board("1", "title", "testUser", new ArrayList<>());
        Board testBoard = boardService.addBoard("title", "testUser");
        assertEquals(exampleBoard.getTitle(), testBoard.getTitle());
        assertEquals(exampleBoard.getUserId(), testBoard.getUserId());
    }

    @Test
    void testAddTaskToDefaultBoard() throws IOException {
    }

    @Test
    void testAddTaskToBoard() {
        BoardService boardService = new BoardService(authService, taskService, boardRepository);
        when(authService.getCurrentUserId()).thenReturn("testUser");
        Task task = new Task("taskId", "Sample task", "text", new ArrayList<>(), new ArrayList<>(), "ownerId", "responsibleId", new ArrayList<>(), false);
        List<String> tasksId = new ArrayList<>();
        tasksId.add(task.get_id());
        Board exampleBoard = new Board("1", "title", "testUser", tasksId);
        when(boardRepository.findById(anyString())).thenReturn(Optional.of(exampleBoard));
        when(taskService.getTaskById(anyString())).thenReturn(task);
        Board testBoard = boardService.addTaskToBoard("taskId", "1");

        assertEquals(exampleBoard.getTasksId().get(0), testBoard.getTasksId().get(0));


    }

    @Test
    void removeTaskFromBoard() {
    }

    @Test
    void getBoardsByUserId() {
    }
}
package matvey.springtodolist.service;

import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.Board;
import matvey.springtodolist.model.Role;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.model.User;
import matvey.springtodolist.repository.BoardRepository;
import matvey.springtodolist.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class BoardServiceTest {

    AuthService authService = mock(AuthService.class);
    TaskService taskService = mock(TaskService.class);
    BoardRepository boardRepository = mock(BoardRepository.class);
    BoardService boardService = new BoardService(authService, taskService, boardRepository);

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
            new ArrayList<>(),
            false);
    Board board = new Board("1", "testBoard", new ArrayList<>());

    User user = new User("1",
            "testUser",
            "testUser@example.com",
            "12345",
            Role._USER,
            List.of(board.get_id()));


    AddTaskRequest addTaskRequest = new AddTaskRequest("testTask", "text", LocalDateTime.now(), "1", "1");



    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddBoard() {
        Board testBoard = boardService.addBoard("testBoard");
        assertEquals(board.getTitle(), testBoard.getTitle());
    }

    @Test
    void testAddTaskToBoard() throws IOException {
        when(authService.getCurrentUser()).thenReturn(user);
        when(boardRepository.findById(anyString())).thenReturn(Optional.of(board));
        when(taskService.addTask(addTaskRequest)).thenReturn(task);
        when(taskService.getTaskById(anyString())).thenReturn(task);

        Board testBoard = boardService.addTaskToBoard(addTaskRequest);
        assertEquals(board.getTasksId().get(0), testBoard.getTasksId().get(0));
    }

    @Test
    void testGetTasksFromBoard() {
        Board testBoard = new Board("2", "testBoard", List.of(task.get_id()));

        when(boardRepository.findById(anyString())).thenReturn(Optional.of(testBoard));
        when(taskService.getTaskById(anyString())).thenReturn(task);

        List<Task> testTasks = boardService.getTasksFromBoard(testBoard.get_id());

        assertEquals(task, testTasks.get(0));

    }

    @Test
    void testRemoveTaskFromBoard() {
        Board testBoard = new Board("2", "testBoard", new ArrayList(List.of(task)));
        when(boardRepository.findById(anyString())).thenReturn(Optional.of(testBoard));
        when(taskService.getTaskById(anyString())).thenReturn(task);

        boardService.removeTaskFromBoard(task.get_id(), testBoard.get_id());

        assertFalse(testBoard.getTasksId().contains("1"));
    }

    @Test
    void testGetBoardById() {
        when(boardRepository.findById(anyString())).thenReturn(Optional.of(board));

        Board testBoard = boardService.getBoardById(board.get_id());
        assertEquals(board, testBoard);
    }

    @Test
    void testGetBoardsByUserId() {
        when(authService.getUserById(anyString())).thenReturn(user);
        when(boardRepository.findById(anyString())).thenReturn(Optional.of(board));

        List<Board> boards = boardService.getBoardsByUserId(user.get_id());

        assertTrue(boards.contains(board));
    }
}
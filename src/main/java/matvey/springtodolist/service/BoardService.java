package matvey.springtodolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.Board;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final AuthService authService;
    private final TaskService taskService;
    private final BoardRepository boardRepository;

    public Board addBoard(String title, String userId) {
        Board board = Board.builder()
                .title(title)
                .userId(userId)
                .tasksId(new ArrayList<>())
                .build();
        boardRepository.save(board);
        return board;
    }


    public Board addTaskToDefaultBoard(AddTaskRequest request) throws IOException {
        if (!checkDefaultBoard(authService.getCurrentUserId())) {
            addBoard("default", authService.getCurrentUserId());
        }
        Task task = taskService.addTask(request);
        addTaskToBoard(task.get_id(), getDefaultBoardId(authService.getCurrentUserId()));
        return getBoardById(getDefaultBoardId(authService.getCurrentUserId()));
    }

    public Board addTaskToBoard(String taskId, String boardId) {
        Board board = getBoardById(boardId);
        List<String> tasksId = board.getTasksId();
        if (taskService.getTaskById(taskId) != null) {
            tasksId.add(taskId);
            board.setTasksId(tasksId);
            boardRepository.save(board);
            return board;
        } else {
            log.error("task {} not found", taskId);
            throw new RuntimeException("task not found");
        }
    }

    public Board removeTaskFromBoard(String taskId, String boardId) {
        Board board = getBoardById(boardId);
        List<String> tasksId = board.getTasksId();
        tasksId.remove(taskId);
        board.setTasksId(tasksId);
        boardRepository.save(board);
        return board;
    }

    public String getDefaultBoardId(String userId) {
        return getBoardsByUserId(userId)
                .stream()
                .filter(board -> Objects.equals(board.getTitle(), "default"))
                .findFirst().get().get_id();
    }

    public boolean checkDefaultBoard(String userId) {
        return getBoardsByUserId(userId).stream().anyMatch(board -> Objects.equals(board.getTitle(), "default"));
    }

    public Board getBoardById(String boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> {
            log.error("Board {} not found", boardId);
            throw new RuntimeException("Board not found");
        });
    }

    public List<Board> getBoardsByUserId(String userId) {
        return boardRepository.getBoardsByUserId(userId);
    }


}

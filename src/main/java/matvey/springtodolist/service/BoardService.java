package matvey.springtodolist.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import matvey.springtodolist.model.Board;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.model.User;
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

    public Board addBoard(String title) {
        Board board = Board.builder()
                .title(title)
                .tasksId(new ArrayList<>())
                .build();
        boardRepository.save(board);
        authService.addBoard(authService.getCurrentUserId(), board.get_id());
        return board;
    }

    public Board addTaskToBoard(String boardId, String title) throws IOException {
        Board board = getBoardById(boardId);
        Task task = taskService.addTask(title);
        List<String> tasksId = board.getTasksId();
        if (taskService.getTaskById(task.get_id()) != null) {
            tasksId.add(task.get_id());
            board.setTasksId(tasksId);
            boardRepository.save(board);
            return board;
        } else {
            log.error("task {} not found", task.get_id());
            throw new RuntimeException("task not found");
        }
    }

    public Board addTaskToDefaultBoard(String userId, String taskId){
        User user = authService.getUserById(userId);
        Board board = user.getBoardsId().stream().map(this::getBoardById).filter(b -> Objects.equals(b.getTitle(), "default")).findFirst().orElseThrow(()->{
            log.error("Board not found");
            throw new RuntimeException("Board not found");
        });
        List<String> tasks = board.getTasksId();
        if(taskService.getTaskById(taskId) != null){
            tasks.add(taskId);
            board.setTasksId(tasks);
            boardRepository.save(board);
        }
        return board;
    }

    public List<Task> getTasksFromBoard(String boardId){
        Board board = getBoardById(boardId);
        return board.getTasksId().stream().map(taskService::getTaskById).toList();
    }

    public Board removeTaskFromBoard(String taskId, String boardId) {
        Board board = getBoardById(boardId);
        List<String> tasksId = board.getTasksId();
        tasksId.remove(taskId);
        board.setTasksId(tasksId);
        boardRepository.save(board);
        return board;
    }


    public Board getBoardById(String boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> {
            log.error("Board {} not found", boardId);
            throw new RuntimeException("Board not found");
        });
    }

    public List<Board> getBoardsByUserId(String userId) {
        return authService.getUserById(userId).getBoardsId().stream().map(this::getBoardById).toList();
    }


}

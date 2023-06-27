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

    public Board addTaskToBoard(AddTaskRequest request) throws IOException {
        Board board = getBoardById(request.getBoardId());
        Task task = taskService.addTask(request);
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

package matvey.springtodolist.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.*;
import matvey.springtodolist.repository.BoardRepository;
import matvey.springtodolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final AuthService authService;
    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;

    @Value("${path_to_files}")
    String PATH_TO_FILES;

    public Task addTask(AddTaskRequest request) throws IOException {
        Task task = Task.builder()
                .title(request.getTitle())
                .text(request.getText())
                .ownerUserId(authService.getCurrentUser().get_id())
                .boardId(null)
                .responsibleUserId(request.getResponsibleUserId())
                .todos(new ArrayList<>())
                .comments(new ArrayList<>())
                .files(new ArrayList<>())
                .performerUsersId(new ArrayList<>())
                .isCompleted(false)
                .build();
        taskRepository.save(task);
        Files.createDirectory(Path.of(PATH_TO_FILES + task.get_id()));
        log.info("Task {} created", task.get_id());
        return task;
    }

    public Task completeTask(String taskId) {
        Task task = getTaskById(taskId);
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
        log.info("Task {} completed", taskId);
        return task;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getAllTaskByOwner(String ownerUserId) {
        return getAllTasks().stream().filter(task -> Objects.equals(task.getOwnerUserId(), ownerUserId)).collect(Collectors.toList());
    }

    public List<Task> getAllTaskByResponsible(String responsibleUserId) {
        return getAllTasks().stream().filter(task -> Objects.equals(task.getResponsibleUserId(), responsibleUserId)).collect(Collectors.toList());
    }

    public List<Task> getAllTaskByPerformer(String performerUserId) {
        return getAllTasks().stream().filter(task -> task.getPerformerUsersId().contains(performerUserId)).collect(Collectors.toList());
    }


    public Task getTaskById(String taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> {
            log.warn("Task {} not found", taskId);
            return new RuntimeException("Task not found");
        });
    }

    public Task addTodo(String taskId, String text) {
        Task task = getTaskById(taskId);
        List<Todo> todos = task.getTodos();
        Todo todo = Todo.builder()
                .todoId(UUID.randomUUID().toString())
                .text(text)
                .isCompleted(false)
                .build();
        todos.add(todo);
        task.setTodos(todos);
        taskRepository.save(task);
        log.info("Todo {} added", todo.getTodoId());
        return task;
    }

    public Task completeTodo(String taskId, String todoId) {
        Task task = getTaskById(taskId);
        List<Todo> todos = task.getTodos();
        Todo todo = todos.stream()
                .filter(t -> Objects.equals(t.getTodoId(), todoId))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Todo {} not found", todoId);
                    return new RuntimeException("Todo not found");
                });
        int index = todos.indexOf(todo);
        todo.setCompleted(!todo.isCompleted());
        todos.set(index, todo);
        task.setTodos(todos);
        taskRepository.save(task);
        log.info("Todo {} is completed", todoId);
        return task;
    }

    public Task addComment(String taskId, String text) {
        Task task = getTaskById(taskId);
        List<Comment> comments = task.getComments();
        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .authorName(authService.getCurrentUser().getName())
                .text(text)
                .build();
        comments.add(comment);
        task.setComments(comments);
        taskRepository.save(task);
        log.info("Comment {} added", comment.getId());
        return task;
    }

    public Task addFile(String taskId, MultipartFile file) throws IOException {
        Task task = getTaskById(taskId);
        String path = PATH_TO_FILES + taskId + "//" + file.getOriginalFilename();
        try {
            file.transferTo(new File(path));
        } catch (Exception e) {
            log.warn(e.toString());
            throw new IOException(e.toString());
        }
        List<FileInfo> files = task.getFiles();
        files.add(FileInfo.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .filePath(path)
                .build());
        task.setFiles(files);
        taskRepository.save(task);
        log.info("File {} added", file.getOriginalFilename());
        return task;
    }

    public ResponseEntity<?> getFile(String taskId, String fileName) throws IOException {
        Task task = getTaskById(taskId);
        FileInfo fileInfo = task.getFiles().stream().filter(fi -> Objects.equals(fi.getFileName(), fileName)).findFirst().orElseThrow(() -> {
            log.warn("File {} not found", fileName);
            return new FileNotFoundException();
        });
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf(fileInfo.getContentType()))
                .body(Files.readAllBytes(new File(fileInfo.getFilePath()).toPath()));
    }

    public Board addBoard(String title) {
        Board board = Board.builder()
                .userId(authService.getCurrentUser().get_id())
                .title(title)
                .tasksId(new ArrayList<>())
                .build();
        boardRepository.save(board);
        return board;
    }

    public Board getBoardById(String boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            log.error("board {} not found", boardId);
            throw new RuntimeException("board not found");
        });
        return board;
    }

    public Board addTaskToBoard(String boardId, String taskId) {
        Board board = getBoardById(boardId);
        if (taskRepository.findById(taskId).isPresent()) {
            List<String> tasksId = board.getTasksId();
            tasksId.add(taskId);
            board.setTasksId(tasksId);
            boardRepository.save(board);
            return board;
        } else {
            log.error("task {} not found", taskId);
            throw new RuntimeException("task not found");
        }
    }

    public Board removeTaskFromBoard(String boardId, String taskId) {
        Board board = getBoardById(boardId);
        if (board.getTasksId().contains(taskId)) {
            List<String> tasksId = board.getTasksId();
            tasksId.remove(tasksId);
            board.setTasksId(tasksId);
            boardRepository.save(board);
            return board;
        } else {
            log.error("task {} not found on board", taskId);
            throw new RuntimeException("task not found on board");
        }

    }

    public List<Task> getTasksByBoard(String boardId) {
        Board board = getBoardById(boardId);
        return board.getTasksId().stream().map(this::getTaskById).toList();
    }

    public List<Board> getBoardsByUserId(String userId) {
        return boardRepository.findAll().stream().filter(board -> Objects.equals(board.getUserId(), userId)).toList();
    }
}

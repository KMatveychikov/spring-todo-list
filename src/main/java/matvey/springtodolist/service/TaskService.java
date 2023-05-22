package matvey.springtodolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.Comment;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.model.Todo;
import matvey.springtodolist.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final AuthService authService;
    private final TaskRepository taskRepository;

    public Task addTask(AddTaskRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .text(request.getText())
                .ownerUserId(authService.getCurrentUser().get_id())
                .responsibleUserId(request.getResposibleUserId())
                .todos(new ArrayList<>())
                .comments(new ArrayList<>())
                .files(new ArrayList<>())
                .performerUsersId(new ArrayList<>())
                .isCompleted(false)
                .build();
        taskRepository.save(task);
        log.info("Task {} created", task.get_id());
        return task;
    }

    public Task completeTask(String taskId){
        Task task = getTaskById(taskId);
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
        log.info("Task {} completed", taskId);
        return task;
    }

    public List<Task> getAllTasks(){
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
        Comment comment  = Comment.builder()
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
        Path path = Path.of("files/"+taskId+"/"+file.getOriginalFilename());
        try {
            file.transferTo(path);
        } catch(Exception e) {
            log.warn(e.toString());
            throw new IOException(e.toString());
        }
        List<Path> paths = task.getFiles();
        paths.add(path);
        task.setFiles(paths);
        taskRepository.save(task);
        log.info("File {} added", file.getOriginalFilename());
        return task;
    }


}

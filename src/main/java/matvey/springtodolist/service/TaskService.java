package matvey.springtodolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import matvey.springtodolist.model.*;
import matvey.springtodolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final AuthService authService;
    private final BoardService boardService;

   public TaskService (@Lazy BoardService boardService , TaskRepository taskRepository, AuthService authService) {
       this.boardService = boardService;
       this.taskRepository = taskRepository;
       this.authService = authService;
   }

    @Value("${path_to_files}")
    private String path_to_files;

    public Task addTask(String title) throws IOException {
        Task task = Task.builder()
                .title(title)
                .text("")
                .ownerUserId(authService.getCurrentUserId())
                .performerUsersId(new ArrayList<>())
                .comments(new ArrayList<>())
                .files(new ArrayList<>())
                .todos(new ArrayList<>())
                .tags(new ArrayList<>())
                .isCompleted(false)
                .build();
        taskRepository.save(task);
        addDefaultTaskDirectory(task.get_id());
        return task;
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task setResponsibleUser(String taskId, String responsibleUserId){
        Task task = getTaskById(taskId);
        task.setResponsibleUserId(responsibleUserId);
        taskRepository.save(task);
        return task;
    }

    public Task updateTask(Task task) {
        taskRepository.save(task);
        return task;
    }

    public Task getTaskById(String taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("Task {} not found", taskId);
            throw new RuntimeException("Task not found");
        });
    }

    public Task editText(String taskId, String text) {
        Task task = getTaskById(taskId);
        task.setText(text);
        return updateTask(task);
    }

    public Task addTag(String taskId, String title, String color) {
        Task task = getTaskById(taskId);
        List<Tag> tags = task.getTags();
        tags.add(new Tag(UUID.randomUUID().toString(), title, color));
        task.setTags(tags);
        return updateTask(task);
    }


    public Task removeTag(String taskId, String id) {
        Task task = getTaskById(taskId);
        List<Tag> tags = task.getTags();
        Tag tag = tags.stream().filter(tag1 -> Objects.equals(tag1.getId(), id)).findFirst().orElseThrow(() -> {
            log.error("tag {} not found", id);
            throw new RuntimeException("tag not found");
        });
        tags.remove(tag);
        task.setTags(tags);
        return updateTask(task);
    }

    public Task addTodo(String taskId, String text) {
        Task task = getTaskById(taskId);
        List<Todo> todos = task.getTodos();
        todos.add(new Todo(UUID.randomUUID().toString(), text, false));
        task.setTodos(todos);
        return updateTask(task);
    }

    public Todo getTodoById(String taskId, String todoId) {
        Task task = getTaskById(taskId);
        List<Todo> todos = task.getTodos();
        return todos.stream().filter(todo -> todo.getId() == todoId).findFirst().orElseThrow(() -> {
            log.error("todo {} not found", todoId);
            throw new RuntimeException("todo not found");
        });
    }

    public Task completeTodo(String taskId, String todoId) {
        Task task = getTaskById(taskId);
        List<Todo> todos = task.getTodos();
        Todo todo = getTodoById(taskId, todoId);
        int index = todos.indexOf(todo);
        todo.setCompleted(!todo.isCompleted());
        todos.set(index, todo);
        task.setTodos(todos);
        return updateTask(task);
    }

    public Task completeTask(String taskId) {
        Task task = getTaskById(taskId);
        task.setCompleted(!task.isCompleted());
        updateTask(task);
        return task;
    }


    public Task addComment(String taskId, String text) {
        Task task = getTaskById(taskId);
        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .text(text)
                .authorName(authService.getCurrentUserName())
                .build();
        List<Comment> comments = task.getComments();
        comments.add(comment);
        task.setComments(comments);
        return updateTask(task);

    }

    public Task addPerformerUser(String taskId, String performerUserId) {
        Task task = getTaskById(taskId);
        List<String> performerUsers = task.getPerformerUsersId();
        if (authService.isPresentUser(performerUserId)) {
            performerUsers.add(performerUserId);
            task.setPerformerUsersId(performerUsers);
            return updateTask(task);
        } else {
            log.error("user {} not found", performerUserId);
            throw new RuntimeException("user  not found");
        }
    }

    public Task removePerformerUser(String taskId, String userId) {
        Task task = getTaskById(taskId);
        List<String> performerUsers = task.getPerformerUsersId();
        performerUsers.remove(userId);
        task.setPerformerUsersId(performerUsers);
        return updateTask(task);
    }


    public FileInfo addFileToTask(MultipartFile file, String taskId) throws IOException {
        Task task = getTaskById(taskId);
        String path = path_to_files + taskId + "//" + file.getOriginalFilename();
        try {
            file.transferTo(new File(path));
        } catch (Exception e) {
            log.error(e.toString());
            throw new IOException(e.toString());
        }
        List<FileInfo> files = task.getFiles();
        FileInfo fileInfo = FileInfo.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .filePath(path)
                .build();
        files.add(fileInfo);
        task.setFiles(files);
        updateTask(task);

        return fileInfo;
    }

    public void addDefaultTaskDirectory(String taskId) throws IOException {
        Files.createDirectory(Path.of(path_to_files + taskId));
    }

}

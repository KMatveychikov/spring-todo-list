package matvey.springtodolist.controller;

import lombok.RequiredArgsConstructor;
import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/add_task")
    public ResponseEntity<Task> addTask(@RequestBody AddTaskRequest request) throws IOException {
        return ResponseEntity.ok(taskService.addTask(request));
    }

    @PutMapping("/complete")
    public ResponseEntity<Task> completeTask(@RequestParam String taskId) {
        return ResponseEntity.ok(taskService.completeTask(taskId));
    }

    @GetMapping("/all_tasks")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping("/add_todo")
    public ResponseEntity<Task> addTodo(@RequestParam String taskId, @RequestParam String text) {
        return ResponseEntity.ok(taskService.addTodo(taskId, text));
    }

    @PostMapping("/complete_todo")
    public ResponseEntity<Task> completeTodo(@RequestParam String taskId, @RequestParam String todoId) {
        return ResponseEntity.ok(taskService.completeTodo(taskId, todoId));
    }

    @PostMapping("/add_comment")
    public ResponseEntity<Task> addComment(String taskId, String text) {
        return ResponseEntity.ok(taskService.addComment(taskId, text));
    }

    @PostMapping("/add_file")
    public ResponseEntity<Task> addFile(@RequestParam String taskId, @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(taskService.addFile(taskId, file));
    }

    @GetMapping("/get_file/{taskId}/{fileName}")
    public ResponseEntity<?> getFile(@PathVariable String taskId, @PathVariable String fileName) throws IOException {
        return taskService.getFile(taskId,fileName);
    }

}

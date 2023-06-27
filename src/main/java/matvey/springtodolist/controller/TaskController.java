package matvey.springtodolist.controller;

import lombok.RequiredArgsConstructor;
import matvey.springtodolist.model.FileInfo;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/task")
public class TaskController {
    private final TaskService taskService;


    @PostMapping("/{taskId}/add_comment")
    public ResponseEntity<Task> addComment(@PathVariable String taskId,
                                           @RequestParam String text) {
        return ResponseEntity.ok(taskService.addComment(taskId, text));
    }

    @PutMapping("/{taskId}/edit_text")
    public ResponseEntity<Task> editTaskText(@PathVariable String taskId,
                                             @RequestParam String text) {
        return ResponseEntity.ok(taskService.editText(taskId, text));
    }

    @PostMapping("/{taskId}/add_tag")
    public ResponseEntity<Task> addTag(@PathVariable String taskId,
                                       @RequestParam String title,
                                       @RequestParam String color) {
        return ResponseEntity.ok(taskService.addTag(taskId, title, color));
    }

    @DeleteMapping("/{taskId}/delete_tag")
    public ResponseEntity<Task> deleteTag(@PathVariable String taskId,
                                          @RequestParam String tagId) {
        return ResponseEntity.ok(taskService.removeTag(taskId, tagId));
    }

    @PostMapping("/{taskId}/add_todo")
    public ResponseEntity<Task> addTodo(@PathVariable String taskId,
                                        @RequestParam String todoText) {
        return ResponseEntity.ok(taskService.addTodo(taskId, todoText));
    }

    @PostMapping("/{taskId}/complete_todo")
    public ResponseEntity<Task> completeTodo(@PathVariable String taskId,
                                             @RequestParam String todoId) {
        return ResponseEntity.ok(taskService.completeTodo(taskId, todoId));
    }

    @PostMapping("/{taskId}/complete_task")
    public ResponseEntity<Task> completeTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.completeTask(taskId));
    }

    @PostMapping("/{taskId}/add_performer")
    public ResponseEntity<Task> addPerformerUser(@PathVariable String taskId,
                                                 @RequestParam String userId) {
        return ResponseEntity.ok(taskService.addPerformerUser(taskId, userId));
    }

    @DeleteMapping("/{taskId}/remove_performer")
    public ResponseEntity<Task> removePerformerUser(@PathVariable String taskId,
                                                    @RequestParam String userId) {
        return ResponseEntity.ok(taskService.removePerformerUser(taskId, userId));
    }

    @PostMapping("/{taskId}/add_file")
    public ResponseEntity<FileInfo> addFile(@PathVariable String taskId,
                                            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(taskService.addFileToTask(file, taskId));
    }

}

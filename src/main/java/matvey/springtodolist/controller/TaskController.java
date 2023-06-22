package matvey.springtodolist.controller;

import lombok.RequiredArgsConstructor;
import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/task")
public class TaskController {
  private final TaskService taskService;

  @PostMapping("/add")
  public ResponseEntity<Task> addTask(AddTaskRequest addTaskRequest) throws IOException {
      return ResponseEntity.ok(taskService.addTask(addTaskRequest));
  }

  @PostMapping("/add_comment")
  public ResponseEntity<Task> addComment(@RequestParam String taskId, @RequestParam String text){
      return ResponseEntity.ok(taskService.addComment(taskId,text));
  }
}

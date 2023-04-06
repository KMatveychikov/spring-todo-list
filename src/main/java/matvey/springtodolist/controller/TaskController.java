package matvey.springtodolist.controller;

import lombok.RequiredArgsConstructor;
import matvey.springtodolist.dto.AddCoUserResponse;
import matvey.springtodolist.dto.AddCommentRequest;
import matvey.springtodolist.dto.UserResponse;
import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.dto.todo.AddTodoRequest;
import matvey.springtodolist.dto.todo.CompleteTodoRequest;
import matvey.springtodolist.dto.todo.DeleteTodoRequest;
import matvey.springtodolist.dto.todo.EditTodoRequest;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.service.TaskService;
import matvey.springtodolist.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TodoService todoService;

    @PostMapping("/add_task")
    public void addTask(@RequestBody AddTaskRequest addTaskRequest) {
        taskService.addTask(addTaskRequest);
    }

    @GetMapping("/get_task_by_email/{email}")
    public List<Task> getTasksByUser(@PathVariable String email) {
        return taskService.getAllTasksByUser(email);
    }

    @GetMapping("/get_task_by_number/{taskNumber}")
    public Task getTaskByNumber(@PathVariable int taskNumber) {
        return taskService.getTaskByNumber(taskNumber);
    }

    @PostMapping("/add_todo")
    public void addTodo(@RequestBody AddTodoRequest addTodoRequest) {
        todoService.addTodo(addTodoRequest);
    }

    @PostMapping("/add_comment")
    public void addComment(@RequestBody AddCommentRequest addCommentRequest) {
        taskService.addComment(addCommentRequest);
    }

    @PostMapping("/add_couser")
    public void addCoUserToTask(@RequestBody AddCoUserResponse addCoUserResponse) {
        taskService.addCoUser(addCoUserResponse);
    }

    @DeleteMapping("/delete_todo")
    public void deleteTodo(@RequestBody DeleteTodoRequest deleteTodoRequest) {
        todoService.deleteTodo(deleteTodoRequest);
    }

    @PutMapping("/complete_todo")
    public void completeTodo(@RequestBody CompleteTodoRequest completeTodoRequest) {
        todoService.completeTodo(completeTodoRequest);
    }

    @PutMapping("/edit_todo")
    public void editTodo(@RequestBody EditTodoRequest editTodoRequest) {
        todoService.editTodo(editTodoRequest);
    }

}

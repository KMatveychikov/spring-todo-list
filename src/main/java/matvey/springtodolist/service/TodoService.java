package matvey.springtodolist.service;

import lombok.AllArgsConstructor;
import matvey.springtodolist.dto.todo.AddTodoRequest;
import matvey.springtodolist.dto.todo.CompleteTodoRequest;
import matvey.springtodolist.dto.todo.DeleteTodoRequest;
import matvey.springtodolist.dto.todo.EditTodoRequest;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.model.Todo;
import matvey.springtodolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TodoService {
    private final TaskRepository taskRepository;

    public void addTodo(AddTodoRequest addTodoRequest) {
        Task task = taskRepository.findTaskByTaskNumber(addTodoRequest.getTaskNumber()).orElseThrow();
        task.getTodos().add(Todo.builder()
                .title(addTodoRequest.getTitle())
                .text(addTodoRequest.getText())
                .taskNumber(task.getTaskNumber())
                .todoId(task.getTodos().size() + 1)
                .isCompleted(false)
                .build());
        taskRepository.save(task);

    }

    public void completeTodo(CompleteTodoRequest completeTodoRequest) {
        Task task = taskRepository.findTaskByTaskNumber(completeTodoRequest.getTaskNumber()).orElseThrow();
        task.setTodos(task.getTodos().stream().peek(t -> {
            if (t.getTodoId() == completeTodoRequest.getTodoId()) t.setCompleted(!t.isCompleted());
        }).toList());
        taskRepository.save(task);

    }

    public void editTodo(EditTodoRequest editTodoRequest) {
        Task task = taskRepository.findTaskByTaskNumber(editTodoRequest.getTaskNumber()).orElseThrow();
        task.setTodos(task.getTodos().stream().peek(t -> {
            if (t.getTodoId() == editTodoRequest.getTodoId()) {
                if (!Objects.equals(t.getTitle(), editTodoRequest.getTitle())) t.setTitle(editTodoRequest.getTitle());
                if (!Objects.equals(t.getText(), editTodoRequest.getText())) t.setText(editTodoRequest.getText());
            }
        }).toList());
        taskRepository.save(task);
    }


    public void deleteTodo(DeleteTodoRequest deleteTodoRequest) {
        Task task = taskRepository.findTaskByTaskNumber(deleteTodoRequest.getTaskNumber()).orElseThrow();
        task.setTodos(task.getTodos().stream().filter(t -> t.getTodoId() != deleteTodoRequest.getTodoId()).toList());
        taskRepository.save(task);
    }
}

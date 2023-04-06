package matvey.springtodolist.service;

import lombok.RequiredArgsConstructor;
import matvey.springtodolist.dto.AddCoUserResponse;
import matvey.springtodolist.dto.AddCommentRequest;
import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.Comment;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final AuthService authService;
    private final TaskRepository taskRepository;

    public void addTask(AddTaskRequest addTaskRequest) {
        Task task = Task.builder()
                .taskNumber(generateTaskNumber())
                .coUsers(new ArrayList<>())
                .ownerUser(authService.convertUserToResponse(authService.getCurrentUser()))
                .title(addTaskRequest.getTitle())
                .text(addTaskRequest.getText())
                .todos(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        taskRepository.save(task);
    }

    public List<Task> getAllTasksByUser(String userEmail) {
        return taskRepository.findAll().stream()
                .filter(t -> Objects.equals(t.getOwnerUser().getEmail(), userEmail) || t.getCoUsers().contains(authService.getUserResponseByEmail(userEmail)))
                .toList();

    }
    public Task getTaskByNumber(int taskNumber) {
        return taskRepository.findTaskByTaskNumber(taskNumber).orElseThrow();
    }


    public void addCoUser(AddCoUserResponse addCoUserResponse) {
        Task task = taskRepository.findTaskByTaskNumber(addCoUserResponse.getTaskNumber()).orElseThrow();
        task.getCoUsers().add(authService.getUserResponseByEmail(addCoUserResponse.getUserEmail()));
        taskRepository.save(task);
    }

    public void addComment(AddCommentRequest addCommentRequest) {
        Task task = taskRepository.findTaskByTaskNumber(addCommentRequest.getTaskNumber()).orElseThrow();
        task.getComments().add(Comment.builder()
                .author(authService.convertUserToResponse(authService.getCurrentUser()))
                .taskNumber(task.getTaskNumber())
                .text(addCommentRequest.getText())
                .build());
        taskRepository.save(task);
    }

    private int generateTaskNumber() {
        Random random = new Random();
        int res = random.nextInt(0, Integer.MAX_VALUE);
        if (taskRepository.findTaskByTaskNumber(res).isEmpty()) return res;
        else return generateTaskNumber();
    }
}

package matvey.springtodolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matvey.springtodolist.dto.task.AddTaskRequest;
import matvey.springtodolist.model.Comment;
import matvey.springtodolist.model.FileInfo;
import matvey.springtodolist.model.Task;
import matvey.springtodolist.repository.FileInfoRepository;
import matvey.springtodolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final FileInfoRepository fileInfoRepository;
    private final AuthService authService;

    @Value("${path_to_files}")
    private String path_to_files;

    public Task addTask(AddTaskRequest request) throws IOException {
        Task task = Task.builder()
                .title(request.getTitle())
                .text(request.getText())
                .responsibleUserId(request.getResponsibleUserId())
                .performerUsersId(new ArrayList<>())
                .ownerUserId(authService.getCurrentUserId())
                .comments(new ArrayList<>())
                .files(new ArrayList<>())
                .isCompleted(false)
                .build();
        taskRepository.save(task);
        addDefaultTaskDirectory(task.get_id());
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



    public Task addComment(String taskId, String text) {
        Task task = getTaskById(taskId);
        Comment comment = Comment.builder()
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
        fileInfoRepository.save(fileInfo);
        files.add(fileInfo);
        task.setFiles(files);
        updateTask(task);

        return fileInfo;
    }

    public void addDefaultTaskDirectory(String taskId) throws IOException {
        Files.createDirectory(Path.of(path_to_files+taskId));
    }

}

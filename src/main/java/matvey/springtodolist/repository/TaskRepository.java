package matvey.springtodolist.repository;

import matvey.springtodolist.dto.UserResponse;
import matvey.springtodolist.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findTasksByOwnerUser(UserResponse ownerUser);
    List<Task> findTasksByCoUsersContaining(UserResponse coUser);
    Optional<Task> findTaskByTaskNumber(int taskNumber);
}

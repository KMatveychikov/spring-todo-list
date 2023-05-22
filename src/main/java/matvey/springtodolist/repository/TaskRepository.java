package matvey.springtodolist.repository;

import matvey.springtodolist.dto.UserResponse;
import matvey.springtodolist.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findTasksByOwnerUser(UserResponse ownerUser);

}

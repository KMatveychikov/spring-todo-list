package matvey.springtodolist.repository;


import matvey.springtodolist.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
}

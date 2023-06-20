package matvey.springtodolist.repository;

import matvey.springtodolist.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepository extends MongoRepository<Board, String> {
}

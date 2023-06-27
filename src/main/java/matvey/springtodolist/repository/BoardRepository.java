package matvey.springtodolist.repository;

import matvey.springtodolist.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BoardRepository extends MongoRepository<Board, String> {
    //List<Board> getBoardsByUserId(String userId);
}

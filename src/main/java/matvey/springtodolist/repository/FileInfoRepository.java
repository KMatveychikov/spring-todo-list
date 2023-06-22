package matvey.springtodolist.repository;

import matvey.springtodolist.model.FileInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileInfoRepository extends MongoRepository<FileInfo, String> {
}

package matvey.springtodolist.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.repository.query.StringBasedMongoQuery;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AddTaskRequest {
    String title;
    String text;
    LocalDateTime deadline;
    String responsibleUserId;
    String boardId;
}

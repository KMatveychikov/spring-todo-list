package matvey.springtodolist.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddTaskRequest {

    String title;
    String text;
    String responsibleUserId;
}

package matvey.springtodolist.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import matvey.springtodolist.dto.UserResponse;

@Data
@AllArgsConstructor
public class AddTaskRequest {

    String title;
    String text;
    String resposibleUserId;
}

package matvey.springtodolist.dto.task;

import lombok.Data;
import matvey.springtodolist.dto.UserResponse;

@Data
public class GetTaskRequest {
    private UserResponse user;
}

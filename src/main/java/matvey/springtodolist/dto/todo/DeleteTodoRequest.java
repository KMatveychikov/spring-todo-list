package matvey.springtodolist.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteTodoRequest {
    private int taskNumber;
    private int todoId;

}

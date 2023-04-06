package matvey.springtodolist.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditTodoRequest {
    private int taskNumber;
    private int todoId;
    private String title;
    private String text;
}

package matvey.springtodolist.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddTodoRequest {
    private int taskNumber;
    private String title;
    private String text;

}

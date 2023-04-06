package matvey.springtodolist.dto;

import lombok.Data;

@Data
public class AddCommentRequest {
    private int taskNumber;
    private String text;
}

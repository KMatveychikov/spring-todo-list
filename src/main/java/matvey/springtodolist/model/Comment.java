package matvey.springtodolist.model;

import lombok.*;
import matvey.springtodolist.dto.UserResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private int taskNumber;
    private UserResponse author;
    private String text;
}

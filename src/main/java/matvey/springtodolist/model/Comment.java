package matvey.springtodolist.model;

import lombok.*;
import matvey.springtodolist.dto.UserResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String id;
    private String authorName;
    private String text;
}

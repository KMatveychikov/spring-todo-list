package matvey.springtodolist.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String id;
    private String authorName;
    private String text;
}

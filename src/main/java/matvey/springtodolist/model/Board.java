package matvey.springtodolist.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Board {
    private String _id;
    private String title;
    private List<String> tasksId;
}

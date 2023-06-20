package matvey.springtodolist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private String _id;
    private String title;
    private String userId;
    private List<String> tasksId;
}

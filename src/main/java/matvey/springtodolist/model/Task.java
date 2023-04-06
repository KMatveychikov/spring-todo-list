package matvey.springtodolist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import matvey.springtodolist.dto.UserResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
   private String _id;
   private int taskNumber;
   private String title;
   private String text;
   private List<Todo> todos;
   private UserResponse ownerUser;
   private List<UserResponse> coUsers;
   private boolean isCompleted;
   private List<Comment> comments;
}

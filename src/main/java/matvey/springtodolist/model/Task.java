package matvey.springtodolist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
   private String _id;
   private String title;
   private String text;
   private List<Todo> todos;
   private List<Comment> comments;
   private List<Path> files;
   private String ownerUserId;
   private String responsibleUserId;
   private List<String> performerUsersId;
   private boolean isCompleted;

}

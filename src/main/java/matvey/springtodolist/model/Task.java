package matvey.springtodolist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
   private String _id;
   private String title;
   private String text;
   private List<Comment> comments;
   private List<FileInfo> files;
   private String ownerUserId;
   private String responsibleUserId;
   private List<String> performerUsersId;
   private LocalDateTime deadline;
   private List<Tag> tags;
   private List<Todo> todos;
   private boolean isCompleted;
}

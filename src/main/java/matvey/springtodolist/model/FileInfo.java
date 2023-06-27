package matvey.springtodolist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private String id;
    private String fileName;
    private String filePath;
    private String contentType;
}

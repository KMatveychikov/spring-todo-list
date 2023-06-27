package matvey.springtodolist.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import matvey.springtodolist.model.User;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private User user;
}

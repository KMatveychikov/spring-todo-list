package matvey.springtodolist.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import matvey.springtodolist.dto.UserResponse;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserResponse user;
}

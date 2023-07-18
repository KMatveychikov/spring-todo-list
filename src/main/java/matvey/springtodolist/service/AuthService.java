package matvey.springtodolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matvey.springtodolist.dto.auth.AuthRequest;
import matvey.springtodolist.dto.auth.AuthResponse;
import matvey.springtodolist.dto.auth.RegisterRequest;
import matvey.springtodolist.model.Board;
import matvey.springtodolist.model.User;
import matvey.springtodolist.repository.BoardRepository;
import matvey.springtodolist.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BoardRepository boardRepository;



    public AuthResponse register(RegisterRequest request) {
        Board board = Board.builder()
                .title("Default")
                .tasksId(new ArrayList<>())
                .build();
        boardRepository.save(board);
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .boardsId(new ArrayList<>(List.of(board.get_id())))
                .build();
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }
    public User getUserById(String userId){
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("user {} not found", userId);
            throw new RuntimeException("user not found");
        });
    }

    public User addBoard(String userId, String boardId) {
        User user = getUserById(userId);
        List<String> boardsId = user.getBoardsId();
        boardsId.add(boardId);
        user.setBoardsId(boardsId);
        userRepository.save(user);
        return user;
    }

    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserEmail()).orElseThrow(() -> {
            log.error("user not found");
            throw new RuntimeException("user not found");
        });
    }

    public String getCurrentUserId() {
        return getCurrentUser().get_id();
    }
    public String getCurrentUserName(){
        return getCurrentUser().getUsername();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String getUsernameById(String id) throws RuntimeException {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User {} not found", id);
            return new RuntimeException("user not found");
        });
        return user.getName();
    }
    public boolean isPresentUser(String userId){
        if(userRepository.existsById(userId)) {
            return true;
        } else {
            return false;
        }
    }
}

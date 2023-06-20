package matvey.springtodolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matvey.springtodolist.dto.auth.AuthRequest;
import matvey.springtodolist.dto.auth.AuthResponse;
import matvey.springtodolist.dto.auth.RegisterRequest;
import matvey.springtodolist.dto.auth.UserResponse;
import matvey.springtodolist.model.User;
import matvey.springtodolist.repository.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .username(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .user(convertUserToResponse(user))
                .build();
    }

    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserEmail()).orElseThrow();
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(u -> userResponses.add(convertUserToResponse(u)));
        return userResponses;
    }

    public UserResponse convertUserToResponse(User user) {
        return UserResponse.builder()
                .id(user.get_id())
                .email(user.getEmail())
                .role(user.getRole())
                .name(user.getName()).build();
    }
    public UserResponse getUserResponseByEmail(String email) {
      return convertUserToResponse(userRepository.findByEmail(email).orElseThrow()) ;
    }

    public String getUsernameById(String id) throws ChangeSetPersister.NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User {} not found", id);
            return new ChangeSetPersister.NotFoundException();
        });
        return user.getName();
    }
}

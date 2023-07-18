package matvey.springtodolist.controller;

import lombok.RequiredArgsConstructor;
import matvey.springtodolist.dto.auth.AuthRequest;
import matvey.springtodolist.dto.auth.AuthResponse;
import matvey.springtodolist.dto.auth.RegisterRequest;

import matvey.springtodolist.model.User;
import matvey.springtodolist.service.AuthService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    )  {
        return ResponseEntity.ok()
                .body(service.authenticate(request));
    }

    @GetMapping("/get_all")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("get_name_by_id/{id}")
    public ResponseEntity<String> getUsernameById(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(service.getUsernameById(id));
    }

}

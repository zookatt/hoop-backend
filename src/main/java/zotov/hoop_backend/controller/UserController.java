package zotov.hoop_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import zotov.hoop_backend.dto.user.CreateUserDTORequest;
import zotov.hoop_backend.dto.user.UserDTOResponse;
import zotov.hoop_backend.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTOResponse> findAll() {
        return userService.findAllResponses();
    }

    @PostMapping
    public ResponseEntity<UserDTOResponse> create(@RequestBody CreateUserDTORequest request) {
        try {
            UserDTOResponse savedUser = userService.create(request);

            return ResponseEntity
                    .created(URI.create("/api/v1/users/" + savedUser.id()))
                    .body(savedUser);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
        }
    }
}

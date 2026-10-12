package zotov.hoop_backend.user.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import zotov.hoop_backend.user.dto.CreateUserDTORequest;
import zotov.hoop_backend.user.dto.UserDTOResponse;
import zotov.hoop_backend.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Test
    void findAllReturnsUsers() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);
        UserDTOResponse user = response();

        when(userService.findAllResponses()).thenReturn(List.of(user));

        assertEquals(List.of(user), controller.findAll());
    }

    @Test
    void findByIdMapsMissingUserToNotFound() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);

        when(userService.findResponseById(99)).thenThrow(new IllegalArgumentException("User not found"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.findById(99));

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void createReturnsCreatedUser() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);
        CreateUserDTORequest request = new CreateUserDTORequest("Admin", "admin@hoop.test", "Admin1234", 1);
        UserDTOResponse savedUser = response();

        when(userService.create(request)).thenReturn(savedUser);

        ResponseEntity<UserDTOResponse> response = controller.create(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(savedUser, response.getBody());
    }

    @Test
    void createMapsDuplicateEmailToConflict() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);
        CreateUserDTORequest request = new CreateUserDTORequest("Admin", "admin@hoop.test", "Admin1234", 1);

        when(userService.create(request)).thenThrow(new IllegalStateException("Email already exists"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.create(request));

        assertEquals(409, exception.getStatusCode().value());
    }

    private UserDTOResponse response() {
        return new UserDTOResponse(1, "Admin", "admin@hoop.test", true, 1, "ADMIN", false);
    }
}

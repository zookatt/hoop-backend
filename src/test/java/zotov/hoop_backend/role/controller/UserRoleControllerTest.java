package zotov.hoop_backend.role.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import zotov.hoop_backend.role.dto.CreateUserRoleDTORequest;
import zotov.hoop_backend.role.dto.UserRoleDTOResponse;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.role.service.UserRoleService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRoleControllerTest {

    @Test
    void findAllReturnsRoleResponses() {
        UserRoleService userRoleService = mock(UserRoleService.class);
        UserRoleController controller = new UserRoleController(userRoleService);
        UserRole admin = role(1, "ADMIN");

        when(userRoleService.findAll()).thenReturn(List.of(admin));

        List<UserRoleDTOResponse> response = controller.findAll();

        assertEquals(1, response.size());
        assertEquals("ADMIN", response.getFirst().name());
    }

    @Test
    void createReturnsCreatedRole() {
        UserRoleService userRoleService = mock(UserRoleService.class);
        UserRoleController controller = new UserRoleController(userRoleService);
        UserRole savedRole = role(2, "RECEPTION");

        when(userRoleService.save(org.mockito.ArgumentMatchers.any(UserRole.class))).thenReturn(savedRole);

        ResponseEntity<UserRoleDTOResponse> response = controller.create(new CreateUserRoleDTORequest("RECEPTION"));

        assertEquals(201, response.getStatusCode().value());
        assertEquals("RECEPTION", response.getBody().name());
    }

    private UserRole role(Integer id, String name) {
        UserRole role = new UserRole(name);
        ReflectionTestUtils.setField(role, "id", id);
        return role;
    }
}

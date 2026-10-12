package zotov.hoop_backend.role.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zotov.hoop_backend.role.dto.CreateUserRoleDTORequest;
import zotov.hoop_backend.role.dto.UserRoleDTOResponse;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.role.service.UserRoleService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public List<UserRoleDTOResponse> findAll() {
        return userRoleService.findAll()
                .stream()
                .map(UserRoleController::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<UserRoleDTOResponse> create(@RequestBody CreateUserRoleDTORequest request) {
        UserRole savedRole = userRoleService.save(new UserRole(request.name()));

        return ResponseEntity
                .created(URI.create("/api/v1/user-roles/" + savedRole.getId()))
                .body(toResponse(savedRole));
    }

    private static UserRoleDTOResponse toResponse(UserRole role) {
        return new UserRoleDTOResponse(
                role.getId(),
                role.getName());
    }
}

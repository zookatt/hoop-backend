package zotov.hoop_backend.dto.user;

public record CreateUserDTORequest(
        String name,
        String email,
        String password,
        Integer roleId) {
}

package zotov.hoop_backend.user.dto;

public record CreateUserDTORequest(
        String name,
        String email,
        String password,
        Integer roleId) {
}

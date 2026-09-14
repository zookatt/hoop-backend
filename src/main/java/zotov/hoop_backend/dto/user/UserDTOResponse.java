package zotov.hoop_backend.dto.user;

public record UserDTOResponse(
        Integer id,
        String name,
        String email,
        Boolean active,
        Integer roleId,
        String roleName) {
}

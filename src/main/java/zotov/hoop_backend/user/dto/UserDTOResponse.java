package zotov.hoop_backend.user.dto;

public record UserDTOResponse(
        Integer id,
        String name,
        String email,
        Boolean active,
        Integer roleId,
        String roleName) {
}

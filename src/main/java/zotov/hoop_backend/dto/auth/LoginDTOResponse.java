package zotov.hoop_backend.dto.auth;

public record LoginDTOResponse(
        Integer userId,
        String name,
        String email,
        String role) {
}

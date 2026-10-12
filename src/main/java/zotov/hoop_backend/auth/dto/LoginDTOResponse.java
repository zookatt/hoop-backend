package zotov.hoop_backend.auth.dto;

public record LoginDTOResponse(
        Integer userId,
        String name,
        String email,
        String role) {
}

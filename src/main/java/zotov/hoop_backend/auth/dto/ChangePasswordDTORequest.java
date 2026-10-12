package zotov.hoop_backend.auth.dto;

public record ChangePasswordDTORequest(
        String email,
        String currentPassword,
        String newPassword) {
}

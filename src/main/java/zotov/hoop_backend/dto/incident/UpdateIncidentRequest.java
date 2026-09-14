package zotov.hoop_backend.dto.incident;

public record UpdateIncidentRequest(
        String title,
        String description,
        String roomNumber) {
}

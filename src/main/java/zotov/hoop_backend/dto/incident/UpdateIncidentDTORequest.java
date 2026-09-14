package zotov.hoop_backend.dto.incident;

public record UpdateIncidentDTORequest(
        String title,
        String description,
        String roomNumber) {
}

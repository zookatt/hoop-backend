package zotov.hoop_backend.incident.dto;

public record UpdateIncidentDTORequest(
        String title,
        String description,
        String roomNumber) {
}

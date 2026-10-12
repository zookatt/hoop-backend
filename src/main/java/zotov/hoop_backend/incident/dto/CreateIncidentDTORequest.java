package zotov.hoop_backend.incident.dto;

public record CreateIncidentDTORequest(
        String title,
        String description,
        String roomNumber) {
}

package zotov.hoop_backend.dto.incident;

public record CreateIncidentDTORequest(
        String title,
        String description,
        String roomNumber) {
}

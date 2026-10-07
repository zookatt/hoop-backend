package zotov.hoop_backend.incident.dto;

import zotov.hoop_backend.incident.enums.IncidentStatus;

public record UpdateIncidentStatusDTORequest(
                IncidentStatus status) {
}

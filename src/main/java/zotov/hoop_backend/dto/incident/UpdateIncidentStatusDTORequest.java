package zotov.hoop_backend.dto.incident;

import zotov.hoop_backend.enums.IncidentStatus;

public record UpdateIncidentStatusDTORequest(
                IncidentStatus status) {
}

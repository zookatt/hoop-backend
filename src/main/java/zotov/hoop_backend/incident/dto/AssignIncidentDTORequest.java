package zotov.hoop_backend.incident.dto;

import zotov.hoop_backend.incident.enums.Department;
import zotov.hoop_backend.incident.enums.Priority;

public record AssignIncidentDTORequest(
                Department department,
                Priority priority,
                Integer assignedToUserId) {
}

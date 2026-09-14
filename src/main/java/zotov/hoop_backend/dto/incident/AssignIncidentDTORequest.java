package zotov.hoop_backend.dto.incident;

import zotov.hoop_backend.enums.Department;
import zotov.hoop_backend.enums.Priority;

public record AssignIncidentDTORequest(
                Department department,
                Priority priority,
                Integer assignedToUserId) {
}

package zotov.hoop_backend.incident.dto;

import zotov.hoop_backend.incident.enums.Department;
import zotov.hoop_backend.incident.enums.IncidentStatus;
import zotov.hoop_backend.incident.enums.Priority;

import java.time.LocalDateTime;

public record IncidentDTOResponse(
                Integer id,
                String title,
                String description,
                String roomNumber,
                Department department,
                Priority priority,
                IncidentStatus status,
                Integer createdByUserId,
                String createdByUserName,
                String createdByUserRoleName,
                Integer assignedToUserId,
                String assignedToUserName,
                String assignedToUserRoleName,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}

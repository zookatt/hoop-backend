package zotov.hoop_backend.incident.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import zotov.hoop_backend.incident.dto.AssignIncidentDTORequest;
import zotov.hoop_backend.incident.dto.CreateIncidentDTORequest;
import zotov.hoop_backend.incident.dto.IncidentDTOResponse;
import zotov.hoop_backend.incident.dto.UpdateIncidentDTORequest;
import zotov.hoop_backend.incident.dto.UpdateIncidentStatusDTORequest;
import zotov.hoop_backend.incident.entity.Incident;
import zotov.hoop_backend.incident.enums.IncidentStatus;
import zotov.hoop_backend.user.entity.User;
import zotov.hoop_backend.incident.service.IncidentService;
import zotov.hoop_backend.user.service.UserService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final UserService userService;

    public IncidentController(IncidentService incidentService, UserService userService) {
        this.incidentService = incidentService;
        this.userService = userService;
    }

    @GetMapping
    public List<IncidentDTOResponse> findAll() {
        return incidentService.findAll()
                .stream()
                .map(IncidentController::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public IncidentDTOResponse findById(@PathVariable Integer id) {
        Incident incident = findIncidentOrFail(id);
        return toResponse(incident);
    }

    @PostMapping
    public ResponseEntity<IncidentDTOResponse> create(
            Authentication authentication,
            @RequestBody CreateIncidentDTORequest request) {
        User createdBy = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with email " + authentication.getName()));

        Incident incident = new Incident(
                request.title(),
                request.description(),
                request.roomNumber(),
                createdBy);

        Incident savedIncident = incidentService.save(incident);

        return ResponseEntity
                .created(URI.create("/api/v1/incidents/" + savedIncident.getId()))
                .body(toResponse(savedIncident));
    }

    @PutMapping("/{id}")
    public IncidentDTOResponse update(
            @PathVariable Integer id,
            @RequestBody UpdateIncidentDTORequest request, Authentication authentication) {
        Incident incident = findIncidentOrFail(id);

        if (request.title() != null) {
            incident.setTitle(request.title());
        }
        if (request.description() != null) {
            incident.setDescription(request.description());
        }
        if (request.roomNumber() != null) {
            incident.setRoomNumber(request.roomNumber());
        }
        incident.setUpdatedAt(LocalDateTime.now());

        return toResponse(incidentService.save(incident));
    }

    @PutMapping("/{id}/assignment")
    public IncidentDTOResponse assign(
            @PathVariable Integer id,
            @RequestBody AssignIncidentDTORequest request) {
        Incident incident = findIncidentOrFail(id);

        try {
            if (request.department() != null) {
                incident = incidentService.setDepartment(incident, request.department());
            }
            if (request.priority() != null) {
                incident = incidentService.setPriority(incident, request.priority());
            }
            if (request.assignedToUserId() != null) {
                User assignedTo = findUserOrFail(request.assignedToUserId());
                incident = incidentService.assignTo(incident, assignedTo);
            }
            return toResponse(incident);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
        }
    }

    @PutMapping("/{id}/status")
    public IncidentDTOResponse updateStatus(
            @PathVariable Integer id,
            @RequestBody UpdateIncidentStatusDTORequest request,
            Authentication authentication) {
        Incident incident = findIncidentOrFail(id);

        if (request.status() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The incident status is required");
        }

        boolean canCloseIncident = authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("SCOPE_ADMIN")
                        || authority.getAuthority().equals("SCOPE_RECEPTION"));

        if (request.status() == IncidentStatus.CLOSED && !canCloseIncident) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only ADMIN or RECEPTION can close incidents");
        }

        try {
            Incident updatedIncident = switch (request.status()) {
                case OPEN -> incident;
                case IN_PROGRESS -> incidentService.startWork(incident);
                case RESOLVED -> incidentService.resolve(incident);
                case CLOSED -> closeIncident(incident);
            };

            return toResponse(updatedIncident);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
        }
    }

    private Incident closeIncident(Incident incident) {
        if (incident.getAssignedTo() == null) {
            return incidentService.closeWithoutAssignment(incident);
        }
        return incidentService.close(incident);
    }

    private Incident findIncidentOrFail(Integer id) {
        return incidentService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Incident not found with id " + id));
    }

    private User findUserOrFail(Integer id) {
        return userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id " + id));
    }

    private static IncidentDTOResponse toResponse(Incident incident) {
        return new IncidentDTOResponse(
                incident.getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getRoomNumber(),
                incident.getDepartment(),
                incident.getPriority(),
                incident.getStatus(),
                getUserId(incident.getCreatedBy()),
                getUserId(incident.getAssignedTo()),
                incident.getCreatedAt(),
                incident.getUpdatedAt());
    }

    private static Integer getUserId(User user) {
        if (user == null) {
            return null;
        }
        return user.getId();
    }
}

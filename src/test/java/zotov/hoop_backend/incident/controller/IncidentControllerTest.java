package zotov.hoop_backend.incident.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import zotov.hoop_backend.characteristics.entity.UserCharacteristics;
import zotov.hoop_backend.characteristics.service.UserCharacteristicsService;
import zotov.hoop_backend.incident.dto.AssignIncidentDTORequest;
import zotov.hoop_backend.incident.dto.CreateIncidentDTORequest;
import zotov.hoop_backend.incident.dto.IncidentDTOResponse;
import zotov.hoop_backend.incident.dto.UpdateIncidentDTORequest;
import zotov.hoop_backend.incident.dto.UpdateIncidentStatusDTORequest;
import zotov.hoop_backend.incident.entity.Incident;
import zotov.hoop_backend.incident.enums.Department;
import zotov.hoop_backend.incident.enums.IncidentStatus;
import zotov.hoop_backend.incident.enums.Priority;
import zotov.hoop_backend.incident.service.IncidentService;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.user.entity.User;
import zotov.hoop_backend.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentControllerTest {

    @Mock
    private IncidentService incidentService;

    @Mock
    private UserService userService;

    @Mock
    private UserCharacteristicsService userCharacteristicsService;

    @Test
    void findAllReturnsAllIncidentsForAdmin() {
        IncidentController controller = controller();
        Incident maintenance = incident(1, Department.MAINTENANCE, IncidentStatus.OPEN);
        Incident cleaning = incident(2, Department.CLEANING, IncidentStatus.OPEN);

        when(incidentService.findAll()).thenReturn(List.of(maintenance, cleaning));
        when(userCharacteristicsService.findByUserId(1)).thenReturn(Optional.of(new UserCharacteristics("Recepcion", maintenance.getCreatedBy())));

        List<IncidentDTOResponse> response = controller.findAll(auth("SCOPE_ADMIN"));

        assertEquals(2, response.size());
    }

    @Test
    void findAllFiltersIncidentsForMaintenance() {
        IncidentController controller = controller();
        Incident maintenance = incident(1, Department.MAINTENANCE, IncidentStatus.OPEN);
        Incident cleaning = incident(2, Department.CLEANING, IncidentStatus.OPEN);

        when(incidentService.findAll()).thenReturn(List.of(maintenance, cleaning));
        when(userCharacteristicsService.findByUserId(1)).thenReturn(Optional.of(new UserCharacteristics("Recepcion", maintenance.getCreatedBy())));

        List<IncidentDTOResponse> response = controller.findAll(auth("SCOPE_MAINTENANCE"));

        assertEquals(1, response.size());
        assertEquals(Department.MAINTENANCE, response.getFirst().department());
    }

    @Test
    void findByIdRejectsDepartmentWithoutPermission() {
        IncidentController controller = controller();
        Incident incident = incident(1, Department.MAINTENANCE, IncidentStatus.OPEN);

        when(incidentService.findById(1)).thenReturn(Optional.of(incident));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.findById(1, auth("SCOPE_CLEANING")));

        assertEquals(403, exception.getStatusCode().value());
    }

    @Test
    void createUsesLoggedUserAsCreator() {
        IncidentController controller = controller();
        User creator = user(1, "RECEPTION");
        Incident savedIncident = new Incident("Fuga", "Sale agua", "203", creator);
        ReflectionTestUtils.setField(savedIncident, "id", 10);

        when(userService.findByEmail("reception@hoop.test")).thenReturn(Optional.of(creator));
        when(incidentService.save(any(Incident.class))).thenReturn(savedIncident);
        when(userCharacteristicsService.findByUserId(1)).thenReturn(Optional.of(new UserCharacteristics("Recepcion", creator)));

        ResponseEntity<IncidentDTOResponse> response = controller.create(
                auth("reception@hoop.test", "SCOPE_RECEPTION"),
                new CreateIncidentDTORequest("Fuga", "Sale agua", "203"));

        assertEquals(201, response.getStatusCode().value());
        assertEquals(10, response.getBody().id());
        assertEquals(1, response.getBody().createdByUserId());
    }

    @Test
    void updateRejectsMaintenanceRole() {
        IncidentController controller = controller();
        Incident incident = incident(1, Department.MAINTENANCE, IncidentStatus.OPEN);

        when(incidentService.findById(1)).thenReturn(Optional.of(incident));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.update(
                        1,
                        new UpdateIncidentDTORequest("Nuevo", "Nueva descripcion", "204"),
                        auth("SCOPE_MAINTENANCE")));

        assertEquals(403, exception.getStatusCode().value());
    }

    @Test
    void updateSavesBasicIncidentDataForAdmin() {
        IncidentController controller = controller();
        Incident incident = incident(1, Department.MAINTENANCE, IncidentStatus.OPEN);

        when(incidentService.findById(1)).thenReturn(Optional.of(incident));
        when(incidentService.save(incident)).thenReturn(incident);
        when(userCharacteristicsService.findByUserId(1)).thenReturn(Optional.of(new UserCharacteristics("Recepcion", incident.getCreatedBy())));

        IncidentDTOResponse response = controller.update(
                1,
                new UpdateIncidentDTORequest("Nuevo titulo", "Nueva descripcion", "204"),
                auth("SCOPE_ADMIN"));

        assertEquals("Nuevo titulo", response.title());
        assertEquals("Nueva descripcion", response.description());
        assertEquals("204", response.roomNumber());
        verify(incidentService).save(incident);
    }

    @Test
    void assignSetsDepartmentPriorityAndWorker() {
        IncidentController controller = controller();
        Incident incident = incident(1, null, IncidentStatus.OPEN);
        User worker = user(5, "MAINTENANCE");

        when(incidentService.findById(1)).thenReturn(Optional.of(incident));
        when(incidentService.setDepartment(incident, Department.MAINTENANCE)).thenAnswer(invocation -> {
            incident.setDepartment(Department.MAINTENANCE);
            return incident;
        });
        when(incidentService.setPriority(incident, Priority.HIGH)).thenAnswer(invocation -> {
            incident.setPriority(Priority.HIGH);
            return incident;
        });
        when(userService.findById(5)).thenReturn(Optional.of(worker));
        when(incidentService.assignTo(incident, worker)).thenAnswer(invocation -> {
            incident.setAssignedTo(worker);
            return incident;
        });
        when(userCharacteristicsService.findByUserId(1)).thenReturn(Optional.of(new UserCharacteristics("Recepcion", incident.getCreatedBy())));
        when(userCharacteristicsService.findByUserId(5)).thenReturn(Optional.of(new UserCharacteristics("Tecnico", worker)));

        IncidentDTOResponse response = controller.assign(
                1,
                new AssignIncidentDTORequest(Department.MAINTENANCE, Priority.HIGH, 5),
                auth("SCOPE_RECEPTION"));

        assertEquals(Department.MAINTENANCE, response.department());
        assertEquals(Priority.HIGH, response.priority());
        assertEquals(5, response.assignedToUserId());
    }

    @Test
    void updateStatusRejectsCleaningClosingIncident() {
        IncidentController controller = controller();
        Incident incident = incident(1, Department.CLEANING, IncidentStatus.RESOLVED);

        when(incidentService.findById(1)).thenReturn(Optional.of(incident));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.updateStatus(
                        1,
                        new UpdateIncidentStatusDTORequest(IncidentStatus.CLOSED),
                        auth("SCOPE_CLEANING")));

        assertEquals(403, exception.getStatusCode().value());
    }

    @Test
    void updateStatusAllowsDepartmentWorkerToResolveOwnIncident() {
        IncidentController controller = controller();
        Incident incident = incident(1, Department.CLEANING, IncidentStatus.IN_PROGRESS);

        when(incidentService.findById(1)).thenReturn(Optional.of(incident));
        when(incidentService.resolve(incident)).thenAnswer(invocation -> {
            incident.setStatus(IncidentStatus.RESOLVED);
            return incident;
        });
        when(userCharacteristicsService.findByUserId(1)).thenReturn(Optional.of(new UserCharacteristics("Recepcion", incident.getCreatedBy())));

        IncidentDTOResponse response = controller.updateStatus(
                1,
                new UpdateIncidentStatusDTORequest(IncidentStatus.RESOLVED),
                auth("SCOPE_CLEANING"));

        assertEquals(IncidentStatus.RESOLVED, response.status());
    }

    @Test
    void updateStatusRejectsClosedIncident() {
        IncidentController controller = controller();
        Incident incident = incident(1, Department.CLEANING, IncidentStatus.CLOSED);

        when(incidentService.findById(1)).thenReturn(Optional.of(incident));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.updateStatus(
                        1,
                        new UpdateIncidentStatusDTORequest(IncidentStatus.RESOLVED),
                        auth("SCOPE_ADMIN")));

        assertEquals(409, exception.getStatusCode().value());
    }

    private IncidentController controller() {
        return new IncidentController(incidentService, userService, userCharacteristicsService);
    }

    private Authentication auth(String authority) {
        return auth("user@hoop.test", authority);
    }

    private Authentication auth(String email, String authority) {
        return new UsernamePasswordAuthenticationToken(
                email,
                "password",
                List.of(new SimpleGrantedAuthority(authority)));
    }

    private Incident incident(Integer id, Department department, IncidentStatus status) {
        User creator = user(1, "RECEPTION");
        Incident incident = new Incident("Titulo", "Descripcion", "101", creator);
        ReflectionTestUtils.setField(incident, "id", id);
        incident.setDepartment(department);
        incident.setStatus(status);
        return incident;
    }

    private User user(Integer id, String roleName) {
        User user = new User(new UserRole(roleName));
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}

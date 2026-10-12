package zotov.hoop_backend.incident.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zotov.hoop_backend.incident.entity.Incident;
import zotov.hoop_backend.incident.enums.Department;
import zotov.hoop_backend.incident.enums.IncidentStatus;
import zotov.hoop_backend.incident.enums.Priority;
import zotov.hoop_backend.incident.repository.IncidentRepository;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.user.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentServiceImplTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Test
    void startWorkMovesOpenAssignedIncidentToInProgress() {
        IncidentServiceImpl service = new IncidentServiceImpl(incidentRepository);
        Incident incident = openIncident();
        incident.setDepartment(Department.MAINTENANCE);
        incident.setAssignedTo(new User(new UserRole("MAINTENANCE")));

        when(incidentRepository.save(incident)).thenReturn(incident);

        Incident result = service.startWork(incident);

        assertEquals(IncidentStatus.IN_PROGRESS, result.getStatus());
        assertNotNull(result.getUpdatedAt());
        verify(incidentRepository).save(incident);
    }

    @Test
    void startWorkRejectsUnassignedIncident() {
        IncidentServiceImpl service = new IncidentServiceImpl(incidentRepository);
        Incident incident = openIncident();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.startWork(incident));

        assertEquals("The incident must be assigned before starting work", exception.getMessage());
    }

    @Test
    void resolveOnlyAllowsInProgressIncident() {
        IncidentServiceImpl service = new IncidentServiceImpl(incidentRepository);
        Incident incident = openIncident();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.resolve(incident));

        assertEquals("Only IN_PROGRESS incidents can be resolved", exception.getMessage());
    }

    @Test
    void resolveMovesInProgressIncidentToResolved() {
        IncidentServiceImpl service = new IncidentServiceImpl(incidentRepository);
        Incident incident = inProgressIncident();

        when(incidentRepository.save(incident)).thenReturn(incident);

        Incident result = service.resolve(incident);

        assertEquals(IncidentStatus.RESOLVED, result.getStatus());
        verify(incidentRepository).save(incident);
    }

    @Test
    void closeOnlyAllowsResolvedIncident() {
        IncidentServiceImpl service = new IncidentServiceImpl(incidentRepository);
        Incident incident = inProgressIncident();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.close(incident));

        assertEquals("Only RESOLVED incidents can be closed", exception.getMessage());
    }

    @Test
    void closeMovesResolvedIncidentToClosed() {
        IncidentServiceImpl service = new IncidentServiceImpl(incidentRepository);
        Incident incident = inProgressIncident();
        incident.setStatus(IncidentStatus.RESOLVED);

        when(incidentRepository.save(incident)).thenReturn(incident);

        Incident result = service.close(incident);

        assertEquals(IncidentStatus.CLOSED, result.getStatus());
        verify(incidentRepository).save(incident);
    }

    @Test
    void assignToRequiresDepartment() {
        IncidentServiceImpl service = new IncidentServiceImpl(incidentRepository);
        Incident incident = openIncident();
        User worker = new User(new UserRole("MAINTENANCE"));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.assignTo(incident, worker));

        assertEquals("The incident must have a department before assignment", exception.getMessage());
    }

    @Test
    void closedIncidentCannotBeAssignedOrEdited() {
        IncidentServiceImpl service = new IncidentServiceImpl(incidentRepository);
        Incident incident = openIncident();
        incident.setStatus(IncidentStatus.CLOSED);

        assertThrows(IllegalStateException.class, () -> service.setPriority(incident, Priority.HIGH));
        assertThrows(IllegalStateException.class, () -> service.setDepartment(incident, Department.CLEANING));
        assertThrows(IllegalStateException.class, () -> service.assignTo(incident, new User(new UserRole("CLEANING"))));
    }

    @Test
    void closeWithoutAssignmentClosesOpenUnassignedIncident() {
        IncidentServiceImpl service = new IncidentServiceImpl(incidentRepository);
        Incident incident = openIncident();

        when(incidentRepository.save(incident)).thenReturn(incident);

        Incident result = service.closeWithoutAssignment(incident);

        assertEquals(IncidentStatus.CLOSED, result.getStatus());
        verify(incidentRepository).save(incident);
    }

    private Incident openIncident() {
        return new Incident(
                "Averia",
                "Descripcion",
                "101",
                new User(new UserRole("RECEPTION")));
    }

    private Incident inProgressIncident() {
        Incident incident = openIncident();
        incident.setDepartment(Department.MAINTENANCE);
        incident.setAssignedTo(new User(new UserRole("MAINTENANCE")));
        incident.setStatus(IncidentStatus.IN_PROGRESS);
        return incident;
    }
}

package zotov.hoop_backend.service;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.entity.Incident;
import zotov.hoop_backend.entity.User;
import zotov.hoop_backend.enums.Department;
import zotov.hoop_backend.enums.IncidentStatus;
import zotov.hoop_backend.enums.Priority;
import zotov.hoop_backend.repository.IncidentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    public List<Incident> findAll() {
        return incidentRepository.findAll();
    }

    @Override
    public Optional<Incident> findById(Integer id) {
        return incidentRepository.findById(id);
    }

    @Override
    public Incident save(Incident incident) {
        return incidentRepository.save(incident);
    }

    @Override
    public Incident setPriority(Incident incident, Priority priority) {
        incident.setPriority(priority);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    @Override
    public Incident setDepartment(Incident incident, Department department) {
        incident.setDepartment(department);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    @Override
    public Incident assignTo(Incident incident, User user) {
        if (incident.getDepartment() == null) {
            throw new IllegalStateException("The incident must have a department before assignment");
        }
        incident.setAssignedTo(user);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    @Override
    public Incident startWork(Incident incident) {
        if (incident.getAssignedTo() == null) {
            throw new IllegalStateException("The incident must be assigned before starting work");
        }
        if (incident.getStatus() != IncidentStatus.OPEN) {
            throw new IllegalStateException("Only OPEN incidents can be started");
        }
        incident.setStatus(IncidentStatus.IN_PROGRESS);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    @Override
    public Incident resolve(Incident incident) {
        if (incident.getStatus() != IncidentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only IN_PROGRESS incidents can be resolved");
        }
        incident.setStatus(IncidentStatus.RESOLVED);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    @Override
    public Incident close(Incident incident) {
        if (incident.getStatus() != IncidentStatus.RESOLVED) {
            throw new IllegalStateException("Only RESOLVED incidents can be closed");
        }
        incident.setStatus(IncidentStatus.CLOSED);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    @Override
    public Incident closeWithoutAssignment(Incident incident) {
        if (incident.getAssignedTo() != null) {
            throw new IllegalStateException("Only unassigned incidents can be closed with this action");
        }

        if (incident.getStatus() != IncidentStatus.OPEN) {
            throw new IllegalStateException("Only OPEN incidents can be closed without assignment");
        }

        incident.setStatus(IncidentStatus.CLOSED);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }
}

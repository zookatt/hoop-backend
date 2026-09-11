package zotov.hoop_backend.service;

import zotov.hoop_backend.enums.Department;
import zotov.hoop_backend.enums.Priority;
import zotov.hoop_backend.entity.User;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.entity.Incident;
import zotov.hoop_backend.repository.IncidentRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public List<Incident> findAll() {
        return incidentRepository.findAll();
    }

    public Optional<Incident> findById(Integer id) {
        return incidentRepository.findById(id);
    }

    public Incident save(Incident incident) {
        return incidentRepository.save(incident);
    }

    public Incident setPriority(Incident incident, Priority priority) {
        incident.setPriority(priority);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    public Incident setDepartment(Incident incident, Department department) {
        incident.setDepartment(department);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    public Incident assignTo(Incident incident, User user) {
        if (incident.getDepartment() == null) {
            throw new IllegalStateException(
                    "The incident must have a department before assignment");
        }
        incident.setAssignedTo(user);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }
}
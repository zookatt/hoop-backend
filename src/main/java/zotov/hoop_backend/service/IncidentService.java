package zotov.hoop_backend.service;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.entity.Incident;
import zotov.hoop_backend.repository.IncidentRepository;

import java.util.List;
import java.util.Optional;

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
}
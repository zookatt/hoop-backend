package zotov.hoop_backend.incident.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zotov.hoop_backend.incident.entity.Incident;

public interface IncidentRepository extends JpaRepository<Incident, Integer> {

}

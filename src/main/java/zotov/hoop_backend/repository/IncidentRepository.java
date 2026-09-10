package zotov.hoop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zotov.hoop_backend.entity.Incident;

public interface IncidentRepository extends JpaRepository<Incident, Integer> {

}

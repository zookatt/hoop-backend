package zotov.hoop_backend.service;

import zotov.hoop_backend.entity.Incident;
import zotov.hoop_backend.entity.User;
import zotov.hoop_backend.enums.Department;
import zotov.hoop_backend.enums.Priority;

import java.util.List;
import java.util.Optional;

public interface IncidentService {

    List<Incident> findAll();

    Optional<Incident> findById(Integer id);

    Incident save(Incident incident);

    Incident setPriority(Incident incident, Priority priority);

    Incident setDepartment(Incident incident, Department department);

    Incident assignTo(Incident incident, User user);

    Incident startWork(Incident incident);

    Incident resolve(Incident incident);

    Incident close(Incident incident);

    Incident closeWithoutAssignment(Incident incident);
}

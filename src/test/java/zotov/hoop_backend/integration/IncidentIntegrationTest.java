package zotov.hoop_backend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zotov.hoop_backend.characteristics.entity.UserCharacteristics;
import zotov.hoop_backend.characteristics.repository.UserCharacteristicsRepository;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;
import zotov.hoop_backend.incident.entity.Incident;
import zotov.hoop_backend.incident.enums.Department;
import zotov.hoop_backend.incident.enums.IncidentStatus;
import zotov.hoop_backend.incident.repository.IncidentRepository;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.role.repository.UserRoleRepository;
import zotov.hoop_backend.user.entity.User;
import zotov.hoop_backend.user.repository.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class IncidentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private UserCharacteristicsRepository userCharacteristicsRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Test
    void createIncidentUsesAuthenticatedUserAsCreator() throws Exception {
        createUser("reception-it@hoop.test", "Recepcion IT", "RECEPTION");

        mockMvc.perform(post("/api/v1/incidents")
                .with(user("reception-it@hoop.test")
                        .authorities(new SimpleGrantedAuthority("SCOPE_RECEPTION")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "Aire acondicionado roto",
                          "description": "No enfria correctamente",
                          "roomNumber": "204"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Aire acondicionado roto"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.createdByUserName").value("Recepcion IT"))
                .andExpect(jsonPath("$.createdByUserRoleName").value("RECEPTION"));
    }

    @Test
    void findAllFiltersIncidentsByDepartmentForCleaningRole() throws Exception {
        User reception = createUser("reception-list@hoop.test", "Recepcion", "RECEPTION");
        createIncident(reception, Department.CLEANING, IncidentStatus.OPEN, null);
        createIncident(reception, Department.MAINTENANCE, IncidentStatus.OPEN, null);

        mockMvc.perform(get("/api/v1/incidents")
                .with(user("cleaning-list@hoop.test")
                        .authorities(new SimpleGrantedAuthority("SCOPE_CLEANING"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].department").value("CLEANING"));
    }

    @Test
    void workerCannotCloseIncident() throws Exception {
        User reception = createUser("reception-close-denied@hoop.test", "Recepcion", "RECEPTION");
        User cleaner = createUser("cleaner-close-denied@hoop.test", "Cleaner", "CLEANING");
        Incident incident = createIncident(reception, Department.CLEANING, IncidentStatus.RESOLVED, cleaner);

        mockMvc.perform(put("/api/v1/incidents/{id}/status", incident.getId())
                .with(user("cleaner-close-denied@hoop.test")
                        .authorities(new SimpleGrantedAuthority("SCOPE_CLEANING")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "status": "CLOSED"
                        }
                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanCloseResolvedAssignedIncident() throws Exception {
        User reception = createUser("reception-close@hoop.test", "Recepcion", "RECEPTION");
        User maintenance = createUser("maintenance-close@hoop.test", "Tecnico", "MAINTENANCE");
        Incident incident = createIncident(reception, Department.MAINTENANCE, IncidentStatus.RESOLVED, maintenance);

        mockMvc.perform(put("/api/v1/incidents/{id}/status", incident.getId())
                .with(user("admin-close@hoop.test")
                        .authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "status": "CLOSED"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void closedIncidentCannotBeEdited() throws Exception {
        User reception = createUser("reception-closed@hoop.test", "Recepcion", "RECEPTION");
        Incident incident = createIncident(reception, Department.CLEANING, IncidentStatus.CLOSED, null);

        mockMvc.perform(put("/api/v1/incidents/{id}", incident.getId())
                .with(user("admin-edit-closed@hoop.test")
                        .authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "Nuevo titulo"
                        }
                        """))
                .andExpect(status().isConflict());
    }

    private User createUser(String email, String name, String roleName) {
        UserRole role = userRoleRepository.save(new UserRole(roleName));
        User user = userRepository.save(new User(role));
        userCharacteristicsRepository.save(new UserCharacteristics(name, user));

        UserCredentials credentials = new UserCredentials(
                email,
                "encoded-password",
                user);
        credentials.setMustChangePassword(false);
        userCredentialsRepository.save(credentials);

        return user;
    }

    private Incident createIncident(
            User creator,
            Department department,
            IncidentStatus status,
            User assignedTo) {
        Incident incident = new Incident("Titulo", "Descripcion", "101", creator);
        incident.setDepartment(department);
        incident.setStatus(status);
        incident.setAssignedTo(assignedTo);
        return incidentRepository.save(incident);
    }
}

package zotov.hoop_backend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zotov.hoop_backend.characteristics.entity.UserCharacteristics;
import zotov.hoop_backend.characteristics.repository.UserCharacteristicsRepository;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.role.repository.UserRoleRepository;
import zotov.hoop_backend.user.entity.User;
import zotov.hoop_backend.user.repository.UserRepository;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthIntegrationTest {

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
    private PasswordEncoder passwordEncoder;

    @Test
    void tokenReturnsConflictWhenUserMustChangePassword() throws Exception {
        createUser("must-change@hoop.test", "Temporal1234", "CLEANING", true);

        mockMvc.perform(post("/api/v1/auth/token")
                .with(httpBasic("must-change@hoop.test", "Temporal1234")))
                .andExpect(status().isConflict());
    }

    @Test
    void changePasswordAllowsUserToRequestTokenAfterwards() throws Exception {
        createUser("worker-auth@hoop.test", "Temporal1234", "MAINTENANCE", true);

        mockMvc.perform(post("/api/v1/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "email": "worker-auth@hoop.test",
                          "currentPassword": "Temporal1234",
                          "newPassword": "Worker1234"
                        }
                        """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/token")
                .with(httpBasic("worker-auth@hoop.test", "Worker1234")))
                .andExpect(status().isOk())
                .andExpect(content().string(not(isEmptyOrNullString())));
    }

    private User createUser(
            String email,
            String password,
            String roleName,
            boolean mustChangePassword) {
        UserRole role = userRoleRepository.save(new UserRole(roleName));
        User user = userRepository.save(new User(role));
        userCharacteristicsRepository.save(new UserCharacteristics("Test User", user));

        UserCredentials credentials = new UserCredentials(
                email,
                passwordEncoder.encode(password),
                user);
        credentials.setMustChangePassword(mustChangePassword);
        userCredentialsRepository.save(credentials);

        return user;
    }
}

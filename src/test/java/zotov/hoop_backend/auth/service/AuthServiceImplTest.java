package zotov.hoop_backend.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import zotov.hoop_backend.auth.dto.ChangePasswordDTORequest;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.user.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void changePasswordUpdatesPasswordAndDisablesMustChangePassword() {
        AuthServiceImpl service = new AuthServiceImpl(userCredentialsRepository, passwordEncoder);
        UserCredentials credentials = credentials();
        ChangePasswordDTORequest request = new ChangePasswordDTORequest(
                "worker@hoop.test",
                "Temporal1234",
                "Worker1234");

        when(userCredentialsRepository.findByEmail("worker@hoop.test")).thenReturn(Optional.of(credentials));
        when(passwordEncoder.matches("Temporal1234", "old-encoded")).thenReturn(true);
        when(passwordEncoder.encode("Worker1234")).thenReturn("new-encoded");

        service.changePassword(request);

        assertEquals("new-encoded", credentials.getPassword());
        assertFalse(credentials.getMustChangePassword());
        verify(userCredentialsRepository).save(credentials);
    }

    @Test
    void changePasswordRejectsWrongCurrentPassword() {
        AuthServiceImpl service = new AuthServiceImpl(userCredentialsRepository, passwordEncoder);
        UserCredentials credentials = credentials();

        when(userCredentialsRepository.findByEmail("worker@hoop.test")).thenReturn(Optional.of(credentials));
        when(passwordEncoder.matches("bad-password", "old-encoded")).thenReturn(false);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.changePassword(new ChangePasswordDTORequest(
                        "worker@hoop.test",
                        "bad-password",
                        "Worker1234")));

        assertEquals("Current password is incorrect", exception.getMessage());
    }

    @Test
    void changePasswordRejectsUnknownEmail() {
        AuthServiceImpl service = new AuthServiceImpl(userCredentialsRepository, passwordEncoder);

        when(userCredentialsRepository.findByEmail("missing@hoop.test")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.changePassword(new ChangePasswordDTORequest(
                        "missing@hoop.test",
                        "Temporal1234",
                        "Worker1234")));

        assertEquals("User not found with email missing@hoop.test", exception.getMessage());
    }

    private UserCredentials credentials() {
        return new UserCredentials(
                "worker@hoop.test",
                "old-encoded",
                new User(new UserRole("CLEANING")));
    }
}

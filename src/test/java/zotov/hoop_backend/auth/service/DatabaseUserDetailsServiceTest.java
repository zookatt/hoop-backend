package zotov.hoop_backend.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.user.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTest {

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Test
    void loadUserByUsernameBuildsUserDetailsFromCredentials() {
        DatabaseUserDetailsService service = new DatabaseUserDetailsService(userCredentialsRepository);
        User user = new User(new UserRole("ADMIN"));
        UserCredentials credentials = new UserCredentials("admin@hoop.test", "encoded", user);

        when(userCredentialsRepository.findByEmail("admin@hoop.test")).thenReturn(Optional.of(credentials));

        UserDetails result = service.loadUserByUsername("admin@hoop.test");

        assertEquals("admin@hoop.test", result.getUsername());
        assertEquals("encoded", result.getPassword());
        assertTrue(result.isEnabled());
        assertTrue(result.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN")));
    }

    @Test
    void loadUserByUsernameDisablesInactiveUsers() {
        DatabaseUserDetailsService service = new DatabaseUserDetailsService(userCredentialsRepository);
        User user = new User(new UserRole("CLEANING"));
        user.setActive(false);
        UserCredentials credentials = new UserCredentials("cleaning@hoop.test", "encoded", user);

        when(userCredentialsRepository.findByEmail("cleaning@hoop.test")).thenReturn(Optional.of(credentials));

        UserDetails result = service.loadUserByUsername("cleaning@hoop.test");

        assertFalse(result.isEnabled());
    }

    @Test
    void loadUserByUsernameRejectsMissingEmail() {
        DatabaseUserDetailsService service = new DatabaseUserDetailsService(userCredentialsRepository);

        when(userCredentialsRepository.findByEmail("missing@hoop.test")).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing@hoop.test"));
    }
}

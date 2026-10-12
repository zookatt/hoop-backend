package zotov.hoop_backend.support;

import org.junit.jupiter.api.Test;
import zotov.hoop_backend.characteristics.entity.UserCharacteristics;
import zotov.hoop_backend.characteristics.repository.UserCharacteristicsRepository;
import zotov.hoop_backend.characteristics.service.UserCharacteristicsServiceImpl;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;
import zotov.hoop_backend.credentials.service.UserCredentialsServiceImpl;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.role.repository.UserRoleRepository;
import zotov.hoop_backend.role.service.UserRoleServiceImpl;
import zotov.hoop_backend.user.entity.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServiceDelegationTest {

    @Test
    void userRoleServiceDelegatesToRepository() {
        UserRoleRepository repository = mock(UserRoleRepository.class);
        UserRoleServiceImpl service = new UserRoleServiceImpl(repository);
        UserRole role = new UserRole("ADMIN");

        when(repository.findAll()).thenReturn(List.of(role));
        when(repository.findById(1)).thenReturn(Optional.of(role));
        when(repository.save(role)).thenReturn(role);

        assertEquals(List.of(role), service.findAll());
        assertEquals(Optional.of(role), service.findById(1));
        assertEquals(role, service.save(role));
    }

    @Test
    void userCredentialsServiceDelegatesToRepository() {
        UserCredentialsRepository repository = mock(UserCredentialsRepository.class);
        UserCredentialsServiceImpl service = new UserCredentialsServiceImpl(repository);
        UserCredentials credentials = new UserCredentials("admin@hoop.test", "encoded", new User(new UserRole("ADMIN")));

        when(repository.findById(1)).thenReturn(Optional.of(credentials));
        when(repository.findByEmail("admin@hoop.test")).thenReturn(Optional.of(credentials));
        when(repository.save(credentials)).thenReturn(credentials);

        assertEquals(Optional.of(credentials), service.findById(1));
        assertEquals(Optional.of(credentials), service.findByEmail("admin@hoop.test"));
        assertEquals(credentials, service.save(credentials));
    }

    @Test
    void userCharacteristicsServiceDelegatesToRepository() {
        UserCharacteristicsRepository repository = mock(UserCharacteristicsRepository.class);
        UserCharacteristicsServiceImpl service = new UserCharacteristicsServiceImpl(repository);
        UserCharacteristics characteristics = new UserCharacteristics("Katia", new User(new UserRole("RECEPTION")));

        when(repository.findById(1)).thenReturn(Optional.of(characteristics));
        when(repository.findByUserId(2)).thenReturn(Optional.of(characteristics));
        when(repository.save(characteristics)).thenReturn(characteristics);

        assertEquals(Optional.of(characteristics), service.findById(1));
        assertEquals(Optional.of(characteristics), service.findByUserId(2));
        assertEquals(characteristics, service.save(characteristics));
        verify(repository).save(characteristics);
    }
}

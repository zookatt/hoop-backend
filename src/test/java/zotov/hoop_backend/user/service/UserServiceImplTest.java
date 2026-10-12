package zotov.hoop_backend.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import zotov.hoop_backend.characteristics.entity.UserCharacteristics;
import zotov.hoop_backend.characteristics.repository.UserCharacteristicsRepository;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.role.repository.UserRoleRepository;
import zotov.hoop_backend.user.dto.CreateUserDTORequest;
import zotov.hoop_backend.user.dto.UserDTOResponse;
import zotov.hoop_backend.user.entity.User;
import zotov.hoop_backend.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private UserCharacteristicsRepository userCharacteristicsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void createStoresUserWithEncodedTemporaryPassword() {
        UserServiceImpl service = service();
        UserRole role = new UserRole("CLEANING");
        CreateUserDTORequest request = new CreateUserDTORequest(
                "Ana Limpieza",
                "ana@hoop.test",
                "Temporal1234",
                2);

        when(userRoleRepository.findById(2)).thenReturn(Optional.of(role));
        when(userCredentialsRepository.findByEmail("ana@hoop.test")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Temporal1234")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userCredentialsRepository.save(any(UserCredentials.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userCharacteristicsRepository.save(any(UserCharacteristics.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTOResponse response = service.create(request);

        assertEquals("Ana Limpieza", response.name());
        assertEquals("ana@hoop.test", response.email());
        assertEquals("CLEANING", response.roleName());
        assertTrue(response.active());
        assertTrue(response.mustChangePassword());

        ArgumentCaptor<UserCredentials> credentialsCaptor = ArgumentCaptor.forClass(UserCredentials.class);
        verify(userCredentialsRepository).save(credentialsCaptor.capture());
        assertEquals("encoded-password", credentialsCaptor.getValue().getPassword());
    }

    @Test
    void createRejectsDuplicatedEmail() {
        UserServiceImpl service = service();
        UserRole role = new UserRole("ADMIN");
        User existingUser = new User(role);
        UserCredentials existingCredentials = new UserCredentials("admin@hoop.test", "encoded", existingUser);

        when(userRoleRepository.findById(1)).thenReturn(Optional.of(role));
        when(userCredentialsRepository.findByEmail("admin@hoop.test")).thenReturn(Optional.of(existingCredentials));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.create(new CreateUserDTORequest(
                        "Admin",
                        "admin@hoop.test",
                        "Admin1234",
                        1)));

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void createRejectsUnknownRole() {
        UserServiceImpl service = service();

        when(userRoleRepository.findById(99)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(new CreateUserDTORequest(
                        "Sin rol",
                        "norole@hoop.test",
                        "Password1234",
                        99)));

        assertEquals("Role not found with id 99", exception.getMessage());
    }

    private UserServiceImpl service() {
        return new UserServiceImpl(
                userRepository,
                userRoleRepository,
                userCredentialsRepository,
                userCharacteristicsRepository,
                passwordEncoder);
    }
}

package zotov.hoop_backend.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zotov.hoop_backend.user.dto.CreateUserDTORequest;
import zotov.hoop_backend.user.dto.UserDTOResponse;
import zotov.hoop_backend.user.entity.User;
import zotov.hoop_backend.characteristics.entity.UserCharacteristics;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.characteristics.repository.UserCharacteristicsRepository;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;
import zotov.hoop_backend.user.repository.UserRepository;
import zotov.hoop_backend.role.repository.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final UserCharacteristicsRepository userCharacteristicsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            UserCredentialsRepository userCredentialsRepository,
            UserCharacteristicsRepository userCharacteristicsRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.userCharacteristicsRepository = userCharacteristicsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTOResponse> findAllResponses() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userCredentialsRepository.findByEmail(email)
                .map(UserCredentials::getUser);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTOResponse create(CreateUserDTORequest request) {
        UserRole role = userRoleRepository.findById(request.roleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id " + request.roleId()));

        if (userCredentialsRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        User user = userRepository.save(new User(role));
        UserCredentials credentials = userCredentialsRepository.save(new UserCredentials(
                request.email(),
                passwordEncoder.encode(request.password()),
                user));
        UserCharacteristics characteristics = userCharacteristicsRepository.save(new UserCharacteristics(
                request.name(),
                user));

        return toResponse(user, characteristics, credentials);
    }

    @Override
    public User deactivate(User user) {
        user.setActive(false);
        return userRepository.save(user);
    }

    private UserDTOResponse toResponse(User user) {
        UserCharacteristics characteristics = userCharacteristicsRepository.findByUserId(user.getId())
                .orElse(null);
        UserCredentials credentials = userCredentialsRepository.findByUserId(user.getId())
                .orElse(null);

        return toResponse(user, characteristics, credentials);
    }

    private UserDTOResponse toResponse(
            User user,
            UserCharacteristics characteristics,
            UserCredentials credentials) {
        return new UserDTOResponse(
                user.getId(),
                characteristics == null ? null : characteristics.getName(),
                credentials == null ? null : credentials.getEmail(),
                user.getActive(),
                user.getRole().getId(),
                user.getRole().getName());
    }
}

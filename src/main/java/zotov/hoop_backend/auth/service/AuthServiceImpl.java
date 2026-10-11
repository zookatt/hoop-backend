package zotov.hoop_backend.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zotov.hoop_backend.auth.dto.ChangePasswordDTORequest;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserCredentialsRepository userCredentialsRepository,
            PasswordEncoder passwordEncoder) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordDTORequest request) {
        UserCredentials credentials = userCredentialsRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email " + request.email()));

        if (!passwordEncoder.matches(request.currentPassword(), credentials.getPassword())) {
            throw new IllegalStateException("Current password is incorrect");
        }

        credentials.setPassword(passwordEncoder.encode(request.newPassword()));
        credentials.setMustChangePassword(false);

        userCredentialsRepository.save(credentials);
    }
}
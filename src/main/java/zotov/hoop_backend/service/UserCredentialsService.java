package zotov.hoop_backend.service;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.entity.UserCredentials;
import zotov.hoop_backend.repository.UserCredentialsRepository;

import java.util.Optional;

@Service
public class UserCredentialsService {

    private final UserCredentialsRepository userCredentialsRepository;

    public UserCredentialsService(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    public Optional<UserCredentials> findById(Integer id) {
        return userCredentialsRepository.findById(id);
    }

    public Optional<UserCredentials> findByEmail(String email) {
        return userCredentialsRepository.findByEmail(email);
    }

    public UserCredentials save(UserCredentials credentials) {
        return userCredentialsRepository.save(credentials);
    }
}
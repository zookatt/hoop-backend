package zotov.hoop_backend.service;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.entity.UserCredentials;
import zotov.hoop_backend.repository.UserCredentialsRepository;

import java.util.Optional;

@Service
public class UserCredentialsServiceImpl implements UserCredentialsService {

    private final UserCredentialsRepository userCredentialsRepository;

    public UserCredentialsServiceImpl(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public Optional<UserCredentials> findById(Integer id) {
        return userCredentialsRepository.findById(id);
    }

    @Override
    public Optional<UserCredentials> findByEmail(String email) {
        return userCredentialsRepository.findByEmail(email);
    }

    @Override
    public UserCredentials save(UserCredentials credentials) {
        return userCredentialsRepository.save(credentials);
    }
}

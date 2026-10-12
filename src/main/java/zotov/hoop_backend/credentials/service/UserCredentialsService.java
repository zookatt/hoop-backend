package zotov.hoop_backend.credentials.service;

import zotov.hoop_backend.credentials.entity.UserCredentials;

import java.util.Optional;

public interface UserCredentialsService {

    Optional<UserCredentials> findById(Integer id);

    Optional<UserCredentials> findByEmail(String email);

    UserCredentials save(UserCredentials credentials);
}

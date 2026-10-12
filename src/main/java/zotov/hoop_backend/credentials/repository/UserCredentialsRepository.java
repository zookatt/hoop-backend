package zotov.hoop_backend.credentials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Integer> {
    Optional<UserCredentials> findByEmail(String email);

    Optional<UserCredentials> findByUserId(Integer userId);
}

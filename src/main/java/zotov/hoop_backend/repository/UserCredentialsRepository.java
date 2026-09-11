package zotov.hoop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zotov.hoop_backend.entity.UserCredentials;
import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Integer> {
    Optional<UserCredentials> findByEmail(String email);
}

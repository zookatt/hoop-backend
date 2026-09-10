package zotov.hoop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zotov.hoop_backend.entity.UserCredentials;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Integer> {

}

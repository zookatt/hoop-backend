package zotov.hoop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zotov.hoop_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}

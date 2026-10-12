package zotov.hoop_backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zotov.hoop_backend.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}

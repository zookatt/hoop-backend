package zotov.hoop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zotov.hoop_backend.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}

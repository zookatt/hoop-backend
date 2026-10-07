package zotov.hoop_backend.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zotov.hoop_backend.role.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}

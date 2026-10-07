package zotov.hoop_backend.role.service;

import zotov.hoop_backend.role.entity.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {

    List<UserRole> findAll();

    Optional<UserRole> findById(Integer id);

    UserRole save(UserRole role);
}

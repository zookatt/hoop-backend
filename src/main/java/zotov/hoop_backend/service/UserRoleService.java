package zotov.hoop_backend.service;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.entity.UserRole;
import zotov.hoop_backend.repository.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    public Optional<UserRole> findById(Integer id) {
        return userRoleRepository.findById(id);
    }

    public UserRole save(UserRole role) {
        return userRoleRepository.save(role);
    }
}
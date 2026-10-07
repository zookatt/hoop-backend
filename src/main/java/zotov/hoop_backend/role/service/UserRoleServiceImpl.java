package zotov.hoop_backend.role.service;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.role.entity.UserRole;
import zotov.hoop_backend.role.repository.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public Optional<UserRole> findById(Integer id) {
        return userRoleRepository.findById(id);
    }

    @Override
    public UserRole save(UserRole role) {
        return userRoleRepository.save(role);
    }
}

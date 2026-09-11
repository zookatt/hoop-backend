package zotov.hoop_backend.service;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.entity.User;
import zotov.hoop_backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User deactivate(User user) {
        user.setActive(false);
        return userRepository.save(user);
    }
}

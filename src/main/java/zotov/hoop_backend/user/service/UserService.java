package zotov.hoop_backend.user.service;

import zotov.hoop_backend.user.dto.CreateUserDTORequest;
import zotov.hoop_backend.user.dto.UserDTOResponse;
import zotov.hoop_backend.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    List<UserDTOResponse> findAllResponses();

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    User save(User user);

    UserDTOResponse create(CreateUserDTORequest request);

    User deactivate(User user);
}

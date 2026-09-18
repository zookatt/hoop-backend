package zotov.hoop_backend.service;

import zotov.hoop_backend.dto.user.CreateUserDTORequest;
import zotov.hoop_backend.dto.user.UserDTOResponse;
import zotov.hoop_backend.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    List<UserDTOResponse> findAllResponses();

    Optional<User> findById(Integer id);

    User save(User user);

    UserDTOResponse create(CreateUserDTORequest request);

    User deactivate(User user);
}

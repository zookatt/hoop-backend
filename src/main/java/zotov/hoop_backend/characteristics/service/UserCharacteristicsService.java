package zotov.hoop_backend.characteristics.service;

import zotov.hoop_backend.characteristics.entity.UserCharacteristics;

import java.util.Optional;

public interface UserCharacteristicsService {

    Optional<UserCharacteristics> findById(Integer id);

    Optional<UserCharacteristics> findByUserId(Integer userId);

    UserCharacteristics save(UserCharacteristics characteristics);
}

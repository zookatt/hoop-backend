package zotov.hoop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zotov.hoop_backend.entity.UserCharacteristics;
import java.util.Optional;

public interface UserCharacteristicsRepository extends JpaRepository<UserCharacteristics, Integer> {
    Optional<UserCharacteristics> findByUserId(Integer userId);
}

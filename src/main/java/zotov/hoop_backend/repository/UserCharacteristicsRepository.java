package zotov.hoop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zotov.hoop_backend.entity.UserCharacteristics;

public interface UserCharacteristicsRepository extends JpaRepository<UserCharacteristics, Integer> {

}

package zotov.hoop_backend.service;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.entity.UserCharacteristics;
import zotov.hoop_backend.repository.UserCharacteristicsRepository;

import java.util.Optional;

@Service
public class UserCharacteristicsService {

    private final UserCharacteristicsRepository userCharacteristicsRepository;

    public UserCharacteristicsService(
            UserCharacteristicsRepository userCharacteristicsRepository) {
        this.userCharacteristicsRepository = userCharacteristicsRepository;
    }

    public Optional<UserCharacteristics> findById(Integer id) {
        return userCharacteristicsRepository.findById(id);
    }

    public Optional<UserCharacteristics> findByUserId(Integer userId) {
        return userCharacteristicsRepository.findByUserId(userId);
    }

    public UserCharacteristics save(UserCharacteristics characteristics) {
        return userCharacteristicsRepository.save(characteristics);
    }
}
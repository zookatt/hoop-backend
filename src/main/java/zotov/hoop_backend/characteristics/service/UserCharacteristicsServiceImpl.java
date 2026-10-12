package zotov.hoop_backend.characteristics.service;

import org.springframework.stereotype.Service;
import zotov.hoop_backend.characteristics.entity.UserCharacteristics;
import zotov.hoop_backend.characteristics.repository.UserCharacteristicsRepository;

import java.util.Optional;

@Service
public class UserCharacteristicsServiceImpl implements UserCharacteristicsService {

    private final UserCharacteristicsRepository userCharacteristicsRepository;

    public UserCharacteristicsServiceImpl(
            UserCharacteristicsRepository userCharacteristicsRepository) {
        this.userCharacteristicsRepository = userCharacteristicsRepository;
    }

    @Override
    public Optional<UserCharacteristics> findById(Integer id) {
        return userCharacteristicsRepository.findById(id);
    }

    @Override
    public Optional<UserCharacteristics> findByUserId(Integer userId) {
        return userCharacteristicsRepository.findByUserId(userId);
    }

    @Override
    public UserCharacteristics save(UserCharacteristics characteristics) {
        return userCharacteristicsRepository.save(characteristics);
    }
}

package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.RestaurantMenuData;
import tn.greencode.hospitality.repository.RestaurantMenuDataRepository;
import tn.greencode.hospitality.service.RestaurantMenuDataService;

/**
 * Service Implementation for managing {@link RestaurantMenuData}.
 */
@Service
@Transactional
public class RestaurantMenuDataServiceImpl implements RestaurantMenuDataService {

    private final Logger log = LoggerFactory.getLogger(RestaurantMenuDataServiceImpl.class);

    private final RestaurantMenuDataRepository restaurantMenuDataRepository;

    public RestaurantMenuDataServiceImpl(RestaurantMenuDataRepository restaurantMenuDataRepository) {
        this.restaurantMenuDataRepository = restaurantMenuDataRepository;
    }

    @Override
    public RestaurantMenuData save(RestaurantMenuData restaurantMenuData) {
        log.debug("Request to save RestaurantMenuData : {}", restaurantMenuData);
        return restaurantMenuDataRepository.save(restaurantMenuData);
    }

    @Override
    public RestaurantMenuData update(RestaurantMenuData restaurantMenuData) {
        log.debug("Request to save RestaurantMenuData : {}", restaurantMenuData);
        return restaurantMenuDataRepository.save(restaurantMenuData);
    }

    @Override
    public Optional<RestaurantMenuData> partialUpdate(RestaurantMenuData restaurantMenuData) {
        log.debug("Request to partially update RestaurantMenuData : {}", restaurantMenuData);

        return restaurantMenuDataRepository
            .findById(restaurantMenuData.getId())
            .map(existingRestaurantMenuData -> {
                if (restaurantMenuData.getLang() != null) {
                    existingRestaurantMenuData.setLang(restaurantMenuData.getLang());
                }
                if (restaurantMenuData.getTitle() != null) {
                    existingRestaurantMenuData.setTitle(restaurantMenuData.getTitle());
                }
                if (restaurantMenuData.getContent() != null) {
                    existingRestaurantMenuData.setContent(restaurantMenuData.getContent());
                }
                if (restaurantMenuData.getImage() != null) {
                    existingRestaurantMenuData.setImage(restaurantMenuData.getImage());
                }
                if (restaurantMenuData.getImageContentType() != null) {
                    existingRestaurantMenuData.setImageContentType(restaurantMenuData.getImageContentType());
                }

                return existingRestaurantMenuData;
            })
            .map(restaurantMenuDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantMenuData> findAll() {
        log.debug("Request to get all RestaurantMenuData");
        return restaurantMenuDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RestaurantMenuData> findOne(Long id) {
        log.debug("Request to get RestaurantMenuData : {}", id);
        return restaurantMenuDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RestaurantMenuData : {}", id);
        restaurantMenuDataRepository.deleteById(id);
    }
}

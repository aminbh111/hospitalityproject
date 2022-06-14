package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.RoomServiceData;
import tn.greencode.hospitality.repository.RoomServiceDataRepository;
import tn.greencode.hospitality.service.RoomServiceDataService;

/**
 * Service Implementation for managing {@link RoomServiceData}.
 */
@Service
@Transactional
public class RoomServiceDataServiceImpl implements RoomServiceDataService {

    private final Logger log = LoggerFactory.getLogger(RoomServiceDataServiceImpl.class);

    private final RoomServiceDataRepository roomServiceDataRepository;

    public RoomServiceDataServiceImpl(RoomServiceDataRepository roomServiceDataRepository) {
        this.roomServiceDataRepository = roomServiceDataRepository;
    }

    @Override
    public RoomServiceData save(RoomServiceData roomServiceData) {
        log.debug("Request to save RoomServiceData : {}", roomServiceData);
        return roomServiceDataRepository.save(roomServiceData);
    }

    @Override
    public RoomServiceData update(RoomServiceData roomServiceData) {
        log.debug("Request to save RoomServiceData : {}", roomServiceData);
        return roomServiceDataRepository.save(roomServiceData);
    }

    @Override
    public Optional<RoomServiceData> partialUpdate(RoomServiceData roomServiceData) {
        log.debug("Request to partially update RoomServiceData : {}", roomServiceData);

        return roomServiceDataRepository
            .findById(roomServiceData.getId())
            .map(existingRoomServiceData -> {
                if (roomServiceData.getLang() != null) {
                    existingRoomServiceData.setLang(roomServiceData.getLang());
                }
                if (roomServiceData.getTitle() != null) {
                    existingRoomServiceData.setTitle(roomServiceData.getTitle());
                }
                if (roomServiceData.getContent() != null) {
                    existingRoomServiceData.setContent(roomServiceData.getContent());
                }
                if (roomServiceData.getImage() != null) {
                    existingRoomServiceData.setImage(roomServiceData.getImage());
                }
                if (roomServiceData.getImageContentType() != null) {
                    existingRoomServiceData.setImageContentType(roomServiceData.getImageContentType());
                }

                return existingRoomServiceData;
            })
            .map(roomServiceDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomServiceData> findAll() {
        log.debug("Request to get all RoomServiceData");
        return roomServiceDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomServiceData> findOne(Long id) {
        log.debug("Request to get RoomServiceData : {}", id);
        return roomServiceDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoomServiceData : {}", id);
        roomServiceDataRepository.deleteById(id);
    }
}

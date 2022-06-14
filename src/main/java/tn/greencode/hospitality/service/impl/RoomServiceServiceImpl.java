package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.RoomService;
import tn.greencode.hospitality.repository.RoomServiceRepository;
import tn.greencode.hospitality.service.RoomServiceService;

/**
 * Service Implementation for managing {@link RoomService}.
 */
@Service
@Transactional
public class RoomServiceServiceImpl implements RoomServiceService {

    private final Logger log = LoggerFactory.getLogger(RoomServiceServiceImpl.class);

    private final RoomServiceRepository roomServiceRepository;

    public RoomServiceServiceImpl(RoomServiceRepository roomServiceRepository) {
        this.roomServiceRepository = roomServiceRepository;
    }

    @Override
    public RoomService save(RoomService roomService) {
        log.debug("Request to save RoomService : {}", roomService);
        return roomServiceRepository.save(roomService);
    }

    @Override
    public RoomService update(RoomService roomService) {
        log.debug("Request to save RoomService : {}", roomService);
        return roomServiceRepository.save(roomService);
    }

    @Override
    public Optional<RoomService> partialUpdate(RoomService roomService) {
        log.debug("Request to partially update RoomService : {}", roomService);

        return roomServiceRepository
            .findById(roomService.getId())
            .map(existingRoomService -> {
                if (roomService.getDate() != null) {
                    existingRoomService.setDate(roomService.getDate());
                }
                if (roomService.getPublish() != null) {
                    existingRoomService.setPublish(roomService.getPublish());
                }
                if (roomService.getContentPosition() != null) {
                    existingRoomService.setContentPosition(roomService.getContentPosition());
                }
                if (roomService.getImagePosition() != null) {
                    existingRoomService.setImagePosition(roomService.getImagePosition());
                }

                return existingRoomService;
            })
            .map(roomServiceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomService> findAll() {
        log.debug("Request to get all RoomServices");
        return roomServiceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomService> findOne(Long id) {
        log.debug("Request to get RoomService : {}", id);
        return roomServiceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoomService : {}", id);
        roomServiceRepository.deleteById(id);
    }
}

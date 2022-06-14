package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.EventData;
import tn.greencode.hospitality.repository.EventDataRepository;
import tn.greencode.hospitality.service.EventDataService;

/**
 * Service Implementation for managing {@link EventData}.
 */
@Service
@Transactional
public class EventDataServiceImpl implements EventDataService {

    private final Logger log = LoggerFactory.getLogger(EventDataServiceImpl.class);

    private final EventDataRepository eventDataRepository;

    public EventDataServiceImpl(EventDataRepository eventDataRepository) {
        this.eventDataRepository = eventDataRepository;
    }

    @Override
    public EventData save(EventData eventData) {
        log.debug("Request to save EventData : {}", eventData);
        return eventDataRepository.save(eventData);
    }

    @Override
    public EventData update(EventData eventData) {
        log.debug("Request to save EventData : {}", eventData);
        return eventDataRepository.save(eventData);
    }

    @Override
    public Optional<EventData> partialUpdate(EventData eventData) {
        log.debug("Request to partially update EventData : {}", eventData);

        return eventDataRepository
            .findById(eventData.getId())
            .map(existingEventData -> {
                if (eventData.getLang() != null) {
                    existingEventData.setLang(eventData.getLang());
                }
                if (eventData.getTitle() != null) {
                    existingEventData.setTitle(eventData.getTitle());
                }
                if (eventData.getContent() != null) {
                    existingEventData.setContent(eventData.getContent());
                }
                if (eventData.getImage() != null) {
                    existingEventData.setImage(eventData.getImage());
                }
                if (eventData.getImageContentType() != null) {
                    existingEventData.setImageContentType(eventData.getImageContentType());
                }

                return existingEventData;
            })
            .map(eventDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventData> findAll() {
        log.debug("Request to get all EventData");
        return eventDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventData> findOne(Long id) {
        log.debug("Request to get EventData : {}", id);
        return eventDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventData : {}", id);
        eventDataRepository.deleteById(id);
    }
}

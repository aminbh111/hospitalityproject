package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.Event;
import tn.greencode.hospitality.repository.EventRepository;
import tn.greencode.hospitality.service.EventService;

/**
 * Service Implementation for managing {@link Event}.
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event save(Event event) {
        log.debug("Request to save Event : {}", event);
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        log.debug("Request to save Event : {}", event);
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> partialUpdate(Event event) {
        log.debug("Request to partially update Event : {}", event);

        return eventRepository
            .findById(event.getId())
            .map(existingEvent -> {
                if (event.getDate() != null) {
                    existingEvent.setDate(event.getDate());
                }
                if (event.getPublish() != null) {
                    existingEvent.setPublish(event.getPublish());
                }
                if (event.getContentPosition() != null) {
                    existingEvent.setContentPosition(event.getContentPosition());
                }
                if (event.getImagePosition() != null) {
                    existingEvent.setImagePosition(event.getImagePosition());
                }

                return existingEvent;
            })
            .map(eventRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        log.debug("Request to get all Events");
        return eventRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Event> findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
    }
}

package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.Meeting;
import tn.greencode.hospitality.repository.MeetingRepository;
import tn.greencode.hospitality.service.MeetingService;

/**
 * Service Implementation for managing {@link Meeting}.
 */
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {

    private final Logger log = LoggerFactory.getLogger(MeetingServiceImpl.class);

    private final MeetingRepository meetingRepository;

    public MeetingServiceImpl(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @Override
    public Meeting save(Meeting meeting) {
        log.debug("Request to save Meeting : {}", meeting);
        return meetingRepository.save(meeting);
    }

    @Override
    public Meeting update(Meeting meeting) {
        log.debug("Request to save Meeting : {}", meeting);
        return meetingRepository.save(meeting);
    }

    @Override
    public Optional<Meeting> partialUpdate(Meeting meeting) {
        log.debug("Request to partially update Meeting : {}", meeting);

        return meetingRepository
            .findById(meeting.getId())
            .map(existingMeeting -> {
                if (meeting.getDate() != null) {
                    existingMeeting.setDate(meeting.getDate());
                }
                if (meeting.getPublish() != null) {
                    existingMeeting.setPublish(meeting.getPublish());
                }
                if (meeting.getContentPosition() != null) {
                    existingMeeting.setContentPosition(meeting.getContentPosition());
                }
                if (meeting.getImagePosition() != null) {
                    existingMeeting.setImagePosition(meeting.getImagePosition());
                }

                return existingMeeting;
            })
            .map(meetingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Meeting> findAll() {
        log.debug("Request to get all Meetings");
        return meetingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Meeting> findOne(Long id) {
        log.debug("Request to get Meeting : {}", id);
        return meetingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Meeting : {}", id);
        meetingRepository.deleteById(id);
    }
}

package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.MeetingData;
import tn.greencode.hospitality.repository.MeetingDataRepository;
import tn.greencode.hospitality.service.MeetingDataService;

/**
 * Service Implementation for managing {@link MeetingData}.
 */
@Service
@Transactional
public class MeetingDataServiceImpl implements MeetingDataService {

    private final Logger log = LoggerFactory.getLogger(MeetingDataServiceImpl.class);

    private final MeetingDataRepository meetingDataRepository;

    public MeetingDataServiceImpl(MeetingDataRepository meetingDataRepository) {
        this.meetingDataRepository = meetingDataRepository;
    }

    @Override
    public MeetingData save(MeetingData meetingData) {
        log.debug("Request to save MeetingData : {}", meetingData);
        return meetingDataRepository.save(meetingData);
    }

    @Override
    public MeetingData update(MeetingData meetingData) {
        log.debug("Request to save MeetingData : {}", meetingData);
        return meetingDataRepository.save(meetingData);
    }

    @Override
    public Optional<MeetingData> partialUpdate(MeetingData meetingData) {
        log.debug("Request to partially update MeetingData : {}", meetingData);

        return meetingDataRepository
            .findById(meetingData.getId())
            .map(existingMeetingData -> {
                if (meetingData.getLang() != null) {
                    existingMeetingData.setLang(meetingData.getLang());
                }
                if (meetingData.getTitle() != null) {
                    existingMeetingData.setTitle(meetingData.getTitle());
                }
                if (meetingData.getContent() != null) {
                    existingMeetingData.setContent(meetingData.getContent());
                }
                if (meetingData.getImage() != null) {
                    existingMeetingData.setImage(meetingData.getImage());
                }
                if (meetingData.getImageContentType() != null) {
                    existingMeetingData.setImageContentType(meetingData.getImageContentType());
                }

                return existingMeetingData;
            })
            .map(meetingDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingData> findAll() {
        log.debug("Request to get all MeetingData");
        return meetingDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MeetingData> findOne(Long id) {
        log.debug("Request to get MeetingData : {}", id);
        return meetingDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MeetingData : {}", id);
        meetingDataRepository.deleteById(id);
    }
}

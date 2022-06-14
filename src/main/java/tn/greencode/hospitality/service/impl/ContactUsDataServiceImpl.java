package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.ContactUsData;
import tn.greencode.hospitality.repository.ContactUsDataRepository;
import tn.greencode.hospitality.service.ContactUsDataService;

/**
 * Service Implementation for managing {@link ContactUsData}.
 */
@Service
@Transactional
public class ContactUsDataServiceImpl implements ContactUsDataService {

    private final Logger log = LoggerFactory.getLogger(ContactUsDataServiceImpl.class);

    private final ContactUsDataRepository contactUsDataRepository;

    public ContactUsDataServiceImpl(ContactUsDataRepository contactUsDataRepository) {
        this.contactUsDataRepository = contactUsDataRepository;
    }

    @Override
    public ContactUsData save(ContactUsData contactUsData) {
        log.debug("Request to save ContactUsData : {}", contactUsData);
        return contactUsDataRepository.save(contactUsData);
    }

    @Override
    public ContactUsData update(ContactUsData contactUsData) {
        log.debug("Request to save ContactUsData : {}", contactUsData);
        return contactUsDataRepository.save(contactUsData);
    }

    @Override
    public Optional<ContactUsData> partialUpdate(ContactUsData contactUsData) {
        log.debug("Request to partially update ContactUsData : {}", contactUsData);

        return contactUsDataRepository
            .findById(contactUsData.getId())
            .map(existingContactUsData -> {
                if (contactUsData.getLang() != null) {
                    existingContactUsData.setLang(contactUsData.getLang());
                }
                if (contactUsData.getTitle() != null) {
                    existingContactUsData.setTitle(contactUsData.getTitle());
                }
                if (contactUsData.getContent() != null) {
                    existingContactUsData.setContent(contactUsData.getContent());
                }
                if (contactUsData.getImage() != null) {
                    existingContactUsData.setImage(contactUsData.getImage());
                }
                if (contactUsData.getImageContentType() != null) {
                    existingContactUsData.setImageContentType(contactUsData.getImageContentType());
                }

                return existingContactUsData;
            })
            .map(contactUsDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactUsData> findAll() {
        log.debug("Request to get all ContactUsData");
        return contactUsDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactUsData> findOne(Long id) {
        log.debug("Request to get ContactUsData : {}", id);
        return contactUsDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactUsData : {}", id);
        contactUsDataRepository.deleteById(id);
    }
}

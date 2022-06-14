package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.ContactUs;
import tn.greencode.hospitality.repository.ContactUsRepository;
import tn.greencode.hospitality.service.ContactUsService;

/**
 * Service Implementation for managing {@link ContactUs}.
 */
@Service
@Transactional
public class ContactUsServiceImpl implements ContactUsService {

    private final Logger log = LoggerFactory.getLogger(ContactUsServiceImpl.class);

    private final ContactUsRepository contactUsRepository;

    public ContactUsServiceImpl(ContactUsRepository contactUsRepository) {
        this.contactUsRepository = contactUsRepository;
    }

    @Override
    public ContactUs save(ContactUs contactUs) {
        log.debug("Request to save ContactUs : {}", contactUs);
        return contactUsRepository.save(contactUs);
    }

    @Override
    public ContactUs update(ContactUs contactUs) {
        log.debug("Request to save ContactUs : {}", contactUs);
        return contactUsRepository.save(contactUs);
    }

    @Override
    public Optional<ContactUs> partialUpdate(ContactUs contactUs) {
        log.debug("Request to partially update ContactUs : {}", contactUs);

        return contactUsRepository
            .findById(contactUs.getId())
            .map(existingContactUs -> {
                if (contactUs.getDate() != null) {
                    existingContactUs.setDate(contactUs.getDate());
                }
                if (contactUs.getPublish() != null) {
                    existingContactUs.setPublish(contactUs.getPublish());
                }
                if (contactUs.getContentPosition() != null) {
                    existingContactUs.setContentPosition(contactUs.getContentPosition());
                }
                if (contactUs.getImagePosition() != null) {
                    existingContactUs.setImagePosition(contactUs.getImagePosition());
                }

                return existingContactUs;
            })
            .map(contactUsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactUs> findAll() {
        log.debug("Request to get all Contactuses");
        return contactUsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactUs> findOne(Long id) {
        log.debug("Request to get ContactUs : {}", id);
        return contactUsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactUs : {}", id);
        contactUsRepository.deleteById(id);
    }
}

package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.AboutUs;
import tn.greencode.hospitality.repository.AboutUsRepository;
import tn.greencode.hospitality.service.AboutUsService;

/**
 * Service Implementation for managing {@link AboutUs}.
 */
@Service
@Transactional
public class AboutUsServiceImpl implements AboutUsService {

    private final Logger log = LoggerFactory.getLogger(AboutUsServiceImpl.class);

    private final AboutUsRepository aboutUsRepository;

    public AboutUsServiceImpl(AboutUsRepository aboutUsRepository) {
        this.aboutUsRepository = aboutUsRepository;
    }

    @Override
    public AboutUs save(AboutUs aboutUs) {
        log.debug("Request to save AboutUs : {}", aboutUs);
        return aboutUsRepository.save(aboutUs);
    }

    @Override
    public AboutUs update(AboutUs aboutUs) {
        log.debug("Request to save AboutUs : {}", aboutUs);
        return aboutUsRepository.save(aboutUs);
    }

    @Override
    public Optional<AboutUs> partialUpdate(AboutUs aboutUs) {
        log.debug("Request to partially update AboutUs : {}", aboutUs);

        return aboutUsRepository
            .findById(aboutUs.getId())
            .map(existingAboutUs -> {
                if (aboutUs.getDate() != null) {
                    existingAboutUs.setDate(aboutUs.getDate());
                }
                if (aboutUs.getPublish() != null) {
                    existingAboutUs.setPublish(aboutUs.getPublish());
                }
                if (aboutUs.getContentPosition() != null) {
                    existingAboutUs.setContentPosition(aboutUs.getContentPosition());
                }
                if (aboutUs.getImagePosition() != null) {
                    existingAboutUs.setImagePosition(aboutUs.getImagePosition());
                }

                return existingAboutUs;
            })
            .map(aboutUsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AboutUs> findAll() {
        log.debug("Request to get all Aboutuses");
        return aboutUsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AboutUs> findOne(Long id) {
        log.debug("Request to get AboutUs : {}", id);
        return aboutUsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AboutUs : {}", id);
        aboutUsRepository.deleteById(id);
    }
}

package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.AboutUsData;
import tn.greencode.hospitality.repository.AboutUsDataRepository;
import tn.greencode.hospitality.service.AboutUsDataService;

/**
 * Service Implementation for managing {@link AboutUsData}.
 */
@Service
@Transactional
public class AboutUsDataServiceImpl implements AboutUsDataService {

    private final Logger log = LoggerFactory.getLogger(AboutUsDataServiceImpl.class);

    private final AboutUsDataRepository aboutUsDataRepository;

    public AboutUsDataServiceImpl(AboutUsDataRepository aboutUsDataRepository) {
        this.aboutUsDataRepository = aboutUsDataRepository;
    }

    @Override
    public AboutUsData save(AboutUsData aboutUsData) {
        log.debug("Request to save AboutUsData : {}", aboutUsData);
        return aboutUsDataRepository.save(aboutUsData);
    }

    @Override
    public AboutUsData update(AboutUsData aboutUsData) {
        log.debug("Request to save AboutUsData : {}", aboutUsData);
        return aboutUsDataRepository.save(aboutUsData);
    }

    @Override
    public Optional<AboutUsData> partialUpdate(AboutUsData aboutUsData) {
        log.debug("Request to partially update AboutUsData : {}", aboutUsData);

        return aboutUsDataRepository
            .findById(aboutUsData.getId())
            .map(existingAboutUsData -> {
                if (aboutUsData.getLang() != null) {
                    existingAboutUsData.setLang(aboutUsData.getLang());
                }
                if (aboutUsData.getTitle() != null) {
                    existingAboutUsData.setTitle(aboutUsData.getTitle());
                }
                if (aboutUsData.getContent() != null) {
                    existingAboutUsData.setContent(aboutUsData.getContent());
                }
                if (aboutUsData.getImage() != null) {
                    existingAboutUsData.setImage(aboutUsData.getImage());
                }
                if (aboutUsData.getImageContentType() != null) {
                    existingAboutUsData.setImageContentType(aboutUsData.getImageContentType());
                }

                return existingAboutUsData;
            })
            .map(aboutUsDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AboutUsData> findAll() {
        log.debug("Request to get all AboutUsData");
        return aboutUsDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AboutUsData> findOne(Long id) {
        log.debug("Request to get AboutUsData : {}", id);
        return aboutUsDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AboutUsData : {}", id);
        aboutUsDataRepository.deleteById(id);
    }
}

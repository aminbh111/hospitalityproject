package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.SpaData;
import tn.greencode.hospitality.repository.SpaDataRepository;
import tn.greencode.hospitality.service.SpaDataService;

/**
 * Service Implementation for managing {@link SpaData}.
 */
@Service
@Transactional
public class SpaDataServiceImpl implements SpaDataService {

    private final Logger log = LoggerFactory.getLogger(SpaDataServiceImpl.class);

    private final SpaDataRepository spaDataRepository;

    public SpaDataServiceImpl(SpaDataRepository spaDataRepository) {
        this.spaDataRepository = spaDataRepository;
    }

    @Override
    public SpaData save(SpaData spaData) {
        log.debug("Request to save SpaData : {}", spaData);
        return spaDataRepository.save(spaData);
    }

    @Override
    public SpaData update(SpaData spaData) {
        log.debug("Request to save SpaData : {}", spaData);
        return spaDataRepository.save(spaData);
    }

    @Override
    public Optional<SpaData> partialUpdate(SpaData spaData) {
        log.debug("Request to partially update SpaData : {}", spaData);

        return spaDataRepository
            .findById(spaData.getId())
            .map(existingSpaData -> {
                if (spaData.getLang() != null) {
                    existingSpaData.setLang(spaData.getLang());
                }
                if (spaData.getTitle() != null) {
                    existingSpaData.setTitle(spaData.getTitle());
                }
                if (spaData.getContent() != null) {
                    existingSpaData.setContent(spaData.getContent());
                }
                if (spaData.getImage() != null) {
                    existingSpaData.setImage(spaData.getImage());
                }
                if (spaData.getImageContentType() != null) {
                    existingSpaData.setImageContentType(spaData.getImageContentType());
                }

                return existingSpaData;
            })
            .map(spaDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpaData> findAll() {
        log.debug("Request to get all SpaData");
        return spaDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpaData> findOne(Long id) {
        log.debug("Request to get SpaData : {}", id);
        return spaDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SpaData : {}", id);
        spaDataRepository.deleteById(id);
    }
}

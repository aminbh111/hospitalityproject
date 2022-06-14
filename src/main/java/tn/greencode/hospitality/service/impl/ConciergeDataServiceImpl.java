package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.ConciergeData;
import tn.greencode.hospitality.repository.ConciergeDataRepository;
import tn.greencode.hospitality.service.ConciergeDataService;

/**
 * Service Implementation for managing {@link ConciergeData}.
 */
@Service
@Transactional
public class ConciergeDataServiceImpl implements ConciergeDataService {

    private final Logger log = LoggerFactory.getLogger(ConciergeDataServiceImpl.class);

    private final ConciergeDataRepository conciergeDataRepository;

    public ConciergeDataServiceImpl(ConciergeDataRepository conciergeDataRepository) {
        this.conciergeDataRepository = conciergeDataRepository;
    }

    @Override
    public ConciergeData save(ConciergeData conciergeData) {
        log.debug("Request to save ConciergeData : {}", conciergeData);
        return conciergeDataRepository.save(conciergeData);
    }

    @Override
    public ConciergeData update(ConciergeData conciergeData) {
        log.debug("Request to save ConciergeData : {}", conciergeData);
        return conciergeDataRepository.save(conciergeData);
    }

    @Override
    public Optional<ConciergeData> partialUpdate(ConciergeData conciergeData) {
        log.debug("Request to partially update ConciergeData : {}", conciergeData);

        return conciergeDataRepository
            .findById(conciergeData.getId())
            .map(existingConciergeData -> {
                if (conciergeData.getLang() != null) {
                    existingConciergeData.setLang(conciergeData.getLang());
                }
                if (conciergeData.getTitle() != null) {
                    existingConciergeData.setTitle(conciergeData.getTitle());
                }
                if (conciergeData.getContent() != null) {
                    existingConciergeData.setContent(conciergeData.getContent());
                }
                if (conciergeData.getImage() != null) {
                    existingConciergeData.setImage(conciergeData.getImage());
                }
                if (conciergeData.getImageContentType() != null) {
                    existingConciergeData.setImageContentType(conciergeData.getImageContentType());
                }

                return existingConciergeData;
            })
            .map(conciergeDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConciergeData> findAll() {
        log.debug("Request to get all ConciergeData");
        return conciergeDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConciergeData> findOne(Long id) {
        log.debug("Request to get ConciergeData : {}", id);
        return conciergeDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConciergeData : {}", id);
        conciergeDataRepository.deleteById(id);
    }
}

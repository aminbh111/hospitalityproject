package tn.greencode.hospitality.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.domain.BarsData;
import tn.greencode.hospitality.repository.BarsDataRepository;
import tn.greencode.hospitality.service.BarsDataService;

/**
 * Service Implementation for managing {@link BarsData}.
 */
@Service
@Transactional
public class BarsDataServiceImpl implements BarsDataService {

    private final Logger log = LoggerFactory.getLogger(BarsDataServiceImpl.class);

    private final BarsDataRepository barsDataRepository;

    public BarsDataServiceImpl(BarsDataRepository barsDataRepository) {
        this.barsDataRepository = barsDataRepository;
    }

    @Override
    public BarsData save(BarsData barsData) {
        log.debug("Request to save BarsData : {}", barsData);
        return barsDataRepository.save(barsData);
    }

    @Override
    public BarsData update(BarsData barsData) {
        log.debug("Request to save BarsData : {}", barsData);
        return barsDataRepository.save(barsData);
    }

    @Override
    public Optional<BarsData> partialUpdate(BarsData barsData) {
        log.debug("Request to partially update BarsData : {}", barsData);

        return barsDataRepository
            .findById(barsData.getId())
            .map(existingBarsData -> {
                if (barsData.getLang() != null) {
                    existingBarsData.setLang(barsData.getLang());
                }
                if (barsData.getTitle() != null) {
                    existingBarsData.setTitle(barsData.getTitle());
                }
                if (barsData.getContent() != null) {
                    existingBarsData.setContent(barsData.getContent());
                }
                if (barsData.getImage() != null) {
                    existingBarsData.setImage(barsData.getImage());
                }
                if (barsData.getImageContentType() != null) {
                    existingBarsData.setImageContentType(barsData.getImageContentType());
                }

                return existingBarsData;
            })
            .map(barsDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BarsData> findAll() {
        log.debug("Request to get all BarsData");
        return barsDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BarsData> findOne(Long id) {
        log.debug("Request to get BarsData : {}", id);
        return barsDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BarsData : {}", id);
        barsDataRepository.deleteById(id);
    }
}
